package net.parttimepolymath.properties.resolver;

import net.parttimepolymath.cache.SimpleLRUCache;

/**
 * resolver that looks in it's own LRU cache for a match.
 * 
 * @author robert
 */
public class CacheResolverImpl implements CacheResolver {
    private final static int DEFAULT = 16;
    private final SimpleLRUCache<String, String> cache;

    /**
     * create an instance with an internal cache of the specified size.
     * 
     * @param cacheSize the size of the internal cache. If less than 1, defaults to 16.
     */
    public CacheResolverImpl(int cacheSize) {
        cache = new SimpleLRUCache<>(cacheSize < 1 ? DEFAULT : cacheSize);
    }

    @Override
    public String get(String key) {
        return cache.get(key);
    }

    @Override
    public void touchCache(String key, String value) {
        if (get(key) == null) {
            cache.put(key, value);
        }
    }
}
