package com.labs.game.service;

import com.labs.game.event.Event;
import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    private List<Observer> observers = new ArrayList<>();

    public void addObserver (Observer observer){
        observers.add(observer);
    }

    public void notify (Event event){
        for(Observer o: observers){
            o.notify(event);
        }
    }
}
