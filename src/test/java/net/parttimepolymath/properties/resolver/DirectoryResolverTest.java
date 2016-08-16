package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DirectoryResolverTest {

    private static Path propertyPathOne;
    private static Path propertyPathTwo;
    private static Path propertyPathThree;
    private static Path goodDirectory;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        goodDirectory = Files.createTempDirectory("DirectoryResolver");
        goodDirectory.toFile().deleteOnExit();

        propertyPathOne = Files.createTempFile(goodDirectory, "FileResolverTest1", ".properties");
        propertyPathTwo = Files.createTempFile(goodDirectory, "FileResolverTest2", ".properties");
        propertyPathThree = Files.createTempFile(goodDirectory, "FileResolverTest3", ".txt");
        propertyPathOne.toFile().deleteOnExit();
        propertyPathTwo.toFile().deleteOnExit();
        propertyPathThree.toFile().deleteOnExit();

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

        Properties propertiesThree = new Properties();
        propertiesThree.setProperty("frt.key.two", "2");
        propertiesThree.setProperty("frt.key.three", "3");
        propertiesThree.setProperty("frt.key.four", "4");
        propertiesThree.store(new FileOutputStream(propertyPathThree.toString()), null);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        propertyPathOne.toFile().delete();
        propertyPathTwo.toFile().delete();
        propertyPathThree.toFile().delete();
        goodDirectory.toFile().delete();
    }

    @Test
    public void testGoodDirectory() {
        Resolver instance = new DirectoryResolver(goodDirectory.toString());
        assertNull(instance.get("no.such"));
        assertEquals("one", instance.get("frt.key.one"));
        assertEquals("two", instance.get("frt.key.two"));
        assertEquals("III", instance.get("frt.key.three"));
        assertEquals("four", instance.get("frt.key.four"));
    }

    @Test
    public void testBadDirectory() {
        Resolver instance = new DirectoryResolver("^.:this/is\not/even:a/path,/is/it");
        assertNull(instance.get("no.such"));
        assertNull("one", instance.get("frt.key.one"));
        assertNull("two", instance.get("frt.key.two"));
        assertNull("III", instance.get("frt.key.three"));
        assertNull("four", instance.get("frt.key.four"));
    }

    @Test
    public void testNullDirectory() {
        Resolver instance = new DirectoryResolver(null);
        assertNull(instance.get("no.such"));
        assertNull("one", instance.get("frt.key.one"));
        assertNull("two", instance.get("frt.key.two"));
        assertNull("III", instance.get("frt.key.three"));
        assertNull("four", instance.get("frt.key.four"));
    }

    @Test
    public void testBlankDirectory() {
        Resolver instance = new DirectoryResolver(null);
        assertNull(instance.get("no.such"));
        assertNull("one", instance.get("frt.key.one"));
        assertNull("two", instance.get("frt.key.two"));
        assertNull("III", instance.get("frt.key.three"));
        assertNull("four", instance.get("frt.key.four"));
    }

    @Test
    public void testEmptyDirectory() throws IOException {
        Path emptyDir = Files.createTempDirectory("DirectoryResolver");
        Resolver instance = new DirectoryResolver(emptyDir.toString());
        assertNull(instance.get("no.such"));
        assertNull("one", instance.get("frt.key.one"));
        assertNull("two", instance.get("frt.key.two"));
        assertNull("III", instance.get("frt.key.three"));
        assertNull("four", instance.get("frt.key.four"));
    }

}
