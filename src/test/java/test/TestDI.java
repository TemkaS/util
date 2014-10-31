package test;





public class TestDI {
    public static class A {
        public A(int a) {}
        public A(Integer b) {}
    }


    public static void main(String[] args) throws Exception {
        Class<?> clazz = A.class;

        System.out.println( clazz.getConstructors()[0].getParameterTypes()[0] );
        System.out.println( clazz.getConstructors()[1].getParameterTypes()[0] );

    }


}
