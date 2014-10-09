package test;

import java.nio.charset.Charset;
import net.darkslave.crypto.Bytes;





public class TestBytes {
    private static final Charset CHARSET = Charset.forName("UTF-8");


    public static void main(String[] args) throws Exception {
        byte[] src, res;
        String enc;

        src = Bytes.from("Hello World!", CHARSET);
        enc = Bytes.printHex(src);
        System.out.println("HEX: " + enc);

        res = Bytes.fromHex(enc);
        System.out.println("res: " + new String(res, CHARSET));


        src = Bytes.from("Hello World!", CHARSET);
        enc = Bytes.printBit(src);
        System.out.println("BIT: " + enc);

        res = Bytes.fromBit(enc);
        System.out.println("res: " + new String(res, CHARSET));

    }


}
