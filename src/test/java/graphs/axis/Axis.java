package graphs.axis;

import graphs.axis.Location.Type;
import graphs.core.Rectangle;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;



public class Axis {
    public static final Font   DEFAULT_AXIS_TITLE_FONT  = new Font("Tahoma", Font.PLAIN, 12);
    public static final Color  DEFAULT_AXIS_TITLE_COLOR = Color.black;
    public static final Stroke DEFAULT_AXIS_LINE_STROKE = new BasicStroke(0.8f);
    public static final Color  DEFAULT_AXIS_LINE_COLOR  = Color.black;

    public static final Font   DEFAULT_TICK_TITLE_FONT  = new Font("Tahoma", Font.PLAIN, 8);
    public static final Color  DEFAULT_TICK_TITLE_COLOR = Color.black;
    public static final Stroke DEFAULT_TICK_LINE_STROKE = new BasicStroke(0.5f);
    public static final Color  DEFAULT_TICK_LINE_COLOR  = Color.black;

    public static final int    DEFAULT_AXIS_OUTSET = 20;
    public static final Shape  DEFAULT_ARROW_SHAPE;



    static {
        Polygon temp = new Polygon();
        temp.addPoint(0, 0);
        temp.addPoint(-2, 2);
        temp.addPoint(2, 2);
        DEFAULT_ARROW_SHAPE = temp;
    }


    private final Type type;

    private String   title;
    private Location location;

    private Font   axisTitleFont;
    private Color  axisTitleColor;
    private Stroke axisLineStroke;
    private Color  axisLineColor;

    private Font   tickTitleFont;
    private Color  tickTitleColor;
    private Stroke tickLineStroke;
    private Color  tickLineColor;

    private Shape    arrowShape;
    private boolean arrowVisible;

    private int  axisOutset;


    public Axis(Type type, String title) {
        this.type  = type;
        this.title = title;

        this.location = type.getDefaultLocation();

        this.axisTitleFont  = DEFAULT_AXIS_TITLE_FONT;
        this.axisTitleColor = DEFAULT_AXIS_TITLE_COLOR;
        this.axisLineStroke = DEFAULT_AXIS_LINE_STROKE;
        this.axisLineColor  = DEFAULT_AXIS_LINE_COLOR;

        this.tickTitleFont  = DEFAULT_TICK_TITLE_FONT;
        this.tickTitleColor = DEFAULT_TICK_TITLE_COLOR;
        this.tickLineStroke = DEFAULT_TICK_LINE_STROKE;
        this.tickLineColor  = DEFAULT_TICK_LINE_COLOR;

        this.axisOutset   = DEFAULT_AXIS_OUTSET;
        this.arrowShape   = DEFAULT_ARROW_SHAPE;
        this.arrowVisible = true;

    }


    public Type getType() {
        return type;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public Location getLocation() {
        return location;
    }


    public void setLocation(Location location) {
        if (location == null)
            throw new NullPointerException();
        if (location.getType() != this.type)
            throw new IllegalArgumentException("Location type must be " + type);
        this.location = location;
    }


    public boolean isArrowVisible() {
        return arrowVisible;
    }


    public void setArrowVisible(boolean arrowVisible) {
        this.arrowVisible = arrowVisible;
    }


    public Font getAxisTitleFont() {
        return axisTitleFont;
    }


    public void setAxisTitleFont(Font axisTitleFont) {
        this.axisTitleFont = axisTitleFont;
    }


    public Color getAxisTitleColor() {
        return axisTitleColor;
    }


    public void setAxisTitleColor(Color axisTitleColor) {
        this.axisTitleColor = axisTitleColor;
    }


    public Stroke getAxisLineStroke() {
        return axisLineStroke;
    }


    public void setAxisLineStroke(Stroke axisLineStroke) {
        this.axisLineStroke = axisLineStroke;
    }


    public Color getAxisLineColor() {
        return axisLineColor;
    }


    public void setAxisLineColor(Color axisLineColor) {
        this.axisLineColor = axisLineColor;
    }


    public Font getTickTitleFont() {
        return tickTitleFont;
    }


    public void setTickTitleFont(Font tickTitleFont) {
        this.tickTitleFont = tickTitleFont;
    }


    public Color getTickTitleColor() {
        return tickTitleColor;
    }


    public void setTickTitleColor(Color tickTitleColor) {
        this.tickTitleColor = tickTitleColor;
    }


    public Stroke getTickLineStroke() {
        return tickLineStroke;
    }


    public void setTickLineStroke(Stroke tickLineStroke) {
        this.tickLineStroke = tickLineStroke;
    }


    public Color getTickLineColor() {
        return tickLineColor;
    }


    public void setTickLineColor(Color tickLineColor) {
        this.tickLineColor = tickLineColor;
    }


    public Shape getArrowShape() {
        return arrowShape;
    }


    public void setArrowShape(Shape arrowShape) {
        this.arrowShape = arrowShape;
    }


    public int getAxisOutset() {
        return axisOutset;
    }


    public void setAxisOutset(int axisOutset) {
        this.axisOutset = axisOutset;
    }


    void draw(Graphics2D g2d, Rectangle drawArea, Rectangle outset) {
        Font   prevFont   = g2d.getFont();
        Color  prevColor  = g2d.getColor();
        Stroke prevStroke = g2d.getStroke();

        g2d.setColor(axisLineColor);
        g2d.setStroke(axisLineStroke);


        switch (location) {
            case TOP: {
                int offsetTop = drawArea.getTop() + outset.addTop(axisOutset);
                g2d.drawLine(drawArea.getLeft(), offsetTop, drawArea.getRight(), offsetTop);

            } break;

            case RIGHT: {
                int offsetRight = drawArea.getRight() - outset.addRight(axisOutset);
                g2d.drawLine(offsetRight, drawArea.getBottom(), offsetRight, drawArea.getTop());

            } break;

            case BOTTOM: {
                int offsetBottom = drawArea.getBottom() - outset.addBottom(axisOutset);
                g2d.drawLine(drawArea.getLeft(), offsetBottom, drawArea.getRight(), offsetBottom);

            } break;

            case LEFT: {
                int offsetLeft = drawArea.getLeft() + outset.addLeft(axisOutset);
                g2d.drawLine(offsetLeft, drawArea.getBottom(), offsetLeft, drawArea.getTop());

            } break;

            default:
                throw new IllegalStateException("Unknown location type");
        }


        // g2d.drawLine(5, 5, (int) area.getWidth() - 10, (int) area.getHeight() - 10);


        g2d.setFont  (prevFont);
        g2d.setColor (prevColor);
        g2d.setStroke(prevStroke);
    }

}
