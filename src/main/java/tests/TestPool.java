package tests;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import util.resc.ResourceHolder;
import util.resc.ResourcePool;
import util.resc.ResourceService;





class Item {
    private final String name;
    private int value = 0;

    public Item(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void inc() {
        this.value++;
    }

    public String getName() {
        return name;
    }
}





public class TestPool {
    private static final AtomicInteger index = new AtomicInteger(0);

    public static void main(String[] args) throws FileNotFoundException {

        final ScheduledExecutorService pool = Executors.newScheduledThreadPool(50);

        final ResourcePool<Item> resc = new ResourcePool<Item>(new ResourceService<Item>() {
            @Override
            public Item create() throws Exception {
                return new Item("item-" + index.incrementAndGet());
            }

            @Override
            public void reset(Item resource) throws Exception {
                // Thread.sleep(10);
            }

            @Override
            public void close(Item resource) throws Exception {
                System.err.println(resource.getName() + " closed");
            }
        }, Integer.MAX_VALUE);


        for (int i = 0; i < 10; i++) {
            final String fi = "thread-" + i;
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.interrupted()) {
                        try (ResourceHolder<Item> item = resc.acquire(10, TimeUnit.MILLISECONDS)) {
                            item.get().inc();
                            Thread.sleep(10);
                        } catch (Exception e) {
                            System.out.println(fi + " " + e);
                            if (e instanceof InterruptedException)
                                return;
                        }
                    }
                }
            });
        }


        try {
            Thread.sleep(1000);
            pool.shutdownNow();
            pool.awaitTermination(100, TimeUnit.MILLISECONDS);

            System.out.println("==== " + resc.size());

            while (resc.size() > 0) {
                ResourceHolder<Item> item = resc.acquire();
                System.out.println(item.get().getName() + ": " + item.get().getValue());
            }

        } catch (Exception ie) {
            ie.printStackTrace(System.out);
            return;
        }

    }

}
