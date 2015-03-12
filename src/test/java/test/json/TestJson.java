package test.json;

import java.util.LinkedHashMap;
import java.util.Map;
import net.darkslave.json.*;
import net.darkslave.util.Misc;





public class TestJson {

    @JsonSerialize(replaceWith="replaceWith")
    static class A {
        private double field = 123.45;

        protected Object replaceWith() {
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

        protected Object getField() {
            return field;
        }

    }


    static class C {
        private static    double fieldS = 123.45;
        private transient double fieldT = 123.45;
        private double field = 123.45;
    }


    @JsonSerialize(replaceWith="name")
    static enum E {
        Beer,
        Wine;
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
        map1.put("objC",   new C());
        map1.put("objE",   E.Beer);

        Map<Object, Object> map2 = new LinkedHashMap<>();
        map1.put("map",    map2);

        map2.put("string", "abcdef");
        map2.put("number", 123.45);
        map2.put("bool",   false);
        // map2.put("map1",   map1);

        System.out.println("res: " + Json.encode(map1));
        System.out.println("res: " + JsonEncoder.encode(map1));



        Throwable e = new Exception("The main exception", new Exception("Cause exception"));
        e.addSuppressed(new Exception("Suppressed exception"));

        System.out.println("res: " + Misc.getErrorTrace(e));

        // System.out.println("res: " + JsonEncoder.encode(e));

    }


}
