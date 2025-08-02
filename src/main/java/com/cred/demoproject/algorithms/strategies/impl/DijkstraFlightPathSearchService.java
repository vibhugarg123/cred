package com.cred.demoproject.algorithms.strategies.impl;

import com.cred.demoproject.algorithms.strategies.FlightPathSearchService;
import com.cred.demoproject.algorithms.utils.AlgorithmUtils;
import com.cred.demoproject.domains.AirportTimeNode;
import com.cred.demoproject.domains.FlightEdge;
import com.cred.demoproject.domains.FlightGraph;
import com.cred.demoproject.domains.FlightPathStep;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * DijkstraFlightPathSearchService implements Dijkstra's algorithm to find optimal flight paths
 * between two airport-time nodes.
 *
 * <p>Returns all optimal paths from source to destination with up to maxHops. Each hop can be a
 * different airline/flight.
 */
@Slf4j
public class DijkstraFlightPathSearchService implements FlightPathSearchService {

  public DijkstraFlightPathSearchService() {}

  @Override
  public List<List<FlightPathStep>> findKOptimalPaths(
      FlightGraph flightGraph,
      AirportTimeNode source,
      AirportTimeNode destination,
      int maxHops,
      int k) {
    PriorityQueue<PathState> pq = new PriorityQueue<>(Comparator.comparingLong(s -> s.currentTime));
    List<List<FlightPathStep>> result = new ArrayList<>();
    pq.add(new PathState(source, new ArrayList<>(), 0, source.getTime().toEpochSecond()));
    Set<String> visited = new HashSet<>();

    while (!pq.isEmpty() && result.size() < k) {
      PathState state = pq.poll();
      AirportTimeNode current = state.currentNode;
      List<FlightPathStep> path = state.path;
      int hops = state.hops;
      long currentTime = state.currentTime;

      if (hops > maxHops) continue;
      if (current.equals(destination)) {
        result.add(new ArrayList<>(path));
        continue;
      }
      String visitKey = AlgorithmUtils.getVisitKey(current, hops);
      if (!visited.add(visitKey)) continue;

      Map<AirportTimeNode, FlightEdge> neighbors = flightGraph.getNeighborsWithWeights(current);
      for (Map.Entry<AirportTimeNode, FlightEdge> entry : neighbors.entrySet()) {
        AirportTimeNode neighbor = entry.getKey();
        FlightEdge edge = entry.getValue();
        long arrivalTime = edge.getFlightDetails().getArrivalTime().toEpochSecond();
        List<FlightPathStep> newPath = new ArrayList<>(path);
        newPath.add(new FlightPathStep(neighbor, edge.getFlightDetails()));
        pq.add(new PathState(neighbor, newPath, hops + 1, arrivalTime));
      }
    }
    return result;
  }

  private static class PathState {
    AirportTimeNode currentNode;
    List<FlightPathStep> path;
    int hops;
    long currentTime;

    PathState(AirportTimeNode currentNode, List<FlightPathStep> path, int hops, long currentTime) {
      this.currentNode = currentNode;
      this.path = path;
      this.hops = hops;
      this.currentTime = currentTime;
    }
  }
}
