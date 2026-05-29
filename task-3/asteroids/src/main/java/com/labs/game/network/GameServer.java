package com.labs.game.network;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.labs.game.controller.GameCore;
import com.labs.game.model.GameModel;

public class GameServer {
    private static int PORT = 8080;
    private GameModel model;
    private GameCore core;

    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public GameServer(int port, int width, int height){
        PORT = port;
        model = new GameModel(width, height);
        core = new GameCore(model, this);
    }

    public void startServer(){
        core.start();

        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server running on port " + PORT);

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("New player connected: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, model, this);
                clients.add(handler);
                new Thread(handler).start();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void broadcast(GameState state){
        for(ClientHandler client : clients) {
            client.sendState(state);
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }


}
