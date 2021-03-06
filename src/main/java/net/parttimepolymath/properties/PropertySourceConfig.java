package net.parttimepolymath.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.ThreadSafe;

/**
 * simple class using a builder pattern to construct a configuration to pass to the property source factory.
 * 
 * @author robert
 */
@ThreadSafe
public final class PropertySourceConfig {
    /**
     * default size of the cache.
     */
    public static final int CACHE_SIZE = 64;

    /**
     * will local caching be used?
     */
    private boolean useCache;
    /**
     * the list of files used for the file resolver.
     */
    private List<String> files;
    /**
     * the base class used to define the root of the resource search.
     */
    @SuppressWarnings("rawtypes")
    private Class resourceClass;
    /**
     * the list of resource paths to look for.
     */
    private List<String> resources;
    /**
     * the path to use for the directory resolver.
     */
    private String directory;

    /**
     * the host or IP address for Consul.
     */
    private String consulHost;

    /**
     * the port for Consul.
     */
    private int consulPort;

    /*
     * the consul key prefix.
     */
    private String consulPrefix;

    /**
     * private constructor to prevent direct construction.
     */
    private PropertySourceConfig() {

    }

    /**
     * @return the useCache
     */
    public boolean isUseCache() {
        return useCache;
    }

    /**
     * get the cache size.
     * 
     * @return 0 if we are not using the cache, the default size otherwise.
     */
    public int getCacheSize() {
        return useCache ? CACHE_SIZE : 0;
    }

    /**
     * @return the files
     */
    public List<String> getFiles() {
        return files;
    }

    /**
     * @return the resourceClass
     */
    @SuppressWarnings("rawtypes")
    public Class getResourceClass() {
        return resourceClass;
    }

    /**
     * @return the resources
     */
    public List<String> getResources() {
        return resources;
    }

    /**
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * @return the consulHost
     */
    public String getConsulHost() {
        return consulHost;
    }

    /**
     * @return the consulPort
     */
    public int getConsulPort() {
        return consulPort;
    }

    /**
     * @return the consulPrefix
     */
    public String getConsulPrefix() {
        return consulPrefix;
    }

    /**
     * obtain a builder to construct a config instance with.
     * 
     * @return a non-null PropertySourceConfig.Builder
     */
    public static Builder builder() {
        return new PropertySourceConfig.Builder();
    }

    /**
     * utility class to perform the actual construction. Ideally this is not constructed directly, instead use
     * PropertySourceConfig.builder()
     * 
     * @author robert
     */
    public static final class Builder {

        /**
         * the instance of the configuration that is being assembled.
         */
        private final PropertySourceConfig instance = new PropertySourceConfig();

        /**
         * default constructor, sets some defaults.
         */
        public Builder() {
            instance.useCache = false;
            instance.files = Collections.emptyList();
            instance.resources = Collections.emptyList();
        }

        /**
         * specify that local caching of recently resolved properties is done.
         * 
         * @return the Builder instance.
         */
        public Builder withCaching() {
            instance.useCache = true;
            return this;
        }

        /**
         * specify a list of properties files that can be used as sources.
         * 
         * @param files the list of file paths. A null list will be ignored.
         * @return the Builder instance.
         */
        public Builder withFiles(final List<String> files) {
            if (files != null) {
                instance.files = new ArrayList<>(files);
            }
            return this;
        }

        /**
         * specify a set of properties files that can be used as sources.
         * 
         * @param files the set of file paths.
         * @return the Builder instance.
         */
        public Builder withFiles(final String... files) {
            return withFiles(Arrays.asList(files));
        }

        /**
         * specify a directory containing *.properties files that will be used.
         * 
         * @param path the path to the directory, which is assumed to be a good path pointing at a directory.
         * @return the Builder instance.
         */
        public Builder withDirectory(final String path) {
            instance.directory = path;
            return this;
        }

        /**
         * specify a class that will be used as the reference point for resolving resource property files.
         * 
         * @param clazz a non-null Class.
         * @return the Builder instance.
         */
        public Builder withResourceBase(@SuppressWarnings("rawtypes") final Class clazz) {
            instance.resourceClass = clazz;
            return this;
        }

        /**
         * specify a list of resource paths to use as sources. Paths are resolved using the semantics of
         * Class.getResourceAsStream(). Note that if the resourceBase class has not been defined, this list
         * will not be used.
         * 
         * @param resources the list of resource paths. A null list will be ignored.
         * @return the Builder instance.
         */
        public Builder withResources(final List<String> resources) {
            if (resources != null) {
                instance.resources = new ArrayList<>(resources);
            }
            return this;
        }

        /**
         * specify a set of resource paths to use as sources. Paths are resolved using the semantics of
         * Class.getResourceAsStream(). Note that if the resourceBase class has not been defined, this set
         * will not be used.
         * 
         * @param resources the set of resource paths.
         * @return the Builder instance.
         */
        public Builder withResources(final String... resources) {
            return withResources(Arrays.asList(resources));
        }

        /**
         * Include Consul in the resolution chain. If using Consul and frequently polling a key, it is recommended
         * that you use caching, to avoid the overhead of frequent trips across the network.
         * 
         * @param host the target host name or IP Address of the Consul service. All parameters are ignored if host is null.
         * @param port the port to lookup (8500 is the default for Consul)
         * @param prefix the base prefix for lookups. May be null or empty.
         * @return the Builder instance.
         */
        public Builder usingConsul(final String host, int port, String prefix) {
            if (host != null) {
                instance.consulHost = host;
                instance.consulPort = port;
                instance.consulPrefix = prefix;
            }
            return this;
        }

        /**
         * return the constructed instances of the configuration.
         * 
         * @return the non-null instance of the configuration.
         */
        public PropertySourceConfig build() {
            return instance;
        }
    }
}
