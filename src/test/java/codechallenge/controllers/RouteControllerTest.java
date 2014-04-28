package codechallenge.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Application;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class RouteControllerTest {

   private static UndertowJaxrsServer server;

   @BeforeClass
   public static void init() throws Exception {
      server = new UndertowJaxrsServer().start();
   }

   @AfterClass
   public static void stop() throws Exception {
      server.stop();
   }
   
   @ApplicationPath("/base")
   public static class MyApp extends Application
   {
      @Override
      public Set<Class<?>> getClasses()
      {
         HashSet<Class<?>> classes = new HashSet<Class<?>>();
         classes.add(RouteController.class);
         return classes;
      }
   }

   @Test
   public void route() throws Exception {
      server.deploy(MyApp.class);
      Client client = ClientBuilder.newClient();
      String result = client.target(TestPortProvider.generateURL("/base/route/Oxford Circus/to/Westminster"))
                         .request().get(String.class);
      Assert.assertEquals("{\"stations\":[\"Oxford Circus\",\"Green Park\",\"Westminster\"],\"duration\":18}", result);
      client.close();
   }
}
