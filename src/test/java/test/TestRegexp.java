package test;

import java.util.Arrays;
import net.darkslave.util.Regexp;





public class TestRegexp {


    public static void main(String[] args) throws Exception {
        String source = "21.06.2013, 21.12.2014, 01.09.2015";
        String regexp = "(\\d+)[.](\\d+)[.](\\d+)";

        System.out.println("1: " + Arrays.toString(Regexp.match(source, regexp)));

        for (String[] line : Regexp.matchAll(source, regexp))
            System.out.println("2: " + Arrays.toString(line));


        Regexp.Replacer<Object> replacer = match -> match[3] + "-" + match[2] + "-" + match[1];

        System.out.println("3: " + Regexp.replaceAll(source, regexp, replacer));


    }


}
