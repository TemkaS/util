package graphs;

import graphs.axis.Axis;
import graphs.axis.Location;
import graphs.base.NumberBase;
import graphs.core.Rectangle;
import graphs.data.NumberData;
import graphs.plot.Chart;
import graphs.plot.LineDataRender;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class SimpleExample extends JFrame {
    private static final long serialVersionUID = 1L;



    public SimpleExample() {
       setTitle("Simple example");

       setDefaultCloseOperation(EXIT_ON_CLOSE);

       add(new DrawingLine());

       pack();
       setLocationRelativeTo(null);

       setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleExample();
            }
        });
    }


    private static class DrawingLine extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;


            Chart chart = new Chart();
            Axis axisX = new Axis(Location.Type.X, "");  axisX.setAxisLineColor(Color.red);
            Axis axisY = new Axis(Location.Type.Y, "");  axisY.setAxisLineColor(Color.blue);
            Axis axisZ = new Axis(Location.Type.Y, "");  axisZ.setAxisLineColor(Color.green);

            chart.setBaseAxis(axisX, new NumberBase("x", new double[0]));

            chart.setDataAxis(axisY, new LineDataRender(new NumberData("y", new double[0])));
            chart.setDataAxis(axisZ, new LineDataRender(new NumberData("z", new double[0])));


            Rectangle area = new Rectangle(0, 0, 800, 600);

            chart.draw(g2d, area);


        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 600);
        }

    }

}