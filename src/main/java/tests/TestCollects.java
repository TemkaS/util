package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.darkslave.util.Collects;





public class TestCollects {



    public static void main(String[] args) throws Exception {
        List<Integer> src1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> src2 = new ArrayList<Integer>(src1);

        List<Integer> res1 = Collects.sublist(src2, -3, 0);
        res1.add(10);

        System.out.println(src2);
        System.out.println(res1);


    }

}
