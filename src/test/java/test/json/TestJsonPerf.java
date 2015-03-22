package test.json;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import test.json.JsonEncoderOld.JsonException;
import net.darkslave.json.JsonEncoder;
import net.darkslave.test.TestExecutor;





public class TestJsonPerf {
    private static Object result;
    private static Object source;
    private static int repeat = 500;


    public static void main(String[] args) throws Exception {
        Map<Object, Object> map = new LinkedHashMap<>();

        for (int i = 1; i <= 1000; i++)
            map.put("field-" + i, generate(10));

        source = map;


        System.out.println("old: " + TestExecutor.measure(TestJsonPerf::testOld, 10, 10) );
        System.out.println("new: " + TestExecutor.measure(TestJsonPerf::testNew, 10, 10) );

    }


    private static void testOld() throws IOException, JsonException {
        for (int i = 0; i < repeat; i++) {
            result = JsonEncoderOld.encode(source);
        }
    }


    private static void testNew() throws IOException {
        for (int i = 0; i < repeat; i++) {
            result = JsonEncoder.encode(source);
        }
    }


    private static final char[] CHARS = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя1234567890".toCharArray();

    private static String generate(int length) {
        char[] result = new char[length];
        Random rand = new Random();

        for (int i = 0; i < length; i++)
            result[i] = CHARS[ rand.nextInt(CHARS.length) ];

        return new String(result);
    }


}