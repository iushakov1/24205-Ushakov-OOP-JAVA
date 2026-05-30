package com.labs.game.network;

import com.labs.game.model.GameModel;
import java.io.*;
import java.net.Socket;

public class GameClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameModel model;
    private String serverAddress;
    private int port;

    public GameClient(String serverAddress, int port, GameModel model){
        this.serverAddress = serverAddress;
        this.port = port;
        this.model = model;
    }

    public boolean connect() {
        try {
            this.socket = new Socket(serverAddress, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Can't connect to the server: " + e.getMessage());
            return false;
        }
    }

    public void start() {
        if (socket != null && socket.isConnected()) {
            new Thread(this::listenToServer).start();
        }
    }

    public void sendCommand(String command) {
        try {
            out.writeObject(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenToServer(){
        try{
            while(true){
                GameState newState = (GameState) in.readObject();

                model.applyNetworkState(newState);
            }
        }
        catch (IOException | ClassNotFoundException e){
            System.err.println("Lost connection");
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
