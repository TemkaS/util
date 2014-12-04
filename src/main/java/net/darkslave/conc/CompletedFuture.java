package net.darkslave.conc;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;




/**
 * Завершенное задание
 */
public class CompletedFuture<T> implements Future<T> {
    private final Throwable error;
    private final T value;


    public CompletedFuture(Callable<T> command) {
        Exception error = null;
        T value = null;

        try {
            value = command.call();
        } catch (Exception e) {
            error = e;
        }

        this.value = value;
        this.error = error;
    }


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
