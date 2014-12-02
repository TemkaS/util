package graphs.core;

import graphs.core.Location.Type;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;




public class Plot {
    private DataSet axisX;
    private Map<Name, DataPlotSet> axisY = new LinkedHashMap<Name, DataPlotSet>(4);



    public void setAxisX(Axis axis, Data data) {
        if (axis.getType() != Type.X)
            throw new IllegalArgumentException("Axis location is incorrect");

        if (this.axisX != null)
            throw new IllegalStateException("Axis X is already defined");

        this.axisX = new DataSet(axis, Arrays.asList(data));
    }



    public void setAxisY(Axis axis, DataPlot ... data) {
        if (axis.getType() != Type.Y)
            throw new IllegalArgumentException("Axis location is incorrect");

        if (axisY.get(axis.getName()) != null)
            throw new IllegalStateException("Axis `" + axis.getName() + "` is already defined");

        axisY.put(axis.getName(), new DataPlotSet(axis, Arrays.asList(data)));
    }


    public void draw(Graphics2D g2d, Rectangle drawArea) {
        if (axisX == null)
            throw new IllegalStateException("Axis X is not defined");

        if (axisY.size() == 0)
            throw new IllegalStateException("Axis Y is not defined");

        Rectangle outset = new Rectangle();

        // рисуем данные
        for (Map.Entry<Name, DataPlotSet> e : axisY.entrySet())
            e.getValue().getAxis().draw(g2d, drawArea, outset);

        axisX.getAxis().draw(g2d, drawArea, outset);

    }

}
