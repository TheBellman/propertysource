package net.parttimepolymath.properties.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;

/**
 * get a property from the set of JRE system properties.
 * 
 * @author robert
 */
@ThreadSafe
public class SystemResolver implements Resolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemResolver.class);

    @Override
    public final String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        return key == null || key.isEmpty() ? null : System.getProperty(key);
    }

}
