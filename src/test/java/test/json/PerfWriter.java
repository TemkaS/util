package test.json;

import java.io.IOException;
import java.io.Writer;
import net.darkslave.util.Misc;





public class PerfWriter {


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



    public static void main(String[] args) throws Exception {

        System.out.println("std:str: " + write(new java.io.StringWriter(512)));
        System.out.println("new:str: " + write(new net.darkslave.io.StringWriter(512)));

        System.out.println("std:chr: " + write(new java.io.CharArrayWriter(512)));
        System.out.println("new:chr: " + write(new net.darkslave.io.CharArrayWriter(512)));


        System.out.println("----------------------------------------------------");

        int TOTAL_SIZE = 16000;
        String LINE = Misc.repeat("*", 16);
        int REPEAT = 2000;
        int SIZE   = TOTAL_SIZE / LINE.length();
        long time;

        for (int ROUND = 1; ROUND <= 5; ROUND++) {
            long origin;

            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                Writer wr = new java.io.StringWriter(512);
                for (int j = 0; j < SIZE; j++) {
                    wr.write(LINE);
                }
            }
            origin = time = System.nanoTime() - time;
            System.out.printf("%02d std:str ~ %5.2f ms.%n", ROUND, time * 1e-6);
            //---------------------------------------------

            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                Writer wr = new net.darkslave.io.StringWriter(512);
                for (int j = 0; j < SIZE; j++) {
                    wr.write(LINE);
                }
            }
            time = System.nanoTime() - time;
            System.out.printf("%02d new:str ~ %5.2f ms. (%+3.0f%%)%n", ROUND, time * 1e-6, (100.0 * (time - origin) / origin));
            //---------------------------------------------

        }


        System.out.println("----------------------------------------------------");

        for (int ROUND = 1; ROUND <= 5; ROUND++) {
            long origin;

            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                Writer wr = new java.io.CharArrayWriter(512);
                for (int j = 0; j < SIZE; j++) {
                    wr.write(LINE);
                }
            }
            origin = time = System.nanoTime() - time;
            System.out.printf("%02d std:chr ~ %5.2f ms.%n", ROUND, time * 1e-6);
            //---------------------------------------------

            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                Writer wr = new net.darkslave.io.CharArrayWriter(512);
                for (int j = 0; j < SIZE; j++) {
                    wr.write(LINE);
                }
            }
            time = System.nanoTime() - time;
            System.out.printf("%02d new:chr ~ %5.2f ms. (%+3.0f%%)%n", ROUND, time * 1e-6, (100.0 * (time - origin) / origin));
            //---------------------------------------------


        }

    }


}
