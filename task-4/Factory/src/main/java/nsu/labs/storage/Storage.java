package nsu.labs.storage;

import nsu.labs.model.Entity;

import java.util.ArrayDeque;
import java.util.Deque;

public class Storage<T extends Entity> {
    private final Deque<T> items = new ArrayDeque<>();
    private final int capacity;
    private StorageItemListener listener;

    public Storage(int capacity){
        this.capacity = capacity;
    }

    public void setStorageItemListener(StorageItemListener listener){
        this.listener = listener;
    }

    public synchronized void put(T item) throws InterruptedException{
        while(items.size() >= capacity){
            wait();
        }
        items.addLast(item);
        notifyAll();
    }

    public synchronized T get() throws InterruptedException{
        while(items.isEmpty()){
            wait();
        }
        T item = items.removeFirst();

        if(listener != null){
            listener.onItemRemoved();
        }

        notifyAll();
        return item;
    }

    public synchronized int size(){
        return items.size();
    }

    public int getCapacity(){
        return capacity;
    }
}
