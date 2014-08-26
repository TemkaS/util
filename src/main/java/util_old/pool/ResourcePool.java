package util_old.pool;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




public class ResourcePool<E> {
    private final ResourceFactory<E> factory;

    private final Object[] pool;
    private final Lock lock;
    private final Condition busy;

    private final int capacity;
    private final Thread service;

    private int count = 0;
    private int usage = 0;



    /**
     * Создать пул ресурсов
     *
     * @param capacity   - лимит ресурсов (при достижении лимита, потоки запрашивающие ресурс будут ожидать освобождения ресурса)
     * @param factory - фабрика ресурсов
     */
    public ResourcePool(ResourceFactory<E> factory, ResourcePoolConfig config) {
        if (factory == null)
            throw new IllegalArgumentException("Resource factory can't be null");

        if (config == null)
            throw new IllegalArgumentException("Configuration can't be null");

        this.factory = factory;

        capacity = config.capacity();

        if (capacity <= 0)
            throw new IllegalArgumentException("Pool capacity (" + capacity + ") is not correct");

        pool = new Object[capacity];
        lock = new ReentrantLock();
        busy = lock.newCondition();

        if (config.lifetime() > 0) {
            service = new Thread(new ServiceThread(config));
            service.setDaemon(true);
            service.start();
        } else {
            service = null;
        }

    }


    /**
     * Запросить ресурс из пула.
     * если в пуле есть свободный ресурс, то возвращается он;
     * если ресурсов нет, но лимит ресурсов не превышен, то создается новый ресурс;
     * в ином случае поток останавливается, до появления свободных ресурсов.
     *
     * @return обертка ресурса
     */
    @SuppressWarnings("unchecked")
    public Resource<E> acquire() throws ResourceException, InterruptedException {
        lock.lockInterruptibly();
        try {
            if (usage < 0)
                throw new ResourceException("Resource pool is closed");

            ResourceHolder item = null;

            do {
                if (count > 0) {
                    count--;
                    item = (ResourceHolder) pool[count];
                    pool[count] = null;
                } else
                if (usage < capacity) {
                    item = new ResourceHolder(factory.create());
                } else {
                    busy.await();
                }
            } while (item == null);

            usage++;

            return item;

        } finally {
            lock.unlock();
        }
    }


    /**
     * Возврат ресурса в пул.
     * если при сбросе ресурса произошла ошибка, то ресурс закрывается;
     * если лимит ресурсов превышен, то ресурс закрывается;
     * в иных случаях ресурс возвращается в пул.
     */
    private void release(ResourceHolder item) throws ResourceException, InterruptedException {
        lock.lockInterruptibly();
        try {
            if (usage > 0)
                usage--;

            if (count + usage >= capacity || usage < 0) {
                factory.close(item.resource);
                return;
            }

            try {
                factory.reset(item.resource);

                pool[count] = item;
                count++;

            } catch (ResourceException e) {
                try {
                    factory.close(item.resource);
                } catch (Exception c) {
                    e.addSuppressed(c);
                }

                throw e;

            } finally {
                busy.signal();
            }

        } finally {
            lock.unlock();
        }
    }


    /**
     * Проверка срока жизни ресурсов.
     * ресурсы с истекшим сроком жизни удаляются из пула.
     */
    @SuppressWarnings("unchecked")
    private void validate(long expired) throws ResourceException, InterruptedException {
        lock.lockInterruptibly();
        try {
            ResourceException error = null;
            ResourceHolder item;

            int index = count,
                removed = 0;

            while (--index >= 0) {
                item = (ResourceHolder) pool[index];

                if (item.created > expired)
                    continue;

                try {
                    factory.close(item.resource);
                } catch (Exception e) {
                    if (error == null)
                        error = new ResourceException("Resource closing error");
                    error.addSuppressed(e);
                }

                count--;
                System.arraycopy(pool, index + 1, pool, index, count - index);
                pool[count] = null;
                removed++;
            }

            if (removed > 0)
                busy.signal();

            if (error != null)
                throw error;

        } finally {
            lock.unlock();
        }
    }


    @SuppressWarnings("unchecked")
    public void close() throws ResourceException, InterruptedException {
        lock.lockInterruptibly();
        try {
            ResourceException error = null;
            ResourceHolder item;

            while (--count >= 0) {
                item = (ResourceHolder) pool[count];

                try {
                    factory.close(item.resource);
                } catch (Exception e) {
                    if (error == null)
                        error = new ResourceException("Resource closing error");
                    error.addSuppressed(e);
                }

                pool[count] = null;
            }

            usage = -1;

            if (service != null && !service.isInterrupted()) {
                try {
                    service.interrupt();
                } catch (Exception e) {
                    if (error == null)
                        error = new ResourceException("Resource closing error");
                    error.addSuppressed(e);
                }
            }

            busy.signalAll();

            if (error != null)
                throw error;

        } finally {
            lock.unlock();
        }
    }



    /**
     * Обертка ресурса
     */
    private class ResourceHolder implements Resource<E> {
        private final E resource;
        private final long created;

        public ResourceHolder(E resource) {
            this.resource = resource;
            this.created  = System.currentTimeMillis();
        }

        @Override
        public E get() {
            return resource;
        }

        @Override
        public void close() throws ResourceException, InterruptedException {
            release(this);
        }

    }


    /**
     * Сервисный поток
     * -- инициирует удаление устаревших ресурсов
     */
    private class ServiceThread implements Runnable {
        private final ResourcePoolConfig config;

        public ServiceThread(ResourcePoolConfig config) {
            this.config = config;
        }

        @Override
        public void run() {
            int delta = config.lifetime();
            int sleep = Math.max(delta / 10, 1000);

            do {
                try {
                    validate(System.currentTimeMillis() - delta);
                    Thread.sleep(sleep);
                } catch(Exception e) {
                    config.logging(e);
                    if (e instanceof InterruptedException)
                        return;
                }
            } while (true);
        }

    }

}
