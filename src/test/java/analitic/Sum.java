package analitic;





public class Sum implements Value {
    private double value = 0;


    @Override
    public void add(double item) {
        value += item;
    }


    @Override
    public double get() {
        return value;
    }

}
