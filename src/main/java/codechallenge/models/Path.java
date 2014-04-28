package codechallenge.models;

import java.io.Serializable;
import java.util.List;

public class Path implements Serializable{

   private static final long serialVersionUID = 8028187121890489685L;
   
   private final List<String> stations;
   private final long duration;

   public Path(List<String> stations, long duration) {
      super();
      this.stations = stations;
      this.duration = duration;
   }

   public List<String> getStations() {
      return stations;
   }

   public long getDuration() {
      return duration;
   }
}
