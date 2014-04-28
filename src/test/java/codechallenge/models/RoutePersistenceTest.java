package codechallenge.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RoutePersistenceTest {

   @Deployment
   public static Archive<?> createDeployment() {

      JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
            .addPackage(Route.class.getPackage())
            .addAsManifestResource("test-persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

      return jar;
   }

   @PersistenceContext
   EntityManager em;

   @Inject
   UserTransaction utx;

   @Before
   public void clearData() throws Exception {
      utx.begin();
      em.joinTransaction();
      em.createQuery("delete from Route").executeUpdate();
      utx.commit();
   }

   private void insertRoutes() throws Exception {
      utx.begin();
      em.joinTransaction();
      for (Route route : routes()) {
         em.persist(route);
      }
      utx.commit();
      em.clear();
   }
  
   private List<Route> routes() {
      List<Route> routes = new ArrayList<Route>();
      routes.add(createRoute(1, "Bakerloo Line"));
      routes.add(createRoute(6, "Hammersmith & City Line"));
      routes.add(createRoute(4, "District Line"));
      routes.add(createRoute(5, "East London Line"));
      routes.add(createRoute(13, "Docklands Light Railway"));
      return routes;
   }

   private Route createRoute(int id, String name) {
      Route route = new Route();
      route.setId(id);
      route.setName(name);
      return route;
   }

   @Test
   public void shouldInsertRoutes() throws Exception {
      insertRoutes();
     
      @SuppressWarnings("unchecked")
      List<Route> result = em.createQuery("select r from Route r order by r.id").getResultList();
      Assert.assertTrue(result.size() == routes().size());
   }
}