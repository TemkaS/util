package tests;

import java.util.HashMap;
import java.util.Map;

import net.darkslave.json.*;





public class TestJson2 {

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




    public static void main(String[] args) throws Exception {
        Map<Object, Object> map1 = new HashMap<>();
        map1.put("string", "abcdef");
        map1.put("number", 123.45);
        map1.put("bool",   false);
        map1.put("null",   null);

        Map<Object, Object> map2 = new HashMap<>();
        map1.put("map",    map2);

        map2.put("string", "abcdef");
        map2.put("number", 123.45);
        map2.put("bool",   false);
        map2.put("null",   null);

        //map1.put("map",    map1);

        map2.put("obj",    new A());

        System.out.println("res: " + JsonEncoder.encode(new A()));

        System.out.println("res: " + JsonEncoder.encode(map1));

    }


}
