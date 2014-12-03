package graphs.axis;


import graphs.plot.DataRender;





public class AxisDataRender {
    private final Axis axis;
    private final DataRender[] data;


    public AxisDataRender(Axis axis, DataRender ... data) {
        if (axis == null)
            throw new IllegalArgumentException("Axis can't be null");

        if (data == null)
            throw new IllegalArgumentException("Data render can't be null");

        if (data.length == 0)
            throw new IllegalArgumentException("Data render can't be empty");

        this.axis = axis;
        this.data = data;
    }


    public Axis getAxis() {
        return axis;
    }


    public DataRender[] getDataRender() {
        return data;
    }

}
