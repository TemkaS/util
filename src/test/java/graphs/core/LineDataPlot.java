package graphs.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;



public class LineDataPlot extends DataPlot {
    public static final Stroke DEFAULT_LINE_STROKE = new BasicStroke(0.8f);
    public static final Color  DEFAULT_LINE_COLOR  = Color.black;

    private Stroke lineStroke;
    private Color  lineColor;


    public LineDataPlot(Data data) {
        super(data);
        this.lineStroke = DEFAULT_LINE_STROKE;
        this.lineColor  = DEFAULT_LINE_COLOR;
    }


    public Stroke getLineStroke() {
        return lineStroke;
    }


    public void setLineStroke(Stroke lineStroke) {
        this.lineStroke = lineStroke;
    }


    public Color getLineColor() {
        return lineColor;
    }


    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }


}
