package codechallenge.services.graph;

public class Edge {

   private final Vertex target;
   private final double weight;
   private final int line;

   public Edge(Vertex target, double weight, int line) {
      this.target = target;
      this.weight = weight;
      this.line = line;
   }

   public Vertex getTarget() {
      return target;
   }

   public double getWeight() {
      return weight;
   }

   public int getLine() {
      return line;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((target == null) ? 0 : target.hashCode());
      long temp;
      temp = Double.doubleToLongBits(weight);
      result = prime * result + (int) (temp ^ (temp >>> 32));
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
      Edge other = (Edge) obj;
      if (target == null) {
         if (other.target != null)
            return false;
      } else if (!target.equals(other.target))
         return false;
      if (Double.doubleToLongBits(weight) != Double
            .doubleToLongBits(other.weight))
         return false;
      return true;
   }
}
