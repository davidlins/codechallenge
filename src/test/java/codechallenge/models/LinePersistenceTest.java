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
import org.junit.Test;
import org.junit.runner.RunWith;

import codechallenge.repository.LineRepository;

@RunWith(Arquillian.class)
public class LinePersistenceTest {

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
 
   private List<Line> lines() {
      List<Line> lines = new ArrayList<Line>();
      Route route = createRoute(6, "Hammersmith & City Line");
      lines.add(createLine(route, createStation(301, "Woodford"),
            createStation(302, "Woodside Park")));
      lines.add(createLine(route, createStation(302, "Woodside Park"),
            createStation(303, "Wood Green")));
      return lines;
   }

   private Line createLine(Route route, Station beforeStation,
         Station afterStation) {
      Line line = new Line();
      line.setRoute(route);
      line.setBeforeStation(beforeStation);
      line.setAfterStation(afterStation);
      return line;
   }

   private Station createStation(int id, String name) {
      Station route = new Station();
      route.setId(id);
      route.setName(name);
      return route;
   }

   private Route createRoute(int id, String name) {
      Route route = new Route();
      route.setId(id);
      route.setName(name);
      return route;
   }

   @Test
   public void shouldInsertLines() throws Exception {
      LineRepository lineRepository = new LineRepository(em, utx);
      lineRepository.addLines(lines());
      List<Line> result = lineRepository.all();
      Assert.assertTrue(result.size() == lines().size());
   }
}