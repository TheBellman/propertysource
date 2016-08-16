package net.parttimepolymath.properties.resolver;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;

/**
 * resolver which searches for *.properties files in a specified directory. It delegates responsibility to an internal
 * FileResolver.
 * 
 * @author robert
 */
@ThreadSafe
public final class DirectoryResolver implements Resolver {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryResolver.class);

    /**
     * filter to match properties files.
     */
    private static final FileFilter FILTER = new FileFilter() {
        @Override
        public boolean accept(final File pathname) {
            return pathname.isFile() && pathname.getName().endsWith(".properties");
        }
    };

    /**
     * delegate resolver.
     */
    private final Resolver fileResolver;

    /**
     * construct using the path to the directory. If the supplied path does not resolve to a directory, or the directory
     * does not have *.properties files in it, this resolver will silently do nothing
     * 
     * @param path what we hope is a valid path to a directory.
     */
    public DirectoryResolver(final String path) {
        List<String> files = findFiles(path);
        if (files.isEmpty()) {
            fileResolver = new NullResolver();
        } else {
            fileResolver = new FileResolver(files);
        }
    }

    /**
     * attempt to find *.properties under the supplied path. If the path is not a resolvable directory, this
     * will automatically be an empty list. Note that this does not recurse into subdirectories, however the
     * result list will be sorted according to string collation rules.
     * 
     * @param path what we assume to be a valid representation of a resolvable directory.
     * @return a non-null but possibly empty list of valid paths to discovered properties files.
     */
    private List<String> findFiles(final String path) {
        if (!isGoodPath(path)) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();

        for (File file : new File(path).listFiles(FILTER)) {
            result.add(file.getAbsolutePath());
        }

        Collections.sort(result);
        return result;
    }

    /**
     * does the parameter represent a resolvable directory?
     * 
     * @param path the string path to test.
     * @return true if the path is good, false otherwise.
     */
    private boolean isGoodPath(final String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        try {
            Path trial = Paths.get(path);
            return Files.exists(trial) && Files.isDirectory(trial);
        } catch (InvalidPathException ipe) {
            LOGGER.warn("Invalid path [{}] supplied", path);
            return false;
        }
    }

    @Override
    public String get(final String key) {
        LOGGER.debug("attempting get({})", key);
        return fileResolver.get(key);
    }

}
