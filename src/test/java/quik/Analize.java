package quik;

import java.util.Calendar;
import java.util.List;
import analitic.MaxValue;
import quik.Export.Period;








public class Analize {


    public static void main(String[] args) throws Exception {
        Calendar from = Calendar.getInstance();
        from.set(2015, Calendar.JANUARY, 23);

        List<Candle> result = Export.get("SBER", from, from, Period.MIN1);

        MaxValue max = new MaxValue();
        double delta = 0.0;

        for (Candle item : result) {
            delta += item.getHigh() - item.getLow();
        }

        System.out.println("Avg Delta: " + (delta / result.size()));

    }


}