package net.parttimepolymath.properties.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;
import net.parttimepolymath.cache.SimpleLRUCache;

/**
 * resolver that looks in it's own LRU cache for a match.
 * 
 * @author robert
 */
@ThreadSafe
public final class CacheResolverImpl implements CacheResolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheResolverImpl.class);

    /**
     * default size of the cache.
     */
    private static final int DEFAULT = 16;
    /**
     * the cache instance which is used.
     */
    private final SimpleLRUCache<String, String> cache;

    /**
     * create an instance with an internal cache of the specified size.
     * 
     * @param cacheSize the size of the internal cache. If less than 1, defaults to 16.
     */
    public CacheResolverImpl(final int cacheSize) {
        cache = new SimpleLRUCache<>(cacheSize < 1 ? DEFAULT : cacheSize);
    }

    @Override
    public String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        return cache.get(key);
    }

    @Override
    public void touchCache(final String key, final String value) {
        if (get(key) == null) {
            cache.put(key, value);
        }
    }
}
