package net.parttimepolymath.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * simple class using a builder pattern to construct a configuration to pass to the property source factory.
 * 
 * @author robert
 */
public final class PropertySourceConfig {
    /**
     * default size of the cache.
     */
    public static final int CACHE_SIZE = 64;

    private boolean useCache;
    private List<String> files;
    private Class resourceClass;
    private List<String> resources;

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
    public Class getResourceClass() {
        return resourceClass;
    }

    /**
     * @param resourceClass the resourceClass to set
     */
    public void setResourceClass(Class resourceClass) {
        this.resourceClass = resourceClass;
    }

    /**
     * @return the resources
     */
    public List<String> getResources() {
        return resources;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(List<String> resources) {
        this.resources = resources;
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

        private final PropertySourceConfig instance = new PropertySourceConfig();

        /**
         * default constructor, sets some defaults.
         */
        public Builder() {
            instance.useCache = false;
            instance.files = Collections.emptyList();
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
            instance.files = new ArrayList<>(Arrays.asList(files));
            return this;
        }

        /**
         * specify a class that will be used as the reference point for resolving resource property files.
         * 
         * @param clazz a non-null Class.
         * @return the Builder instance.
         */
        public Builder withResourceBase(final Class clazz) {
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
            instance.resources = new ArrayList<>(Arrays.asList(resources));
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
