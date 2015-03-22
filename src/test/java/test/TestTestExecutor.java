package test;

import net.darkslave.test.TestExecutor;






public class TestTestExecutor {



    public static void main(String[] args) throws Exception {
        System.out.println("warmup 10");
        System.out.println( TestExecutor.measure(TestTestExecutor::test1, 10, 10) );
        System.out.println( TestExecutor.measure(TestTestExecutor::test2, 10, 10) );
        System.out.println( TestExecutor.measure(TestTestExecutor::test3, 10, 10) );
        System.out.println();

        System.out.println("warmup 100");
        System.out.println( TestExecutor.measure(TestTestExecutor::test1, 100, 10) );
        System.out.println( TestExecutor.measure(TestTestExecutor::test2, 100, 10) );
        System.out.println( TestExecutor.measure(TestTestExecutor::test3, 100, 10) );
        System.out.println();


        System.out.println("warmup 1000");
        System.out.println( TestExecutor.measure(TestTestExecutor::test1, 1000, 10) );
        System.out.println( TestExecutor.measure(TestTestExecutor::test2, 1000, 10) );
        System.out.println( TestExecutor.measure(TestTestExecutor::test3, 1000, 10) );
        System.out.println();

    }



    static void test1() {
        String aa = "";
        for (int i = 0; i < 1000; i++)
            aa = aa + "*";
    }


    static void test2() {
        StringBuilder aa = new StringBuilder();
        for (int i = 0; i < 1000; i++)
            aa.append("*");
    }


    static void test3() {
        StringBuffer aa = new StringBuffer();
        for (int i = 0; i < 1000; i++)
            aa.append("*");
    }


}
