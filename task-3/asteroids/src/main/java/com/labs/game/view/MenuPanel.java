package com.labs.game.view;

import com.labs.game.model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private GameModel model;
    private JButton startGameButton = new JButton("Start Game");
    MenuPanel(GameModel model, int width, int height){
        this.addComponentListener(new MenuPanelAdapter(this));
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(width, height));
        this.model = model;
        startGameButton.setBounds(0, height/4, width/2, height/10);
        startGameButton.setFont(new Font("Comic Sans", Font.BOLD, width/20));
        startGameButton.setFocusable(false);
        this.add(startGameButton);
    }

    public void setButtonListener(ActionListener listener){
        startGameButton.addActionListener(listener);
    }

    public JButton getStartGameButton(){
        return this.startGameButton;
    }

    public void updateBounds(int width, int height){
        startGameButton.setBounds(0, height/4, width/2, height/10);
    }
}
