package analitic;





public class AvgValue {
    private double value = 0;
    private double count = 0;


    public void add(double item) {
        value+= item;
        count+= 1;
    }


    public double get() {
        return value / count;
    }


}
