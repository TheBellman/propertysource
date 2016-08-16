package net.parttimepolymath.properties.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * resolver which always succesfully does not find a result.
 * 
 * @author robert
 */
public final class NullResolver implements Resolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NullResolver.class);

    @Override
    public String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        return null;
    }

}
