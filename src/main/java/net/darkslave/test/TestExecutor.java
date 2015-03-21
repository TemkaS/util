package net.darkslave.test;





public class TestExecutor {

    private TestExecutor() {}


    public static Result measure(Runnable target, int warming, int measure) {
        if (target == null)
            throw new IllegalArgumentException("Target method can't be null");

        if (warming < 0)
            throw new IllegalArgumentException("Warming count " + warming + " is not correct");

        if (measure < 1)
            throw new IllegalArgumentException("Measure count " + measure + " is not correct");

        long[] time = new long[measure];

        // разогрев
        for (int index = 0; index < warming; index++) {
            target.run();
        }

        // целевые измерения
        for (int index = 0; index < measure; index++) {
            long started = System.nanoTime();
            target.run();
            time[index] = System.nanoTime() - started;
        }

        // сбор статистики
        long timeMax = time[0];
        long timeMin = time[0];

        double summAvg = 0;
        double summStd = 0;

        for (int index = 0; index < measure; index++) {
            long d = time[index];

            if (timeMax < d)
                timeMax = d;

            if (timeMin > d)
                timeMin = d;

            summAvg+= d;
            summStd+= d * d;
        }

        double timeAvg = summAvg / measure;
        double timeStd = Math.sqrt(summStd / measure - timeAvg * timeAvg);

        return new Result(timeAvg, timeMin, timeMax, timeStd);
    }



    public static class Result {
        private final double avg;
        private final double min;
        private final double max;
        private final double std;


        private Result(double avg, double min, double max, double std) {
            this.avg = avg;
            this.min = min;
            this.max = max;
            this.std = std;
        }


        public double avg() {
            return avg;
        }


        public double min() {
            return min;
        }


        public double max() {
            return max;
        }


        public double std() {
            return std;
        }


        private String toStringCache;

        @Override
        public String toString() {
            if (toStringCache == null) {
                StringBuilder result = new StringBuilder();

                result.append("Avg: ").append(TimeFormat.format(avg)).append("; ");
                result.append("Min: ").append(TimeFormat.format(min)).append("; ");
                result.append("Max: ").append(TimeFormat.format(max)).append("; ");
                result.append("Std: ").append(TimeFormat.format(std));

                toStringCache = result.toString();
            }

            return toStringCache;
        }

    }



    public static enum TimeFormat {
        NANOSECONDS (1, "ns"),
        MICROSECONDS(1000, "μs"),
        MILLISECONDS(1000 * 1000, "ms"),
        SECONDS     (1000 * 1000 * 1000, "s");


        private final double factor;
        private final String suffix;


        private TimeFormat(double factor, String suffix) {
            this.factor = factor;
            this.suffix = suffix;
        }


        private String format0(double source) {
            double absolute = Math.abs(source);
            String format;

            if (absolute < 20) {
                format = "%.2f%s";
            } else
            if (absolute < 200) {
                format = "%.1f%s";
            } else {
                format = "%.0f%s";
            }

            return String.format(format, source, suffix);
        }


        public static String format(double source) {
            double absolute = Math.abs(source);
            TimeFormat result;

            if (absolute >= SECONDS.factor) {
                result = SECONDS;
            } else
            if (absolute >= MILLISECONDS.factor) {
                result = MILLISECONDS;
            } else
            if (absolute >= MICROSECONDS.factor) {
                result = MICROSECONDS;
            } else {
                result = NANOSECONDS;
            }

            return result.format0(source / result.factor);
        }

    }


}

