package com.cred.demoproject.algorithms.strategies.impl;

import com.cred.demoproject.algorithms.strategies.FlightPathSearchService;
import com.cred.demoproject.domains.AirportTimeNode;
import com.cred.demoproject.domains.FlightEdge;
import com.cred.demoproject.domains.FlightGraph;
import com.cred.demoproject.domains.FlightPathStep;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * YenFlightPathSearchService implements Yen's algorithm to find multiple optimal flight paths
 * between two airport-time nodes.
 *
 * <p>Returns up to maxHops optimal loopless paths from source to destination. Each hop can be a
 * different airline/flight.
 */
@Slf4j
public class YenFlightPathSearchService implements FlightPathSearchService {
  public YenFlightPathSearchService() {}

  /**
   * Finds k optimal loopless paths using Yen's algorithm. Uses DijkstraFlightPathSearchService for
   * shortest path calculation. Returns up to k optimal paths from source to destination with up to
   * maxHops.
   */
  @Override
  public List<List<FlightPathStep>> findKOptimalPaths(
      FlightGraph flightGraph,
      AirportTimeNode source,
      AirportTimeNode destination,
      int maxHops,
      int k) {
    List<List<FlightPathStep>> result = new ArrayList<>();
    FlightPathSearchService dijkstra = new DijkstraFlightPathSearchService();
    List<List<FlightPathStep>> shortestPaths =
        dijkstra.findKOptimalPaths(flightGraph, source, destination, maxHops, 1);
    if (shortestPaths.isEmpty()) return result;
    // Only add valid paths that start at source and end at destination
    List<FlightPathStep> firstPath = shortestPaths.get(0);
    if (!firstPath.isEmpty()
        && firstPath.get(0).getAirportTimeNode().equals(source)
        && firstPath.get(firstPath.size() - 1).getAirportTimeNode().equals(destination)) {
      result.add(firstPath);
    }
    List<List<FlightPathStep>> candidates = new ArrayList<>();

    if (result.isEmpty()) return result;
    for (int i = 1; i < k; i++) {
      List<FlightPathStep> prevPath = result.get(i - 1);
      for (int j = 0; j < prevPath.size(); j++) {
        AirportTimeNode spurNode = prevPath.get(j).getAirportTimeNode();
        List<FlightPathStep> rootPath = prevPath.subList(0, j);
        Set<String> removedEdges = new HashSet<>();
        for (List<FlightPathStep> p : result) {
          if (j > 0 && p.size() > j && rootPath.equals(p.subList(0, j))) {
            AirportTimeNode u = p.get(j - 1).getAirportTimeNode();
            AirportTimeNode v = p.get(j).getAirportTimeNode();
            removedEdges.add(
                u.getAirportCode()
                    + "@"
                    + u.getTime()
                    + "->"
                    + v.getAirportCode()
                    + "@"
                    + v.getTime());
          }
        }
        FlightGraph tempGraph = buildGraphWithoutEdges(flightGraph, removedEdges);
        List<List<FlightPathStep>> spurPaths =
            dijkstra.findKOptimalPaths(
                tempGraph, spurNode, destination, maxHops - rootPath.size(), 1);
        if (!spurPaths.isEmpty()) {
          List<FlightPathStep> totalPath = new ArrayList<>(rootPath);
          totalPath.addAll(spurPaths.get(0));
          // Only add valid paths that start at source and end at destination
          if (!totalPath.isEmpty()
              && totalPath.get(0).getAirportTimeNode().equals(source)
              && totalPath.get(totalPath.size() - 1).getAirportTimeNode().equals(destination)
              && !containsPath(result, totalPath)
              && !containsPath(candidates, totalPath)) {
            candidates.add(totalPath);
          }
        }
      }
      if (candidates.isEmpty()) break;
      candidates.sort(Comparator.comparingLong(this::getTotalArrivalTime));
      result.add(candidates.remove(0));
    }
    return result;
  }

  /** Utility to build a copy of the graph without certain edges (for Yen's algorithm). */
  private FlightGraph buildGraphWithoutEdges(FlightGraph original, Set<String> removedEdges) {
    FlightGraph copy = new FlightGraph();
    for (AirportTimeNode node : original.getNodes()) {
      copy.addNode(node);
    }
    // Iterate adjacencyList to get all edges
    for (Map.Entry<AirportTimeNode, Map<AirportTimeNode, FlightEdge>> entry :
        original.getAdjacencyList().entrySet()) {
      for (Map.Entry<AirportTimeNode, FlightEdge> edgeEntry : entry.getValue().entrySet()) {
        FlightEdge edge = edgeEntry.getValue();
        String edgeKey =
            edge.getSource().getAirportCode()
                + "@"
                + edge.getSource().getTime()
                + "->"
                + edge.getTarget().getAirportCode()
                + "@"
                + edge.getTarget().getTime();
        if (!removedEdges.contains(edgeKey)) {
          copy.addEdge(edge);
        }
      }
    }
    return copy;
  }

  private boolean containsPath(List<List<FlightPathStep>> paths, List<FlightPathStep> candidate) {
    for (List<FlightPathStep> path : paths) {
      if (path.equals(candidate)) return true;
    }
    return false;
  }

  private long getTotalArrivalTime(List<FlightPathStep> path) {
    if (path.isEmpty()) return Long.MAX_VALUE;
    return path.get(path.size() - 1).getFlightDetails().getArrivalTime().toEpochSecond();
  }
}
