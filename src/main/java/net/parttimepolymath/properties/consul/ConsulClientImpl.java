package net.parttimepolymath.properties.consul;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * facade for Consul, wrapped around a HTTP client.
 * 
 * @author robert
 */
public final class ConsulClientImpl implements ConsulClient {
    /**
     * logging instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulClientImpl.class);

    /**
     * the default port;
     */
    public static final int DEFAULT_PORT = 8500;
    /**
     * the default target host;
     */
    public static final String DEFAULT_HOST = "localhost";
    /**
     * the target host.
     */
    private final String host;
    /**
     * the target port.
     */
    private final int port;

    /**
     * the HTTP client implementation.
     */
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Primary constructor.
     * 
     * @param theHost the target host - defaults to 'localhost' if null.
     * @param thePort the target port - defaults to 8500 if zero.
     */
    public ConsulClientImpl(final String theHost, final int thePort) {
        host = theHost == null ? DEFAULT_HOST : theHost;
        port = thePort == 0 ? DEFAULT_PORT : thePort;
    }

    @Override
    public String getValue(final String prefix, final String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }

        String url = constructUrl(prefix, key);
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                LOGGER.debug("Unexpected response " + response);
                return null;
            }
            return response.body().string();
        } catch (IOException ioe) {
            LOGGER.debug("IOException while retrieving {}", url);
            return null;
        }
    }

    /**
     * construct the target GET url.
     * 
     * @param prefix the prefix to use for the key, which may be null or blank.
     * @param key the key, assumed to be non-blank and non-null;
     * @return a Consul GET url.
     */
    private String constructUrl(final String prefix, final String key) {
        if (prefix == null || prefix.isEmpty()) {
            return String.format("http://%s:%d/v1/kv/%s?raw", host, port, key);
        } else {
            return String.format("http://%s:%d/v1/kv/%s/%s?raw", host, port, prefix, key);
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("ConsulClientImpl [host=%s, port=%s]", host, port);
    }

}
