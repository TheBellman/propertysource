package net.parttimepolymath.properties.resolver;

/**
 * get a property from the set of JRE system properties.
 * 
 * @author robert
 */
public class SystemResolver implements Resolver {

    @Override
    public String get(String key) {
        return key == null || key.isEmpty() ? null : System.getProperty(key);
    }

}
