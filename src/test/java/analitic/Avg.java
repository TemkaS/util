package analitic;





public class Avg implements Value {
    private double value = 0;
    private double count = 0;


    @Override
    public void add(double item) {
        value+= item;
        count+= 1;
    }


    @Override
    public double get() {
        if (count == 0)
            throw new IllegalStateException();
        return value / count;
    }


}
