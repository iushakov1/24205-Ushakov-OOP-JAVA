package com.labs.game.controller;

import com.labs.game.model.Ship;
import com.labs.game.service.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;

public class ShipController extends Observable implements KeyListener {
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
        pressedKeys.remove(e.getExtendedKeyCode());
    }

    public void handleInput(Ship ship){

        if(pressedKeys.contains(KeyEvent.VK_W)){
            ship.thrust();
        }
        if(pressedKeys.contains(KeyEvent.VK_A)){
            ship.rotateLeft();
        }
        if(pressedKeys.contains(KeyEvent.VK_D)){
            ship.rotateRight();
        }
    }
}
