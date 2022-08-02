package com.dtu.roboclient.controller;

import com.dtu.common.model.Board;
import com.dtu.common.model.FieldAction;
import com.dtu.common.model.fileaccess.FieldActionTypeAdapter;
import com.dtu.common.model.fileaccess.LoadBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HttpClientSynchronous {
    public static final String BaseURI = "http://localhost:8080";
    public static UUID gameId;

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();

    public static void main(String[] args) throws IOException, InterruptedException {
        //System.out.println(NewGame(2));
        //System.out.println(GetGame(UUID.fromString("9b1c7d0d-bce4-4483-9ffc-7d9805ee312d")));
        System.out.println(list());
    }

    public static UUID NewGame(int players) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(BaseURI + "/newgame/" + players)).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        /*System.out.println(body);
        System.out.println(UUID.randomUUID().toString());*/
        gameId = UUID.fromString(body.replace("\"", ""));
        return gameId;
    }

    public static UUID NewGameNoExcept(int players) {
        try {
            return NewGame(players);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> list() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(BaseURI + "/list")).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        return Arrays.stream(new Gson().fromJson(body, String[].class)).toList();
    }

    public static List<String> listNoExcept() {
        try {
            return list();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*public static Board GetGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/game/" + id))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return LoadBoard.loadBoard(body);

    }

    public static Board GetGameNoExcept(UUID id) {
        try {
            return GetGame(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/

    /*public static Board finishProgramming() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/finishprogramming/" + gameId))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return LoadBoard.loadBoard(body);
    }

    public static Board finishProgrammingNoExcept() {
        try {
            return finishProgramming();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/

    public static boolean saveGame(UUID id, Board board) throws IOException, InterruptedException {
        GsonBuilder simpleBuilder = new GsonBuilder()
                //.excludeFieldsWithModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(FieldAction.class, new FieldActionTypeAdapter());
        Gson gson = simpleBuilder.create();
        System.out.println(gson.toJson(board));
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(gson.toJson(board))).uri(URI.create(BaseURI + "/game/savegame/" + id)).setHeader("Content-Type", "application/json").build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return response.statusCode() == 200;
    }

    public static boolean saveGameNoExcept(UUID id, Board board) {
        try {
            return saveGame(id, board);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw e;
        }
    }

    public static Board loadGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(BaseURI + "/game/loadgame/" + id)).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return LoadBoard.loadBoard(body);
    }

    public static Board loadGameNoExcept(UUID id) {
        try {
            return loadGame(id);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(URI.create(BaseURI + "/game/" + id)).build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return response.statusCode() == 200;
    }

    public static boolean deleteGameNoExcept(UUID id) {
        try {
            return deleteGame(id);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
