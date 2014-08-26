package util;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.darkslave.util.Misc;





public class CLI {


    private static enum ParseFlow {
        NORMAL,
        QUOTED;
    }


    private static enum ParseChar {
        NORMAL(null),
        SPACE(" \t\n\r\f"),
        QUOTE("'\"");


        private final String value;


        ParseChar(String value) {
            this.value = value;
        }


        public static ParseChar type(char ch) {
            for (ParseChar item : values())
                if (item.value != null && item.value.indexOf(ch) >= 0)
                    return item;
            return NORMAL;
        }

    }


    /**
     * Распарсить строку аргументов
     */
    public static List<String> parse(String source) {
        List<String> result = new ArrayList<String>();

        ParseFlow flow = ParseFlow.NORMAL;

        int length = source.length(), begin = -1;

        char quot = 0;

        for (int index = 0; index < length; index++) {
            char ch = source.charAt(index);
            switch (flow) {
                case NORMAL:
                    switch (ParseChar.type(ch)) {
                        case QUOTE:
                            begin = index;
                            flow = ParseFlow.QUOTED;
                            quot = ch;
                        break;

                        case SPACE:
                            if (begin >= 0) {
                                result.add(source.substring(begin, index));
                                begin = -1;
                            }
                        break;

                        case NORMAL:
                            if (begin < 0) {
                                begin = index;
                            }
                        break;
                    }
                break;

                case QUOTED:
                    if (ch == quot) {
                        int next = index + 1;

                        if (next < length) {
                            if (ParseChar.type(ch = source.charAt(next)) != ParseChar.SPACE)
                                throw new IllegalArgumentException("Illegal character `" + ch + "` found at position " + next);
                        }

                        result.add(source.substring(begin + 1, index));
                        begin = -1;
                        flow = ParseFlow.NORMAL;
                    }
                break;
            }
        }

        switch (flow) {
            case NORMAL:
                if (begin >= 0) {
                    result.add(source.substring(begin));
                }
            break;

            case QUOTED:
                throw new IllegalArgumentException("Unexpected end of quoted string started at position " + begin);
        }

        return result;
    }




    /**
     * Выполнить указанную команду
     */
    public static String exec(String cmd, boolean output, String charset) throws Exception {
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(parse(cmd).toArray(new String[0]));

        if (!output)
            return null;

        StringBuilder result = new StringBuilder();
        String temp;

        temp = read(pr.getInputStream(), charset);

        if (!Misc.isEmpty(temp))
            result.append(temp).append("\n");

        temp = read(pr.getErrorStream(), charset);

        if (!Misc.isEmpty(temp))
            result.append(temp).append("\n");

        return result.toString();
    }


    private static String read(InputStream stream, String charset) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;

        while ((read = stream.read(buffer)) > 0)
            result.write(buffer, 0, read);

        return new String(buffer, charset);
    }

}
