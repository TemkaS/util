package graphs.plot;

import graphs.data.NumberData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;



public class LineDataRender implements DataRender {
    public static final Stroke DEFAULT_LINE_STROKE = new BasicStroke(0.8f);
    public static final Color  DEFAULT_LINE_COLOR  = Color.black;

    private final NumberData data;

    private Stroke lineStroke;
    private Color  lineColor;


    public LineDataRender(NumberData data) {
        this.data = data;
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
