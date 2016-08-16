package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NullResolverTest {

    @Test
    public void test() {
        Resolver instance = new NullResolver();
        assertNull(instance.get("anykey"));
        assertNull(instance.get(null));
        assertNull(instance.get(""));
    }

}
