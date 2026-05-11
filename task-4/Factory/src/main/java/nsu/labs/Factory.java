package nsu.labs;


import nsu.labs.logic.AssemblyTask;
import nsu.labs.logic.CarController;
import nsu.labs.logic.ComponentSupplier;
import nsu.labs.logic.Dealer;
import nsu.labs.model.Accessory;
import nsu.labs.model.Body;
import nsu.labs.model.Car;
import nsu.labs.model.Motor;
import nsu.labs.storage.Storage;
import nsu.labs.threadpool.ThreadPool;
import nsu.labs.ui.FactoryFrame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Factory {
    public static void main(String[] args) {
        Properties config = loadConfig("config.properties");

        int bodyStorageSize = Integer.parseInt(config.getProperty("StorageBodySize", "100"));
        int motorStorageSize = Integer.parseInt(config.getProperty("StorageMotorSize", "100"));
        int accStorageSize = Integer.parseInt(config.getProperty("StorageAccessorySize", "100"));
        int carStorageSize = Integer.parseInt(config.getProperty("StorageCarSize", "100"));
        int accSuppliersCount = Integer.parseInt(config.getProperty("AccessorySuppliers", "5"));
        int workersCount = Integer.parseInt(config.getProperty("WorkerThreads", "10"));
        int dealersCount = Integer.parseInt(config.getProperty("Dealers", "10"));
        boolean logEnabled = Boolean.parseBoolean(config.getProperty("LogEnabled", "true"));

        Storage<Body> bodyStorage = new Storage<>(bodyStorageSize);
        Storage<Motor> motorStorage = new Storage<>(motorStorageSize);
        Storage<Accessory> accStorage = new Storage<>(accStorageSize);
        Storage<Car> carStorage = new Storage<>(carStorageSize);

        ThreadPool pool = new ThreadPool(workersCount);
        AssemblyTask assemblyTask = new AssemblyTask(bodyStorage, motorStorage, accStorage, carStorage);
        CarController controller = new CarController(pool, assemblyTask);
        carStorage.setStorageItemListener(controller);

        List<ComponentSupplier<?>> allSuppliers = new ArrayList<>();

        ComponentSupplier<Body> bodySup = new ComponentSupplier<>(bodyStorage, Body::new, 500);
        ComponentSupplier<Motor> motorSup = new ComponentSupplier<>(motorStorage, Motor::new, 500);
        bodySup.start();
        motorSup.start();
        allSuppliers.add(bodySup);
        allSuppliers.add(motorSup);

        for (int i = 0; i < accSuppliersCount; i++) {
            ComponentSupplier<Accessory> accSup = new ComponentSupplier<>(accStorage, Accessory::new, 1000);
            accSup.start();
            allSuppliers.add(accSup);
        }

        Dealer[] dealers = new Dealer[dealersCount];
        for (int i = 0; i < dealersCount; i++) {
            dealers[i] = new Dealer(carStorage, i, 2000, logEnabled);
            dealers[i].start();
        }

        for (int i = 0; i < carStorageSize; i++) {
            pool.execute(assemblyTask);
        }

        new FactoryFrame(
                bodyStorage,
                motorStorage,
                accStorage,
                carStorage,
                bodySup,
                motorSup,
                allSuppliers.get(2),
                dealers,
                pool,
                () -> {
                    System.out.println("Shutting down factory...");
                    pool.shutdown();
                    bodySup.interrupt();
                    motorSup.interrupt();
                    for (ComponentSupplier<?> s : allSuppliers) s.interrupt();
                    for (Dealer d : dealers) d.interrupt();
                }
        );
    }

    private static Properties loadConfig(String fileName) {
        Properties props = new Properties();
        try (InputStream is = Factory.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                System.out.println("Config file not found, using defaults.");
                return props;
            }
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}