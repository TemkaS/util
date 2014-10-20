package util;





public class TestChars {






    public static void main(String[] args) throws Exception {
        LinkedCharBuffer ch = new LinkedCharBuffer();
        char[] temp = new char[] { 'a', 'b' , 'c'};

        ch.append("123");
        ch.append("456");
        ch.append("789");
        ch.append(temp);

        System.out.println(ch);

        temp[0] = '*';
        System.out.println(ch);


    }


}
