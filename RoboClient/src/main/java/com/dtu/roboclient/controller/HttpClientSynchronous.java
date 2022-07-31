package com.dtu.roboclient.controller;

import com.dtu.common.model.Board;
import com.dtu.common.model.fileaccess.LoadBoard;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

public class HttpClientSynchronous {
    public static final String BaseURI = "http://localhost:8080";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {
        //System.out.println(NewGame(2));
        //System.out.println(GetGame(UUID.fromString("9b1c7d0d-bce4-4483-9ffc-7d9805ee312d")));
        System.out.println(list());
    }

    public static UUID NewGame(int players) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/newgame/" + players))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        /*System.out.println(body);
        System.out.println(UUID.randomUUID().toString());*/
        return UUID.fromString(body.replace("\"", ""));
    }

    public static UUID[] list() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/list"))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        return new Gson().fromJson(body, UUID[].class);
    }

    public static Board GetGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/game/" + id))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return LoadBoard.loadBoard(body);
    }

    public static Board finishProgramming(UUID id, int playernumber) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/finishprogramming/" + id + "/" + playernumber))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return LoadBoard.loadBoard(body);
    }

    public static boolean saveGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "game/savegame/" + id))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return response.statusCode() == 200;
    }
    public static Boolean loadGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/game/loadgame/" + id))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return response.statusCode() == 200;
    }



}

