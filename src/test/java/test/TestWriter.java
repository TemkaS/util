package test;

import net.darkslave.io.StringWriter;





public class TestWriter {


    public static void main(String[] args) throws Exception {
        StringWriter wr = new StringWriter(512);

        wr.append('0');
        wr.write('-');

        wr.append("1");
        wr.write('-');

        wr.append("123", 1, 2);
        wr.write('-');

        wr.write(new char[] { '3' });
        wr.write('-');

        wr.write('4');
        wr.write('-');

        wr.write("5");
        wr.write('-');

        wr.write(new char[] { '5' , '6', '7'} , 1, 1);
        wr.write('-');

        wr.write("678", 1, 1);


        System.out.println("res: " + wr);

    }


}
