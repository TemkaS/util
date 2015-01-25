package quik;

import java.util.Calendar;
import java.util.List;
import quik.Export.Period;








public class TestExport {


    public static void main(String[] args) throws Exception {

        Calendar from = Calendar.getInstance();
        from.set(2015, Calendar.JANUARY, 23);

        List<Candle> result = Export.get("SBER", from, from, Period.MIN1);

        for (Candle item : result)
            System.out.println(item);

    }


}