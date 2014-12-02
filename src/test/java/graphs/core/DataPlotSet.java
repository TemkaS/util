package graphs.core;


import java.util.Collection;





public class DataPlotSet {
    private final Axis axis;
    private final Collection<DataPlot> data;


    public DataPlotSet(Axis axis, Collection<DataPlot> data) {
        if (axis == null)
            throw new IllegalArgumentException("Axis can't be null");

        if (data == null)
            throw new IllegalArgumentException("Data can't be null");

        this.axis = axis;
        this.data = data;
    }


    public Axis getAxis() {
        return axis;
    }


    public Collection<DataPlot> getDataSet() {
        return data;
    }

}
