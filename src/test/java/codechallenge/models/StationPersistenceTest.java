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
public class StationPersistenceTest {

   @Deployment
   public static Archive<?> createDeployment() {

      JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
            .addPackage(Station.class.getPackage())
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
      em.createQuery("delete from Station").executeUpdate();
      utx.commit();
   }

   private void insertStations() throws Exception {
      utx.begin();
      em.joinTransaction();
      for (Station station : stations()) {
         em.persist(station);
      }
      utx.commit();
      em.clear();
   }

   private List<Station> stations() {
      List<Station> stations = new ArrayList<Station>();
      stations.add(createStation(301, "Woodford"));
      stations.add(createStation(302, "Woodside Park"));
      stations.add(createStation(303, "Wood Green"));
      stations.add(createStation(35, "Brixton"));
      stations.add(createStation(6, "Amersham"));
      stations.add(createStation(23, "Bermondsey"));
      stations.add(createStation(50, "Chesham"));
      return stations;
   }

   private Station createStation(int id, String name) {
      Station route = new Station();
      route.setId(id);
      route.setName(name);
      return route;
   }

   @Test
   public void shouldInsertStations() throws Exception {
      insertStations();
     
      @SuppressWarnings("unchecked")
      List<Station> result = em.createQuery("select s from Station s order by s.id").getResultList();
      Assert.assertTrue(result.size() == stations().size());
   }
}