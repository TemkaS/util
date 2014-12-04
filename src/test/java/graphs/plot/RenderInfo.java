package graphs.plot;




public class RenderInfo {
    private final int   from;
    private final int   size;

    private double dataMax;
    private double dataMin;


    public RenderInfo(int from, int size) {
        this.from = from;
        this.size = size;
    }


    public double getDataMax() {
        return dataMax;
    }


    public void setDataMax(double dataMax) {
        this.dataMax = dataMax;
    }


    public double getDataMin() {
        return dataMin;
    }


    public void setDataMin(double dataMin) {
        this.dataMin = dataMin;
    }


    public int getFrom() {
        return from;
    }


    public int getSize() {
        return size;
    }
}
