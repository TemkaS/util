package analitic;





public class Min implements Value {
    private double  value = 0.0;
    private boolean isset = false;


    @Override
    public void add(double item) {
        if (value > item || !isset)
            value = item;
        isset = true;
    }


    @Override
    public double get() {
        if (!isset)
            throw new IllegalStateException();
        return value;
    }


}
