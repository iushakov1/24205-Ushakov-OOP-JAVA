package com.labs.game.network.Data;

public class ShipData extends EntityData{
    public boolean thrusting;
    public int healthPoint;
    public ShipData(String type,
                    int id,
                    double x,
                    double y,
                    double angle,
                    double radius,
                    int ghostTime,
                    boolean thrusting,
                    int healthPoint) {
        super(type, id, x, y, angle, radius, ghostTime);
        this.thrusting = thrusting;
        this.healthPoint = healthPoint;
    }
}
