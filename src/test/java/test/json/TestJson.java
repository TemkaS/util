package test.json;

import java.util.LinkedHashMap;
import java.util.Map;
import net.darkslave.json.*;





public class TestJson {

    @JsonSerialize(replaceWith="replaceWith")
    static class A {
        private double field = 123.45;

        private Object replaceWith() {
            return new Object[] {
                "class.A",
                field
            };
        }
    }

    @JsonSerialize({
        @JsonProperty("field"),
        @JsonProperty(value="field2", field="field"),
        @JsonProperty(value="field3", method="getField"),
    })
    static class B {
        private double field = 123.45;

        private Object getField() {
            return field;
        }

    }

    @JsonSerialize(replaceWith="name")
    static enum C {
        ELEMENT_1,
        ELEMENT_2;
    }



    public static void main(String[] args) throws Exception {
        Map<Object, Object> map1 = new LinkedHashMap<>();
        map1.put("string", "abcdef");
        map1.put("cyrill", "АБВабв");
        map1.put("contrl", "\\\"'\t\f\b\r\n\u0000\u001f\u007f\u009f");
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
        // map2.put("map1",   map1);

        System.out.println("res: " + Json.encode(map1));
        System.out.println("res: " + JsonEncoder.encode(map1));

        System.out.println("res: " + JsonEncoder.encode(C.ELEMENT_1));
    }


}
