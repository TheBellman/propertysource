package net.parttimepolymath.properties.resolver;

/**
 * resolve a lookup by examining the runtime environment.
 * 
 * @author robert
 */
public class EnvironmentResolver implements Resolver {

    @Override
    public String get(String key) {
        return key == null ? null : System.getenv().get(key);
    }

}
