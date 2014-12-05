package graphs;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class SimpleExample extends JFrame {
    private static final long serialVersionUID = 1L;



    public SimpleExample() {
       setTitle("Simple example");

       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       add(new DrawingLine());

       pack();
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

            Line2D line = new Line2D.Double(10, 10, 100, 100);
            g2d.draw(line);

        }


        @Override
        public Dimension getPreferredSize() {
            return new Dimension(300, 300);
        }

    }

}