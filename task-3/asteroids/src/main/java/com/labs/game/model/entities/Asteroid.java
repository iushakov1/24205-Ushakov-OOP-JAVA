package com.labs.game.model.entities;

import com.labs.game.event.RecordUpdateEvent;
import com.labs.game.model.Record;
import com.labs.game.network.Data.AsteroidData;
import com.labs.game.network.Data.EntityData;

import java.util.Random;

import java.awt.*;
import java.util.List;

public class Asteroid extends GameEntity{
    private int level;
    private double rotationSpeed;
    private transient List<GameEntity> entities;
    private Record record;
    private static int asteroidCount = 0;

    public Asteroid(double x, double y, double radius, List<GameEntity> entities, Record record) {
        this(x, y, radius, entities, record, new Random().nextLong());
    }

    public Asteroid(double x, double y, double radius, List<GameEntity> entities, Record record, long seed){
        this.seed = seed;
        this.random = new Random(seed);
        asteroidCount += (destroyed ? 0:1) ;
        this.xSpeed = random.nextDouble()+0.1;
        this.ySpeed = random.nextDouble()+0.1;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.setLevel();
        this.shape = generateShape(radius, 8, random);
        this.rotationSpeed = (random.nextDouble() - 0.5) * 0.1;
        this.setGhost(100 * (4 - level));
        this.price = 10*level;
        this.entities = entities;
        this.record =record;

    }

    public static int getAsteroidCount(){
        return asteroidCount;
    }

    private Polygon generateShape(double radius, int points, Random random){
        int[] xPoints = new int[points];
        int[] yPoints = new int[points];

        for(int i = 0; i < points; ++i){
            double angle = 2 * Math.PI * ((double)i/points);
            double pointRadius = radius * (0.8 + random.nextDouble()*0.4);
            xPoints[i] = (int) (pointRadius * Math.cos(angle));
            yPoints[i] = (int) (pointRadius * Math.sin(angle));
        }
        return new Polygon(xPoints, yPoints, points);
    }

    @Override
    public EntityData toEntityData() {
        AsteroidData aData = new AsteroidData("Asteroid", id, (int)x, (int)y, getAngle(), radius, ghostFormTimer, seed);
        return aData;
    }

    @Override
    public void update(int width, int height){
        double upSpeedLimit = 0.5;
        double dnSpeedLimit = 0.1;

        double currentSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);

        this.updateGhostForm();

        xSpeed += (random.nextDouble() - 0.5) * 0.05;
        ySpeed += (random.nextDouble() - 0.5) * 0.05;
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

        record.update(this.getPrice());


        --asteroidCount;

        if(radius > 15){
            int fragments = (radius > 30) ? 3 : 2;
            double newRadius = radius/2;

            for(int i = 0; i < fragments; ++i){
                double angle = random.nextDouble() * 2 * Math.PI;
                double speed = 0.8 + random.nextDouble() * 1.2;
                double vx = Math.cos(angle) * speed;
                double vy = Math.sin(angle) * speed;

                long newSeed = seed + i + 1;
                Asteroid fragment = new Asteroid(x, y, newRadius, entities, record, newSeed);

                fragment.setId(com.labs.game.controller.GameCore.idGenerator.getAndIncrement());
                fragment.xSpeed = vx;
                fragment.ySpeed = vy;

                entities.add(fragment);
            }
        }

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
