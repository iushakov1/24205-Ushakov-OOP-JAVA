package com.labs.game.model;

import java.util.Vector;

public abstract class GameEntity {
    double x;
    double y;
    double xSpeed;
    double ySpeed;
    double rotationAngle;
    double radius;

    public void update(int width, int height){
        x += xSpeed;
        xSpeed*=0.97;
        y += ySpeed;
        ySpeed*=0.97;

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

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
}
