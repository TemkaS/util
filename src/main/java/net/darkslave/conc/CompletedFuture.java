package net.darkslave.conc;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;




/**
 * Завершенное задание
 */
public class CompletedFuture<T> implements Future<T> {
    private final T value;
    private final Throwable error;


    public CompletedFuture(T value, Throwable error) {
        this.value = value;
        this.error = error;
    }


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }


    @Override
    public boolean isCancelled() {
        return false;
    }


    @Override
    public boolean isDone() {
        return true;
    }


    @Override
    public T get() throws ExecutionException {
        if (error != null)
            throw new ExecutionException(error);
        return value;
    }


    @Override
    public T get(long timeout, TimeUnit unit) throws ExecutionException {
        return get();
    }

}
