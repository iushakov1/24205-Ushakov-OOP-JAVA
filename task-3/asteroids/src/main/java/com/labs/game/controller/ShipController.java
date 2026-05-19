package com.labs.game.controller;

import com.labs.game.model.entities.Ship;
import com.labs.game.service.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class ShipController extends Observable implements KeyListener {
    private Ship ship;
    public ShipController(Ship ship){
        this.ship = ship;
    }
    private final HashSet<Integer> pressedKeys = new HashSet<>();
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getExtendedKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getExtendedKeyCode() == KeyEvent.VK_W){
            ship.setThrusting(false);
        }
        pressedKeys.remove(e.getExtendedKeyCode());
    }

    public void handleInput(){

        if(pressedKeys.contains(KeyEvent.VK_W)){
            ship.thrust();
            ship.setThrusting(true);
        }
        if(pressedKeys.contains(KeyEvent.VK_A)){
            ship.rotateLeft();
        }
        if(pressedKeys.contains(KeyEvent.VK_D)){
            ship.rotateRight();
        }
        if(pressedKeys.contains(KeyEvent.VK_SPACE)){
            ship.fire();
        }
    }
}
