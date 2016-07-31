package net.parttimepolymath.properties.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * resolver that looks through the current classpath for certain *.properties files.
 * TODO: the builder must be able to accept a list of property file names expected to be found in the class path.
 * 
 * @author robert
 */
public final class ResourceResolver implements Resolver {

    private final Properties properties = new Properties();

    /**
     * Primary constructor.
     * 
     * @param clazz A class to use as the base location, assumed to be non-null.
     * @param paths a non-null list of paths, specifying resources relative to the class per the semantics of Class.getResourceAsStream()
     */
    public ResourceResolver(@SuppressWarnings("rawtypes") Class clazz, List<String> paths) {
        for (String path : paths) {
            InputStream stream = clazz.getResourceAsStream(path);
            if (stream != null) {
                Properties props = new Properties();
                try {
                    props.load(stream);
                } catch (IOException e) {
                    // silently do nothing
                }
                properties.putAll(props);
            }
        }
    }

    /**
     * alternate constructor.
     * 
     * @param paths a set of paths, specifying resources relative to the class per the semantics of Class.getResourceAsStream()
     * @param clazz A class to use as the base location, assumed to be non-null.
     */
    public ResourceResolver(@SuppressWarnings("rawtypes") Class clazz, String... paths) {
        this(clazz, Arrays.asList(paths));
    }

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }

}
