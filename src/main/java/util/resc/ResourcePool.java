package util.resc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




public class ResourcePool<E> {
    protected final ResourceService<E> service;
    protected final int capacity;
    protected int allowed;
    protected int size;

    protected final Lock lock;
    protected final Condition busy;

    protected Node<E> head;


    /**
     * Создать пул ресурсов
     *
     * @param capacity - лимит ресурсов (при достижении лимита, потоки запрашивающие ресурс будут ожидать освобождения ресурса)
     * @param service  - сервис ресурсов
     */
    public ResourcePool(ResourceService<E> service, int capacity) {
        if (service == null)
            throw new IllegalArgumentException("Resource service can't be null");

        if (capacity < 1)
            throw new IllegalArgumentException("Pool capacity is not correct");

        this.service  = service;
        this.capacity = capacity;
        this.allowed  = capacity;

        this.lock = new ReentrantLock();
        this.busy = this.lock.newCondition();
    }


    /**
     * Количество ресурсов, дозволенных для запроса
     */
    public int allowed() {
        return allowed;
    }


    /**
     * Количество ресурсов сохраненных в пуле
     */
    public int size() {
        return size;
    }


    /**
     * Запросить ресурс:
     * если в пуле есть свободный ресурс, то возвращается ресурс из пула;
     * если ресурсов нет, но лимит ресурсов не превышен, то создается новый ресурс;
     * в ином случае поток останавливается, до появления свободных ресурсов.
     *
     * @return обертка ресурса
     */
    public ResourceHolder<E> acquire() throws ResourceException {
        return acquire(0, TimeUnit.NANOSECONDS);
    }


    public ResourceHolder<E> acquire(long time, TimeUnit unit) throws ResourceException {
        try {
            if (time > 0) {
                if (!lock.tryLock(time, unit))
                    throw new ResourceTimeoutException();
            } else {
                lock.lockInterruptibly();
            }
        } catch (InterruptedException ie) {
            throw ResourceException.wrap(ie);
        }

        try {
            long expired = System.nanoTime() + unit.toNanos(time);

            while (true) {
                if (head != null) {
                    Node<E> node = head;

                    head = node.next;
                    node.next = null;

                    allowed--;
                    size--;

                    return node;
                }

                if (allowed > 0) {
                    allowed--;
                    break;
                }

                if (time > 0) {
                    long wait = expired - System.nanoTime();

                    if (wait <= 0)
                        throw new ResourceTimeoutException();

                    busy.awaitNanos(wait);

                } else {
                    busy.await();
                }

            }
        } catch (InterruptedException ie) {
            throw ResourceException.wrap(ie);
        } finally {
            lock.unlock();
        }

        try {
            return createNew();
        } catch (Exception e) {
            release0(null, false);
            throw ResourceException.wrap(e);
        }

    }


    /**
     * Возврат ресурса в пул
     */
    private void release(Node<E> node) throws ResourceException {
        ResourceException error = null;

        try {
            service.reset(node.value);
        } catch (Exception e) {
            error = ResourceException.wrap(e);
        }

        if (release0(node, error == null))
            return;

        try {
            service.close(node.value);
        } catch (Exception e) {
            error = ResourceException.wrap(error, e);
        }

        if (error != null)
            throw error;
    }


    /**
     * Возврат ресурса в пул
     */
    private boolean release0(Node<E> node, boolean append) {
        lock.lock();
        try {
            if (allowed >= capacity)
                return false;

            if (append) {
                node.next = head;
                head = node;
                size++;
            }

            allowed++;

            busy.signal();

            return append;

        } finally {
            lock.unlock();
        }
    }


    /**
     * Создание нового ресурса
     */
    protected Node<E> createNew() throws Exception {
        return new Node<E>(this, service.create());
    }


    /**
     * Контейнер ресурса
     */
    protected static class Node<E> implements ResourceHolder<E> {
        protected final ResourcePool<E> pool;
        protected final E value;
        protected Node<E> next;

        protected Node(ResourcePool<E> pool, E value) {
            this.pool  = pool;
            this.value = value;
        }

        @Override
        public E get() {
            return value;
        }

        @Override
        public void close() throws ResourceException {
            pool.release(this);
        }

    }


}
