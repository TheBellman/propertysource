package net.parttimepolymath.properties.resolver;

public interface CacheResolver extends Resolver {
    /**
     * touch an item in the cache to keep it fresh. Note that this may add to the cache if it was not previously present.
     * 
     * @param key the key of the item of interest.
     * @param value the value of the item. If the key was not already in the cache, this is the value stored.
     */
    void touchCache(String key, String value);

}
