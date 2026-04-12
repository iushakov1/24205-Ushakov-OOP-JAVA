package com.labs.game.model;

import java.util.Vector;

public class GameModel {
    private final int width;
    private final int height;
    private boolean isGameOver = false;

    private Ship ship;

    public GameModel(int width, int height){
        this.width = width;
        this.height = height;
        initModel();
    }

    public void initModel(){
        ship = new Ship(width/2, height/2);
    }

    public void update(){
        if(isGameOver){
            return;
        }
        ship.update(this.width, this.height);
    }

    public Ship getShip(){
        return ship;
    }
}
