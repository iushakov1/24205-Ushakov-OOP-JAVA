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
        this.setLayout(null);
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(width, height));
        this.model = model;
        startGameButton.setBounds(width/2, height/4, width/8, height/8);
        startGameButton.setFocusable(false);
        this.add(startGameButton);
    }

    public void setButtonListener(ActionListener listener){
        startGameButton.addActionListener(listener);
    }

    public JButton getStartGameButton(){
        return this.startGameButton;
    }
}
