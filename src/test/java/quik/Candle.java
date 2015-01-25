package quik;

public class Candle {
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