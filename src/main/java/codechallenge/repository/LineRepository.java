package codechallenge.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import codechallenge.models.Line;
import codechallenge.models.Route;
import codechallenge.models.Station;


public class LineRepository implements ILineRepository  {

   private EntityManager em;
   private UserTransaction utx;
   
   public LineRepository(EntityManager em, UserTransaction utx) {
      super();
      this.em  = em;
      this.utx = utx;
   }

   @Override
   public void addLines(List<Line> lines) throws Exception {

      Map<Integer, Station> savedStations = new HashMap<Integer, Station>();
      Map<Integer, Route> savedroutes     = new HashMap<Integer, Route>();

      utx.begin();
      em.joinTransaction();

      em.createQuery("delete from Line").executeUpdate();
      em.createQuery("delete from Route").executeUpdate();
      em.createQuery("delete from Station").executeUpdate();
      
      for (Line line : lines) {

         Route route = savedroutes.get(line.getRoute().getId());
         if (route != null) {
            line.setRoute(route);
         } else {
            route = line.getRoute();
            em.persist(route);
            savedroutes.put(route.getId(), route);
         }

         Station beforeStation = savedStations.get(line.getBeforeStation()
               .getId());
         if (beforeStation != null) {
            line.setBeforeStation(beforeStation);
         } else {
            beforeStation = line.getBeforeStation();
            em.persist(beforeStation);
            savedStations.put(beforeStation.getId(), beforeStation);
         }

         Station afterStation = savedStations.get(line.getAfterStation()
               .getId());
         if (afterStation != null) {
            line.setAfterStation(afterStation);
         } else {
            afterStation = line.getAfterStation();
            em.persist(afterStation);
            savedStations.put(afterStation.getId(), afterStation);
         }

         em.persist(line);
      }

      utx.commit();
      em.clear();
   }

   @SuppressWarnings("unchecked")
   @Override
   public List<Line> all() throws Exception {
      return (List<Line>) em.createQuery("select l from Line l")
            .getResultList();
   }
}
