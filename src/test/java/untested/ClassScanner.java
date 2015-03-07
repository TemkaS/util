/**
 * java utilites © darkslave.net
 * https://github.com/darkslave86/util
 */
package untested;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class ClassScanner {
    private final static Pattern MATCH_JAVA_FILE = Pattern.compile("(?i)\\.class$");
    private final static Pattern CLEAR_JAR_NAME1 = Pattern.compile("(?i)^file:");
    private final static Pattern CLEAR_JAR_NAME2 = Pattern.compile("(?i)\\.jar!.*$");


    public static Set<String> scan(Class<?> clazz) throws IOException {
        URL clazzURL = null;

        CodeSource source = clazz.getProtectionDomain().getCodeSource();

        if (source != null) {
            clazzURL = source.getLocation();
        } else {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String pack = clazz.getPackage().getName();
            clazzURL = loader.getResource(pack.replace('.', '/'));
        }

        if (clazzURL == null)
            throw new IOException("Can't retrieve resource for " + clazz.getCanonicalName());

        String path = clazzURL.getPath();
        path = CLEAR_JAR_NAME1.matcher(path).replaceFirst("");
        path = CLEAR_JAR_NAME2.matcher(path).replaceFirst(".jar");

        Set<String> result = new LinkedHashSet<String>();

        if (path.endsWith(".jar")) {
            try (JarFile file = new JarFile(path)) {
                readJAR(result, file);
            }
        } else {
            readDIR(result, new File(path), "");
        }

        return result;
    }


    private static void readJAR(Collection<String> result, JarFile archive) {
        Enumeration<JarEntry> list = archive.entries();

        while (list.hasMoreElements()) {
            JarEntry item = list.nextElement();
            String name = item.getName();

            Matcher matcher = MATCH_JAVA_FILE.matcher(name);
            if (matcher.find()) {
                name = matcher.replaceFirst("").replace('\\', '.').replace('/', '.');
                result.add(name);
            }

        }
    }


    private static void readDIR(Collection<String> result, File directory, String prefix) {
        for (String name : directory.list()) {
            File item = new File(directory, name);

            if (item.isDirectory()) {
                readDIR(result, item, prefix + name + ".");
                continue;
            }

            if (!item.isFile())
                continue;

            Matcher matcher = MATCH_JAVA_FILE.matcher(name);
            if (matcher.find()) {
                name = prefix + matcher.replaceFirst("");
                result.add(name);
            }

        }
    }


    private ClassScanner() {}
}



