package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.parttimepolymath.properties.PropertySource;
import net.parttimepolymath.properties.PropertySourceConfig;
import net.parttimepolymath.properties.PropertySourceFactory;

public class PropertyResolverTest {
    private PropertySource source;
    private static final String TESTKEY = "net.partimepolymath.test.value";
    private Path propertyPathOne;
    private Path propertyPathTwo;

    @Before
    public void setup() throws IOException {
        propertyPathOne = Files.createTempFile("FileResolverTest1", "properties");
        propertyPathTwo = Files.createTempFile("FileResolverTest2", "properties");

        Properties propertiesOne = new Properties();
        propertiesOne.setProperty("frt.key.one", "one");
        propertiesOne.setProperty("frt.key.two", "two");
        propertiesOne.setProperty("frt.key.three", "three");
        propertiesOne.store(new FileOutputStream(propertyPathOne.toString()), null);

        Properties propertiesTwo = new Properties();
        propertiesTwo.setProperty("frt.key.two", "two");
        propertiesTwo.setProperty("frt.key.three", "III");
        propertiesTwo.setProperty("frt.key.four", "four");
        propertiesTwo.store(new FileOutputStream(propertyPathTwo.toString()), null);

        source = PropertySourceFactory
                .build(PropertySourceConfig.builder().withFiles(propertyPathOne.toString(), propertyPathTwo.toString())
                        .withResourceBase(this.getClass()).withResources("/test1.properties", "test2.properties").build());
    }

    @After
    public void teardown() throws IOException {
        Files.delete(propertyPathOne);
        Files.delete(propertyPathTwo);
    }

    @Test
    public void testFileResolution() {
        assertEquals("one", source.get("frt.key.one"));
        assertEquals("two", source.get("frt.key.two"));
        assertEquals("III", source.get("frt.key.three"));
        assertEquals("four", source.get("frt.key.four"));
    }

    @Test
    public void testResourceResolution() {
        assertEquals("one", source.get("key.one"));
        assertEquals("two", source.get("key.two"));
        assertEquals("III", source.get("key.three"));
        assertEquals("four", source.get("key.four"));
    }

    @Test
    public void testResolveSystem() {
        assertNotNull(source.get("java.class.path"));
    }

    @Test
    public void testResolveEnvironment() {
        assertNotNull(source.get("PATH"));
    }

    @Test
    public void testBoolean() {
        assertFalse(source.getFlag("some.flag.that.does.not.exist"));
    }

    @Test
    public void testBooleanDefault() {
        assertTrue(source.getFlag("some.flag.that.does.not.exist", true));
    }

    @Test
    public void testNumber() {
        assertEquals(0, source.getNumber("some.weird.value.or.other"));
    }

    @Test
    public void testNumberDefault() {
        assertEquals(123456, source.getNumber("some.weird.value.or.other", 123456));
    }

    @Test
    public void testKnownNumber() {
        assertEquals(0, source.getNumber(TESTKEY));
        System.setProperty(TESTKEY, "42");
        assertEquals(42, source.getNumber(TESTKEY));
        System.clearProperty(TESTKEY);
        assertEquals(0, source.getNumber(TESTKEY));
    }

    @Test
    public void testBadNumber() {
        assertEquals(0, source.getNumber(TESTKEY));
        System.setProperty(TESTKEY, "fish");
        assertEquals(42, source.getNumber(TESTKEY, 42));
        System.clearProperty(TESTKEY);
        assertEquals(0, source.getNumber(TESTKEY));
    }

    @Test
    public void testKnownFlag() {
        assertFalse(source.getFlag(TESTKEY));
        System.setProperty(TESTKEY, "TRUE");
        assertTrue(source.getFlag(TESTKEY));
        System.clearProperty(TESTKEY);
        assertFalse(source.getFlag(TESTKEY));
    }

    @Test
    public void testBadFlag() {
        assertFalse(source.getFlag(TESTKEY));
        System.setProperty(TESTKEY, "fish");
        assertFalse(source.getFlag(TESTKEY));
        System.clearProperty(TESTKEY);
        assertFalse(source.getFlag(TESTKEY));
    }

    @Test
    public void testKnownValue() {
        assertNull(source.get(TESTKEY));
        System.setProperty(TESTKEY, "42");
        assertEquals("42", source.get(TESTKEY));
        System.clearProperty(TESTKEY);
        assertNull(source.get(TESTKEY));
    }

}
