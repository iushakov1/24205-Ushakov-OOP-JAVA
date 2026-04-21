package com.labs.game.model.entities;

import java.awt.*;

public class Asteroid extends GameEntity{
    private int level;
    private double rotationSpeed;
    private Polygon shape;

    public Asteroid(double x, double y, double radius){
        this.destroyed = (radius < 6);
        this.xSpeed = Math.random()+0.1;
        this.ySpeed = Math.random()+0.1;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.setLevel();
        this.shape = generateShape(radius, 8);
        this.rotationSpeed = (Math.random() - 0.5) * 0.1;
        this.setGhost(100 * (4 - level));
        this.price = 10*level;
    }

    public void push(Asteroid other) {
        this.xSpeed -= other.xSpeed * other.getRadius();
        this.ySpeed -= other.ySpeed * other.getRadius();
    }

    private Polygon generateShape(double radius, int points){
        int[] xPoints = new int[points];
        int[] yPoints = new int[points];

        for(int i = 0; i < points; ++i){
            double angle = 2 * Math.PI * ((double)i/points);
            double pointRadius = radius * (0.8 + Math.random()*0.4);
            xPoints[i] = (int) (pointRadius * Math.cos(angle));
            yPoints[i] = (int) (pointRadius * Math.sin(angle));
        }
        return new Polygon(xPoints, yPoints, points);
    }

    public Polygon getShape(){
        return this.shape;
    }

    @Override
    public void update(int width, int height){
        double upSpeedLimit = 0.5;
        double dnSpeedLimit = 0.1;

        double currentSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);

        this.updateGhostForm();

        xSpeed += (Math.random() - 0.5) * 0.05;
        ySpeed += (Math.random() - 0.5) * 0.05;
        if (currentSpeed > 0) {
            if (currentSpeed > upSpeedLimit) {
                xSpeed = (xSpeed / currentSpeed) * upSpeedLimit;
                ySpeed = (ySpeed / currentSpeed) * upSpeedLimit;
            } else if (currentSpeed < dnSpeedLimit) {
                xSpeed = (xSpeed / currentSpeed) * dnSpeedLimit;
                ySpeed = (ySpeed / currentSpeed) * dnSpeedLimit;
            }
        } else {
            xSpeed = dnSpeedLimit;
            ySpeed = 0;
        }

        x += xSpeed;
        y += ySpeed;
        rotationAngle += rotationSpeed;

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
    public void damaged(){
        if(this.isGhost()){
            return;
        }
        --level;
        this.destroyed = true;

    }

    public int getLevel(){
        return this.level;
    }

    private void setLevel(){
        if(radius >= 30){
            this.level = 3;
        }
        else if(15 <= radius){
            this.level = 2;
        }
        else if(0 < radius){
            this.level = 1;
        }
        else{
            this.level = 0;
            this.destroyed=true;
        }
    }
}
