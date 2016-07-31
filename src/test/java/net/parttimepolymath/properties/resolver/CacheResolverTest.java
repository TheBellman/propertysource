package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class CacheResolverTest {
    private static CacheResolverImpl resolver;

    @BeforeClass
    public static void setup() {
        resolver = new CacheResolverImpl(16);
    }

    @Test
    public void testNotFound() {
        String value = resolver.get("no.such.property");
        assertNull(value);
    }

    @Test
    public void testGetAndTouch() {
        String value = resolver.get("property.to.add");
        assertNull(value);

        resolver.touchCache("property.to.add", "fish");

        value = resolver.get("property.to.add");
        assertEquals("fish", value);
    }
}
