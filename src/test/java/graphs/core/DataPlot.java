package graphs.core;




abstract public class DataPlot {
    private final Data data;


    public DataPlot(Data data) {
        if (data == null)
            throw new IllegalArgumentException("Parameter can't be null");

        this.data = data;
    }


}
