package com.cred.demoproject.domains;

import static org.junit.jupiter.api.Assertions.*;

import com.cred.demoproject.utils.TestDataUtils;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FlightGraphTest {
  @Test
  void testAddNodeAndEdge() {
    FlightGraph graph = new FlightGraph();
    AirportTimeNode source = TestDataUtils.createAirportTimeNode("DEL");
    AirportTimeNode target = TestDataUtils.createAirportTimeNode("BOM");
    FlightDetails details = TestDataUtils.createFlightDetails();
    FlightEdge edge = new FlightEdge(source, target, details);
    graph.addNode(source);
    graph.addNode(target);
    graph.addEdge(edge);

    assertTrue(graph.getNodes().contains(source));
    assertTrue(graph.getNodes().contains(target));
    assertEquals(edge, graph.getNeighborsWithWeights(source).get(target));
    assertEquals(edge, graph.getEdgeByFlightId(details.getFlightNumber()));
    assertEquals(edge, graph.getEdgeBetweenNodes(source, target));
  }

  @Test
  void testGetNeighborsWithWeights() {
    FlightGraph graph = new FlightGraph();
    AirportTimeNode source = TestDataUtils.createAirportTimeNode("DEL");
    AirportTimeNode target = TestDataUtils.createAirportTimeNode("BOM");
    FlightDetails details = TestDataUtils.createFlightDetails();
    FlightEdge edge = new FlightEdge(source, target, details);
    graph.addNode(source);
    graph.addNode(target);
    graph.addEdge(edge);
    Map<AirportTimeNode, FlightEdge> neighbors = graph.getNeighborsWithWeights(source);

    assertEquals(1, neighbors.size());
    assertEquals(edge, neighbors.get(target));
  }

  @Test
  void testGetEdgeByFlightIdAndBetweenNodes() {
    FlightGraph graph = new FlightGraph();
    AirportTimeNode source = TestDataUtils.createAirportTimeNode("DEL");
    AirportTimeNode target = TestDataUtils.createAirportTimeNode("BOM");
    FlightDetails details = TestDataUtils.createFlightDetails();
    FlightEdge edge = new FlightEdge(source, target, details);
    graph.addNode(source);
    graph.addNode(target);
    graph.addEdge(edge);

    assertEquals(edge, graph.getEdgeByFlightId(details.getFlightNumber()));
    assertEquals(edge, graph.getEdgeBetweenNodes(source, target));
  }

  @Test
  void testEmptyGraph() {
    FlightGraph graph = new FlightGraph();
    AirportTimeNode node = TestDataUtils.createAirportTimeNode("DEL");

    assertTrue(graph.getNeighborsWithWeights(node).isEmpty());
    assertNull(graph.getEdgeByFlightId("NO_FLIGHT"));
    assertNull(graph.getEdgeBetweenNodes(node, node));
  }
}
