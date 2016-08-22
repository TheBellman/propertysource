package net.parttimepolymath.properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.parttimepolymath.properties.resolver.PropertyResolver;

public class PropertySourceFactoryTest {

    @Test
    public void test() {
        PropertySource result = PropertySourceFactory.build(PropertySourceConfig.builder().withCaching()
                .usingConsul("192.168.99.100", 8500, "fred/mary/jane").withDirectory("/tmp").build());
        assertNotNull(result);
        assertThat(result, instanceOf(PropertyResolver.class));
    }

}
