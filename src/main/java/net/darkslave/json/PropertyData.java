/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.json;




/**
 * Mock объект для аннотаций JsonProperty
 */
public abstract class PropertyData {

    /**
     * Обертка аннотации
     *
     * @param source - аннотация
     */
    public static PropertyData from(JsonProperty source) {
        return new Annotation(source);
    }


    /**
     * Обертка для поля
     *
     * @param source - целевое поле
     * @param output - синоним
     */
    public static PropertyData forField(String source, String output) {
        return new Static(output, source, null);
    }


    /**
     * Обертка для поля
     *
     * @param source - целевое поле
     */
    public static PropertyData forField(String source) {
        return new Static(source, source, null);
    }


    /**
     * Обертка для метода
     *
     * @param source - целевой метод
     * @param output - синоним
     */
    public static PropertyData forMethod(String source, String output) {
        return new Static(output, null, source);
    }


    /**
     * Обертка для метода
     *
     * @param source - целевой метод
     */
    public static PropertyData forMethod(String source) {
        return new Static(source, null, source);
    }


    public abstract String value();


    public abstract String field();


    public abstract String method();



    private static class Static extends PropertyData {
        private final String value;
        private final String field;
        private final String method;


        public Static(String value, String field, String method) {
            this.value  = value;
            this.field  = field;
            this.method = method;
        }


        @Override
        public String value() {
            return value;
        }


        @Override
        public String field() {
            return field;
        }


        @Override
        public String method() {
            return method;
        }

    }


    private static class Annotation extends PropertyData {
        private final JsonProperty source;


        public Annotation(JsonProperty source) {
            this.source = source;
        }


        @Override
        public String value() {
            return source.value();
        }


        @Override
        public String field() {
            return source.field();
        }


        @Override
        public String method() {
            return source.method();
        }

    }


}