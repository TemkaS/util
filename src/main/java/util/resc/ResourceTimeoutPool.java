package util.resc;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;




public class ResourceTimeoutPool<E> extends ResourcePool<E> {
    private static final long TIMEOUT_THRESHOLD = TimeUnit.MILLISECONDS.toNanos(10);

    private static final ScheduledExecutorService SERVICE_THREAD_POOL = Executors.newScheduledThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable command) {
            Thread thread = new Thread(command);
            thread.setDaemon(true);
            return thread;
        }
    });


    protected final long timeout;


    public ResourceTimeoutPool(ResourceService<E> service, int capacity, long time, TimeUnit unit) {
        super(service, capacity);

        timeout = unit.toNanos(time);

        if (timeout == 0)
            return;

        if (timeout < TIMEOUT_THRESHOLD)
            throw new IllegalArgumentException("Timeout " + time + " " + unit + " is not allowed");

        SERVICE_THREAD_POOL.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    validate();
                } catch (ResourceException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, timeout >>> 3, TimeUnit.NANOSECONDS);

    }


    /**
     * Проверка валидности ресурсов
     */
    public void validate() throws ResourceException {
        Collection<Node<E>> rejected = new LinkedList<Node<E>>();

        lock.lock();
        try {
            long expired = System.nanoTime() - timeout;

            TimeoutNode<E> node = (TimeoutNode<E>) head;

            head = null;
            size = 0;

            while (node != null) {
                TimeoutNode<E> next = (TimeoutNode<E>) node.next;

                if (node.created - expired > 0) {
                    node.next = head;
                    head = node;
                    size++;
                } else {
                    node.next = null;
                    rejected.add(node);
                }

                node = next;
            }

            if (rejected.size() > 0)
                busy.signal();

        } finally {
            lock.unlock();
        }

        ResourceException error = null;

        for (Node<E> node : rejected) {
            try {
                service.close(node.value);
            } catch (Exception e) {
                error = ResourceException.wrap(error, e);
            }
        }

        if (error != null)
            throw error;

    }


    @Override
    protected Node<E> createNew() throws Exception {
        return new TimeoutNode<E>(this, service.create());
    }


    /**
     * Контейнер ресурса с временем жизни
     */
    protected static class TimeoutNode<E> extends Node<E> {
        protected final long created;

        protected TimeoutNode(ResourcePool<E> pool, E value) {
            super(pool, value);
            created = System.nanoTime();
        }

    }

}
