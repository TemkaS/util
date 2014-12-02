package graphs;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class SimpleExample extends JFrame {
    private static final long serialVersionUID = 1L;



    public SimpleExample() {
       setTitle("Simple example");
       setSize(300, 200);
       setLocationRelativeTo(null);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SimpleExample ex = new SimpleExample();
                ex.setVisible(true);
            }
        });
    }

}