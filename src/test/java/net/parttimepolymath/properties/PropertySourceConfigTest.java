package net.parttimepolymath.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

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
    public void testDirectory() {
        PropertySourceConfig configOne = PropertySourceConfig.builder().build();
        PropertySourceConfig configTwo = PropertySourceConfig.builder().withDirectory("/tmp").build();
        assertNull(configOne.getDirectory());
        assertEquals("/tmp", configTwo.getDirectory());
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

    @Test
    public void testNullFileList() {
        PropertySourceConfig config = PropertySourceConfig.builder().withFiles((List<String>) null).build();
        assertTrue(config.getFiles().isEmpty());
    }

    @Test
    public void testConsul() {
        PropertySourceConfig config = PropertySourceConfig.builder().usingConsul("192.168.99.100", 8500, "fred/mary/jane").build();
        assertEquals("192.168.99.100", config.getConsulHost());
        assertEquals(8500, config.getConsulPort());
        assertEquals("fred/mary/jane", config.getConsulPrefix());
    }

    @Test
    public void testConsulNull() {
        PropertySourceConfig config = PropertySourceConfig.builder().usingConsul(null, 8500, "fred/mary/jane").build();
        assertNull(config.getConsulHost());
        assertEquals(0, config.getConsulPort());
        assertNull(config.getConsulPrefix());
    }

    @Test
    public void testResourceList() {
        PropertySourceConfig configOne = PropertySourceConfig.builder().withResourceBase(this.getClass())
                .withResources(Arrays.asList("fred", "mary")).build();
        PropertySourceConfig configTwo = PropertySourceConfig.builder().withResourceBase(this.getClass()).withResources().build();
        PropertySourceConfig configThree = PropertySourceConfig.builder().withResourceBase(this.getClass()).withResources("fred", "mary")
                .build();
        PropertySourceConfig configFour = PropertySourceConfig.builder().withResourceBase(this.getClass())
                .withResources((List<String>) null).build();

        assertNotNull(configOne.getClass());
        assertNotNull(configTwo.getClass());
        assertNotNull(configThree.getClass());
        assertNotNull(configFour.getClass());

        assertFalse(configOne.getResources().isEmpty());
        assertTrue(configTwo.getResources().isEmpty());
        assertFalse(configThree.getResources().isEmpty());
        assertTrue(configFour.getResources().isEmpty());

    }
}
