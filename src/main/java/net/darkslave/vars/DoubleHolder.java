package net.darkslave.vars;




public class DoubleHolder implements Comparable<DoubleHolder> {
    private double value;


    public DoubleHolder(double value) {
        this.value = value;
    }


    public double get() {
        return value;
    }


    public void set(double value) {
        this.value = value;
    }


    public double increment() {
        return ++value;
    }


    public double decrement() {
        return --value;
    }


    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DoubleHolder) {
            double other = ((DoubleHolder) obj).value;
            return Double.doubleToLongBits(value) == Double.doubleToLongBits(other);
        }
        return false;
    }


    @Override
    public int compareTo(DoubleHolder oth) {
        return Double.compare(value, oth.value);
    }


}
