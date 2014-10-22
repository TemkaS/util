package util.buffers.linked3;

import java.io.StringWriter;





public class TestChars {



    public static void main(String[] args) throws Exception {
        LinkedCharArray ch = new LinkedCharArray();
        char[] temp = new char[] { '7', '8' , '9'};

        ch.append("123");
        ch.append("456");
        ch.append(temp);

        System.out.println(ch);
        System.out.println(new String(ch.toCharArray()));

        temp[0] = '*';
        System.out.println(new String(ch.toCharArray()));

        StringWriter wr = new StringWriter();
        ch.writeTo(wr);
        System.out.println(wr.toString());

    }


}
