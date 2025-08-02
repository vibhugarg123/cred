package com.cred.demoproject.algorithms.strategies;

import com.cred.demoproject.domains.AirportTimeNode;
import com.cred.demoproject.domains.FlightGraph;
import com.cred.demoproject.domains.FlightPathStep;
import java.util.List;

/**
 * Interface for flight path search algorithms. Implementations should return optimal flight paths
 * between two airport-time nodes.
 */
public interface FlightPathSearchService {
  /**
   * Finds k optimal paths from source to destination with up to maxHops.
   *
   * @param flightGraph The flight graph
   * @param source The starting AirportTimeNode
   * @param destination The ending AirportTimeNode
   * @param maxHops Maximum number of hops allowed
   * @param k Number of optimal paths to return
   * @return List of k optimal paths (each path is a list of FlightPathStep)
   */
  List<List<FlightPathStep>> findKOptimalPaths(
      FlightGraph flightGraph,
      AirportTimeNode source,
      AirportTimeNode destination,
      int maxHops,
      int k);
}
