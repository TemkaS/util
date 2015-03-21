package net.darkslave.vars;




public class LongHolder implements Comparable<LongHolder> {
    private long value;


    public LongHolder(long value) {
        this.value = value;
    }


    public long get() {
        return value;
    }


    public void set(long value) {
        this.value = value;
    }


    public long increment() {
        return ++value;
    }


    public long decrement() {
        return --value;
    }


    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LongHolder)
            return value == ((LongHolder) obj).value;
        return false;
    }


    @Override
    public int compareTo(LongHolder oth) {
        return Long.compare(value, oth.value);
    }


}
