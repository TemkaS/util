package graphs.plot;

import graphs.core.Rectangle;
import graphs.data.NumberData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
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


    @Override
    public RenderInfo getRenderInfo() {
        // TODO Auto-generated method stub
        return null;
    }



    public void draw(Graphics2D g2d, Rectangle area, RenderInfo info, int[] coordX) {
        Color  prevColor  = g2d.getColor();
        Stroke prevStroke = g2d.getStroke();

        g2d.setColor(lineColor);
        g2d.setStroke(lineStroke);

        double scaleY = (info.getDataMax() - info.getDataMin()) / area.getHeight();
        int[]  coordY = new int[info.getSize()];

        for (int i = 0; i < info.getSize(); i++) {
            coordY[i] = (int) ((data.get(i) - info.getDataMin()) * scaleY);
        }

        g2d.drawPolyline(coordX, coordY, info.getSize());

        g2d.setColor (prevColor);
        g2d.setStroke(prevStroke);
    }





}
