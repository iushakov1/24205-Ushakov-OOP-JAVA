package com.labs.game.model.entities;


import java.awt.*;

public class Ship extends GameEntity {
    private double thrustPower = 0.3;
    private Polygon shape;
    private boolean openFire;
    private int shootCooldown = 30;
    private int cooldownTimer = 0;

    public Ship(int x, int y){
        this.x = x;
        this.y = y;
        this.ySpeed = 0;
        this.xSpeed = 0;
        this.rotationAngle = 0;
        this.shape = generateShape();
        this.setGhost(30);
    }

    @Override
    public void update(int width, int height){

        if(destroyed){
            return;
        }
        if(this.cooldownTimer > 0){
            --this.cooldownTimer;
        }
        this.updateGhostForm();

        x += xSpeed;
        xSpeed*=0.99;
        y += ySpeed;
        ySpeed*=0.99;

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

    public void thrust(){
        int speedLimit = 4;

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
        rotationAngle = (rotationAngle-2 + 360)%360;
    }

    public void rotateRight(){
        rotationAngle = (rotationAngle+2)%360;
    }

    public void fire(){
        openFire = true;
    }

    public double getAngle(){
        return rotationAngle;
    }

    public double getAngleRadians(){
        return Math.toRadians(this.rotationAngle);
    }

    private Polygon generateShape(){
        int[] xPoints = {15, -10, -10};
        int[] yPoints = {0, 10, -10};
        int nDots = 3;
        return new Polygon(xPoints, yPoints, nDots);
    }

    public Polygon getShape(){
        return this.shape;
    }

    @Override
    public void damaged() {
        destroyed = true;
    }

    public boolean isFiring(){
        if(this.openFire){
            this.openFire = false;
            return true;
        }
        return false;
    }

    public boolean canShoot(){
        return cooldownTimer == 0;
    }
    public void resetCooldown(){
        cooldownTimer = shootCooldown;
    }
}
