package net.parttimepolymath.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class PropertySourceConfigTest {
    @Test
    public void testConstruction() {
        assertNotNull(PropertySourceConfig.builder().build());
    }

    @Test
    public void testCachingFlag() {
        PropertySourceConfig configOne = PropertySourceConfig.builder().build();
        PropertySourceConfig configTwo = PropertySourceConfig.builder().withCaching().build();

        assertFalse(configOne.isUseCache());
        assertTrue(configTwo.isUseCache());

        assertEquals(0, configOne.getCacheSize());
        assertEquals(PropertySourceConfig.CACHE_SIZE, configTwo.getCacheSize());
    }

    @Test
    public void testFileList() {
        PropertySourceConfig configOne = PropertySourceConfig.builder().withFiles(Arrays.asList("fred", "mary")).build();
        PropertySourceConfig configTwo = PropertySourceConfig.builder().withFiles().build();
        PropertySourceConfig configThree = PropertySourceConfig.builder().withFiles("fred", "mary").build();

        assertFalse(configOne.getFiles().isEmpty());
        assertTrue(configTwo.getFiles().isEmpty());
        assertFalse(configThree.getFiles().isEmpty());
    }
}
