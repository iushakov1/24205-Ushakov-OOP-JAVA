package com.labs.game.view;

import com.labs.game.model.GameModel;
import com.labs.game.model.entities.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener {
    private GameModel model;

    public GamePanel(GameModel model, int width, int height){
        this.model = model;
        this.setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        this.addComponentListener(new GamePanelAdapter(model));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        render(g2d);
    }

    private void render(Graphics2D g2d){
        drawShip(g2d, model.getShip());
        drawAsteroids(g2d, model.getAsteroids());
        drawBullet(g2d, model.getBullets());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private void drawShip(Graphics2D g2d, Ship ship){
        AffineTransform old = g2d.getTransform();

        g2d.translate((int)ship.getX(), (int)ship.getY());
        g2d.rotate(ship.getAngleRadians());

        if(ship.isGhost()){
            boolean isVisible =  System.currentTimeMillis()%2 == 0;
            if(!isVisible){
                g2d.setTransform(old);
                return;
            }
            else{
                g2d.setColor(Color.GRAY);
            }
        }
        else{
            g2d.setColor(Color.WHITE);
        }

        g2d.drawPolygon(ship.getShape());

        g2d.setTransform(old);
    }

    private void drawAsteroids(Graphics2D g2d, List<Asteroid> asteroids){
        for(Asteroid asteroid: asteroids){
            AffineTransform old = g2d.getTransform();

            Polygon shape = asteroid.getShape();

            int xPos = (int)asteroid.getX();
            int yPos = (int)asteroid.getY();
            g2d.translate(xPos, yPos);

            if(asteroid.isGhost()){
                boolean isVisible =  System.currentTimeMillis()%2 == 0;
                if(!isVisible){
                    g2d.setTransform(old);
                    continue;
                }
                else{
                    g2d.setColor(Color.GRAY);
                }
            }
            else{
                g2d.setColor(Color.WHITE);
            }
            g2d.drawPolygon(shape);

            g2d.setTransform(old);
        }
    }

    private void drawBullet(Graphics2D g2d, List<Bullet> bullets){
        for(Bullet bullet: bullets){
            AffineTransform old = g2d.getTransform();

            Polygon shape = bullet.getShape();

            int xPos = (int)bullet.getX();
            int yPos = (int)bullet.getY();
            g2d.translate(xPos, yPos);

            g2d.setColor(Color.BLUE);
            g2d.drawPolygon(shape);

            g2d.setTransform(old);
        }
    }

    private void drawGhost(){

    }
}
