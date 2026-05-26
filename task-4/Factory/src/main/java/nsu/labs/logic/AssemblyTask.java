package nsu.labs.logic;

import nsu.labs.model.*;

import nsu.labs.storage.*;

public class AssemblyTask implements Runnable {
    private final Storage<Body> bodyStorage;
    private final Storage<Motor> motorStorage;
    private final Storage<Accessory> accessoryStorage;
    private final Storage<Car> carStorage;

    public AssemblyTask(Storage<Body> bs, Storage<Motor> ms, Storage<Accessory> as, Storage<Car> cs){
        this.bodyStorage = bs;
        this.motorStorage = ms;
        this.accessoryStorage = as;
        this.carStorage = cs;
    }

    @Override
    public void run(){
        try {
            Body body = bodyStorage.get();
            Motor motor = motorStorage.get();
            Accessory accessory = accessoryStorage.get();

            Car car = new Car(body, motor, accessory);

            carStorage.put(car);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
