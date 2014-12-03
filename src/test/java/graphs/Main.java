package graphs;

import graphs.axis.Axis;
import graphs.axis.Location;
import graphs.core.Rectangle;
import graphs.core.StringName;
import graphs.data.Data;
import graphs.data.NumberData;
import graphs.plot.DataRender;
import graphs.plot.LineDataRender;
import graphs.plot.Chart;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;






public class Main {

    public static void main(String[] args) throws IOException {
        Chart plot = new Chart();
        Axis axisX = new Axis(new StringName("x"), "", Location.Type.X);    axisX.setAxisLineColor(Color.red);
        Axis axisY = new Axis(new StringName("y"), "", Location.Type.Y);    axisY.setAxisLineColor(Color.blue);
        Axis axisZ = new Axis(new StringName("z"), "", Location.Type.Y);    axisZ.setAxisLineColor(Color.green);

        Data dataX = new NumberData();
        plot.setAxisX(axisX, dataX);

        Data dataY = new NumberData();
        DataRender dataPlotY = new LineDataRender(dataY);
        plot.setAxisY(axisY, dataPlotY);

        Data dataZ = new NumberData();
        DataRender dataPlotZ = new LineDataRender(dataZ);
        plot.setAxisY(axisZ, dataPlotZ);


        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Rectangle area = new Rectangle(0, 0, 800, 600);

        Graphics2D g2d = image.createGraphics();
        plot.draw(g2d, area);
        g2d.dispose();

        ImageIO.write(image, "png", new File("d:/test.png"));
        System.out.println("done!");
    }


    private static BufferedImage createImage(int wd, int hg) {
        BufferedImage image = new BufferedImage(wd, hg, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setBackground(new Color(0xfff7f7));
        g2d.clearRect(0, 0, wd, hg);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, null, 0);
        g2d.setStroke(bs);

        g2d.setColor(new Color(0xff0000));

        g2d.draw(new Line2D.Float(0, 0, 1920, 1080));



        g2d.dispose();

        return image;
    }


}
