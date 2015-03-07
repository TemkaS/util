/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.darkslave.util.Misc;
import net.darkslave.vars.Holder;
import net.darkslave.vars.ImmutableHolder;





/**
 *  Класс декоратор презентера свойств с обработкой подстановочных шаблонов
 */
public class ParametrizedPresenter extends AbstractPropertyPresenter implements PropertyPresenter {
    private final Map<String, Holder<String>> cached = new ConcurrentHashMap<String, Holder<String>>();
    private final PropertyPresenter parent;
    private final Map<String, Object> params;


    public ParametrizedPresenter(PropertyPresenter parent, Map<?, ?>... params) {
        if (parent == null)
            throw new IllegalArgumentException("Parent can't be null");

        if (params == null || params.length == 0)
            throw new IllegalArgumentException("Params can't be empty");

        final Map<String, Object> temp = new HashMap<String, Object>();

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
    protected String getValue(String name) {
        Holder<String> holder = cached.get(name);

        if (holder != null)
            return holder.get();

        String source = Misc.template(parent.getString(name), params);

        cached.put(name, new ImmutableHolder<String>(source));
        return source;
    }


    @Override
    public Set<String> getNames() {
        return parent.getNames();
    }


}
