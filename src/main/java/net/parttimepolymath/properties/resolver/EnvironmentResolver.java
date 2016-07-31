package net.parttimepolymath.properties.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;

/**
 * resolve a lookup by examining the runtime environment.
 * 
 * @author robert
 */
@ThreadSafe
public final class EnvironmentResolver implements Resolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentResolver.class);

    @Override
    public String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        return key == null ? null : System.getenv().get(key);
    }

}
