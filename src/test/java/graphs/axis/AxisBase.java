package graphs.axis;


import java.awt.Graphics2D;
import graphs.base.Base;
import graphs.core.Rectangle;





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


    public void draw(Graphics2D g2d, Rectangle drawArea, Rectangle outset) {
        axis.draw(g2d, drawArea, outset);
    }
}
