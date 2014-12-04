package util.old;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.darkslave.util.Misc;
import net.darkslave.util.Regexp;





public class Html {


    private static final Replacer[] SANITIZE = {
            // вырезаем элемент документа, если есть
            new BodyReplacer(),

            // удаляем комментарии, скрипты, стили
            new PatternReplacer("(?is)<!--.*?-->", ""),
            new PatternReplacer("(?is)<script[^>]*>.*?</script>", ""),
            new PatternReplacer("(?is)<style[^>]*>.*?</style>", ""),

            // заменяем все переходы на другую строку строку на символ \n
            new PatternReplacer("(?is)\\r\\n?|[\\u0085\\u2028\\u2029]", "\n"),

            // заменяем все блочные теги на символ \n
            new PatternReplacer("(?is)\\s*(?:<[/]?(?:br|div|h\\d+|hr|p|pre|table|tr|li|ol|ul|dd|dl|dt|blockquote)[^>]*>\\s*)+", "\n"),

            // заменяем все ячейки таблиц на символ \t
            new PatternReplacer("(?is)\\s*(?:<[/]?(?:td|th)[^>]*>\\s*)+", "\t"),

            // удаляем все оставшиеся теги
            new PatternReplacer("(?is)<[/]?[a-z][^>]*>", ""),

            // множественные переходы на другую строку заменяем одним
            new PatternReplacer("(?is)\\n+", "\n"),

            // удаляем пробелы из начала и конца всего текста и каждой строки
            new PatternReplacer("(?is)^\\s+|\\s+$|(?<=\\n)[^\\S\\n]+|[^\\S\\n]+(?=\\n)", ""),

            // заменяем хтмл сущности
            new EntityReplacer(),

    };


    private static interface Replacer {
        public String replace(String source);
    }


    private static class PatternReplacer implements Replacer {
        private final Pattern pattern;
        private final String result;


        public PatternReplacer(String pattern, String result) {
            this.pattern = Pattern.compile(pattern);
            this.result = result;
        }


        @Override
        public String replace(String source) {
            return pattern.matcher(source).replaceAll(result);
        }

    }


    private static class BodyReplacer implements Replacer {
        private static final Pattern pattern = Pattern.compile("(?is)<body[^>]*>(.*?)</body>");


        @Override
        public String replace(String source) {
            Matcher matcher = pattern.matcher(source);
            if (matcher.find())
                return matcher.group(1);
            return source;
        }
    }


    private static class EntityReplacer implements Replacer {
        private static final Map<String, Character> entities;

        static {
            entities = new HashMap<String, Character>();
            entities.put("nbsp", (char) 160);
            entities.put("iexcl", (char) 161);
            entities.put("cent", (char) 162);
            entities.put("pound", (char) 163);
            entities.put("curren", (char) 164);
            entities.put("yen", (char) 165);
            entities.put("brvbar", (char) 166);
            entities.put("sect", (char) 167);
            entities.put("uml", (char) 168);
            entities.put("copy", (char) 169);
            entities.put("ordf", (char) 170);
            entities.put("laquo", (char) 171);
            entities.put("raquo", (char) 187);
            entities.put("not", (char) 172);
            entities.put("shy", (char) 173);
            entities.put("reg", (char) 174);
            entities.put("macr", (char) 175);
            entities.put("deg", (char) 176);
            entities.put("plusmn", (char) 177);
            entities.put("sup2", (char) 178);
            entities.put("sup3", (char) 179);
            entities.put("acute", (char) 180);
            entities.put("micro", (char) 181);
            entities.put("para", (char) 182);
            entities.put("middot", (char) 183);
            entities.put("cedil", (char) 184);
            entities.put("sup1", (char) 185);
            entities.put("ordm", (char) 186);
            entities.put("frac14", (char) 188);
            entities.put("frac12", (char) 189);
            entities.put("frac34", (char) 190);
            entities.put("iquest", (char) 191);
            entities.put("Agrave", (char) 192);
            entities.put("Aacute", (char) 193);
            entities.put("Acirc", (char) 194);
            entities.put("Atilde", (char) 195);
            entities.put("Auml", (char) 196);
            entities.put("Aring", (char) 197);
            entities.put("AElig", (char) 198);
            entities.put("Ccedil", (char) 199);
            entities.put("Egrave", (char) 200);
            entities.put("Eacute", (char) 201);
            entities.put("Ecirc", (char) 202);
            entities.put("Euml", (char) 203);
            entities.put("Igrave", (char) 204);
            entities.put("Iacute", (char) 205);
            entities.put("Icirc", (char) 206);
            entities.put("Iuml", (char) 207);
            entities.put("ETH", (char) 208);
            entities.put("Ntilde", (char) 209);
            entities.put("Ograve", (char) 210);
            entities.put("Oacute", (char) 211);
            entities.put("Ocirc", (char) 212);
            entities.put("Otilde", (char) 213);
            entities.put("Ouml", (char) 214);
            entities.put("times", (char) 215);
            entities.put("Oslash", (char) 216);
            entities.put("Ugrave", (char) 217);
            entities.put("Uacute", (char) 218);
            entities.put("Ucirc", (char) 219);
            entities.put("Uuml", (char) 220);
            entities.put("Yacute", (char) 221);
            entities.put("THORN", (char) 222);
            entities.put("szlig", (char) 223);
            entities.put("agrave", (char) 224);
            entities.put("aacute", (char) 225);
            entities.put("acirc", (char) 226);
            entities.put("atilde", (char) 227);
            entities.put("auml", (char) 228);
            entities.put("aring", (char) 229);
            entities.put("aelig", (char) 230);
            entities.put("ccedil", (char) 231);
            entities.put("egrave", (char) 232);
            entities.put("eacute", (char) 233);
            entities.put("ecirc", (char) 234);
            entities.put("euml", (char) 235);
            entities.put("igrave", (char) 236);
            entities.put("iacute", (char) 237);
            entities.put("icirc", (char) 238);
            entities.put("iuml", (char) 239);
            entities.put("eth", (char) 240);
            entities.put("ntilde", (char) 241);
            entities.put("ograve", (char) 242);
            entities.put("oacute", (char) 243);
            entities.put("ocirc", (char) 244);
            entities.put("otilde", (char) 245);
            entities.put("ouml", (char) 246);
            entities.put("divide", (char) 247);
            entities.put("oslash", (char) 248);
            entities.put("ugrave", (char) 249);
            entities.put("uacute", (char) 250);
            entities.put("ucirc", (char) 251);
            entities.put("uuml", (char) 252);
            entities.put("yacute", (char) 253);
            entities.put("thorn", (char) 254);
            entities.put("yuml", (char) 255);
            entities.put("fnof", (char) 402);
            entities.put("Alpha", (char) 913);
            entities.put("Beta", (char) 914);
            entities.put("Gamma", (char) 915);
            entities.put("Delta", (char) 916);
            entities.put("Epsilon", (char) 917);
            entities.put("Zeta", (char) 918);
            entities.put("Eta", (char) 919);
            entities.put("Theta", (char) 920);
            entities.put("Iota", (char) 921);
            entities.put("Kappa", (char) 922);
            entities.put("Lambda", (char) 923);
            entities.put("Mu", (char) 924);
            entities.put("Nu", (char) 925);
            entities.put("Xi", (char) 926);
            entities.put("Omicron", (char) 927);
            entities.put("Pi", (char) 928);
            entities.put("Rho", (char) 929);
            entities.put("Sigma", (char) 931);
            entities.put("Tau", (char) 932);
            entities.put("Upsilon", (char) 933);
            entities.put("Phi", (char) 934);
            entities.put("Chi", (char) 935);
            entities.put("Psi", (char) 936);
            entities.put("Omega", (char) 937);
            entities.put("alpha", (char) 945);
            entities.put("beta", (char) 946);
            entities.put("gamma", (char) 947);
            entities.put("delta", (char) 948);
            entities.put("epsilon", (char) 949);
            entities.put("zeta", (char) 950);
            entities.put("eta", (char) 951);
            entities.put("theta", (char) 952);
            entities.put("iota", (char) 953);
            entities.put("kappa", (char) 954);
            entities.put("lambda", (char) 955);
            entities.put("mu", (char) 956);
            entities.put("nu", (char) 957);
            entities.put("xi", (char) 958);
            entities.put("omicron", (char) 959);
            entities.put("pi", (char) 960);
            entities.put("rho", (char) 961);
            entities.put("sigmaf", (char) 962);
            entities.put("sigma", (char) 963);
            entities.put("tau", (char) 964);
            entities.put("upsilon", (char) 965);
            entities.put("phi", (char) 966);
            entities.put("chi", (char) 967);
            entities.put("psi", (char) 968);
            entities.put("omega", (char) 969);
            entities.put("thetasy", (char) 977);
            entities.put("upsih", (char) 978);
            entities.put("piv", (char) 982);
            entities.put("bull", (char) 8226);
            entities.put("hellip", (char) 8230);
            entities.put("prime", (char) 8242);
            entities.put("Prime", (char) 8243);
            entities.put("oline", (char) 8254);
            entities.put("frasl", (char) 8260);
            entities.put("weierp", (char) 8472);
            entities.put("image", (char) 8465);
            entities.put("real", (char) 8476);
            entities.put("trade", (char) 8482);
            entities.put("alefsym", (char) 8501);
            entities.put("larr", (char) 8592);
            entities.put("uarr", (char) 8593);
            entities.put("rarr", (char) 8594);
            entities.put("darr", (char) 8595);
            entities.put("harr", (char) 8596);
            entities.put("crarr", (char) 8629);
            entities.put("lArr", (char) 8656);
            entities.put("uArr", (char) 8657);
            entities.put("rArr", (char) 8658);
            entities.put("dArr", (char) 8659);
            entities.put("hArr", (char) 8660);
            entities.put("forall", (char) 8704);
            entities.put("part", (char) 8706);
            entities.put("exist", (char) 8707);
            entities.put("empty", (char) 8709);
            entities.put("nabla", (char) 8711);
            entities.put("isin", (char) 8712);
            entities.put("notin", (char) 8713);
            entities.put("ni", (char) 8715);
            entities.put("prod", (char) 8719);
            entities.put("sum", (char) 8721);
            entities.put("minus", (char) 8722);
            entities.put("lowast", (char) 8727);
            entities.put("radic", (char) 8730);
            entities.put("prop", (char) 8733);
            entities.put("infin", (char) 8734);
            entities.put("ang", (char) 8736);
            entities.put("and", (char) 8743);
            entities.put("or", (char) 8744);
            entities.put("cap", (char) 8745);
            entities.put("cup", (char) 8746);
            entities.put("int", (char) 8747);
            entities.put("there4", (char) 8756);
            entities.put("sim", (char) 8764);
            entities.put("cong", (char) 8773);
            entities.put("asymp", (char) 8776);
            entities.put("ne", (char) 8800);
            entities.put("equiv", (char) 8801);
            entities.put("le", (char) 8804);
            entities.put("ge", (char) 8805);
            entities.put("sub", (char) 8834);
            entities.put("sup", (char) 8835);
            entities.put("nsub", (char) 8836);
            entities.put("sube", (char) 8838);
            entities.put("supe", (char) 8839);
            entities.put("oplus", (char) 8853);
            entities.put("otimes", (char) 8855);
            entities.put("perp", (char) 8869);
            entities.put("sdot", (char) 8901);
            entities.put("lceil", (char) 8968);
            entities.put("rceil", (char) 8969);
            entities.put("lfloor", (char) 8970);
            entities.put("rfloor", (char) 8971);
            entities.put("lang", (char) 9001);
            entities.put("rang", (char) 9002);
            entities.put("loz", (char) 9674);
            entities.put("spades", (char) 9824);
            entities.put("clubs", (char) 9827);
            entities.put("hearts", (char) 9829);
            entities.put("diams", (char) 9830);
            entities.put("apos", (char) 39);
            entities.put("quot", (char) 34);
            entities.put("amp", (char) 38);
            entities.put("lt", (char) 60);
            entities.put("gt", (char) 62);
            entities.put("OElig", (char) 338);
            entities.put("oelig", (char) 339);
            entities.put("Scaron", (char) 352);
            entities.put("scaron", (char) 353);
            entities.put("Yuml", (char) 376);
            entities.put("circ", (char) 710);
            entities.put("tilde", (char) 732);
            entities.put("ensp", (char) 8194);
            entities.put("emsp", (char) 8195);
            entities.put("thinsp", (char) 8201);
            entities.put("zwnj", (char) 8204);
            entities.put("zwj", (char) 8205);
            entities.put("lrm", (char) 8206);
            entities.put("rlm", (char) 8207);
            entities.put("ndash", (char) 8211);
            entities.put("mdash", (char) 8212);
            entities.put("lsquo", (char) 8216);
            entities.put("rsquo", (char) 8217);
            entities.put("sbquo", (char) 8218);
            entities.put("ldquo", (char) 8220);
            entities.put("rdquo", (char) 8221);
            entities.put("bdquo", (char) 8222);
            entities.put("dagger", (char) 8224);
            entities.put("Dagger", (char) 8225);
            entities.put("permil", (char) 8240);
            entities.put("lsaquo", (char) 8249);
            entities.put("rsaquo", (char) 8250);
            entities.put("euro", (char) 8364);
        }

        private static final Pattern pattern = Pattern.compile("(?i)&(?:(\\w+)|#(\\d+)|#x([\\da-f]+));");

        private static final Regexp.Replacer replacer = new Regexp.Replacer() {

            @Override
            public Object replace(String[] match) {
                Character result = null;

                if (match[1] != null) {
                    result = entities.get(match[1]);
                } else
                if (match[2] != null) {
                    result = fromCode(match[2], 10);
                } else
                if (match[3] != null) {
                    result = fromCode(match[3], 16);
                }

                if (result == null)
                    return match[0];

                return result;
            }

            private Character fromCode(String text, int radix) {
                try {
                    return (char) Integer.parseInt(text, radix);
                } catch(NumberFormatException e) {
                    return null;
                }
            }

        };

        @Override
        public String replace(String source) {
            return Regexp.replaceAll(source, pattern, replacer);
        }

    }



    public static String sanitize(String source) {
        if (Misc.isEmpty(source))
            return Misc.EMPTY_STRING;

        for (Replacer item : SANITIZE)
            source = item.replace(source);

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


    private Html() {}
}
