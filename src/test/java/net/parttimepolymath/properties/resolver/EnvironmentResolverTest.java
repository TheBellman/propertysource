package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class EnvironmentResolverTest {

    private static Resolver instance;

    @BeforeClass
    public static void setup() {
        instance = new EnvironmentResolver();
    }

    @Test
    public void testResolvable() {
        // TODO: this might be OS specific
        assertNotNull(instance.get("PATH"));
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
