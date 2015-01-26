package quik;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import analitic.Max;
import analitic.Min;
import analitic.Sum;
import analitic.Value;
import quik.Export.Period;








public class Analize {



    public static void main(String[] args) throws Exception {
        List<Result> list = new ArrayList<Result>();

        Calendar date = Calendar.getInstance();
        date.set(2015, Calendar.JANUARY, 23);


        Collection<String> need1 = Arrays.asList("SBER", "LKOH", "RASP", "MTSS");
        Collection<String> need = Securities.getByCode().keySet();

        for (String secureCode : need) {
            Result item = getStat(secureCode, date);
            if (item != null)
                list.add(item);

            System.out.println(secureCode + " loaded");
        }

        int show = Math.min(list.size(), 100);


        Collections.sort(list, COMPARE_BY_AVG);

        for (int i = 0; i < show; i++)
            System.out.println(i + ": " + list.get(i));


        Collections.sort(list, COMPARE_BY_DAY);

        for (int i = 0; i < show; i++)
            System.out.println(i + ": " + list.get(i));


    }



    private static Result getStat(String secureCode, Calendar date) throws IOException {
        Security secure = Securities.getByCode(secureCode);

        List<Candle> temp = Export.get(secure, date, date, Period.MIN1);

        if (temp.size() == 0)
            return null;

        Value deltaMax = new Max();
        Value deltaSum = new Sum();
        Value high  = new Max();
        Value low   = new Min();

        for (Candle item : temp) {
            deltaMax.add(item.getHigh() - item.getLow());
            deltaSum.add(item.getHigh() - item.getLow());
            high.add(item.getHigh());
            low .add(item.getLow ());
        }

        double price = temp.get(0).getOpen();

        Result result = new Result();

        result.security = secure;
        result.deltaDay = 100.0 * (high.get() - low.get()) / price;
        result.deltaMax = 100.0 * deltaMax.get() / price;
        result.deltaAvg = 100.0 * deltaSum.get() / price / 540.0;

        return result;
    }



    public static class Result {
        private Security security;
        private double deltaMax;
        private double deltaAvg;
        private double deltaDay;

        @Override
        public String toString() {
            return "[" + security.getCode() + ": " + deltaDay + "%; " + deltaMax + "%; " + deltaAvg + "%]";
        }

    }


    private static final Comparator<Result> COMPARE_BY_AVG = new Comparator<Result>() {
        @Override
        public int compare(Result a, Result b) {
            return Double.compare(b.deltaAvg, a.deltaAvg);
        }
    };


    private static final Comparator<Result> COMPARE_BY_DAY = new Comparator<Result>() {
        @Override
        public int compare(Result a, Result b) {
            return Double.compare(b.deltaDay, a.deltaDay);
        }
    };

}