package util.old;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.darkslave.util.Misc;





abstract public class Html {


    public static String sanitize(String source) {
        if (Misc.isEmpty(source))
            return Misc.EMPTY_STRING;

        // вырезаем элемент документа, если есть
        Matcher m = Pattern.compile("(?is)<body[^>]*>(.*?)</body>").matcher(source);
        if(m.find())
            source = m.group(1);

        // удаляем комментарии, скрипты, стили
        source = source.replaceAll("(?is)<!--.*?-->", "");
        source = source.replaceAll("(?is)<script[^>]*>.*?</script>", "");
        source = source.replaceAll("(?is)<style[^>]*>.*?</style>",   "");

        // заменяем все переходы на другую строку строку на символ \n
        source = source.replaceAll("(?is)\\r\\n?|[\\u0085\\u2028\\u2029]", "\n");

        // заменяем все блочные теги на символ \n
        source = source.replaceAll("(?is)\\s*(?:<[/]?(?:br|div|h\\d+|hr|p|pre|table|tr|li|ol|ul|dd|dl|dt|blockquote)[^>]*>\\s*)+", "\n");

        // заменяем все ячейки таблиц на символ \t
        source = source.replaceAll("(?is)\\s*(?:<[/]?(?:td|th)[^>]*>\\s*)+", "\t");

        // удаляем все оставшиеся теги
        source = source.replaceAll("(?is)<[/]?[a-z][^>]*>", "");

        // множественные переходы на другую строку заменяем одним
        source = source.replaceAll("(?is)\\n+", "\n");

        // удаляем пробелы из начала и конца всего текста и каждой строки
        source = source.replaceAll("(?is)^\\s+|\\s+$|(?<=\\n)[^\\S\\n]+|[^\\S\\n]+(?=\\n)", "");

        return source;
    }



    public static String nl2br(CharSequence source){
        if (Misc.isEmpty(source))
            return Misc.EMPTY_STRING;

        StringBuilder temp;
        char c;

        int n = source.length();
        temp = new StringBuilder(n);

        for(int i = 0; i < n; i++) {
            switch (c = source.charAt(i)) {
                case '\r':
                    if (i + 1 < n && source.charAt(i + 1) == '\n') i++;
                case '\n':
                    temp.append("<br>");
                break;

                default:
                    temp.append(c);
                break;
            }
        }

        return temp.toString();
    }



    public static String escape(CharSequence source){
        if (Misc.isEmpty(source))
            return Misc.EMPTY_STRING;

        StringBuilder temp;
        char c;

        int n = source.length();
        temp = new StringBuilder(n);

        for(int i = 0; i < n; i++) {
            switch (c = source.charAt(i)) {
                case '"':
                    temp.append("&quot;");
                break;
                case '&':
                    temp.append("&amp;");
                break;
                case '\'':
                    temp.append("&#39;");
                break;
                case '<':
                    temp.append("&lt;");
                break;
                case '>':
                    temp.append("&gt;");
                break;
                default:
                    temp.append(c);
                break;
            }
        }

        return temp.toString();
    }


}
