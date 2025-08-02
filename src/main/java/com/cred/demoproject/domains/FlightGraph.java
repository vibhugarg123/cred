package com.cred.demoproject.domains;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightGraph {
  private List<AirportTimeNode> nodes;
  private Map<AirportTimeNode, Map<AirportTimeNode, FlightEdge>> adjacencyList;
  private Map<String, FlightEdge> flightIdToEdgeMap;
  private Map<String, FlightEdge> nodePairToEdgeMap;

  public FlightGraph() {
    this.nodes = new ArrayList<>();
    this.adjacencyList = new HashMap<>();
    this.flightIdToEdgeMap = new HashMap<>();
    this.nodePairToEdgeMap = new HashMap<>();
  }

  public void addNode(AirportTimeNode node) {
    nodes.add(node);
    adjacencyList.putIfAbsent(node, new HashMap<>());
  }

  public void addEdge(FlightEdge edge) {
    AirportTimeNode source = edge.getSource();
    AirportTimeNode target = edge.getTarget();
    adjacencyList.computeIfAbsent(source, k -> new HashMap<>()).put(target, edge);
    flightIdToEdgeMap.put(edge.getFlightDetails().getFlightNumber(), edge);
    nodePairToEdgeMap.put(
        source.getAirportCode()
            + "@"
            + source.getTime()
            + "->"
            + target.getAirportCode()
            + "@"
            + target.getTime(),
        edge);
  }

  public Map<AirportTimeNode, FlightEdge> getNeighborsWithWeights(AirportTimeNode node) {
    return adjacencyList.getOrDefault(node, Collections.emptyMap());
  }

  public FlightEdge getEdgeByFlightId(String flightId) {
    return flightIdToEdgeMap.get(flightId);
  }

  public FlightEdge getEdgeBetweenNodes(AirportTimeNode source, AirportTimeNode target) {
    return nodePairToEdgeMap.get(
        source.getAirportCode()
            + "@"
            + source.getTime()
            + "->"
            + target.getAirportCode()
            + "@"
            + target.getTime());
  }
}
