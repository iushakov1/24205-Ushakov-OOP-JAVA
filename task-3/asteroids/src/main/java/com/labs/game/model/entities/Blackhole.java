package com.labs.game.model.entities;

import java.awt.*;

public class Blackhole extends GameEntity{
    private double pulseScale = 1.0;
    private boolean pulseUp = true;
    private double massEffect = 0;
    private double absorbed = 0;
    private double absorbedRadius = 0;
    private double initRadius = 0;
    private boolean isCollapse = false;
    private int softeningConstant = 300;
    private int mapWidth;
    private int mapHeight;
    public Blackhole(double x, double y, double radius, int width, int height){
        this.destroyed = false;
        this.xSpeed = 0.4;
        this.ySpeed = 0.5;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.initRadius = radius;
        this.absorbedRadius = radius*7;
        this.shape = generateShape(radius, 20);
        this.setGhost(100);
        this.massEffect = radius*0.5;
        this.mapWidth = width;
        this.mapHeight = height;
    }

    public double getAbsorbedRadius(){
        return this.absorbedRadius;
    }

    private Polygon generateShape(double radius, int points){
        int[] xPoints = new int[points];
        int[] yPoints = new int[points];

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * ((double) i / points);
            double pointRadius = radius * (0.95 + Math.random() * 0.1);
            xPoints[i] = (int) (pointRadius * Math.cos(angle));
            yPoints[i] = (int) (pointRadius * Math.sin(angle));
        }
        return new Polygon(xPoints, yPoints, points);
    }

    @Override
    public void update(int width, int height) {

        if(radius > initRadius*3){
            this.isCollapse = true;
        }

        if(isCollapse){
            radius*=1.1;
            if(radius > initRadius*6){
                setDestroyed();
            }
        }

        else{
            this.updateGhostForm();
            if (pulseUp) {
                pulseScale += 0.005;
                if (pulseScale > 1.05) pulseUp = false;
            } else {
                pulseScale -= 0.005;
                if (pulseScale < 0.95) pulseUp = true;
            }
            this.shape = generateShape(this.radius, 20);

            x += xSpeed * 0.5;
            y += ySpeed * 0.5;

            if (x < 0) x = width;
            if (x > width) x = 0;
            if (y < 0) y = height;
            if (y > height) y = 0;
        }
    }

    @Override
    public void damaged(){

    }

    public double getPulseScale(){
        return this.pulseScale;
    }

    public double getMassEffect(){
        return massEffect;
    }

    public void addAbsorbed(double radiusOfAbsorbed){
        ++this.absorbed;
        this.radius += (0.3)*radiusOfAbsorbed;
        this.shape = generateShape(this.radius, 30);
    }

    public void gravityAffect(GameEntity other){
        double xDir = this.x - other.x;
        double yDir = this.y - other.y;

        if(Math.abs(xDir) > this.mapWidth/2){
            xDir = xDir - Math.signum(xDir) * this.mapWidth;
        }

        if(Math.abs(yDir) > this.mapHeight/2){
            yDir = yDir - Math.signum(yDir) * this.mapHeight;
        }

        double actualDistanceSq = xDir * xDir + yDir * yDir;
        double actualDistance = Math.sqrt(actualDistanceSq);
        if (actualDistance < 1){
            actualDistance = 1;
        }

        double force = this.massEffect / (actualDistance + softeningConstant);

        double xAcc = (xDir / actualDistance) * force;
        double yAcc = (yDir / actualDistance) * force;

        other.giveAcceleration(xAcc, yAcc);
    }

    @Override
    public boolean isAffectable(GameEntity other){
        if(this == other){
            return false;
        }
        double dist = this.getDistance(other);
        if (dist <= this.getAbsorbedRadius()) {
            return true;
        }
        return false;
    }

    @Override
    public void shipAffect(Ship ship){
        Blackhole b = this;
        if(this.isAffectable(ship) && !ship.isGhost()){
            this.gravityAffect(ship);
        }
        if(this.isColliding(ship) && !ship.isGhost()){
            ship.damaged();
        }
    }

    @Override
    public void entityAffect(GameEntity entity){
        Blackhole b = this;
        if(this.isAffectable(entity)){
            this.gravityAffect(entity);
            if(this.isColliding(entity) && !entity.isGhost()){
                entity.damaged();
                if(!this.isCollapse){
                    this.addAbsorbed(this.getRadius());
                }
            }
        }
    }
}
