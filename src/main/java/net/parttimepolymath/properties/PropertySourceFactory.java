package net.parttimepolymath.properties;

import java.util.ArrayList;
import java.util.List;

import net.parttimepolymath.properties.resolver.EnvironmentResolver;
import net.parttimepolymath.properties.resolver.FileResolver;
import net.parttimepolymath.properties.resolver.PropertyResolver;
import net.parttimepolymath.properties.resolver.Resolver;
import net.parttimepolymath.properties.resolver.ResourceResolver;
import net.parttimepolymath.properties.resolver.SystemResolver;

/**
 * helper class for building a PropertySource instance.
 * 
 * @author robert
 */
public final class PropertySourceFactory {

    /**
     * private constructor to prevent instantiation.
     */
    private PropertySourceFactory() {

    }

    // TODO add a directory resolver - if passed a path which is a directory, read all .properties in there

    /**
     * build a PropertySource. The priority order is: System, Environment, Files, Resources.
     * 
     * @return a non-null PropertySource.
     */
    public static PropertySource build(PropertySourceConfig config) {

        List<Resolver> resolvers = new ArrayList<>();
        resolvers.add(new SystemResolver());
        resolvers.add(new EnvironmentResolver());
        if (!config.getFiles().isEmpty()) {
            resolvers.add(new FileResolver(config.getFiles()));
        }
        if (config.getResourceClass() != null) {
            resolvers.add(new ResourceResolver(config.getResourceClass(), config.getResources()));
        }

        return new PropertyResolver(config.getCacheSize(), resolvers);
    }
}
