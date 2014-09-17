package tests;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import net.darkslave.crypto.Bytes;
import net.darkslave.crypto.Crypt;





public class TestCrypt {


    public static void main(String[] args) throws Exception {
        byte[] src, enc, res;
        Cipher cipher;
        Key secureKey;
        IvParameterSpec iv = new IvParameterSpec(Bytes.fromHex("00102030405060708090A0B0C0D0E0F0"));


        System.out.println("md5: " + Bytes.printHex( Crypt.hash("MD5", Bytes.from("12345", "UTF-8")) ));
        System.out.println("sha-1: " + Bytes.printHex( Crypt.hash("SHA-1", Bytes.from("12345", "UTF-8")) ));
        System.out.println("sha-512: " + Bytes.printHex( Crypt.hash("SHA-512", Bytes.from("12345", "UTF-8")) ));


        secureKey = Crypt.createKey("AES", Bytes.fromHex("aabbccddeeff"), 128);
        cipher = Crypt.encoder("AES/CFB8/PKCS5Padding", secureKey, iv);
        src = "Hello World!".getBytes("utf-8");
        enc = cipher.doFinal(src);
        System.out.println("AES: " + Bytes.printHex(enc));

        secureKey = Crypt.createKey("AES", Bytes.fromHex("aabbccddeeff"), 128);
        cipher = Crypt.decoder("AES/CFB8/PKCS5Padding", secureKey, iv);
        res = cipher.doFinal(enc);
        System.out.println("res: " + new String(res, "utf-8"));


        secureKey = Crypt.encodeKeyRSA(Bytes.fromHex("305c300d06092a864886f70d0101010500034b003048024100a06d21a0aa933f002026960d060ce8de953a7c521c181739246e547c653e174241f55ba421498fa8ee7936046fb16703fa7110898b98a4fd9161b0d169a3c0810203010001"));
        cipher = Crypt.encoder("RSA", secureKey);
        src = "Hello World!".getBytes("utf-8");
        enc = cipher.doFinal(src);
        System.out.println("RSA: " + Bytes.printHex(enc));

        secureKey = Crypt.decodeKeyRSA(Bytes.fromHex("30820154020100300d06092a864886f70d01010105000482013e3082013a020100024100a06d21a0aa933f002026960d060ce8de953a7c521c181739246e547c653e174241f55ba421498fa8ee7936046fb16703fa7110898b98a4fd9161b0d169a3c0810203010001024074b65093b1e5441d54469e3dc55c1902ee304987f71673b6c7d158b7d37433b8820a5bd77f8ed7f576b920a3c5b7aaccebbcf07eac659f6d1ecb61988b6cb72d022100d7efe6d146047bc6b77edb11bd2f5b63c069a30022e01c4be339652ea94bb73b022100be30b369e57f142c04448624e474a6d8f32682583a375c9a6e28a311a0444373022100a5816bc164d014eaa743e4b6c0f12633db8b5bf2c3c85007de3de48e9215c5770220498ec66547988db1b478d834fe92cb510f41ae99f6f22083ade553a4668ae057022056ae0ab24e83f7e174d6338567445b01c99f8d99572badb0940604c595dc3bfd"));
        cipher = Crypt.decoder("RSA", secureKey);
        res = cipher.doFinal(enc);
        System.out.println("res: " + new String(res, "utf-8"));

    }


}
