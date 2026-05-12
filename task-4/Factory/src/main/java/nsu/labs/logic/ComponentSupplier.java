package nsu.labs.logic;


import nsu.labs.model.Entity;

import nsu.labs.storage.Storage;

public class ComponentSupplier<T extends Entity> extends Thread{
    private final Storage<T> storage;
    private final Class<T> compClass;
    private int delay;

    public ComponentSupplier(Storage<T> storage, Class<T> compClass, int initialDelay){
        this.storage = storage;
        this.compClass = compClass;
        this.delay = initialDelay;
    }

    public synchronized void setDelay(int delay){
        this.delay = delay;
    }

    @Override
    public void run(){
        try{
            while(!isInterrupted()){
                T item = null;
                try{
                    item = compClass.getConstructor().newInstance();
                }
                catch (Exception ignored){}
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
