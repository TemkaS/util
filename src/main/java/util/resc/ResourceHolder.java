package util.resc;




public interface ResourceHolder<T> extends AutoCloseable {

    public T get();

    @Override
    public void close() throws ResourceException;

}
