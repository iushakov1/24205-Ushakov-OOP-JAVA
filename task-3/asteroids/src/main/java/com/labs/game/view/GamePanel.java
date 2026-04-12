package com.labs.game.view;

import com.labs.game.model.GameModel;
import com.labs.game.model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel implements ActionListener {
    private GameModel model;

    public GamePanel(GameModel model){
        this.model = model;
        this.setPreferredSize(new Dimension(600, 800));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        render(g2d);
    }

    private void render(Graphics2D g2d){
        drawShip(g2d, model.getShip());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private void drawShip(Graphics2D g2d, Ship ship){
        AffineTransform old = g2d.getTransform();

        g2d.translate((int)ship.getX(), (int)ship.getY());
        g2d.rotate(ship.getAngleRadians());

        int[] xPoints = {15, -10, -10};
        int[] yPoints = {0, 10, -10};

        g2d.setColor(Color.WHITE);
        g2d.drawPolygon(xPoints, yPoints, 3);

        g2d.setTransform(old);
    }
}
