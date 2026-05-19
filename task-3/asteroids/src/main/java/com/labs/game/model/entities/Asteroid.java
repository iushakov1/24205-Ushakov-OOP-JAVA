package com.labs.game.model.entities;

import com.labs.game.event.RecordUpdateEvent;
import com.labs.game.model.Record;

import java.awt.*;
import java.util.List;

public class Asteroid extends GameEntity{
    private int level;
    private double rotationSpeed;
    private List<GameEntity> entities;
    private Record record;
    private static int asteroidCount = 0;
    public Asteroid(double x, double y, double radius, List<GameEntity> entities, Record record){
        this.destroyed = (radius < 6);
        asteroidCount += (destroyed ? 0:1) ;
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
        this.entities = entities;
        this.record =record;

    }

    public static int getAsteroidCount(){
        return asteroidCount;
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
        Asteroid a = this;
        switch(a.getLevel()){
            case 2:{
                double distribution = Math.random();
                double distribution1 = Math.random()*(1-distribution);

                double r1Scale = distribution;
                double r2Scale = (1-distribution)*distribution1;
                double r3Scale = 1 - ((1-distribution)*distribution1);

                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r1Scale, entities, record));
                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r2Scale, entities, record));
                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r3Scale, entities, record));

                --asteroidCount;
                a.setDestroyed();
                break;
            }
            case 1:{
                double distribution = Math.random();

                double r1Scale = distribution;
                double r2Scale = 1 - distribution;

                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r1Scale, entities, record));
                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r2Scale, entities, record));
                --asteroidCount;

                a.setDestroyed();
                break;
            }
            case 0:{
                --asteroidCount;
                a.setDestroyed();
                break;
            }

        }
        record.update(a.getPrice());

        this.setGhost(50);

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

    @Override
    public boolean isAffectableOnShip(Ship ship){
        return !ship.isGhost() && !this.isGhost() && this.isColliding(ship);
    }

    @Override
    public boolean isAffectableOnEntity(GameEntity other){
        if(this == other) {
            return false;
        }

        Asteroid a = this;
        return !other.isGhost() && !a.isGhost() && a.isColliding(other);
    }

    @Override
    public void shipAffect(Ship ship){
        if(this.isAffectableOnShip(ship)){
            ship.damaged();
        }
    }

    @Override
    public void entityAffect(GameEntity entity){

        if(this.isAffectableOnEntity(entity)){

            Class entityClass = entity.getClass();

            if(entityClass == Asteroid.class){
                if(this.getRadius() > entity.getRadius()){
                    this.push(entity);
                }
            }

            else{
                entity.damaged();
            }

        }
    }

    public static void setAsteroidCount(int count){
        asteroidCount = 0;
    }

}
