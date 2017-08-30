package com.eko.lieferando.assignment.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.server.Server;
import org.glassfish.tyrus.test.tools.GrizzlyModProxy;
import org.glassfish.tyrus.test.tools.TestContainer;
import org.junit.Test;

import com.eko.lieferando.assignment.util.NumberEngine;


public class TestCases extends TestContainer {

    private static final String IP = "localhost";
    private static final int PORT = 8026;
    private static final String URL = "http://" + IP + ":" + PORT;

	@Test
	public void testForGeneratedNumber() {
		assertEquals(19, NumberEngine.getNextNumber(56));
		assertEquals(6, NumberEngine.getNextNumber(19));
		assertEquals(2, NumberEngine.getNextNumber(6));
	}
	
    @Test
    public void testForConnection() throws DeploymentException, IOException, InterruptedException {
        Server server = startServer(TestServerEndpoint.class);

        GrizzlyModProxy proxy = new GrizzlyModProxy(IP, PORT);
        proxy.start();

        try {
            ClientManager client = createClient();
            Map<String, Object> properties = client.getProperties();
            properties.put(ClientProperties.PROXY_URI, URL);

            CountDownLatch latch = new CountDownLatch(1);
            client.connectToServer(new TestClientEndpoint(latch), getURI(TestServerEndpoint.class));

            assertTrue(latch.await(5, TimeUnit.SECONDS));
        } finally {
            proxy.stop();
            stopServer(server);
        }
    }

    @Test
    public void testForNonExistentServer() throws DeploymentException, InterruptedException, IOException {
        GrizzlyModProxy proxy = new GrizzlyModProxy(IP, PORT);
        proxy.start();

        try {
            ClientManager client = createClient();
            Map<String, Object> properties = client.getProperties();
            properties.put(ClientProperties.PROXY_URI, URL);

            try {
                client.connectToServer(new TestClientEndpoint(new CountDownLatch(1)),
                        URI.create("ws://nonExistentServer.com"));
                fail();
            } catch (DeploymentException e) {
                // At least check it is an IOException and that there is a [P|p]roxy problem
                assertTrue(e.getCause() instanceof IOException);
                assertTrue(e.getCause().getMessage().contains("roxy"));
            }

        } finally {
            proxy.stop();
        }
    }

    @Test
    public void testForConnectionStuck() throws IOException {
        GrizzlyModProxy proxy = new GrizzlyModProxy(IP, PORT) {
            @Override
            protected NextAction handleConnect(final FilterChainContext ctx, final HttpContent content) {

                // simulate a situation when we receive CONNECT, but don't reply for some reason
                return ctx.getStopAction();
            }
        };

        proxy.start();

        try {
            ClientManager client = createClient();
            Map<String, Object> properties = client.getProperties();
            properties.put(ClientProperties.PROXY_URI, URL);
            properties.put(ClientProperties.HANDSHAKE_TIMEOUT, 200);

            try {
                client.connectToServer(new TestClientEndpoint(new CountDownLatch(1)), getURI(TestServerEndpoint.class));
                fail();
            } catch (DeploymentException e) {
                assertTrue(e.getMessage().contains("Handshake response not received"));
            }

        } finally {
            proxy.stop();
        }
    }

    @ServerEndpoint("/testEndpoint")
    public static class TestServerEndpoint {

        @OnMessage
        public String onMessage(String message) {
            return message;
        }

    }

    @ClientEndpoint
    public static class TestClientEndpoint {

        private final CountDownLatch latch;

        public TestClientEndpoint(final CountDownLatch latch) {
            this.latch = latch;
        }

        @OnOpen
        public void onOpen(Session session) throws IOException {
            session.getBasicRemote().sendText("Hello");
        }

        @OnMessage
        public void onMessage(String message) {
            latch.countDown();
        }
    }
}