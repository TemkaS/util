package quik;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.GZIPInputStream;
import net.darkslave.io.Streams;
import net.darkslave.util.Misc;
import net.darkslave.util.StringParser;





public class Export {
    private static final String CHARSET = "windows-1251";


    static {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }


    public static enum Period {
        MIN1 (2),
        MIN5 (3),
        MIN10(4),
        MIN15(5),
        MIN30(6),
        HOUR (7),
        DAY  (8),
        WEEK (9),
        MONTH(10);


        private final int period;


        private Period(int period) {
            this.period = period;
        }


        public int getPeriod() {
            return period;
        }

    }


    public static List<Candle> get(String secureCode, Calendar from, Calendar till, Period period) throws IOException {
        Security secure = Securities.getByCode(secureCode);

        if (secure == null)
            throw new IllegalArgumentException("Security " + secureCode + " is not found");

        return get(secure, from, till, period);
    }


    public static List<Candle> get(Security secure, Calendar from, Calendar till, Period period) throws IOException {
        try (BufferedReader reader = getContent(secure, from, till, period)) {
            List<Candle> result = new LinkedList<Candle>();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);

            while (true) {
                // <TICKER>;<PER>;<DATE>;<TIME>;<OPEN>;<HIGH>;<LOW>;<CLOSE>;<VOL>
                // RASP;1;20150119;100100;24.6700000;25.0000000;24.6300000;24.6300000;1150
                String line = reader.readLine();

                if (Misc.isEmpty(line))
                    break;

                String[] cols = line.split(";");
                Candle   cand = new Candle();

                calendar.set(Calendar.YEAR, StringParser.toInteger(cols[2].substring(0, 4), null));
                calendar.set(Calendar.MONTH, StringParser.toInteger(cols[2].substring(4, 6), null));
                calendar.set(Calendar.DATE, StringParser.toInteger(cols[2].substring(6, 8), null));

                calendar.set(Calendar.HOUR_OF_DAY, StringParser.toInteger(cols[3].substring(0, 2), null));
                calendar.set(Calendar.MINUTE, StringParser.toInteger(cols[3].substring(2, 4), null));
                calendar.set(Calendar.SECOND, StringParser.toInteger(cols[3].substring(4, 6), null));

                cand.setTime(calendar.getTimeInMillis());

                cand.setOpen(StringParser.toDouble(cols[4], 0.0));
                cand.setHigh(StringParser.toDouble(cols[5], 0.0));
                cand.setLow(StringParser.toDouble(cols[6], 0.0));
                cand.setClose(StringParser.toDouble(cols[7], 0.0));
                cand.setValue(StringParser.toInteger(cols[8], 0));

                result.add(cand);
            }

            return result;
        }
    }



    private static BufferedReader getContent(Security secure, Calendar from, Calendar till, Period period) throws IOException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String rand = Long.toString(System.nanoTime());

        String[] result = {
                "market=" + secure.getMarket(),

                "em="   + secure.getEmitent(),
                "code=" + secure.getCode(),

                "df=" + from.get(Calendar.DATE),
                "mf=" + from.get(Calendar.MONTH),
                "yf=" + from.get(Calendar.YEAR),
                "from=" + df.format(from.getTime()),

                "dt=" + till.get(Calendar.DATE),
                "mt=" + till.get(Calendar.MONTH),
                "yt=" + till.get(Calendar.YEAR),
                "to=" + df.format(till.getTime()),

                "cn=" + secure.getCode(),

                "mstime=on",
                "mstimever=1",
                "MSOR=1",
                "fsp=",
                "at=",
                "p=" + period.getPeriod(),
                "datf=1",
                "dtf=1",
                "tmf=1",
                "sep=3",
                "sep2=1",
                "f=" + rand,
                "e=.txt"
        };

        String path = "http://195.128.78.52/" + rand + ".txt?" + Misc.join("&", result);

        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();

        conn.setRequestProperty("Host", "195.128.78.52");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
        conn.setRequestProperty("Connection", "keep-alive");
        conn.connect();

        int code = conn.getResponseCode();

        Map<String, String> headers = new HashMap<String, String>();

        for (Map.Entry<String, List<String>> e : conn.getHeaderFields().entrySet()) {
            if (e.getKey() != null) {
                headers.put(e.getKey().toLowerCase(), e.getValue().get(0));
            }
        }

        try (InputStream strerr = conn.getErrorStream()) {
            if (strerr != null)
                throw new IOException(Streams.readAll(strerr, CHARSET));
        }

        InputStream stream = conn.getInputStream();

        if (Objects.equals(headers.get("content-encoding"), "gzip"))
            stream = new GZIPInputStream(stream);

        return new BufferedReader(new InputStreamReader(stream, CHARSET));
    }


}
