package net.parttimepolymath.properties.resolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;

/**
 * resolver which looks in a specified file(s). Note that if there are duplicated keys across different files,
 * there is no particular guarantee of which value is used, but it will probably be the last one found.
 * 
 * @author robert
 */
@ThreadSafe
public final class FileResolver implements Resolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileResolver.class);
    /**
     * time in ms after which a reload will be done.
     */
    private static final long REFRESH_INTERVAL = 60000;
    /**
     * the set of files being referenced. Note that this is the set of files discovered
     * at the time of construction, not at the time of load.
     */
    private final List<Path> paths;
    /**
     * the set of properties discovered during load.
     */
    private final Properties properties = new Properties();
    /**
     * when were the files last loaded?
     */
    private long lastRefreshed;
    /**
     * resource lock to allow for double-lock idiom while loading the files.
     */
    private final Lock resourceLock = new ReentrantLock();

    /**
     * specify a set of file paths to use. Any unresolvable paths are ignored.
     * 
     * @param files a set of file paths to use.
     */
    public FileResolver(final String... files) {
        this(Arrays.asList(files));
    }

    /**
     * specify a list of file paths to use. Any unresolvable paths are ignored.
     * 
     * @param files a non-null list of file paths to use.
     */
    public FileResolver(final List<String> files) {
        this.paths = new ArrayList<>();
        for (String path : files) {
            try {
                this.paths.add(Paths.get(path));
            } catch (InvalidPathException ipe) {
                LOGGER.warn("Invalid path [{}] supplied", path);
            }
        }
    }

    /**
     * specify a set of paths to use. It is assumed that all paths are resolvable.
     * 
     * @param files a set of paths to use.
     */
    public FileResolver(final Path... files) {
        this.paths = new ArrayList<>(Arrays.asList(files));
    }

    /**
     * find the files and load the contents.
     */
    private void doLoad() {
        LOGGER.debug("loading from files");
        if (reloadDue()) {
            resourceLock.lock();
            try {
                if (reloadDue()) {
                    properties.clear();
                    for (Path path : paths) {
                        try {
                            Properties props = new Properties();
                            props.load(Files.newInputStream(path));
                            properties.putAll(props);
                        } catch (IOException ioe) {
                            LOGGER.warn("Error loading from [{}]", path);
                        }
                    }
                    lastRefreshed = System.currentTimeMillis();
                }
            } finally {
                resourceLock.unlock();
            }
        }
    }

    /**
     * is a reload from files due?
     * 
     * @return true if it is.
     */
    private boolean reloadDue() {
        return System.currentTimeMillis() - lastRefreshed > REFRESH_INTERVAL;
    }

    @Override
    public String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        doLoad();
        return properties.getProperty(key);
    }

}
