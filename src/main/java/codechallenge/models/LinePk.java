package codechallenge.models;

import java.io.Serializable;

public class LinePk implements Serializable {

   private static final long serialVersionUID = 8886352348091954349L;

   private Integer route;
   
   private Integer beforeStation;

   private Integer afterStation;
   
   public Integer getRoute() {
      return route;
   }

   public void setRoute(Integer route) {
      this.route = route;
   }

   public Integer getBeforeStation() {
      return beforeStation;
   }

   public void setBeforeStation(Integer beforeStation) {
      this.beforeStation = beforeStation;
   }

   public Integer getAfterStation() {
      return afterStation;
   }

   public void setAfterStation(Integer afterStation) {
      this.afterStation = afterStation;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
            + ((afterStation == null) ? 0 : afterStation.hashCode());
      result = prime * result
            + ((beforeStation == null) ? 0 : beforeStation.hashCode());
      result = prime * result + ((route == null) ? 0 : route.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) 
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      LinePk other = (LinePk) obj;
      if ((route == null && other.route != null) || !route.equals(other.route)) {
         return false;
      } else if (beforeStation == null && other.beforeStation != null) {
         return false;
      } else if (afterStation == null && other.afterStation != null) {
         return false;
      } else if(beforeStation.equals(other.beforeStation) && afterStation.equals(other.afterStation)){
         return true;
      } else if(beforeStation.equals(other.afterStation) && afterStation.equals(other.beforeStation)){
         return true;
      }
      return false;
   }
}