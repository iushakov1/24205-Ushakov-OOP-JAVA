package com.labs.game.network;

import com.labs.game.controller.GameCore;
import com.labs.game.model.GameModel;
import com.labs.game.model.ModelStatus;
import com.labs.game.model.entities.Bullet;
import com.labs.game.model.entities.Ship;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private GameServer server;
    private GameModel model;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Ship playerShip;

    public ClientHandler(Socket socket, GameModel model, GameServer server) {
        this.socket = socket;
        this.model = model;
        this.server = server;

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            playerShip = new Ship(400, 300);
            playerShip.setGhost(0);
            if (model.getStatus() != ModelStatus.PLAYING) {
                model.changeStatus(ModelStatus.STARTNEWGAME);
            }

            playerShip.setId(GameCore.idGenerator.getAndIncrement());

            model.getShips().add(playerShip);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            while (true) {
                String command = (String) in.readObject();

                handleCommand(command);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player Disconnected: " + socket.getInetAddress());
        } finally {
            disconnect();
        }
    }

    private void handleCommand(String command) {
        if(playerShip.isDestroyed()){
            return;
        }
        switch (command) {


            case "W_PRESSED": playerShip.setThrusting(true); break;
            case "W_RELEASED": playerShip.setThrusting(false); break;

            case "A_PRESSED": playerShip.setRotatingLeft(true); break;
            case "A_RELEASED": playerShip.setRotatingLeft(false); break;

            case "D_PRESSED": playerShip.setRotatingRight(true); break;
            case "D_RELEASED": playerShip.setRotatingRight(false); break;

            case "SPACE_PRESSED":
                if (playerShip.canShoot() && !playerShip.isDestroyed()) {
                    playerShip.resetCooldown();
                    Bullet b = new Bullet(playerShip);
                    b.setId(GameCore.idGenerator.getAndIncrement());
                    model.getBullets().add(b);
                }
                break;
        }
    }

    public void sendState(GameState state) {
        try {
            out.reset();
            out.writeObject(state);
            out.flush();
        } catch (IOException e) {
            System.out.println("Cannot send data to client");
            disconnect();
        }
    }

    private void disconnect() {
        model.getShips().remove(playerShip);
        server.removeClient(this);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}