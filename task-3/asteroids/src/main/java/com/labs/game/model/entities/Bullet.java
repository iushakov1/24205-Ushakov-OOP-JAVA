package com.labs.game.model.entities;

import java.awt.*;

public class Bullet extends GameEntity{
    private Polygon shape;

    public Bullet(Ship ship){
        int bulletSpeed = 8;
        this.radius = 2;
        this.xSpeed = ship.xSpeed;
        this.ySpeed = ship.ySpeed;

        shape = generateShape();

        this.x = ship.getX();
        this.y = ship.getY();

        double radians = ship.getAngleRadians();
        double rotSin = Math.sin(radians);
        double rotCos = Math.cos(radians);

        this.xSpeed = rotCos*bulletSpeed;
        this.ySpeed = rotSin*bulletSpeed;
    }

    @Override
    public void update(int width, int height){
        x += xSpeed;
        xSpeed*=0.985;
        y += ySpeed;
        ySpeed*=0.985;

        if (x < 0){
            x = width;
        }
        if (x > width){
            x = 0;
        }
        if (y < 0){
            y = height;
        }
        if (y > height){
            y = 0;
        }
    }

    @Override
    public void damaged() {
        this.destroyed = true;
    }

    private Polygon generateShape(){
        int[] xPoints = {0, 2, 0, 2};
        int[] yPoints = {0, 0, 2, 2};
        int nDots = 4;
        return new Polygon(xPoints, yPoints, nDots);
    }

    public Polygon getShape(){
        return this.shape;
    }

    public double getVectorSpeed(){
        return Math.sqrt(this.xSpeed*this.xSpeed + this.ySpeed*this.ySpeed);
    }
}
