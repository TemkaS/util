package util_old;






abstract public class Comb {


    public static double binomial(int n, int m) {
        double r = 1.0;
        int a = Math.min(m, n - m);

        while (a > 0) {
            r*= (double) n / a;
            n--;
            a--;
        }

        return Math.ceil(r);
    }



}
