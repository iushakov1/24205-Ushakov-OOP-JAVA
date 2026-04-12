package com.labs.game.controller;

import com.labs.game.model.GameModel;
import com.labs.game.view.GameFrame;
import com.labs.game.view.GamePanel;

import javax.swing.Timer;

public class GameCore {
    private GameModel model;
    private GamePanel panel;
    private GameFrame frame;
    private ShipController shipController;
    private Timer timer;

    public GameCore(int width, int height){
        model = new GameModel(width, height);
        shipController = new ShipController();
        frame = new GameFrame(model, shipController);
        panel = frame.getGamePanel();
        timer = new Timer(16, e -> gameTick());
    }

    private void gameTick(){
        shipController.handleInput(model.getShip());
        model.update();
        panel.repaint();
    }

    public void start(){
        timer.start();
    }
}
