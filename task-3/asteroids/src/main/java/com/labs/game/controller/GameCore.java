package com.labs.game.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.network.GameServer;
import com.labs.game.network.GameState;

public class GameCore {
    private final GameModel model;
    private final ScheduledExecutorService scheduler;
    private final GameServer networkServer;

    public static final AtomicInteger idGenerator = new AtomicInteger(1);

    public GameCore(GameModel model, GameServer networkServer) {
        this.model = model;
        model.changeStatus(ModelStatus.MENU);

        this.networkServer = networkServer;
        this.scheduler = Executors.newScheduledThreadPool(1);

    }

    public void start() {

        scheduler.scheduleAtFixedRate(this::gameTick, 0, 18, TimeUnit.MILLISECONDS);
    }

    private void gameTick() {
        model.update();

        GameState currentState = model.buildGameState();
        networkServer.broadcast(currentState);
    }
}
