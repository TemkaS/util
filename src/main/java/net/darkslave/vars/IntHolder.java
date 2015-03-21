package net.darkslave.vars;




public class IntHolder implements Comparable<IntHolder> {
    private int value;


    public IntHolder(int value) {
        this.value = value;
    }


    public int get() {
        return value;
    }


    public void set(int value) {
        this.value = value;
    }


    public int increment() {
        return ++value;
    }


    public int decrement() {
        return --value;
    }


    @Override
    public int hashCode() {
        return value;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntHolder)
            return value == ((IntHolder) obj).value;
        return false;
    }


    @Override
    public int compareTo(IntHolder oth) {
        return Integer.compare(value, oth.value);
    }


}
