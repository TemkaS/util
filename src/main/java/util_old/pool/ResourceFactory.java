package util_old.pool;





public interface ResourceFactory<T> {
    public T create() throws ResourceException;

    public void reset(T resource) throws ResourceException;

    public void close(T resource) throws ResourceException;

}
