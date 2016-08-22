package net.parttimepolymath.properties.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import net.parttimepolymath.properties.consul.ConsulClient;

public class ConsulResolverTest {

    private ConsulClient client;

    @Before
    public void setUp() throws Exception {
        client = new ConsulClient() {
            @Override
            public String getValue(String prefix, String key) {
                if (key.equals("fail")) {
                    return null;
                }
                return String.format("prefix:{%s}, key:{%s}", prefix, key);
            }
        };
    }

    @Test
    public void testKeyFound() {
        ConsulResolver instance = new ConsulResolver(client, "some/prefix");
        assertEquals("prefix:{some/prefix}, key:{somekey}", instance.get("somekey"));
    }

    @Test
    public void testKeyNotFound() {
        ConsulResolver instance = new ConsulResolver(client, "some/prefix");
        assertNull(instance.get("fail"));
    }

    @Test
    public void testConstructor() {
        ConsulResolver instance = new ConsulResolver("some.server.com", 8555, "aPrefix");
        assertEquals("ConsulResolver [consulClient=ConsulClientImpl [host=some.server.com, port=8555], prefix=aPrefix]",
                instance.toString());
    }

    @Test
    public void testConstructorTwo() {
        ConsulResolver instance = new ConsulResolver("some.server.com", "aPrefix");
        assertEquals("ConsulResolver [consulClient=ConsulClientImpl [host=some.server.com, port=8500], prefix=aPrefix]",
                instance.toString());
    }

    @Test
    public void testConstructorThree() {
        ConsulResolver instance = new ConsulResolver("aPrefix");
        assertEquals("ConsulResolver [consulClient=ConsulClientImpl [host=localhost, port=8500], prefix=aPrefix]", instance.toString());
    }

}
