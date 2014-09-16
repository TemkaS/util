package tests;

import net.darkslave.crypto.Hash;
import net.darkslave.crypto.RSACipher;





public class TestCipher {



    public static void main(String[] args) throws Exception {
        RSACipher ciph = RSACipher.createNewCipher(512);

        byte[] inp1 = "123456".getBytes("UTF-8");
        byte[] out1 = ciph.encode(inp1);

        byte[] inp2 = out1;
        byte[] out2 = ciph.decode(inp2);

        System.out.println(Hash.toHexString(out1));
        System.out.println(new String(out2, "UTF-8"));


    }

}
