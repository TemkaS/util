/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.Set;





public class PropertyFilePresenter extends PropertyPresenter {
    private final Properties properties;
    private volatile Set<String> names;


    public PropertyFilePresenter(String path, String charset) throws IOException {
        this(new FileInputStream(path), charset);
    }


    public PropertyFilePresenter(InputStream stream, String charset) throws IOException {
        properties = new Properties();

        try (Reader reader = new InputStreamReader(stream, charset)) {
            properties.load(reader);
        }

    }


    @Override
    public String getValue(String name) {
        return properties.getProperty(name);
    }


    public Set<String> getNames() {
        Set<String> result = null;

        if ((result = names) == null) {
            synchronized (this) {
                if ((result = names) == null) {
                    result = names = properties.stringPropertyNames();
                }
            }
        }

        return result;
    }

}
