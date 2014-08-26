package util.resc;





public interface ResourceService<T> {

    public T create() throws Exception;

    public void reset(T resource) throws Exception;

    public void close(T resource) throws Exception;

}
