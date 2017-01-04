package com.github.rjkrish.vimjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.rjkrish.vimjson.util.Connection;
import com.github.rjkrish.vimjson.util.TrustAllHostNameVerifier;
import com.github.rjkrish.vimjson.util.TrustAllTrustManager;
import junit.framework.TestCase;

public abstract class ConnectedTest extends TestCase {

    protected Connection connection;
    protected final ObjectMapper objectMapper;

    public void setUp() throws Exception {
        super.setUp();
        String host = System.getProperty("vc.host");
        String username = System.getProperty("vc.username");
        String password = System.getProperty("vc.password");

        assertNotNull(host);
        assertNotNull(username);
        assertNotNull(password);

        String url = String.format("https://%s/sdk/",host);

        try {
            new TrustAllHostNameVerifier().register();
            new TrustAllTrustManager().register();

            connection = new Connection()
                    .setUrl(url)
                    .setUsername(username)
                    .setPassword(password).connect();
//            connection = new Connection()
//                    .setUrl("https://10.112.187.63/sdk")
//                    .setUsername("administrator@vsphere.local")
//                    .setPassword("ca$hc0w").connect();
            System.out.println(String.format("Connected to %s as %s",
                    connection.getServiceContent().getAbout().getFullName(),
                    connection.getUserSession().getFullName()));

        } catch (Exception x) {
            System.out.println(String.format("Failed to connect to %s as %s", url, username));
            throw new RuntimeException(x);
        }

    }

    public void tearDown() throws Exception {
        super.tearDown();
        connection.disconnect();
        System.out.println("Disconnected");
    }

    public ConnectedTest(String name) {
        super(name);
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    protected void println(JsonNode n) {
        try {
            System.out.println(
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(n)
            );
        } catch (JsonProcessingException jpx) {
            jpx.printStackTrace(System.err);
        }
    }
}
