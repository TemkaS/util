package analitic;





public class MinValue {
    private double value = Double.MAX_VALUE;


    public void add(double item) {
        if (value > item)
            value = item;
    }


    public double get() {
        return value;
    }


}
