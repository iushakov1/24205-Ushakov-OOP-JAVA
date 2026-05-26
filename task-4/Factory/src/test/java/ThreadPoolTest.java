import nsu.labs.threadpool.ThreadPool;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {
    private ThreadPool pool;
    private final int PoolSize = 3;

    @BeforeEach
    void setUp(){
        pool = new ThreadPool(PoolSize);
    }

    @AfterEach
    void shutDown(){
        pool.shutdown();
    }

    @Test
    void singleTask() throws InterruptedException{
        CountDownLatch latch = new CountDownLatch(1);

        pool.execute(latch::countDown);
        boolean finished = latch.await(1, TimeUnit.SECONDS);
        assertTrue(finished);
    }

    @Test
    void multipleTasks() throws InterruptedException{
        int taskCount = 50;
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicInteger completedCount = new AtomicInteger(0);

        for(int i = 0; i < taskCount; ++i){
            pool.execute(() -> {
                completedCount.incrementAndGet();
                latch.countDown();
            });
        }

        boolean allFinished = latch.await(3, TimeUnit.SECONDS);
        assertTrue(allFinished);
        assertEquals(taskCount, completedCount.get());
        assertEquals(0, pool.getTaskQueueSize());
    }

    @Test
    void testShutdown() throws InterruptedException{
        CountDownLatch started = new CountDownLatch(1);
        pool.execute(() -> {
            started.countDown();
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException ignored) {}
        });

        started.await(500, TimeUnit.MILLISECONDS);
        pool.shutdown();

        AtomicInteger counter = new AtomicInteger(0);
        pool.execute(counter::incrementAndGet);

        Thread.sleep(200);
        assertEquals(0, counter.get());
    }
}
