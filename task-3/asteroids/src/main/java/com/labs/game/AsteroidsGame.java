package com.labs.game;

import javax.swing.*;

import com.labs.game.controller.GameCore;

public class AsteroidsGame {
    public static void main(String[] args) {

        System.setProperty("sun.java2d.d3d", "true");
        System.setProperty("sun.java2d.opengl", "true");
        GameCore core = new GameCore(600, 800);
        core.start();
    }
}