package net.parttimepolymath.properties;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import net.parttimepolymath.properties.resolver.PropertyResolver;

public class PropertySourceFactoryTest {

    @Test
    public void test() {
        PropertySource result = PropertySourceFactory.build(PropertySourceConfig.builder().withCaching().withDirectory("/tmp").build());
        assertNotNull(result);
        assertThat(result, instanceOf(PropertyResolver.class));
    }

}
