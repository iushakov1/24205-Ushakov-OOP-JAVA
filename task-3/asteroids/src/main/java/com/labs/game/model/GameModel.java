package com.labs.game.model;

import com.labs.game.controller.MenuController;
import com.labs.game.event.RepaintEvent;
import com.labs.game.event.StatusChangeEvent;
import com.labs.game.model.entities.Asteroid;
import com.labs.game.model.entities.Bullet;
import com.labs.game.model.entities.Ship;
import com.labs.game.service.Observable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel extends Observable {
    private final int width;
    private final int height;
    private ModelStatus status = ModelStatus.MENU;

    private Ship ship;
    private List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private List<Asteroid> asteroids = new CopyOnWriteArrayList<>();

    public GameModel(int width, int height){
        this.width = width;
        this.height = height;
        this.status = ModelStatus.MENU;
    }

    public void initModel(){


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
                ship = new Ship(width/2, height/2);
                asteroids.add(new Asteroid(width/2, height/4, 20));
                asteroids.add(new Asteroid(width/2, height/8, 20));
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
                        if(b.isColliding(a)){
                            a.damaged();
                            if(a.isDestroyed()){
                                asteroids.remove(a);
                            }
                            b.damaged();
                            bullets.remove(b);
                        }
                    }
                }

                ship.update(this.width, this.height);

                for(Asteroid a: asteroids){

                    if(a.isColliding(ship)){
                        ship.damaged();
                    }

                    a.update(this.width, this.height);

                    for(Asteroid other: asteroids){
                        if(a.equals(other)){
                            continue;
                        }
                        if(a.isColliding(other)){
                            a.damaged();
                            other.damaged();
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
                    this.status = ModelStatus.GAMEOVER;
                    this.notify(new StatusChangeEvent());
                }
                this.notify(new RepaintEvent());
                break;
            }
            case PAUSED: {
                break;
            }
            case GAMEOVER: {
                this.status = ModelStatus.MENU;
                this.notify(new StatusChangeEvent());
                return;
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
        this.status = newStatus;
    }
}
