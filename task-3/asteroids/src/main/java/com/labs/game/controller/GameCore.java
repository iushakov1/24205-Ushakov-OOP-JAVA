package com.labs.game.controller;

import com.labs.game.model.GameModel;

import javax.swing.Timer;

public class GameCore {
    private final GameModel model;
    private final Timer timer;

    public GameCore(int width, int height){
        model = new GameModel(width, height);

        timer = new Timer(20, e -> {gameTick();});

    }

    private void gameTick(){

        switch (model.getStatus()){
            case PLAYING:
            {
                break;
            }
            case MENU:
            {
                break;
            }
            case EXITGAME:
                this.end();
        }

        model.update();
    }

    public void start(){
        timer.start();
    }

    public void end(){
        this.timer.stop();
    }
}
