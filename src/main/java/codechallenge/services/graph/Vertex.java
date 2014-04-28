package codechallenge.services.graph;

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable<Vertex> {

   private final String name;
   private List<Edge> adjacencies;
   private double minDistance = Double.POSITIVE_INFINITY;
   private Vertex previous;
   private int previousLine;

   public Vertex(String name) {
      this.name = name;
      this.adjacencies = new ArrayList<Edge>();
   }

   public List<Edge> getAdjacencies() {
      return adjacencies;
   }

   public void setAdjacencies(List<Edge> adjacencies) {
      this.adjacencies = adjacencies;
   }

   public double getMinDistance() {
      return minDistance;
   }

   public void setMinDistance(double minDistance) {
      this.minDistance = minDistance;
   }

   public Vertex getPrevious() {
      return previous;
   }

   public void setPrevious(Vertex previous) {
      this.previous = previous;
   }

   public int getPreviousLine() {
      return previousLine;
   }

   public void setPreviousLine(int previousLine) {
      this.previousLine = previousLine;
   }

   public String getName() {
      return name;
   }

   public void addEdge(Edge adjacent) {
      if (!adjacencies.contains(adjacent))
         adjacencies.add(adjacent);
   }

   public String toString() {
      return name;
   }

   public int compareTo(Vertex other) {
      return Double.compare(minDistance, other.minDistance);
   }
}
