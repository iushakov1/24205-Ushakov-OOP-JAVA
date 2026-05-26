package nsu.labs.threadpool;

import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final WorkerThread[] threads;
    private boolean isShutdown = false;

    public ThreadPool(int poolSize){
        threads = new WorkerThread[poolSize];
        for(int i = 0; i < poolSize; ++i){
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    public void execute(Runnable task){
        synchronized (taskQueue){
            if(isShutdown){
                return;
            }
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    public void shutdown(){
        synchronized (taskQueue){
            isShutdown = true;
            taskQueue.notifyAll();;
        }
        for(WorkerThread thread: threads){
            thread.interrupt();
        }
    }

    private class WorkerThread extends Thread{
        @Override
        public void run(){
            while(true){
                Runnable task;
                synchronized (taskQueue){
                    while(taskQueue.isEmpty() && !isShutdown){
                        try{
                            taskQueue.wait();
                        } catch (InterruptedException e){
                            return;
                        }
                    }

                    if(isShutdown && taskQueue.isEmpty()){
                        return;
                    }

                    task = taskQueue.poll();
                }

                if(task != null){
                    try{
                        task.run();
                    }
                    catch (RuntimeException e){
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public int getTaskQueueSize(){
        synchronized (taskQueue){
            return taskQueue.size();
        }
    }
}
