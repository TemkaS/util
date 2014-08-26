package util_old.pool;




public interface Resource<T> extends AutoCloseable {
    public T get();

    @Override
    public void close() throws ResourceException, InterruptedException;

}
