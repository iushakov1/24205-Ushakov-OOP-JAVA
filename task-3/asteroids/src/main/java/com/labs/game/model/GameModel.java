package com.labs.game.model;

import com.labs.game.controller.GameCore;
import com.labs.game.event.RepaintEvent;
import com.labs.game.event.StatusChangeEvent;
import com.labs.game.model.entities.*;
import com.labs.game.model.factory.AsteroidFactory;
import com.labs.game.model.factory.BlackholeFactory;
import com.labs.game.model.factory.EntityFactory;
import com.labs.game.network.Data.EntityData;
import com.labs.game.network.Data.ShipData;
import com.labs.game.network.GameState;
import com.labs.game.service.Observable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class GameModel extends Observable {
    private int width;
    private int height;
    private long seed;
    private ModelStatus status = ModelStatus.MENU;
    private ModelStatus lastStatus = ModelStatus.MENU;
    private Record record;
    private Random modelRandom = new Random();
    private final Map<String, EntityFactory> entityFactories = new HashMap<>();

    private List<Ship> ships = new CopyOnWriteArrayList<>();
    private List<Bullet> bullets = new CopyOnWriteArrayList<>();
    private List<GameEntity> entities = new CopyOnWriteArrayList<>();
    public GameModel(int width, int height){
        this.seed = (long)(100 * Math.random());
        this.record = new Record();
        this.width = width;
        this.height = height;
        this.status = ModelStatus.MENU;

        registerEntityFactory(new AsteroidFactory());
        registerEntityFactory(new BlackholeFactory(width, height));
    }

    private void registerEntityFactory(EntityFactory factory) {
        entityFactories.put(factory.getType(), factory);
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
                for(Ship s: ships){
                    s.setGhostForm(100);
                    s.setHealthPoint(3);
                    s.setCoord(width/2, height/2);
                    s.setSpeed(0, 0);
                }
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
                long count = countAsteroids();
                if (count == 0) {
                    for (Ship s : ships){
                        s.setDestroyed(false);
                        s.setHealthPoint(3);
                        s.setGhostForm(100);
                    }


                    generateAsteroids(this.seed);
                    generateBlackhole(this.seed);

                }
                int destroyedCount = 0;
                for (Ship s : ships) {
                    s.update(this.width, this.height);
                    if(s.isDestroyed()){
                        ++destroyedCount;
                        s.setGhostForm(20);
                    }
                }
                if(destroyedCount == ships.size()){
                    changeStatus(ModelStatus.STARTNEWGAME);
                }

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

                entities.removeIf(GameEntity::isDestroyed);
                for(GameEntity entity: entities){

                    for(Ship ship: ships){
                        entity.shipAffect(ship);
                    }
                    entity.update(this.width, this.height);
                    for(GameEntity otherEntity: entities){
                        entity.entityAffect(otherEntity);
                    }

                }
                entities.removeIf(GameEntity::isDestroyed);



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
        this.notify(new StatusChangeEvent());
    }

    public ModelStatus getLastStatus(){
        return this.lastStatus;
    }

    public void updateBounds(int newWidth, int newHeight){
        this.width = newWidth;
        this.height = newHeight;
    }

    private void generateAsteroids(long seed){
        long batchSeed = modelRandom.nextLong();
        Random random = new Random(batchSeed);
        int numOfAsteroids = random.nextInt(5) + 1;

        for(int i = 0; i < numOfAsteroids; ++i){

            double x = random.nextDouble() * width;
            double y = random.nextDouble() * height;
            double r = 8 + random.nextDouble() * 40;

            Asteroid a = new Asteroid(x, y, r, entities, record, random.nextLong());

            a.setId(GameCore.idGenerator.getAndIncrement());
            this.entities.add(a);
        }
    }

    private void generateBlackhole(long seed){
        long batchSeed = modelRandom.nextLong();
        Random random = new Random(batchSeed);
        double x = random.nextDouble() * width;
        double y = random.nextDouble() * height;
        double r = 20 + random.nextDouble() * 40;
        Blackhole b = new Blackhole(x, y, r, width, height);
        b.setId(GameCore.idGenerator.getAndIncrement());
        this.entities.add(b);
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

    public void applyNetworkState(GameState state){
        this.record.setCurScore(state.currentScore);
        if(record.getCurScore() > record.getMaxScore()){
            record.setMaxScore(record.getCurScore());
        }

        for(EntityData sData : state.ships){
            ShipData shipData = (ShipData)sData;

            Ship localShip = findShipById(shipData.id);

            if(localShip == null){
                localShip = new Ship((int)shipData.x, (int)shipData.y);
                localShip.setId(shipData.id);
                this.ships.add(localShip);
            }
            localShip.setCoord(shipData.x, shipData.y);
            localShip.setRotationAngle(shipData.angle);
            localShip.setGhost(shipData.ghostTime);
            localShip.setThrusting(shipData.thrusting);
            localShip.setHealthPoint(shipData.healthPoint);

            localShip.setDestroyed(localShip.getHealthPoint() <= 0);

        }
        this.ships.removeIf(local -> !containsId(state.ships, local.getId()));

        for(EntityData bData: state.bullets){
            Bullet localBullet = (Bullet) findBulletById(bData.id);

            if(localBullet == null){
                localBullet = new Bullet((int)bData.x, (int)bData.y);
                localBullet.setId(bData.id);
                this.bullets.add(localBullet);

            }
            localBullet.setCoord(bData.x, bData.y);
        }
        this.bullets.removeIf(local -> !containsId(state.bullets, local.getId()));

        for(EntityData eData: state.entities){
            GameEntity entity = findEntityById(eData.id);

            if(entity == null){
                EntityFactory factory = entityFactories.get(eData.type);
                if(factory != null){
                    entity = factory.create(eData, this.entities, this.record);
                    if (entity != null) {
                        this.entities.add(entity);
                    } else {
                        System.err.println("Failed to create entity of type: " + eData.type);
                    }
                }
                else{
                    System.err.println("Unknown entity type: " + eData.type);
                }
            }
            else{
                entity.setCoord(eData.x, eData.y);
                entity.setRadius(eData.radius);
                entity.setRotationAngle(eData.angle);
                entity.setGhostForm(eData.ghostTime);
            }
        }

        this.entities.removeIf(local -> (!containsId(state.entities, local.getId())));

        this.notify(new RepaintEvent());

    }

    private Ship findShipById(int id) {
        for(Ship s: ships){
            if(s.getId() == id){
                return s;
            }
        }
        return null;
    }
    private GameEntity findEntityById(int id) {
        for(GameEntity e: entities){
            if(e.getId() == id){
                return e;
            }
        }
        return null;
    }
    private Bullet findBulletById(int id) {
        for(Bullet b: bullets){
            if(b.getId() == id){
                return b;
            }
        }
        return null;
    }
    private boolean containsId(java.util.List<EntityData> list, int id) {
        for(EntityData data: list){
            if(data.id == id){
                return true;
            }
        }
        return false;
    }

    public List<Ship> getShips(){
        return this.ships;
    }

    public GameState buildGameState() {
        List<EntityData> shipData = new ArrayList<>();
        for (Ship s : this.getShips()) {
            shipData.add(s.toEntityData());
        }

        List<EntityData> entityData = new ArrayList<>();
        for (GameEntity e : this.getEntities()) {
            entityData.add(e.toEntityData());
        }

        List<EntityData> bulletData = new ArrayList<>();
        for (Bullet b : this.getBullets()) {
            bulletData.add(b.toEntityData());
        }

        return new GameState(shipData, entityData, bulletData, this.record.getCurScore());
    }

    private long countAsteroids() {
        int c = 0;
        for(GameEntity entity : entities){
            if (entity instanceof Asteroid){
                ++c;
            }
        }
        return c;
    }

}
