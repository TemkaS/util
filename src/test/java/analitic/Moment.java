package analitic;

import java.util.LinkedList;
import java.util.List;





public class Moment {
    private final List<Double> data = new LinkedList<Double>();
    private final AvgValue mean = new AvgValue();
    private final double  pow;


    public Moment(double pow) {
        this.pow = pow;
    }


    public void add(double item) {
        data.add(item);
        mean.add(item);
    }


    public double get() {
        double sum = 0.0;
        double avg = mean.get();

        for (Double item : data)
            sum+= Math.pow(item - avg, pow);

        return sum / data.size();
    }


}
