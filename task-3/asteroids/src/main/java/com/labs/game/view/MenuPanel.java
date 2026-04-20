package com.labs.game.view;

import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private GameModel model;
    private JButton startGameButton = new JButton("Start Game");

    private final Color bgColor = new Color(0, 0, 0, 150);
    private final Font gameOverFont = new Font(Font.MONOSPACED, Font.BOLD, 50);

    MenuPanel(GameModel model, int width, int height){
        this.model = model;
        this.setOpaque(false);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLayout(null);
        this.setPreferredSize(new Dimension(width, height));

        this.addComponentListener(new MenuPanelAdapter(this));
        this.setPreferredSize(new Dimension(width, height));

        startGameButton.setBounds(width/4, height/4, width/2, height/10);
        startGameButton.setFont(new Font("Comic Sans", Font.BOLD, width/20));
        startGameButton.setFocusable(false);
        this.add(startGameButton);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        if (model.getStatus() == ModelStatus.GAMEOVER) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(gameOverFont);
            g2d.drawString("GAME OVER", getWidth()/4, getHeight()/6);
        }
    }

    public void setButtonListener(ActionListener listener){
        startGameButton.addActionListener(listener);
    }

    public JButton getStartGameButton(){
        return this.startGameButton;
    }

    public void updateBounds(int width, int height){
        startGameButton.setBounds(width/4, height/4, width/2, height/10);
    }
}
