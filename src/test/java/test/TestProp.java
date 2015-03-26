package test;

import java.util.HashMap;
import java.util.Map;
import net.darkslave.prop.ParametrizedPresenter;
import net.darkslave.prop.PropertyPresenter;
import net.darkslave.prop.StringMapPresenter;





public class TestProp {


    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("a", "true");
        map.put("b", "123");
        map.put("c", "123.45");
        map.put("d", "01.02.2015");
        map.put("e", "abcdef");
        map.put("f", "dir = {{user.dir}}");

        // простая обертка
        PropertyPresenter prop = new StringMapPresenter(map);

        System.out.println("a = " + prop.getBoolean("a"));
        System.out.println("b = " + prop.getInteger("b"));
        System.out.println("c = " + prop.getDouble("c"));
        System.out.println("d = " + prop.getDate("d", "dd.MM.yyyy"));
        System.out.println("e = " + prop.getString("e"));
        System.out.println("f = " + prop.getString("f"));

        // обертка с шаблонами
        PropertyPresenter prop2 = new ParametrizedPresenter(prop, System.getProperties());
        System.out.println("f = " + prop2.getString("f"));

        // изменения в мапе влияют на обертки
        map.put("f", "dir = temp");
        System.out.println("f = " + prop2.getString("f"));

    }


}
