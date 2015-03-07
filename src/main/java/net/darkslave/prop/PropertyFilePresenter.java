/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package net.darkslave.prop;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;





/**
 *  Класс презентер java properties
 */
public class PropertyFilePresenter extends AbstractPropertyPresenter implements PropertyPresenter {
    private final Properties properties;
    private volatile Set<String> names;


    public PropertyFilePresenter(Path path, Charset charset) throws IOException {
        this(Files.newInputStream(path), charset);
    }


    public PropertyFilePresenter(InputStream stream, Charset charset) throws IOException {
        properties = new Properties();

        try (Reader reader = new InputStreamReader(stream, charset)) {
            properties.load(reader);
        }

    }


    @Override
    protected String getValue(String name) {
        return properties.getProperty(name);
    }


    @Override
    public Set<String> getNames() {

        if (names == null) {
            synchronized (this) {
                if (names == null) {
                    final Set<String> temp = properties.stringPropertyNames();
                    names = temp;
                }
            }
        }

        return names;
    }


}
