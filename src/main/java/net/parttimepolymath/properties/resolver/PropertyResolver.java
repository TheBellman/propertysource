package net.parttimepolymath.properties.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;
import net.parttimepolymath.properties.PropertySource;

/**
 * container class for implementing the hierarchy of locations.
 * 
 * @author robert
 */
@ThreadSafe
public final class PropertyResolver implements PropertySource {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyResolver.class);
    /**
     * the set of resolvers that this class delegates to.
     */
    private final List<Resolver> resolverChain = new ArrayList<>();
    /**
     * the cache of discovered values.
     */
    private final CacheResolver cacheResolver;

    /**
     * create an instance using the defined set of individual resolvers.
     * 
     * @param cacheSize if greater than zero, local result caching will be used.
     * @param resolvers the set of resolvers to use.
     */
    public PropertyResolver(final int cacheSize, final Resolver... resolvers) {
        this(cacheSize, Arrays.asList(resolvers));
    }

    /**
     * create an instance using the defined list of individual resolvers.
     * 
     * @param cacheSize if greater than zero, local result caching will be used.
     * @param resolvers the non-null list of resolvers to use.
     */
    public PropertyResolver(final int cacheSize, final List<Resolver> resolvers) {
        if (cacheSize > 0) {
            cacheResolver = new CacheResolverImpl(cacheSize);
        } else {
            cacheResolver = new CacheResolver() {
                @Override
                public String get(final String key) {
                    return null;
                }

                @Override
                public void touchCache(final String key, final String value) {
                    // silently do nothing
                }

            };
        }
        resolverChain.add(cacheResolver);
        resolverChain.addAll(resolvers);
    }

    @Override
    public String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        for (Resolver resolver : resolverChain) {
            String value = resolver.get(key);
            if (value != null) {
                cacheResolver.touchCache(key, value);
                return value;
            }
        }
        return null;
    }

    @Override
    public int getNumber(final String key, final int defaultValue) {
        String value = get(key);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    @Override
    public boolean getFlag(final String key, final boolean defaultValue) {
        String value = get(key);

        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    @Override
    public int getNumber(final String key) {
        return getNumber(key, 0);
    }

    @Override
    public boolean getFlag(final String key) {
        return getFlag(key, false);
    }

}
