package com.labs.game.model;

import com.labs.game.event.RecordUpdateEvent;
import com.labs.game.event.RepaintEvent;
import com.labs.game.event.StatusChangeEvent;
import com.labs.game.model.entities.Asteroid;
import com.labs.game.model.entities.Bullet;
import com.labs.game.model.entities.Ship;
import com.labs.game.service.Observable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel extends Observable {
    private int width;
    private int height;
    private ModelStatus status = ModelStatus.MENU;
    private ModelStatus lastStatus = ModelStatus.MENU;
    private Record record;


    private Ship ship;
    private List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private List<Asteroid> asteroids = new CopyOnWriteArrayList<>();

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
                this.asteroids.clear();
                this.bullets.clear();
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
                    for(Asteroid a: asteroids){
                        if(b.isColliding(a) && !a.isGhost()){
                            a.damaged();
                            switch(a.getLevel()){
                                case 2:{
                                    double distribution = Math.random();
                                    double distribution1 = Math.random()*(1-distribution);

                                    double r1Scale = distribution;
                                    double r2Scale = (1-distribution)*distribution1;
                                    double r3Scale = 1 - ((1-distribution)*distribution1);

                                    asteroids.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r1Scale));
                                    asteroids.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r2Scale));
                                    asteroids.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r3Scale));

                                    asteroids.remove(a);
                                    break;
                                }
                                case 1:{
                                    double distribution = Math.random();

                                    double r1Scale = distribution;
                                    double r2Scale = 1 - distribution;

                                    asteroids.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r1Scale));
                                    asteroids.add(new Asteroid(a.getX(), a.getY(), a.getRadius()*r2Scale));

                                    asteroids.remove(a);
                                    break;
                                }
                                case 0:{
                                    asteroids.remove(a);
                                    break;
                                }
                            }

                            record.update(a.getPrice());
                            this.notify(new RecordUpdateEvent());

                            b.damaged();
                            bullets.remove(b);
                        }
                    }
                }

                ship.update(this.width, this.height);

                for(Asteroid a: asteroids){

                    if(a.isColliding(ship) && !this.ship.isGhost() && !a.isGhost()){
                        ship.damaged();
                    }

                    a.update(this.width, this.height);

                    for(Asteroid other: asteroids){
                        if(a.equals(other)){
                            continue;
                        }
                        if(a.isColliding(other)){

                            if(a.getRadius() > other.getRadius()){
                                other.push(a);
                            }
                            else if(a.getRadius() < other.getRadius()){
                                a.push(other);
                            }
                            else{
                                a.damaged();
                                other.damaged();
                            }

                        }
                    }

                    if(a.isDestroyed()){
                        asteroids.remove(a);
                    }
                }

                if(ship.isDestroyed()){
                    this.status = ModelStatus.GAMEOVER;
                    this.notify(new StatusChangeEvent());
                }

                if(asteroids.isEmpty()){
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

    public Ship getShip(){
        return ship;
    }

    public List<Asteroid> getAsteroids(){
        return asteroids;
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
        for(int i = 0; i < numOfAsteroids; ++i){
            double x = Math.random()*width;
            double y = Math.random()*height;
            if ((this.getPointDistance(x, y, this.ship.getX(), this.ship.getY()) < this.ship.getRadius())){
                continue;
            }
            double r = 8 + Math.random()*40;
            this.asteroids.add(new Asteroid(x, y, r));
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
