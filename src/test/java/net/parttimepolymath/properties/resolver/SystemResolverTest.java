package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class SystemResolverTest {
    private static Resolver instance;

    @BeforeClass
    public static void setup() {
        instance = new SystemResolver();
    }

    @Test
    public void testResolvable() {
        assertNotNull(instance.get("java.class.path"));
    }

    @Test
    public void testNull() {
        assertNull(instance.get(null));
    }

    @Test
    public void testBlank() {
        assertNull(instance.get(""));
    }

    @Test
    public void testUnresolvable() {
        assertNull(instance.get("this.should.never.exist.unless.you.really.try.hard"));
    }

}
