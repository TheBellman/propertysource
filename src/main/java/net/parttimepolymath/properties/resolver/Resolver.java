package net.parttimepolymath.properties.resolver;

/**
 * classes that implement this will attempt to resolve the lookup.
 * 
 * @author robert
 */
public interface Resolver {
    /**
     * find the value for the specified key.
     * 
     * @param key the key to look up. This is case sensitive.
     * @return the discovered value, or null if it cannot be found. Also null if the key is null or blank.
     */
    String get(String key);
}
