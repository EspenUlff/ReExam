package com.dtu.common.model.fileaccess;

import com.dtu.common.model.Heading;


public class PlayerTemplate {
    public String name;
    public String color;
    public int x;
    public int y;
    public Heading heading = Heading.SOUTH;

    public PlayerTemplate(String name, String color, int x, int y, Heading heading) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.heading = heading;
    }
}

