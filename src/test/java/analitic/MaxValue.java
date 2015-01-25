package analitic;





public class MaxValue {
    private double value = Double.MIN_VALUE;


    public void add(double item) {
        if (value < item)
            value = item;
    }


    public double get() {
        return value;
    }


}
