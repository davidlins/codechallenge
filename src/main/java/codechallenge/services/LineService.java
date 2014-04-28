package codechallenge.services;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import codechallenge.models.Line;
import codechallenge.models.Path;
import codechallenge.models.Route;
import codechallenge.models.Station;
import codechallenge.repository.LineRepository;
import codechallenge.services.graph.Dijkstra;
import codechallenge.services.graph.Edge;
import codechallenge.services.graph.Vertex;

public class LineService {

   private LineRepository lineRepository;

   public LineService(LineRepository lineRepository) {
      super();
      this.lineRepository = lineRepository;
   }

   public Map<Integer, Route> loadRoutes(String dirPath) throws Exception {
      Map<Integer, Route> routesMap = new HashMap<Integer, Route>();

      CSVReader reader = null;
      try {
         reader = createCSVReader(dirPath, "/routes.csv");
         reader.readNext();
         String[] line;
         while ((line = reader.readNext()) != null) {
            Route route = new Route();
            route.setId(Integer.parseInt(line[0]));
            route.setName(line[1]);
            routesMap.put(route.getId(), route);
         }
      } finally {
         if (reader != null) {
            reader.close();
         }
      }
      return routesMap;
   }

   public Map<Integer, Station> loadStations(String dirPath) throws Exception {
      Map<Integer, Station> stationMap = new HashMap<Integer, Station>();

      CSVReader reader = null;
      try {
         reader = createCSVReader(dirPath, "/stations.csv");
         reader.readNext();
         String[] line;
         while ((line = reader.readNext()) != null) {
            Station station = new Station();
            station.setId(Integer.parseInt(line[0]));
            station.setName(line[3]);
            stationMap.put(station.getId(), station);
         }
      } finally {
         if (reader != null) {
            reader.close();
         }
      }

      return stationMap;
   }

   public List<Line> loadLines(String dirPath) throws Exception {

      List<Line> lines = new ArrayList<Line>();

      Map<Integer, Station> stationsMap = loadStations(dirPath);
      Map<Integer, Route> routesMap = loadRoutes(dirPath);

      CSVReader reader = null;
      try {
         reader = createCSVReader(dirPath, "/lines.csv");
         reader.readNext();
         String[] lineStr;
         while ((lineStr = reader.readNext()) != null) {
            Line line = new Line();
            line.setRoute(routesMap.get(Integer.parseInt(lineStr[2])));
            line.setBeforeStation(stationsMap.get(Integer.parseInt(lineStr[0])));
            line.setAfterStation(stationsMap.get(Integer.parseInt(lineStr[1])));
            lines.add(line);
         }
      } finally {
         if (reader != null) {
            reader.close();
         }
      }
      return lines;
   }

   public void prepare(String dirPath) throws Exception {
      lineRepository.addLines(loadLines(dirPath));
   }

   public Path getShortestPath(String source, String target) throws Exception {

      // TODO: Corrigir DI
      Map<String, Vertex> vertexMap = createVertex((lineRepository == null) ? loadLines(null) : lineRepository.all());

      Vertex sourceVertex = vertexMap.get(source);
      Vertex targetVertex = vertexMap.get(target);

      Dijkstra.computePaths(sourceVertex);
      List<Vertex> shortestPath = Dijkstra.getShortestPathTo(targetVertex);
      return createPath(shortestPath);
   }

   public Map<String, Vertex> createVertex(List<Line> lines) {

      Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();

      for (Line line : lines) {

         Station sourceStation = line.getBeforeStation();
         Station targetStation = line.getAfterStation();
         Route route = line.getRoute();

         Vertex sourceVertex = vertexMap.get(sourceStation.getName());
         if (sourceVertex == null) {
            sourceVertex = new Vertex(sourceStation.getName());
            vertexMap.put(sourceStation.getName(), sourceVertex);
         }

         Vertex targetVertex = vertexMap.get(targetStation.getName());
         if (targetVertex == null) {
            targetVertex = new Vertex(targetStation.getName());
            vertexMap.put(targetStation.getName(), targetVertex);
         }

         sourceVertex.addEdge(new Edge(targetVertex, 3, route.getId()));
         targetVertex.addEdge(new Edge(sourceVertex, 3, route.getId()));
      }

      return vertexMap;
   }

   private Path createPath(List<Vertex> vertextList) {
      long duration = 0;
      List<String> stations = new ArrayList<String>();
      int lastLine = 0;
      boolean first = true;

      for (Vertex vertex : vertextList) {
         stations.add(vertex.getName());
         int line = vertex.getPreviousLine();
         if (!first) {
            if (line != lastLine && lastLine != 0)
               duration += 12;

            duration += 3;
         }else{
            first = false;
         }
         
         lastLine = line;
      }

      return new Path(stations, duration);
   }

   private CSVReader createCSVReader(String dirPath, String fileName)
         throws FileNotFoundException {
      Reader reader = (dirPath != null) ? new FileReader(dirPath + fileName)
            : new InputStreamReader(
                  LineService.class.getResourceAsStream(fileName));
      return new CSVReader(reader);
   }
}