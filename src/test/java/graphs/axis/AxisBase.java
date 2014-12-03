package graphs.axis;


import graphs.base.Base;





public class AxisBase {
    private final Axis axis;
    private final Base base;


    public AxisBase(Axis axis, Base base) {
        if (axis == null)
            throw new IllegalArgumentException("Axis can't be null");

        if (base == null)
            throw new IllegalArgumentException("Base can't be null");

        this.axis = axis;
        this.base = base;
    }


    public Axis getAxis() {
        return axis;
    }


    public Base getBase() {
        return base;
    }

}
