package net.parttimepolymath.properties;

import net.parttimepolymath.properties.resolver.Resolver;

/**
 * This is the sole access point for properties. Implementations of this class should not be instantiated directly,
 * but instead obtained via the factory class.
 * 
 * @author robert
 */
public interface PropertySource extends Resolver {

    /**
     * find a value for the specified key and try to treat it as a number.
     * 
     * @param key the key to look up. This is case sensitive.
     * @return the discovered value, or 0 if it cannot be found or could not be parsed as a number. Also 0 if the key is null or blank.
     */
    int getNumber(String key);

    /**
     * find a value for the specified key and try to treat it as a number.
     * 
     * @param key the key to look up. This is case sensitive.
     * @param defaultValue the value to return if the key cannot be resolved.
     * @return the discovered value, or defaultValue if it cannot be found or could not be parsed as a number. Also defaultValue if the key
     *         is null or blank.
     */
    int getNumber(String key, int defaultValue);

    /**
     * find a value for the specified key and treat it as a boolean flag.
     * 
     * @param key the key to lookup. This is case sensitive.
     * @return the discovered value, or false if it cannot be found, could not be parsed into a boolean, or the key is null or blank.
     */
    boolean getFlag(String key);

    /**
     * find a value for the specified key and treat it as a boolean flag.
     * 
     * @param key the key to lookup. This is case sensitive.
     * @param defaultValue the value to return if the key cannot be resolved.
     * @return the discovered value, or false if it cannot be found, could not be parsed into a boolean, or the key is null or blank.
     */
    boolean getFlag(String key, boolean defaultValue);

}
