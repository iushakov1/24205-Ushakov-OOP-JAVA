package com.labs.game.view;

import com.labs.game.model.GameModel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GamePanelAdapter{
    private GameModel model;

    public GamePanelAdapter(GameModel model){
        this.model = model;
    }

}
