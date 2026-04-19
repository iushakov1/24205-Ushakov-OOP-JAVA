package com.labs.game.view;

import com.labs.game.controller.ShipController;
import com.labs.game.event.Event;
import com.labs.game.event.RepaintEvent;
import com.labs.game.event.StatusChangeEvent;
import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.service.Observer;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements Observer {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);

    GamePanel gamePanel;
    MenuPanel menuPanel;
    GameModel model;


    private static final String MENU_KEY = "MENU";
    private static final String GAME_KEY = "GAME";
    public GameFrame(GameModel model, ShipController shipController, int width, int height){
        this.model = model;
        model.addObserver(this);
        this.menuPanel = new MenuPanel(model,width, height);
        this.gamePanel = new GamePanel(model, width, height);

        mainContainer.add(menuPanel, MENU_KEY);
        mainContainer.add(gamePanel, GAME_KEY);
        mainContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY, 5),
                BorderFactory.createLineBorder(Color.WHITE, 2)
        ));

        this.add(mainContainer);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addKeyListener(shipController);

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public GamePanel getGamePanel(){
        return gamePanel;
    }

    public void update(){
        ModelStatus currentStatus = model.getStatus();

        if(this.model.getStatus() == ModelStatus.PLAYING){
            cardLayout.show(mainContainer, GAME_KEY);
            this.gamePanel.repaint();
        }
        if(this.model.getStatus() == ModelStatus.MENU){
            cardLayout.show(mainContainer, MENU_KEY);
            this.menuPanel.repaint();
        }
    }

    @Override
    public void notify(Event event) {
        if(event.getClass().equals(StatusChangeEvent.class)){
            this.update();
        }
        if(event.getClass().equals(RepaintEvent.class)){
            this.update();
        }
    }

    public MenuPanel getMenuPanel(){
        return this.menuPanel;
    }

}
