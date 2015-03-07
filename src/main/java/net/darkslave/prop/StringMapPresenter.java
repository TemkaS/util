/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;





/**
 *  Класс-декоратор на map'ой строк
 */
public class StringMapPresenter extends AbstractPropertyPresenter implements PropertyPresenter {
    private final Map<String, String> parent;


    public StringMapPresenter(Map<String, String> parent) {
        this.parent = new HashMap<>(parent);
    }


    @Override
    protected String getValue(String name) {
        return parent.get(name);
    }


    @Override
    public Set<String> getNames() {
        return parent.keySet();
    }


}
