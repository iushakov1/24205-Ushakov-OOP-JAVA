package com.labs.game.controller;

import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.view.MenuPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {
    private MenuPanel menuPanel;
    private GameModel model;
    MenuController(GameModel model, MenuPanel menuPanel){
        this.model = model;
        this.menuPanel = menuPanel;
        menuPanel.setButtonListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menuPanel.getStartGameButton()){
            model.changeStatus(ModelStatus.STARTNEWGAME);
        }

        if(e.getSource() == this.menuPanel.getExitButton()){
            this.model.changeStatus(ModelStatus.EXITGAME);
        }
    }
}
