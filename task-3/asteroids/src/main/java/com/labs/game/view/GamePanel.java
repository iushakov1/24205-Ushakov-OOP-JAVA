package com.labs.game.view;

import com.labs.game.event.Event;
import com.labs.game.event.RecordUpdateEvent;
import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.model.entities.*;
import com.labs.game.service.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, ComponentListener {
    private GameModel model;
    private int width;
    private int height;

    public GamePanel(GameModel model, int width, int height){
        this.model = model;
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        this.addComponentListener(this);
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;


        double scaleX = (double) this.width / this.model.getWidth();
        double scaleY = (double) this.height / this.model.getHeight();

        g2d.scale(scaleX, scaleY);

        render(g2d);
    }

    private void render(Graphics2D g2d){
        drawShip(g2d, model.getShip());
        drawAsteroids(g2d, model.getAsteroids());
        drawBullet(g2d, model.getBullets());
        drawRecordBar(g2d);
        if(this.model.getStatus() == ModelStatus.GAMEOVER){

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    private void drawShip(Graphics2D g2d, Ship ship){
        AffineTransform old = g2d.getTransform();

        g2d.translate((int)ship.getX(), (int)ship.getY());
        g2d.rotate(ship.getAngleRadians());

        if(ship.isThrusting()){
            drawThruster(g2d, ship);
        }

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

    private void drawThruster(Graphics2D g2d, Ship ship){
        int shipSize = (int) ship.getRadius();

        Polygon flameShape = new Polygon();

        flameShape.addPoint(-shipSize - 5, 0);
        flameShape.addPoint(-shipSize + 2, shipSize/2);
        flameShape.addPoint(-shipSize + 2, -shipSize/2);

        boolean flicker = System.currentTimeMillis()%2==0;
        if(flicker){
            g2d.setColor(Color.GRAY);
        }
        else{
            g2d.setColor(Color.WHITE);
        }

        g2d.fillPolygon(flameShape);

        if(flicker){
            g2d.setColor(Color.GRAY);
        }
        else{
            g2d.setColor(Color.WHITE);
        }
        g2d.drawPolygon(flameShape);
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

    private void drawRecordBar(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

        String curScoreText = "Score: " + model.getRecord().getCurScore();
        String maxScoreText = "Best: " + model.getRecord().getMaxScore();

        FontMetrics fm = g2d.getFontMetrics();
        int maxScoreWidth = fm.stringWidth(maxScoreText);

        g2d.drawString(curScoreText, 20, 30);
        g2d.drawString(maxScoreText, (int) (this.model.getWidth() - maxScoreWidth-10), 30);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        this.width = this.getWidth();
        this.height = this.getHeight();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

}
