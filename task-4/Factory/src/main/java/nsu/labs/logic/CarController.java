package nsu.labs.logic;

import nsu.labs.storage.StorageItemListener;
import nsu.labs.threadpool.ThreadPool;

public class CarController implements StorageItemListener {
    private final ThreadPool pool;
    private final AssemblyTask assemblyTask;

    public CarController(ThreadPool pool, AssemblyTask task){
        this.pool = pool;
        this.assemblyTask = task;
    }

    @Override
    public void onItemRemoved(){
        pool.execute(assemblyTask);
    }
}
