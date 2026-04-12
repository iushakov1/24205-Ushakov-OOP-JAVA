package com.labs.game.model;

import java.lang.Math.*;


public class Ship extends GameEntity{
    private double thrustPower = 2;

    public Ship(int x, int y){
        this.x = x;
        this.y = y;
        this.ySpeed = 0;
        this.xSpeed = 0;
        this.rotationAngle = 0;
    }

    public void thrust(){
        int speedLimit = 8;

        double radians = Math.toRadians(this.rotationAngle);

        double rotSin = Math.sin(radians);
        double rotCos = Math.cos(radians);

        this.xSpeed += rotCos*thrustPower;
        this.ySpeed += rotSin*thrustPower;

        double currentSpeed = Math.sqrt(xSpeed*xSpeed+ySpeed*ySpeed);
        if(currentSpeed > speedLimit){
            xSpeed = (xSpeed / currentSpeed) * speedLimit;
            ySpeed = (ySpeed / currentSpeed) * speedLimit;
        }

    }

    public void rotateLeft(){
        rotationAngle = (rotationAngle-4 + 360)%360;
    }

    public void rotateRight(){
        rotationAngle = (rotationAngle+4)%360;
    }

    public double getAngle(){
        return rotationAngle;
    }

    public double getAngleRadians(){
        return Math.toRadians(this.rotationAngle);
    }
}
