package com.labs.game.view;

import com.labs.game.controller.ShipController;
import com.labs.game.event.Event;
import com.labs.game.event.RepaintEvent;
import com.labs.game.event.StatusChangeEvent;
import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.service.Observer;
import java.awt.event.KeyListener;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements Observer {
    private JLayeredPane layeredPane = new JLayeredPane();
    GamePanel gamePanel;
    MenuPanel menuPanel;
    GameModel model;
    ModelStatus lastStatus;

    public GameFrame(GameModel model, KeyListener shipController, int width, int height){
        this.model = model;

        model.addObserver(this);
        this.menuPanel = new MenuPanel(model,width, height);
        this.gamePanel = new GamePanel(model, width, height);

        this.gamePanel.setBounds(0, 0, width, height);
        this.menuPanel.setBounds(0, 0, width, height);

        this.layeredPane.setPreferredSize(new Dimension(width, height));
        this.gamePanel.setBounds(0, 0, width, height);
        this.menuPanel.setBounds(0, 0, width, height);

        this.layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);
        this.layeredPane.add(menuPanel, JLayeredPane.PALETTE_LAYER);

        this.add(layeredPane);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.gamePanel.addKeyListener(shipController);
        this.gamePanel.setFocusable(true);

        this.addComponentListener(new GameFrameAdapter(this));

        this.setFocusable(true);
        this.requestFocusInWindow();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        update();
    }

    public GamePanel getGamePanel(){
        return gamePanel;
    }

    public void update(){
        ModelStatus status = model.getStatus();
        if (status == ModelStatus.PLAYING || status == ModelStatus.STARTNEWGAME) {
            if (menuPanel.isVisible()) {
                menuPanel.setVisible(false);
                this.requestFocusInWindow();
                this.gamePanel.requestFocusInWindow();
            }
        } else {
            if (!menuPanel.isVisible()) {
                menuPanel.setVisible(true);
            }
        }
        repaint();
    }

    @Override
    public void notify(Event event) {
        if (event instanceof RepaintEvent) {
            if (this.gamePanel != null) {
                this.gamePanel.repaint();
            }
        }
        if(event instanceof RepaintEvent){
            gamePanel.repaint();
        }
        if (event instanceof StatusChangeEvent) {
            SwingUtilities.invokeLater(this::update);
        }
    }

    public MenuPanel getMenuPanel(){
        return this.menuPanel;
    }

    public void resizeLayers() {
        int w = layeredPane.getWidth();
        int h = layeredPane.getHeight();

        if (gamePanel.getWidth() == w && gamePanel.getHeight() == h)
            return;

        gamePanel.setBounds(0, 0, w, h);
        menuPanel.setBounds(0, 0, w, h);

    }

}
