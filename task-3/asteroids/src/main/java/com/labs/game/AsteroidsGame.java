package com.labs.game;

import javax.swing.*;

import com.labs.game.controller.GameCore;
import com.labs.game.network.GameServer;

public class AsteroidsGame {
    public static void main(String[] args) {
        if(args[0] == "server"){
           // GameServer gameServer = new GameServer();

        }
        else{
            System.setProperty("sun.java2d.d3d", "true");
            System.setProperty("sun.java2d.opengl", "true");
        }


    }
}