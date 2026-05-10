package com.labs.game.model;

import com.labs.game.event.RecordUpdateEvent;
import com.labs.game.event.RepaintEvent;
import com.labs.game.event.StatusChangeEvent;
import com.labs.game.model.entities.*;
import com.labs.game.service.Observable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel extends Observable {
    private int width;
    private int height;
    private ModelStatus status = ModelStatus.MENU;
    private ModelStatus lastStatus = ModelStatus.MENU;
    private Record record;
    private int asteroidCount;
    private int gameStage = 0;


    private Ship ship;
    private List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private List<GameEntity> entities = new CopyOnWriteArrayList<>();

    public GameModel(int width, int height){
        this.record = new Record();
        this.width = width;
        this.height = height;
        this.status = ModelStatus.MENU;
        this.ship = new Ship(width/2, height/2);
    }

    public void update(){
        switch (this.status){
            case MENU:
            {
                this.notify(new RepaintEvent());
                break;
            }

            case STARTNEWGAME:
            {
                this.gameStage = 0;
                this.entities.clear();
                this.bullets.clear();
                this.asteroidCount = 0;
                this.ship.setCoord((double) width /2, (double) height /2);
                this.ship.reset();
                this.record.resetCurScore();
                this.changeStatus(ModelStatus.PLAYING);
                this.notify(new StatusChangeEvent());
                break;
            }

            case PLAYING:
            {
                if(ship.isFiring() && ship.canShoot()){
                    ship.resetCooldown();
                    bullets.add(new Bullet(ship));
                }

                for(Bullet b: bullets){
                    if(b.getVectorSpeed() < 2){
                        b.damaged();
                        bullets.remove(b);
                        continue;
                    }
                    b.update(width, height);
                    for(GameEntity e : entities){
                        if(b.isColliding(e) && !e.isGhost()){
                            if(e.getClass() == Asteroid.class){
                                this.processAsteroidCollision((Asteroid) e);
                            }

                            b.damaged();
                            bullets.remove(b);
                        }
                    }
                }

                ship.update(this.width, this.height);

                for(GameEntity e: entities){

                    e.update(this.width, this.height);

                    if(e.getClass() == Asteroid.class){
                        Asteroid a = (Asteroid) e;
                        if(a.isColliding(ship) && !this.ship.isGhost() && !a.isGhost()){
                            ship.damaged();
                        }
                        for(GameEntity other: entities){

                            if(a.equals(other)){
                                continue;
                            }

                            if(a.isColliding(other)){

                                if(other.getClass() == Asteroid.class) {
                                    if (a.getRadius() > other.getRadius()) {
                                        other.push(a);
                                    } else if (a.getRadius() < other.getRadius()) {
                                        a.push(other);
                                    } else {
                                        a.damaged();
                                        other.damaged();
                                    }
                                }

                            }

                        }

                    }

                    else if(e.getClass() == Blackhole.class && !e.isGhost()){
                        Blackhole b = (Blackhole) e;

                        if(b.isColliding(ship) && !this.ship.isGhost()){
                            ship.damaged();
                        }

                        {
                            double dist = b.getDistance(ship);
                            if (dist <= b.getAbsorbedRadius()) {
                                b.affect(ship, width, height);
                            }
                        }
                        for(GameEntity other: entities){
                            if(b.equals(other)){
                                continue;
                            }

                            double dist = b.getDistance(other);
                            if(dist <= b.getAbsorbedRadius()){
                                b.affect(other, width, height);
                            }

                            if(b.isColliding(other) && !other.isGhost()){
                                b.addAbsorbed(other.getRadius());
                                other.damaged();

                            }
                        }
                    }

                    if(e.isDestroyed()){
                        if(e.getClass() == Asteroid.class){
                            --this.asteroidCount;
                        }
                        entities.remove(e);
                    }
                }

                if(ship.isDestroyed()){
                    this.status = ModelStatus.GAMEOVER;
                    this.notify(new StatusChangeEvent());
                }

                if(asteroidCount == 0){
                    ++gameStage;
                    this.entities.add(new Blackhole(width*Math.random(), height*Math.random(), 30 + 30*Math.random()));
                    this.ship.setHealthPoint(3);
                    this.generateAsteroids();
                }

                this.notify(new RepaintEvent());
                break;
            }
            case PAUSED, GAMEOVER: {
                break;
            }
        }

    }

    private void processAsteroidCollision(Asteroid a){
        switch(a.getLevel()){
            case 3:{
                double distribution = Math.random();
                double distribution1 = Math.random()*(1-distribution);

                double r1Scale = distribution;
                double r2Scale = (1-distribution)*distribution1;
                double r3Scale = 1 - ((1-distribution)*distribution1);

                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r1Scale));
                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r2Scale));
                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r3Scale));
                this.asteroidCount+=3;
                a.setDestroyed();
                break;
            }
            case 2:{
                double distribution = Math.random();

                double r1Scale = distribution;
                double r2Scale = 1 - distribution;

                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r1Scale));
                entities.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r2Scale));
                this.asteroidCount+=2;

                a.setDestroyed();
                break;
            }
            case 1:{
                a.setDestroyed();
                break;
            }
        }

        record.update(a.getPrice());
        this.notify(new RecordUpdateEvent());
    }

    public Ship getShip(){
        return ship;
    }


    public List<GameEntity> getEntities(){
        return entities;
    }

    public List<Bullet> getBullets(){
        return bullets;
    }

    public ModelStatus getStatus(){
        return this.status;
    }

    public void changeStatus(ModelStatus newStatus){
        this.lastStatus = this.status;
        this.status = newStatus;
    }

    public ModelStatus getLastStatus(){
        return this.lastStatus;
    }

    public void updateBounds(int newWidth, int newHeight){
        this.width = newWidth;
        this.height = newHeight;
    }

    private void generateAsteroids(){
        int numOfAsteroids = (int)(Math.random()*10);
        this.asteroidCount += numOfAsteroids;
        for(int i = 0; i < numOfAsteroids; ++i){
            double x = Math.random()*width;
            double y = Math.random()*height;
            if ((this.getPointDistance(x, y, this.ship.getX(), this.ship.getY()) < this.ship.getRadius())){
                continue;
            }
            double r = 8 + Math.random()*40;
            this.entities.add(new Asteroid(x, y, r));
        }
    }

    private double getPointDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public double getWidth(){
        return this.width;
    }
    public double getHeight(){
        return this.height;
    }
    public Record getRecord(){
        return this.record;
    }


}
