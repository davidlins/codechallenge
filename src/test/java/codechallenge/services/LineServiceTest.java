package codechallenge.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import codechallenge.models.Line;
import codechallenge.models.Path;
import codechallenge.models.Route;
import codechallenge.models.Station;
import codechallenge.repository.LineRepository;
import codechallenge.services.graph.Edge;
import codechallenge.services.graph.Vertex;

@RunWith(Arquillian.class)
public class LineServiceTest {

   @Deployment
   public static Archive<?> createDeployment() {

      JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
            .addPackage(LineService.class.getPackage())
            .addPackage(Line.class.getPackage())
            .addAsManifestResource("test-persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

      return jar;
   }
   
   @PersistenceContext
   EntityManager em;
   
   @Inject
   UserTransaction utx;
   
   LineService lineService;

   @Before
   public void setup(){
      lineService = new LineService(new LineRepository(em, utx));
   }
   
   @Test
   public void loadRoutes() throws Exception {
      
      Map<Integer,Route> routeMap = lineService.loadRoutes(null);
      Assert.assertFalse(routeMap.isEmpty());
      
      Route centralLine = routeMap.get(2);
      Assert.assertNotNull(centralLine);
      Assert.assertEquals("Central Line", centralLine.getName());
   }

   @Test
   public void loadStations() throws Exception {
      
      Map<Integer,Station> stationMap = lineService.loadStations(null);
      Assert.assertFalse(stationMap.isEmpty());
      
      Station station = stationMap.get(197);
      Assert.assertNotNull(station);
      Assert.assertEquals("Picadilly Circus", station.getName());
   }
   
   
   @Test
   public void loadLines() throws Exception {
      
      List<Line> lines = lineService.loadLines(null);
      Assert.assertFalse(lines.isEmpty());
      
      List<Line> bakerlooLine = new ArrayList<Line>();
      for( Line line : lines){
         if(line.getRoute().getId() == 1){
            bakerlooLine.add(line);
            Assert.assertNotNull(line.getBeforeStation());
            Assert.assertNotNull(line.getAfterStation());
         }
      }
     
      Assert.assertTrue("Expected 24 itens, got "+bakerlooLine.size(),bakerlooLine.size() == 24);
   }
   
   @Test
   public void prepare() throws Exception {
      
      lineService.prepare(null);
      
      List<Line> lines = lineService.loadLines(null);
      LineRepository lineRepository = new LineRepository(em, utx);
      List<Line> result = lineRepository.all();
      Assert.assertTrue("Expected "+lines.size()+" itens, got "+result.size(),result.size() == lines.size());
   }
   
   @Test
   public void createVertex() throws Exception {
      
      Map<String,Vertex> vertextMap = lineService.createVertex(lineService.loadLines(null));
      Assert.assertFalse(vertextMap.isEmpty());
      
      Vertex oxfordCircusVertex = vertextMap.get("Oxford Circus");
      Assert.assertNotNull(oxfordCircusVertex);
      
      List<Edge> adjacencies = oxfordCircusVertex.getAdjacencies();
      Assert.assertFalse(adjacencies.isEmpty());
      
      int expectedAdjacenciesItens = 6;
      Assert.assertTrue("Expected "+expectedAdjacenciesItens+" itens, got "+adjacencies.size(),oxfordCircusVertex.getAdjacencies().size() == expectedAdjacenciesItens);
   }
   
   @Test
   public void getShortestPath() throws Exception {
      lineService.prepare(null);
      
      Path shortestPath = lineService.getShortestPath("Oxford Circus", "Westmister");
      Assert.assertTrue(shortestPath.getStations().isEmpty());
      Assert.assertTrue("Expected 0 minute, got "+shortestPath.getDuration(),shortestPath.getDuration() == 0);
      
      shortestPath = lineService.getShortestPath("Oxford Circus", "Oxford Circus");
      Assert.assertFalse(shortestPath.getStations().isEmpty());
      int expectItens = 1;
      Assert.assertTrue("Expected "+expectItens+" itens, got "+shortestPath.getStations().size(),shortestPath.getStations().size() == expectItens);
      long expectDuration = 0;
      Assert.assertTrue("Expected "+expectDuration+" minute(s), got "+shortestPath.getDuration(),shortestPath.getDuration() == expectDuration);
      
      shortestPath = lineService.getShortestPath("Oxford Circus", "Picadilly Circus");
      Assert.assertFalse(shortestPath.getStations().isEmpty());
      expectItens = 2;
      Assert.assertTrue("Expected "+expectItens+" itens, got "+shortestPath.getStations().size(),shortestPath.getStations().size() == expectItens);
      expectDuration = 3;
      Assert.assertTrue("Expected "+expectDuration+" minute(s), got "+shortestPath.getDuration(),shortestPath.getDuration() == expectDuration);
      
      shortestPath = lineService.getShortestPath("Oxford Circus", "Westminster");
      Assert.assertFalse(shortestPath.getStations().isEmpty());
      expectItens = 3;
      Assert.assertTrue("Expected "+expectItens+" itens, got "+shortestPath.getStations().size(),shortestPath.getStations().size() == expectItens);
      expectDuration = 18;
      Assert.assertTrue("Expected "+expectDuration+" minute(s), got "+shortestPath.getDuration(),shortestPath.getDuration() == expectDuration);
      
      shortestPath = lineService.getShortestPath("Queen's Park", "Borough");
      Assert.assertFalse(shortestPath.getStations().isEmpty());
      expectItens = 14;
      Assert.assertTrue("Expected "+expectItens+" itens, got "+shortestPath.getStations().size(),shortestPath.getStations().size() == expectItens);
      //expectDuration = 99;
      //Assert.assertTrue("Expected "+expectDuration+" minute(s), got "+shortestPath.getDuration(),shortestPath.getDuration() == expectDuration);
      
      
      System.out.println("Duration: "+shortestPath.getDuration()+" - Stations: "+shortestPath.getStations());
      
   }
 }