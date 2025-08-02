package com.cred.demoproject.algorithms.strategies.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.cred.demoproject.algorithms.factory.FlightPathSearchStrategyFactory;
import com.cred.demoproject.algorithms.strategies.FlightPathSearchService;
import com.cred.demoproject.domains.AirportTimeNode;
import com.cred.demoproject.domains.FlightGraph;
import com.cred.demoproject.domains.FlightPathStep;
import com.cred.demoproject.enums.FlightPathSearchStrategyType;
import com.cred.demoproject.utils.SampleGraphs;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class DijkstraFlightPathSearchServiceTest {
  @Test
  void testFindKOptimalPathsOnSampleGraph() {
    FlightGraph graph = SampleGraphs.createBigGraph1();
    AirportTimeNode source = graph.getNodes().get(0); // DEL
    AirportTimeNode destination = graph.getNodes().get(1); // BOM
    FlightPathSearchStrategyFactory factory = new FlightPathSearchStrategyFactory();
    FlightPathSearchService flightPathSearchService =
        factory.getStrategyInstance(FlightPathSearchStrategyType.DIJKSTRA);
    log.info("Source: {}", source);
    log.info("Destination: {}", destination);
    log.info("Paths: skipping logging source and destination from each path");
    List<List<FlightPathStep>> paths =
        flightPathSearchService.findKOptimalPaths(graph, source, destination, 5, 3);
    assertNotNull(paths);
    assertFalse(paths.isEmpty());
    assertTrue(paths.size() <= 3);
    for (List<FlightPathStep> path : paths) {
      assertFalse(path.isEmpty());
      assertEquals(destination, path.get(path.size() - 1).getAirportTimeNode());
      log.info("Path: {}", path);
    }
  }
}
