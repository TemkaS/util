package test;

import util.old.Html;





public class TestHtml {


    public static void main(String[] args) throws Exception {
        String source = "<b>123&nbsp;&lt;&amp;&gt;&#x20;&#39;</b>";

        System.out.println(": " + Html.sanitize(source));

    }


}
