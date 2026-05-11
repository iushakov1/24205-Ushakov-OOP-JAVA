package nsu.labs.logic;


import nsu.labs.model.Entity;

import nsu.labs.storage.Storage;

import java.util.function.Supplier;

public class ComponentSupplier<T extends Entity> extends Thread{
    private final Storage<T> storage;
    private final Supplier<T> factory;
    private int delay;

    public ComponentSupplier(Storage<T> storage, Supplier<T> factory, int initialDelay){
        this.storage = storage;
        this.factory = factory;
        this.delay = initialDelay;
    }

    public synchronized void setDelay(int delay){
        this.delay = delay;
    }

    @Override
    public void run(){
        try{
            while(!isInterrupted()){
                T item = factory.get();
                storage.put(item);

                int currentDelay;
                synchronized (this){
                    currentDelay = delay;
                }
                Thread.sleep(currentDelay);
            }
        }
        catch (InterruptedException e){
            currentThread().interrupt();
        }
    }
}
