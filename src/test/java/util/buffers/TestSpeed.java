package util.buffers;

import net.darkslave.util.Misc;





public class TestSpeed {
    public static StringBuilder   sb;
    public static util.buffers.linked1.LinkedCharArray la1;
    public static util.buffers.linked2.LinkedCharArray la2;


    public static void main(String[] args) throws Exception {
        String LINE = Misc.repeat("*", 4000);
        // char[] LINE = Misc.repeat("*", 2048).toCharArray();
        int REPEAT = 10000;
        int SIZE   = 20;
        long time;

        for (int ROUND = 1; ROUND <= 5; ROUND++) {
            long origin;

            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                sb = new StringBuilder();
                for (int j = 0; j < SIZE; j++) {
                    sb.append(LINE);
                }
            }
            origin = time = System.nanoTime() - time;
            System.out.printf("%02d builder ~ %5.2f ms.%n", ROUND, time * 1e-6);
            //---------------------------------------------


            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                la1 = new util.buffers.linked1.LinkedCharArray();
                for (int j = 0; j < SIZE; j++) {
                    la1.append(LINE);
                }
            }
            time = System.nanoTime() - time;
            System.out.printf("%02d linked1 ~ %5.2f ms. (%+3.0f%%)%n", ROUND, time * 1e-6, (100.0 * (time - origin) / origin));
            //---------------------------------------------


            //---------------------------------------------
            time = System.nanoTime();
            for (int i = 0; i < REPEAT; i++) {
                la2 = new util.buffers.linked2.LinkedCharArray();
                for (int j = 0; j < SIZE; j++) {
                    la2.append(LINE);
                }
            }
            time = System.nanoTime() - time;
            System.out.printf("%02d linked2 ~ %5.2f ms. (%+3.0f%%)%n", ROUND, time * 1e-6, (100.0 * (time - origin) / origin));
            //---------------------------------------------

        }

    }


}
