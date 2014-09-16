/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.util.Holder;





public class ParametrizedPresenter extends PropertyPresenter {
    private final Map<String, Holder<String>> cached = new ConcurrentHashMap<String, Holder<String>>();
    private final PropertyPresenter   parent;
    private final Map<String, Object> params;

    private static final String MARKER_BEGIN = "{{";
    private static final String MARKER_CLOSE = "}}";


    public ParametrizedPresenter(PropertyPresenter parent, Map<?, ?> ... params) {
        if (parent == null)
            throw new IllegalArgumentException("Parent can't be null");

        if (params == null || params.length == 0)
            throw new IllegalArgumentException("Params can't be empty");

        Map<String, Object> temp = new HashMap<String, Object>();

        for (Map<?, ?> item : params) {
            for (Map.Entry<?, ?> e : item.entrySet()) {
                if (e.getKey() instanceof String) {
                    temp.put((String) e.getKey(), e.getValue());
                }
            }
        }

        this.parent = parent;
        this.params = temp;
    }


    @Override
    public String getValue(String name) {
        Holder<String> holder = cached.get(name);

        if (holder != null)
            return holder.value();

        String source = parent.getValue(name);

        if (source != null && source.length() > 0) {
            StringBuilder result = new StringBuilder(source.length());
            int index = 0;

            while (true) {
                int begin = source.indexOf(MARKER_BEGIN, index);
                int close = source.indexOf(MARKER_CLOSE, begin);

                if (begin < 0 || close < 0) {
                    result.append(source.substring(index));
                    break;
                }

                String key = source.substring(begin + MARKER_BEGIN.length(), close);
                Object val = params.get(key);

                if (val != null) {
                    result.append(source.substring(index, begin));
                    result.append(val);
                } else {
                    result.append(source.substring(index, close + MARKER_CLOSE.length()));
                }

                index = close + MARKER_CLOSE.length();
            }

            source = result.toString();
        }

        cached.put(name, new Holder<String>(source));
        return source;
    }


}
