package graphs.core;


import java.util.Collection;





public class DataSet {
    private final Axis axis;
    private final Collection<Data> data;


    public DataSet(Axis axis, Collection<Data> data) {
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


    public Collection<Data> getDataSet() {
        return data;
    }

}
