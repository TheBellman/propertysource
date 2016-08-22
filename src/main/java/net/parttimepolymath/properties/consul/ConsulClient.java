package net.parttimepolymath.properties.consul;

/**
 * Facade for dealing with Consul. The only thing we support is fetching a simple value from a key/value query, using
 * something like "http://{host}:{port}/v1/kv/{prefix}/{key}?raw", where "key" will be the final part of a hierarchical
 * key, and "prefix" is the rest of the key (and obviously optional if the key is not multi-level). The intention is that
 * we interpret the key hierarchy to represent some sort of name space.
 * 
 * @author robert
 */
public interface ConsulClient {
    /**
     * get a value for a given key.
     * 
     * @param prefix the first part of the hierarchical key. May be null or blank.
     * @param key the key to search for, being the last part of the hierarchical key.
     * @return null if not found, or no key provided, otherwise the retrieved value.
     */
    String getValue(String prefix, String key);
}
