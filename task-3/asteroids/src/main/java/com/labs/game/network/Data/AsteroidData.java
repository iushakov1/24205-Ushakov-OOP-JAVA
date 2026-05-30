package com.labs.game.network.Data;

public class AsteroidData extends EntityData{
    public long seed;
    public AsteroidData(String type, int id, double x, double y, double angle, double radius, int ghostTime, long seed) {
        super(type, id, x, y, angle, radius, ghostTime);
        this.seed = seed;
    }
}
