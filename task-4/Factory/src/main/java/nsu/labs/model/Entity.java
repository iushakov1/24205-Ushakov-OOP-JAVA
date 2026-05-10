package nsu.labs.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Entity {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int id;

    public Entity(){
        this.id = idGenerator.getAndIncrement();
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        return String.valueOf(id);
    }
}
