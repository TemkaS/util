package test;

import net.darkslave.util.Html;






public class TestHtml {


    public static void main(String[] args) throws Exception {

        System.out.println(Html.sanitize("&amp; &lt;<b>1&nbsp;&#39;2&#39;&#x20;3</b>&gt;"));

        System.out.println(Html.escape("& <1  '2'  3>"));

        System.out.println(Html.nl2br("1\n2\r\n3\r4"));

    }


}
