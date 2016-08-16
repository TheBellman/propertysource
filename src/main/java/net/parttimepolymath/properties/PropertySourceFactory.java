package net.parttimepolymath.properties;

import java.util.ArrayList;
import java.util.List;

import net.jcip.annotations.ThreadSafe;
import net.parttimepolymath.properties.resolver.DirectoryResolver;
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
@ThreadSafe
public final class PropertySourceFactory {

    /**
     * private constructor to prevent instantiation.
     */
    private PropertySourceFactory() {

    }

    /**
     * build a PropertySource. The priority order is: System, Environment, Files, Directory, Resources.
     * 
     * @param config the non-null configuration to derive the source from.
     * @return a non-null PropertySource.
     */
    public static PropertySource build(final PropertySourceConfig config) {

        List<Resolver> resolvers = new ArrayList<>();
        resolvers.add(new SystemResolver());
        resolvers.add(new EnvironmentResolver());
        if (!config.getFiles().isEmpty()) {
            resolvers.add(new FileResolver(config.getFiles()));
        }
        if (config.getDirectory() != null) {
            resolvers.add(new DirectoryResolver(config.getDirectory()));
        }
        if (config.getResourceClass() != null) {
            resolvers.add(new ResourceResolver(config.getResourceClass(), config.getResources()));
        }

        return new PropertyResolver(config.getCacheSize(), resolvers);
    }
}
