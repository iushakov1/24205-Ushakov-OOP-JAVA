package com.labs.game.view;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameFrameAdapter extends ComponentAdapter {
    private GameFrame gameFrame;

    public GameFrameAdapter(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        this.gameFrame.resizeLayers();
    }
}
