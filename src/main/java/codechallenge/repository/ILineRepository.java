package codechallenge.repository;

import java.util.List;

import codechallenge.models.Line;

public interface ILineRepository {
   
   public void addLines(List<Line> line) throws Exception;
   public List<Line> all() throws Exception;
  
}
