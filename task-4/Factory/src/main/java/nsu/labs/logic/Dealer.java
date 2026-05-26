package nsu.labs.logic;

import nsu.labs.model.Car;
import nsu.labs.storage.Storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dealer extends Thread{
    private static final Logger logger = LoggerFactory.getLogger(Dealer.class);
    private final Storage<Car> carStorage;
    private final int id;
    private int delay;
    private final boolean logEnabled;

    public Dealer(Storage<Car> carStorage, int id, int delay, boolean logEnabled){
        this.carStorage = carStorage;
        this.id = id;
        this.delay = delay;
        this.logEnabled = logEnabled;
    }

    public synchronized void setDelay(int delay){
        this.delay = delay;
    }

    @Override
    public void run(){
        try{
            while (!isInterrupted()){
                Car car = carStorage.get();

                if(logEnabled) {
                    logger.info("Dealer {}: Auto {} (Body: {}, Motor: {}, Accessory: {})",
                            id,
                            car.getId(),
                            car.getBody().getId(),
                            car.getMotor().getId(),
                            car.getAccessory().getId()
                    );

                }

                int currentDelay;
                synchronized (this){
                    currentDelay = delay;
                }
                Thread.sleep(currentDelay);
            }
        } catch(InterruptedException e){
            currentThread().interrupt();
        }
    }
}
