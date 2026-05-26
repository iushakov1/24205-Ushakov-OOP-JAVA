import nsu.labs.model.Entity;
import nsu.labs.storage.Storage;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class StorageTest {

    static class TestItem extends Entity{}

    private Storage<TestItem> storage;
    private final int capacity = 2;

    @BeforeEach
    void setUp() {
        storage = new Storage<>(capacity);
    }

    @Test
    void testSimplePutGet() throws InterruptedException{
        TestItem item = new TestItem();
        storage.put(item);
        assertEquals(1, storage.size());

        TestItem retrieved = storage.get();
        assertEquals(item, retrieved);
        assertEquals(0, storage.size());
    }

    @Test
    void testPutBlocksWhenFull() throws InterruptedException{
        storage.put(new TestItem());
        storage.put(new TestItem());

        AtomicBoolean wasBlocked = new AtomicBoolean(true);
        Thread producer = new Thread(() -> {
            try{
                storage.put(new TestItem());
                wasBlocked.set(false);
            }
            catch(InterruptedException ignored){}
        });

        producer.start();
        Thread.sleep(200);

        assertEquals(Thread.State.WAITING, producer.getState());
        assertTrue(wasBlocked.get());

        storage.get();
        producer.join(500);
        assertEquals(capacity, storage.size());

    }

    @Test
    void testGetBlocksWhenEmpty() throws InterruptedException{
        Thread consumer = new Thread(() -> {
            try{
                storage.get();
            }
            catch (InterruptedException ignored){}
        });

        consumer.start();
        Thread.sleep(200);

        assertEquals(Thread.State.WAITING, consumer.getState());

        storage.put(new TestItem());
        consumer.join(500);
        assertEquals(0, storage.size());

    }


}
