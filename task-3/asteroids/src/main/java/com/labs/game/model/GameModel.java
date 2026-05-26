package com.labs.game.model;

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


    private List<Ship> ships = new CopyOnWriteArrayList<>();
    private List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private List<GameEntity> entities = new CopyOnWriteArrayList<>();

    public GameModel(int width, int height){
        this.record = new Record();
        this.width = width;
        this.height = height;
        this.status = ModelStatus.MENU;
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
                this.entities.clear();
                this.bullets.clear();
                Asteroid.setAsteroidCount(0);

                this.record.resetCurScore();
                this.changeStatus(ModelStatus.PLAYING);
                this.notify(new StatusChangeEvent());
                break;
            }

            case PLAYING:
            {
                /*if(ship.isFiring() && ship.canShoot()){
                    ship.resetCooldown();
                    bullets.add(new Bullet(ship));
                }*/

                for(Bullet b: bullets){
                    if(b.getVectorSpeed() < 2){
                        b.damaged();
                        bullets.remove(b);
                        continue;
                    }
                    b.update(width, height);
                    for(GameEntity e : entities){
                        b.entityAffect(e);
                    }
                    if(b.isDestroyed()){
                        bullets.remove(b);
                    }
                }

                //ship.update(this.width, this.height);

                for(GameEntity entity: entities){

                    entity.update(this.width, this.height);
                    //entity.shipAffect(this.ship);
                    for(GameEntity otherEntity: entities){
                        entity.entityAffect(otherEntity);
                    }
                    if(entity.isDestroyed()){
                        entities.remove(entity);
                    }

                }

                /*if(ship.isDestroyed()){
                    this.status = ModelStatus.GAMEOVER;
                    this.notify(new StatusChangeEvent());
                }*/

                /*if(Asteroid.getAsteroidCount() == 0){
                    this.entities.add(new Blackhole(width*Math.random(), height*Math.random(), 30 + 30*Math.random(), this.width, this.height));
                    this.ship.setHealthPoint(3);
                    this.generateAsteroids();
                }*/

                this.notify(new RepaintEvent());
                break;
            }
            case PAUSED, GAMEOVER: {
                break;
            }
        }

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
        int numOfAsteroids = (int)(Math.random()*5 + 1);

        for(int i = 0; i < numOfAsteroids; ++i){
            double x = Math.random()*width;
            double y = Math.random()*height;
            double r = 8 + Math.random()*40;
            this.entities.add(new Asteroid(x, y, r, entities, record));
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
