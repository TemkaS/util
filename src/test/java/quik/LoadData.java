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








public class LoadData {


    public static void main(String[] args) throws Exception {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

        Params params = new Params();
        params.setCharset("windows-1251");

        params.setCode("RASP");
        params.setEm("17713");

        Calendar from = Calendar.getInstance();
        from.set(2015, 0, 19);

        params.setFrom(from);
        params.setTill(from);


        parseData(params);


    }


    private static List<Candle> parseData(Params params) throws IOException {
        try (BufferedReader reader = loadData(params)) {
            List<Candle> result = new LinkedList<Candle>();

            while (true) {
                // <TICKER>;<PER>;<DATE>;<TIME>;<OPEN>;<HIGH>;<LOW>;<CLOSE>;<VOL>
                // RASP;1;20150119;100100;24.6700000;25.0000000;24.6300000;24.6300000;1150
                String line = reader.readLine();
                if (Misc.isEmpty(line))
                    break;

                String[] cols = line.split(";");
                Candle   cand = new Candle();
                Calendar date = Calendar.getInstance();

                date.set(Calendar.YEAR,   StringParser.toInteger(cols[2].substring(0, 4), null));
                date.set(Calendar.MONTH,  StringParser.toInteger(cols[2].substring(4, 6), null));
                date.set(Calendar.DATE,   StringParser.toInteger(cols[2].substring(6, 8), null));

                date.set(Calendar.HOUR_OF_DAY, StringParser.toInteger(cols[3].substring(0, 2), null));
                date.set(Calendar.MINUTE, StringParser.toInteger(cols[3].substring(2, 4), null));
                date.set(Calendar.SECOND, StringParser.toInteger(cols[3].substring(4, 6), null));

                date.set(Calendar.MILLISECOND, 0);

                cand.setTime(date.getTimeInMillis());

                cand.setOpen (StringParser.toDouble(cols[4], 0.0));
                cand.setHigh (StringParser.toDouble(cols[5], 0.0));
                cand.setLow  (StringParser.toDouble(cols[6], 0.0));
                cand.setClose(StringParser.toDouble(cols[7], 0.0));
                cand.setValue(StringParser.toInteger(cols[8], 0));

                result.add(cand);

                System.out.println(cand);
            }

            return result;
        }
    }



    private static BufferedReader loadData(Params params) throws IOException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String rand = Long.toString(System.nanoTime());

        String[] result = {
                "market=1",

                "em="   + params.getEm(),
                "code=" + params.getCode(),

                "df="   + params.getFrom().get(Calendar.DATE),
                "mf="   + params.getFrom().get(Calendar.MONTH),
                "yf="   + params.getFrom().get(Calendar.YEAR),
                "from=" + df.format(params.getFrom().getTime()),

                "dt="   + params.getTill().get(Calendar.DATE),
                "mt="   + params.getTill().get(Calendar.MONTH),
                "yt="   + params.getTill().get(Calendar.YEAR),
                "to="   + df.format(params.getTill().getTime()),

                "cn="   + params.getCode(),

                "mstime=on",
                "mstimever=1",
                "MSOR=1",
                "fsp=",
                "at=",
                "p=2",
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
        conn.setRequestProperty("Connection",      "keep-alive");
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
                throw new IOException(Streams.readAll(strerr, params.getCharset()));
        }

        InputStream stream = conn.getInputStream();

        if (Objects.equals(headers.get("content-encoding"), "gzip"))
            stream = new GZIPInputStream(stream);

        return new BufferedReader(new InputStreamReader(stream, params.getCharset()));
    }



    public static class Candle {
        private long time;
        private double open;
        private double close;
        private double high;
        private double low;
        private int value;


        public long getTime() {
            return time;
        }


        public void setTime(long time) {
            this.time = time;
        }


        public double getOpen() {
            return open;
        }


        public void setOpen(double open) {
            this.open = open;
        }


        public double getClose() {
            return close;
        }


        public void setClose(double close) {
            this.close = close;
        }


        public double getHigh() {
            return high;
        }


        public void setHigh(double high) {
            this.high = high;
        }


        public double getLow() {
            return low;
        }


        public void setLow(double low) {
            this.low = low;
        }


        public int getValue() {
            return value;
        }


        public void setValue(int value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return "[" + time + "; " + open + "; " + high + "; " + low + "; " + close + "; " + value + "]";
        }

    }



    public static class Params {
        private String code;
        private String em;
        private Calendar from;
        private Calendar till;
        private String charset;

        public String getCode() {
            return code;
        }


        public void setCode(String code) {
            this.code = code;
        }


        public String getEm() {
            return em;
        }


        public void setEm(String em) {
            this.em = em;
        }


        public Calendar getFrom() {
            return from;
        }


        public void setFrom(Calendar from) {
            this.from = from;
        }


        public Calendar getTill() {
            return till;
        }


        public void setTill(Calendar till) {
            this.till = till;
        }


        public String getCharset() {
            return charset;
        }


        public void setCharset(String charset) {
            this.charset = charset;
        }

    }

}