package codechallenge.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Lines")
@IdClass(LinePk.class)
public class Line implements Serializable {

   private static final long serialVersionUID = -7131361938201629871L;

   @Id
   @ManyToOne(targetEntity = Route.class)
   private Route route;

   @Id
   @ManyToOne(targetEntity = Station.class)
   private Station beforeStation;

   @Id
   @ManyToOne(targetEntity = Station.class)
   private Station afterStation;

   public Route getRoute() {
      return route;
   }

   public void setRoute(Route route) {
      this.route = route;
   }

   public Station getBeforeStation() {
      return beforeStation;
   }

   public void setBeforeStation(Station beforeStation) {
      this.beforeStation = beforeStation;
   }

   public Station getAfterStation() {
      return afterStation;
   }

   public void setAfterStation(Station afterStation) {
      this.afterStation = afterStation;
   }
   
}