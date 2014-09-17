package tests;

import net.darkslave.crypto.Bytes;





public class TestBytes {


    public static void main(String[] args) throws Exception {
        byte[] src, res;
        String enc;

        src = Bytes.from("Hello World!", "utf-8");
        enc = Bytes.printHex(src);
        System.out.println("HEX: " + enc);

        res = Bytes.fromHex(enc);
        System.out.println("res: " + new String(res, "utf-8"));


        src = Bytes.from("Hello World!", "utf-8");
        enc = Bytes.printBit(src);
        System.out.println("BIT: " + enc);

        res = Bytes.fromBit(enc);
        System.out.println("res: " + new String(res, "utf-8"));

    }


}
