package test.json;

import java.util.LinkedHashMap;
import java.util.Map;
import net.darkslave.json.*;





public class TestJson {

    @JsonSerialize(replaceWith="replacer")
    private static class A {
        private String  string = "abcdef";
        private double number = 123.45;
        private boolean bool  = false;

        private Object replacer() {
            return new Object[] { "classA", string, number, bool };
        }

    }


    @JsonSerialize({
        @JsonProperty("string"),
        @JsonProperty(value="numBER", field="number"),
        @JsonProperty(value="other", method="other"),
    })
    private static class B {
        private String  string = "abcdef";
        private double number = 123.45;
        private boolean bool  = false;

        private Object other() {
            return new Object[] { number, bool };
        }
    }

    private static String result;

    public static void main(String[] args) throws Exception {
        Map<Object, Object> map1 = new LinkedHashMap<>();
        map1.put("string", "abcdef");
        map1.put("number", 123.45);
        map1.put("bool",   false);
        map1.put("null",   null);
        map1.put("objA",   new A());
        map1.put("objB",   new B());

        Map<Object, Object> map2 = new LinkedHashMap<>();
        map1.put("map",    map2);

        map2.put("string", "abcdef");
        map2.put("number", 123.45);
        map2.put("bool",   false);

        System.out.println("res: " + Json.encode(map1));
        System.out.println("res: " + JsonEncoder.encode(map1));


        int REPEAT = 10_000;
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
