package com.labs.game;

import javax.swing.*;

import com.labs.game.controller.GameCore;
import com.labs.game.view.GameFrame;

public class AsteroidsGame {
    public static void main(String[] args) {
        GameCore core = new GameCore(600, 800);
        core.start();
    }
}