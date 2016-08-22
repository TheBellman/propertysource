package net.parttimepolymath.properties.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;
import net.parttimepolymath.properties.consul.ConsulClient;
import net.parttimepolymath.properties.consul.ConsulClientImpl;

/**
 * resolver which attempts to read from Consul. The only thing we support is fetching a simple value from a key/value query,
 * using something like "http://{host}:{port}/v1/kv/{prefix}/{key}?raw", where "key" will be the final part of a hierarchical
 * key, and "prefix" is the rest of the key (and obviously optional if the key is not multi-level). The intention is that
 * we interpret the key hierarchy to represent some sort of name space.
 * 
 * @author robert
 */
@ThreadSafe
public final class ConsulResolver implements Resolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulResolver.class);
    /**
     * The client facade used to talk to Consul.
     */
    private final ConsulClient consulClient;
    /**
     * the optional key prefix.
     */
    private final String prefix;

    /**
     * primary constructor.
     * 
     * @param host the host to target, assumed to be non-null and non-blank.
     * @param port the port to target, assumed to be a useful port.
     * @param keyPrefix the prefix of the key hierarchy, allowed to be null or blank.
     */
    public ConsulResolver(final String host, int port, final String keyPrefix) {
        prefix = keyPrefix;
        consulClient = new ConsulClientImpl(host, port);
    }

    /**
     * alternate constructor. This defaults the port to 8500.
     * 
     * @param host the host to target, assumed to be non-null and non-blank.
     * @param keyPrefix the prefix of the key hierarchy, allowed to be null or blank.
     */
    public ConsulResolver(final String host, final String keyPrefix) {
        this(host, 8500, keyPrefix);
    }

    /**
     * alternate constructor. This defaults the port to 8500 and host to localhost.
     * 
     * @param keyPrefix the prefix of the key hierarchy, allowed to be null or blank.
     */
    public ConsulResolver(final String keyPrefix) {
        this("localhost", keyPrefix);
    }

    /**
     * alternate constructor, primarily intended for testing.
     * 
     * @param client a ConsulClient to inject.
     * @param keyPrefix the prefix of the key hierarchy, allowed to be null or blank.
     */
    public ConsulResolver(final ConsulClient client, final String keyPrefix) {
        prefix = keyPrefix;
        consulClient = client;
    }

    @Override
    public String get(String key) {
        LOGGER.debug("attempting get({})", key);
        return consulClient.getValue(prefix, key);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ConsulResolver [consulClient=%s, prefix=%s]", consulClient, prefix);
    }

}
