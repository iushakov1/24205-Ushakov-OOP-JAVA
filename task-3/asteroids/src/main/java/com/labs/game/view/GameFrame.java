package com.labs.game.view;

import com.labs.game.controller.ShipController;
import com.labs.game.model.GameModel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    GamePanel gamePanel;
    public GameFrame(GameModel model, ShipController shipController){
        this.gamePanel = new GamePanel(model);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(shipController);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.add(gamePanel);
        this.pack();
        this.setVisible(true);
    }

    public GamePanel getGamePanel(){
        return gamePanel;
    }

}
