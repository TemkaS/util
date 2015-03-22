package test.json;

import java.io.IOException;
import java.io.Writer;





public class TestWriter {


    public static void main(String[] args) throws Exception {

        System.out.println("std:str: " + write(new java.io.StringWriter(512)));
        System.out.println("new:str: " + write(new net.darkslave.io.StringWriter(512)));

        System.out.println("std:chr: " + write(new java.io.CharArrayWriter(512)));
        System.out.println("new:chr: " + write(new net.darkslave.io.CharArrayWriter(512)));

    }


    private static String write(Writer wr) throws IOException {

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

        return wr.toString();
    }


}
