package nsu.labs.logic;

import nsu.labs.model.Entity;

import nsu.labs.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentSupplier<T extends Entity> extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(ComponentSupplier.class);
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
                catch (Exception e){
                    e.printStackTrace();
                }
                storage.put(item);
                String supplierClassName = compClass.getSimpleName();
                logger.debug("receive new {} from {} supplier, componentId: {} ", supplierClassName, Thread.currentThread().getId(), item.getId());

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
