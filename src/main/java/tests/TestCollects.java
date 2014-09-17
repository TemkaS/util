package tests;

import java.util.Arrays;
import java.util.List;
import net.darkslave.util.Collects;





public class TestCollects {


    public static void main(String[] args) throws Exception {
        List<Integer> src = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println(src);
        System.out.println(Collects.sublist(src, -4, -1));

    }


}
