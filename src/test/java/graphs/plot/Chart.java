package graphs.plot;

import graphs.axis.Axis;
import graphs.axis.AxisBase;
import graphs.axis.AxisDataRender;
import graphs.axis.Location.Type;
import graphs.base.Base;
import graphs.core.Rectangle;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;




public class Chart {
    private AxisBase axisBase;
    private List<AxisDataRender> axisData = new LinkedList<AxisDataRender>();


    public void setBaseAxis(Axis axis, Base base) {
        if (axis.getType() != Type.X)
            throw new IllegalArgumentException("Axis location is incorrect");

        this.axisBase = new AxisBase(axis, base);
    }



    public void setDataAxis(Axis axis, DataRender ... data) {
        if (axis.getType() != Type.Y)
            throw new IllegalArgumentException("Axis location is incorrect");

        axisData.add(new AxisDataRender(axis, data));
    }


    public void draw(Graphics2D g2d, Rectangle drawArea) {
        if (axisBase == null)
            throw new IllegalStateException("Base axis is not defined");

        if (axisData.size() == 0)
            throw new IllegalStateException("No data axes are defined");


        Rectangle outset = new Rectangle();

        // рисуем данные
        for (AxisDataRender item : axisData)
            item.draw(g2d, drawArea, outset, 0, 0);

        axisBase.draw(g2d, drawArea, outset);

    }

}
