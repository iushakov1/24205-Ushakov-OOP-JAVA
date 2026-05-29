package com.labs.game.controller;

import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.network.GameClient;
import com.labs.game.network.GameServer;
import com.labs.game.view.MenuPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {
    private MenuPanel menuPanel;
    private GameModel model;
    private ShipController shipController;
    public MenuController(GameModel model, MenuPanel menuPanel, ShipController shipController){
        this.model = model;
        this.menuPanel = menuPanel;
        this.shipController = shipController;
        menuPanel.setButtonListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == menuPanel.getConnectButton()) {
            connectToServer();
        }
        else if (source == menuPanel.getHostButton()) {
            startHost();
        }
        else if (source == menuPanel.getExitButton()) {
            System.exit(0);
        }
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                int port = Integer.parseInt(menuPanel.getPortField().getText());
                String ip = menuPanel.getIpField().getText();

                GameClient client = new GameClient(ip, port, model);

                if (client.connect()) {
                    client.start();

                    this.shipController.setClient(client);

                    SwingUtilities.invokeLater(() -> model.changeStatus(ModelStatus.STARTNEWGAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void startHost() {
        int port = Integer.parseInt(menuPanel.getPortField().getText());
        new Thread(() -> {
            try {
                GameServer server = new GameServer(port);
                server.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try { Thread.sleep(500); } catch (InterruptedException e) {}

        connectToServer();
    }
}
