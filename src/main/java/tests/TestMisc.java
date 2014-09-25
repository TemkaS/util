package tests;

import net.darkslave.util.StringParser;





public class TestMisc {


    public static void main(String[] args) throws Exception {

        System.out.println("1: " + StringParser.toInteger("1111111111.222", null));
        System.out.println("2: " + StringParser.toLong("1111111111.222", null));
        System.out.println("3: " + StringParser.toDouble("1111111111.222", null));


    }


}
