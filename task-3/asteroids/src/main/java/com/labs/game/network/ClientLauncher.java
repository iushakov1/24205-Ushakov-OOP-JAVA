package com.labs.game.network;

import com.labs.game.controller.MenuController;
import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.controller.ShipController;
import com.labs.game.view.GameFrame;

public class ClientLauncher {
    private static final int width = 800;
    private static final int height = 600;
    public static void main(String[] args) {
        System.setProperty("sun.java2d.d3d", "true");
        System.setProperty("sun.java2d.opengl", "true");
        GameModel model = new GameModel(width, height);
        ShipController controller = new ShipController(null);
        GameFrame frame = new GameFrame(model, controller, width, height);
        MenuController menuController = new MenuController(model, frame.getMenuPanel(), controller);


        javax.swing.Timer gameTimer = new javax.swing.Timer(16, e -> {
            frame.repaint();
        });
        gameTimer.start();

        model.changeStatus(ModelStatus.MENU);
        frame.update();
    }
}
