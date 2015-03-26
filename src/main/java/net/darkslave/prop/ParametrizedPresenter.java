/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.darkslave.util.Misc;





/**
 *  Класс декоратор презентера свойств с обработкой подстановочных шаблонов
 */
public class ParametrizedPresenter extends AbstractPropertyPresenter implements PropertyPresenter {
    private final PropertyPresenter   source;
    private final Map<String, Object> params;


    public ParametrizedPresenter(PropertyPresenter source, Map<?, ?> ... params) {
        if (source == null)
            throw new IllegalArgumentException("Source presenter can't be null");

        if (params == null || params.length == 0)
            throw new IllegalArgumentException("Params can't be empty");

        final Map<String, Object> temp = new HashMap<>();

        for (Map<?, ?> item : params) {
            item.entrySet().stream().filter(
                    e -> e.getKey() instanceof String
            ).forEach(
                    e -> temp.put((String) e.getKey(), e.getValue())
            );
        }

        this.source = source;
        this.params = temp;
    }


    @Override
    protected String getValue(String name) {
        return Misc.template(source.getString(name), params);
    }


    @Override
    public Set<String> getNames() {
        return source.getNames();
    }


}
