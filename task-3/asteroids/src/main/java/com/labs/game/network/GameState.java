package com.labs.game.network;

import com.labs.game.network.Data.EntityData;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    public List<EntityData> ships;
    public List<EntityData> entities;
    public List<EntityData> bullets;
    public int currentScore;

    public GameState(List<EntityData> ships, List<EntityData> entities, List<EntityData> bullets, int currentScore) {
        this.ships = ships;
        this.entities = entities;
        this.bullets = bullets;
        this.currentScore = currentScore;
    }

}
