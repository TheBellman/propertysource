package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileResolverTest {

    private static Path propertyPathOne;
    private static Path propertyPathTwo;
    private Resolver instance;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

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

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        propertyPathOne.toFile().delete();
        propertyPathTwo.toFile().delete();
    }

    @Test
    public void testPathConstructor() {
        instance = new FileResolver(propertyPathOne, propertyPathTwo);
        assertNull(instance.get("no.such"));
        assertEquals("one", instance.get("frt.key.one"));
        assertEquals("two", instance.get("frt.key.two"));
        assertEquals("III", instance.get("frt.key.three"));
        assertEquals("four", instance.get("frt.key.four"));
    }

    @Test
    public void testStringConstructor() {
        instance = new FileResolver(propertyPathOne.toString(), propertyPathTwo.toString());
        assertNull(instance.get("no.such"));
        assertEquals("one", instance.get("frt.key.one"));
        assertEquals("two", instance.get("frt.key.two"));
        assertEquals("III", instance.get("frt.key.three"));
        assertEquals("four", instance.get("frt.key.four"));
    }

}
