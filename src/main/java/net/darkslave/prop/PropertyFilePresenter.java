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
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;





public class PropertyFilePresenter extends NamedPropertyPresenter {
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


    @Override
    public NamedPropertyPresenter getChild(String prefix) {
        return new ChildPresenter(this, prefix);
    }


    @Override
    public Set<String> getNames() {
        Set<String> result = null;

        if ((result = names) == null) {
            synchronized (this) {
                if ((result = names) == null) {
                    result = properties.stringPropertyNames();
                    names  = result;
                }
            }
        }

        return result;
    }



    /**
     * Дочерний представитель
     */
    private static class ChildPresenter extends NamedPropertyPresenter {
        private final PropertyFilePresenter parent;
        private final String prefix;
        private volatile Set<String> names;


        public ChildPresenter(PropertyFilePresenter parent, String prefix) {
            if (parent == null)
                throw new IllegalArgumentException("Parent can't be null");

            if (prefix == null)
                throw new IllegalArgumentException("Prefix can't be null");

            this.parent = parent;
            this.prefix = prefix;
        }


        @Override
        public String getValue(String name) {
            return parent.getValue(prefix + name);
        }


        @Override
        public Set<String> getNames() {
            Set<String> result = null;

            if ((result = names) == null) {
                synchronized (this) {
                    if ((result = names) == null) {
                        result = new HashSet<String>();

                        int omit = prefix.length();

                        for (String item : parent.getNames())
                            if (item.startsWith(prefix))
                                result.add(item.substring(omit));

                        names  = result;
                    }
                }
            }

            return result;
        }

    }

}
