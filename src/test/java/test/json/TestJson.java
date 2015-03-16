package test.json;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import net.darkslave.json.*;





public class TestJson {

    /**
     * Класс с указанием метода замены
     */
    @JsonSerialize(replaceWith = "someMethod")
    protected static class A {
        protected String val = "abcdef";

        protected Object someMethod() {
            return new Object[] {
                A.class.getCanonicalName(),
                val
            };
        }
    }



    /**
     * Класс с указанием полей и методов сериализации
     */
    @JsonSerialize({
        // сериализация поля aa
        @JsonProperty("aa"),

        // сериализация поля bb под синонимом xx
        @JsonProperty(value="xx", field="bb"),

        // сериализация через геттер yy под снонимом yy
        @JsonProperty(value="yy", method="yy"),

    })
    protected static class B {
        protected String aa = "abcdef";
        protected double bb = 123.45;

        protected boolean yy() {
            return true;
        }

    }

    /**
     * Аннотации наследуются от суперкласса
     */
    protected static class B2 extends B {
    }


    /**
     * Аннотации переопределяют аннотации суперкласса
     */
    @JsonSerialize({
        @JsonProperty("bb")
    })
    protected static class B3 extends B {
    }



    /**
     * Класс с дефолтовой сериализацией полей
     */
    protected static class C {
        // обычное поле будет сериализовано
        protected String aa = "abcdef";

        // транзиент поле будет пропущено
        protected transient double bb = 123.45;

        // статик поле будет пропущено
        protected static    double cc = 123.45;

        // поле с такой аннотацией будет пропущено
        @JsonIgnore
        protected double dd = 123.45;

    }


    /**
     * Простая сериализация енумов через имя
     */
    @JsonSerialize(replaceWith = "name")
    protected static enum E {
        Beer,
        Wine;
    }



    public static void main(String[] args) throws Exception {
        Throwable error = new Exception("The main exception", new Exception("Cause exception"));
        error.addSuppressed(new Exception("Suppressed exception"));


        Object[][] test = {
                { "class.A",  new A() },
                { "class.B",  new B() },
                { "class.B2", new B2() },
                { "class.B3", new B3() },
                { "class.C",  new C() },
                { "class.E",  E.Beer  },
                { "boolean",  false },
                { "number",   123.45 },
                { "string",   "abcdef" },
                { "cyrillic", "АБВабв" },
                { "controls", "\\\"'\t\f\b\r\n\u0000\u001f\u007f\u009f" },
                { "array",    new Object[] { false, 123.45, "abcdef" } },
                { "list",     Arrays.asList( false, 123.45, "abcdef" ) },
                { "map",      Arrays.asList( false, 123.45, "abcdef" ).stream()
                    .collect(Collectors.toMap(
                        (Object val) -> {
                            return val.getClass().getSimpleName().toLowerCase();
                        },
                        (Object val) -> {
                            return val;
                        }
                    ))
                },
                { "date",     new Date() },
                { "error",    error },
                { "null",     null  },
        };

        for (Object[] a : test)
            System.out.printf("%-10s:  %s%n", a[0], JsonEncoder.encode(a[1]));

    }


}
