package com.labs.game.controller;

import com.labs.game.model.GameModel;
import com.labs.game.view.GameFrame;

import javax.swing.Timer;

public class GameCore {
    private final GameModel model;
    private final GameFrame frame;
    private final ShipController shipController;
    private MenuController menuController;
    private final Timer timer;

    public GameCore(int width, int height){
        model = new GameModel(width, height);
        shipController = new ShipController(model.getShip());

        frame = new GameFrame(model, shipController, width, height);
        menuController = new MenuController(model, frame.getMenuPanel());
        timer = new Timer(20, e -> {gameTick();});

    }

    private void gameTick(){

        switch (model.getStatus()){
            case PLAYING:
            {
                shipController.handleInput();
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
        this.frame.dispose();
        this.timer.stop();
    }
}
