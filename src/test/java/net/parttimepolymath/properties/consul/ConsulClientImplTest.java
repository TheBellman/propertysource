package net.parttimepolymath.properties.consul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class ConsulClientImplTest {
    private ConsulClientImpl instance;
    private static MockWebServer server;
    private static HttpUrl serverUrl;

    @BeforeClass
    public static void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        serverUrl = server.url("/");

        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                if (request.getPath().equals("/v1/kv/web/key?raw")) {
                    return new MockResponse().setResponseCode(200).setBody("testPrefixResult");
                }

                if (request.getPath().equals("/v1/kv/key?raw")) {
                    return new MockResponse().setResponseCode(200).setBody("testNoPrefixResult");
                }

                return new MockResponse().setResponseCode(404);
            }

        };
        server.setDispatcher(dispatcher);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void testPrefixFail() {
        instance = new ConsulClientImpl(serverUrl.host(), serverUrl.port());
        String result = instance.getValue("web", "noSuchKey");
        assertNull(result);
    }

    @Test
    public void testNoPrefixFail() {
        instance = new ConsulClientImpl(serverUrl.host(), serverUrl.port());
        String result = instance.getValue("", "noSuchKey");
        assertNull(result);
    }

    @Test
    public void testPrefixSuccess() {
        instance = new ConsulClientImpl(serverUrl.host(), serverUrl.port());
        String result = instance.getValue("web", "key");
        assertEquals("testPrefixResult", result);
    }

    @Test
    public void testNoPrefixSuccess() {
        instance = new ConsulClientImpl(serverUrl.host(), serverUrl.port());
        String result = instance.getValue("", "key");
        assertEquals("testNoPrefixResult", result);
    }

    @Test
    public void testNoKey() {
        instance = new ConsulClientImpl(serverUrl.host(), serverUrl.port());
        String result = instance.getValue("", "");
        assertNull(result);
    }

    @Test
    public void testNullKey() {
        instance = new ConsulClientImpl(serverUrl.host(), serverUrl.port());
        String result = instance.getValue("", null);
        assertNull(result);
    }

}
