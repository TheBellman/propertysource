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

/**
 * resolver which looks in a specified file(s).
 * 
 * @author robert
 */
public class FileResolver implements Resolver {
    private static final long REFRESH_INTERVAL = 60000;
    private final List<Path> paths;
    private final Properties properties = new Properties();
    private long lastRefreshed;
    private final Lock resourceLock = new ReentrantLock();

    /**
     * specify a set of file paths to use. Any unresolvable paths are ignored.
     * 
     * @param paths a set of file paths to use.
     */
    public FileResolver(String... paths) {
        this(Arrays.asList(paths));
    }

    /**
     * specify a list of file paths to use. Any unresolvable paths are ignored.
     * 
     * @param paths a non-null list of file paths to use.
     */
    public FileResolver(List<String> paths) {
        this.paths = new ArrayList<>();
        for (String path : paths) {
            try {
                this.paths.add(Paths.get(path));
            } catch (InvalidPathException ipe) {
                // silently do nothing
            }
        }
    }

    /**
     * specify a set of paths to use. It is assumed that all paths are resolvable.
     * 
     * @param paths a set of paths to use.
     */
    public FileResolver(Path... paths) {
        this.paths = new ArrayList<>(Arrays.asList(paths));
    }

    private void doLoad() {
        if (reloadDue()) {
            resourceLock.lock();
            try {
                if (reloadDue()) {
                    for (Path path : paths) {
                        try {
                            Properties props = new Properties();
                            props.load(Files.newInputStream(path));
                            properties.putAll(props);
                        } catch (IOException ioe) {
                            // silently do nothing
                        }
                    }
                    lastRefreshed = System.currentTimeMillis();
                }
            } finally {
                resourceLock.unlock();
            }
        }
    }

    private boolean reloadDue() {
        return System.currentTimeMillis() - lastRefreshed > REFRESH_INTERVAL;
    }

    @Override
    public String get(String key) {
        doLoad();
        return properties.getProperty(key);
    }

}
