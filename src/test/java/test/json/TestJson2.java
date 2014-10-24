package test.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import net.darkslave.json.*;





public class TestJson2 {
    private static String result;


    static final char[] CHARS = "абвгдеёжзийклмнопрстуфхцчшщьыъэюя1234567890".toCharArray();

    static String generate(int length) {
        char[] result = new char[length];
        Random rand = new Random();

        for (int i = 0; i < length; i++)
            result[i] = CHARS[ rand.nextInt(CHARS.length) ];

        return new String(result);
    }


    public static void main(String[] args) throws Exception {
        Map<Object, Object> map1 = new LinkedHashMap<>();

        for (int i = 1; i <= 1000; i++) {
            map1.put("field-" + i, generate(10));
        }


        int REPEAT = 200;
        long time;

        for (int ROUND = 1; ROUND <= 5; ROUND++) {
            long origin;

            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                result = Json.encode(map1);
            }
            origin = time = System.nanoTime() - time;
            System.out.printf("%02d old ~ %5.2f ms.%n", ROUND, time * 1e-6);
            //---------------------------------------------


            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                result = JsonEncoder.encode(map1);
            }
            time = System.nanoTime() - time;
            System.out.printf("%02d new ~ %5.2f ms. (%+3.0f%%)%n", ROUND, time * 1e-6, (100.0 * (time - origin) / origin));
            //---------------------------------------------


        }


    }


}
