package net.parttimepolymath.properties.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;

/**
 * resolver that looks through the current classpath for certain properties files.
 * 
 * @author robert
 */
@ThreadSafe
public final class ResourceResolver implements Resolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyResolver.class);

    /**
     * a static set of properties that have been discovered.
     */
    private final Properties properties = new Properties();

    /**
     * Primary constructor.
     * 
     * @param clazz A class to use as the base location, assumed to be non-null.
     * @param paths a non-null list of paths, specifying resources relative to the class per the semantics of Class.getResourceAsStream()
     */
    public ResourceResolver(@SuppressWarnings("rawtypes") final Class clazz, final List<String> paths) {
        for (String path : paths) {
            InputStream stream = clazz.getResourceAsStream(path);
            if (stream != null) {
                Properties props = new Properties();
                try {
                    props.load(stream);
                } catch (IOException e) {
                    LOGGER.warn("Error loading from [{}]", path);
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
    public ResourceResolver(@SuppressWarnings("rawtypes") final Class clazz, final String... paths) {
        this(clazz, Arrays.asList(paths));
    }

    @Override
    public String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        return properties.getProperty(key);
    }

}
