/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.util.Map;
import java.util.Set;





/**
 *  Класс-декоратор на map'ой строк
 */
public class StringMapPresenter extends AbstractPropertyPresenter implements PropertyPresenter {
    private final Map<String, String> source;


    public StringMapPresenter(Map<String, String> source) {
        if (source == null)
            throw new IllegalArgumentException("Source map can't be null");
        this.source = source;
    }


    @Override
    protected String getValue(String name) {
        return source.get(name);
    }


    @Override
    public Set<String> getNames() {
        return source.keySet();
    }


}
