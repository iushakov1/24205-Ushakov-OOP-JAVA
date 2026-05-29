package com.labs.game.controller;

import com.labs.game.model.entities.Ship;
import com.labs.game.network.GameClient;
import com.labs.game.service.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class ShipController implements KeyListener {
    private final HashSet<Integer> pressedKeys = new HashSet<>();
    private GameClient client;

    public ShipController(GameClient client){
        this.client = client;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        if (pressedKeys.contains(key)) return;

        pressedKeys.add(key);
        sendStateToServer(key, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getExtendedKeyCode();
        pressedKeys.remove(key);
        sendStateToServer(key, false);
    }

    private void sendStateToServer(int key, boolean isPressed) {
        String action = isPressed ? "_PRESSED" : "_RELEASED";
        switch (key) {
            case KeyEvent.VK_W -> client.sendCommand("W" + action);
            case KeyEvent.VK_A -> client.sendCommand("A" + action);
            case KeyEvent.VK_D -> client.sendCommand("D" + action);
            case KeyEvent.VK_SPACE -> {
                if (isPressed) client.sendCommand("SPACE_PRESSED");
            }
        }
    }

    public void setClient(GameClient client){
        this.client = client;
    }

}
