package com.labs.game.network.Data;

import java.io.Serializable;

public class EntityData implements Serializable {
    public String type;
    public int id;
    public double x, y;
    public double angle;
    public double radius;
    public int ghostTime;


    public EntityData(String type, int id, double x, double y, double angle, double radius, int ghostTime) {
        this.type = type;
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.radius = radius;
        this.ghostTime = ghostTime;
    }

}