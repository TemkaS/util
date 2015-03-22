package test.json;

import java.io.IOException;
import java.io.Writer;
import net.darkslave.test.TestExecutor;
import net.darkslave.util.Misc;





public class TestWriterPerf {
    private static Object result;
    private static String source;
    private static int repeat = 4000;


    public static void main(String[] args) throws Exception {
        source = Misc.repeat("*", 128);

        System.out.println("std.str: " + TestExecutor.measure(TestWriterPerf::testStdStr, 10, 10) );
        System.out.println("new.str: " + TestExecutor.measure(TestWriterPerf::testNewStr, 10, 10) );

        System.out.println("std.chr: " + TestExecutor.measure(TestWriterPerf::testStdChr, 10, 10) );
        System.out.println("new.chr: " + TestExecutor.measure(TestWriterPerf::testNewChr, 10, 10) );

    }


    private static void testStdStr() throws IOException {
        Writer wr = new java.io.StringWriter(512);
        for (int i = 0; i < repeat; i++) {
            wr.write(source);
        }
        result = wr;
    }

    private static void testNewStr() throws IOException {
        Writer wr = new net.darkslave.io.StringWriter(512);
        for (int i = 0; i < repeat; i++) {
            wr.write(source);
        }
        result = wr;
    }


    private static void testStdChr() throws IOException {
        Writer wr = new java.io.CharArrayWriter(512);
        for (int i = 0; i < repeat; i++) {
            wr.write(source);
        }
        result = wr;
    }

    private static void testNewChr() throws IOException {
        Writer wr = new net.darkslave.io.CharArrayWriter(512);
        for (int i = 0; i < repeat; i++) {
            wr.write(source);
        }
        result = wr;
    }


}
