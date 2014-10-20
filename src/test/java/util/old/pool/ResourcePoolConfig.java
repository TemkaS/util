package util.old.pool;




public interface ResourcePoolConfig {

    int lifetime();

    int capacity();

    void logging(Throwable e);

}
