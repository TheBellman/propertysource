package net.parttimepolymath.properties;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PropertySourceFactoryTest {

    @Test
    public void test() {
        PropertySource result = PropertySourceFactory.build(PropertySourceConfig.builder().withCaching().build());
        assertNotNull(result);
    }

}
