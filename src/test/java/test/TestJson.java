package test;

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

        System.out.println("res: " + JsonEncoder.encode(map1));

    }


}
