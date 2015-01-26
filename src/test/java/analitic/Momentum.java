package analitic;

import java.util.LinkedList;
import java.util.List;





public class Momentum implements Value {
    private final List<Double> data = new LinkedList<Double>();
    private final Avg mean = new Avg();
    private final double pow;


    public Momentum(double pow) {
        this.pow = pow;
    }


    @Override
    public void add(double item) {
        data.add(item);
        mean.add(item);
    }


    @Override
    public double get() {
        double avg = mean.get();
        double sum = 0.0;

        for (Double item : data)
            sum+= Math.pow(item - avg, pow);

        return sum / data.size();
    }


}
