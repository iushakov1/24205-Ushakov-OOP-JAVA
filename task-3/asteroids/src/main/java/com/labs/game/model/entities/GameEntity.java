package com.labs.game.model.entities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class GameEntity {
    protected double x;
    protected double y;
    protected double xSpeed;
    protected double ySpeed;
    protected double rotationAngle;
    protected double radius;
    protected boolean destroyed;
    protected boolean ghostForm;
    protected int ghostFormTimer;
    protected Polygon shape;
    protected int price = 0;
    protected int maxSpeed = 2;

    public void update(int width, int height){
        if(destroyed){
            return;
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

    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public boolean isDestroyed(){
        return this.destroyed;
    }
    public void damaged(){
        this.destroyed = true;
    };

    public Polygon getShape(){
        return this.shape;
    }

    public double getDistance(GameEntity other){
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distanceSq = dx * dx + dy * dy;
        return Math.sqrt(distanceSq);
    }

    public boolean isColliding(GameEntity other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distanceSq = dx * dx + dy * dy;
        double radiusSum = this.radius + other.radius;

        if(distanceSq < (radiusSum * radiusSum)){
            return this.isCollidingByPixel(other);
        }

        return false;
    }

    private boolean isCollidingByPixel(GameEntity other){
        Area area1 = new Area(this.getTransformedShape());
        Area area2 = new Area(other.getTransformedShape());

        area1.intersect(area2);
        return !(area1.isEmpty());
    }

    public double getAngleRadians(){
        return Math.toRadians(this.rotationAngle);
    }

    private Shape getTransformedShape(){
        AffineTransform at = new AffineTransform();
        at.translate(this.x, this.y);
        at.rotate(this.getAngleRadians());
        return at.createTransformedShape(getShape());
    }

    public void setGhost(int time){
        if(time > 0){
            this.ghostForm = true;
            this.ghostFormTimer = time;
        }
    }

    public void setCoord(double x, double y){
        this.x = x;
        this.y = y;
    }

    public boolean isGhost(){
        return this.ghostForm;
    }

    protected void updateGhostForm(){
        if(ghostFormTimer > 0){
            --ghostFormTimer;
        }else{
            this.ghostForm = false;
        }
    }

    protected double getMaxRadius(){
        double sumX = 0, sumY = 0;
        for (int i = 0; i < this.shape.npoints; i++) {
            sumX +=  this.shape.xpoints[i];
            sumY +=  this.shape.ypoints[i];
        }
        double centerX = sumX /  this.shape.npoints;
        double centerY = sumY /  this.shape.npoints;

        double maxDistSq = 0;
        for (int i = 0; i < this.shape.npoints; i++) {
            double xShifted = this.shape.xpoints[i] - centerX;
            double yShifted = this.shape.ypoints[i] - centerY;
            double distSq = xShifted * xShifted + yShifted * yShifted;
            if (distSq > maxDistSq) {
                maxDistSq = distSq;
            }
        }
        return Math.sqrt(maxDistSq);
    }

    public int getPrice(){
        return this.price;
    }

    public double getRadius(){
        return this.radius;
    }

    public void push(GameEntity other) {
        /*this.xSpeed -= other.xSpeed * other.getRadius();
        this.ySpeed -= other.ySpeed * other.getRadius();*/
        double radiusDif = other.getRadius() - this.radius;
        this.giveAcceleration(-other.xSpeed * radiusDif, -other.ySpeed * radiusDif);
    }

    public void setDestroyed(){
        this.destroyed = true;
    }

    public void giveAcceleration(double xAcc, double yAcc){
        this.xSpeed = (maxSpeed >= Math.abs(this.xSpeed + xAcc) ? xAcc + this.xSpeed : maxSpeed);
        this.ySpeed = (maxSpeed >= Math.abs(this.ySpeed + yAcc) ? yAcc + this.ySpeed : maxSpeed);
    }
}
