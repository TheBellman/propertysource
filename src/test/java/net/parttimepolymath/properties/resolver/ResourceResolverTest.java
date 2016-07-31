package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ResourceResolverTest {

    private Resolver instance;

    @Before
    public void setUp() throws Exception {
        instance = new ResourceResolver(this.getClass(), "/test1.properties", "test2.properties");
    }

    @Test
    public void test() {
        assertNull(instance.get("no.such"));
        assertEquals("one", instance.get("key.one"));
        assertEquals("two", instance.get("key.two"));
        assertEquals("III", instance.get("key.three"));
        assertEquals("four", instance.get("key.four"));
    }

}
