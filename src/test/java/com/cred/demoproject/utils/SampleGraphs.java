package com.cred.demoproject.utils;

import com.cred.demoproject.domains.*;
import com.cred.demoproject.enums.FlightProvider;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class SampleGraphs {
  public static FlightGraph createBigGraph1() {
    FlightGraph graph = new FlightGraph();
    List<AirportTimeNode> nodes = new ArrayList<>();
    for (String code :
        new String[] {"DEL", "BOM", "BLR", "HYD", "MAA", "CCU", "GOI", "PNQ", "AMD", "COK"}) {
      AirportTimeNode node =
          new AirportTimeNode(
              code,
              ZonedDateTime.of(2025, 8, 2, 8 + nodes.size(), 0, 0, 0, ZoneId.of("Asia/Kolkata")));
      graph.addNode(node);
      nodes.add(node);
    }
    // Add edges between all pairs with different providers and times
    int flightNum = 100;
    for (int i = 0; i < nodes.size(); i++) {
      for (int j = 0; j < nodes.size(); j++) {
        if (i != j) {
          AirportTimeNode source = nodes.get(i);
          AirportTimeNode target = nodes.get(j);
          ZonedDateTime dep = source.getTime();
          ZonedDateTime arr = dep.plusHours(2 + Math.abs(i - j));
          FlightDetails details =
              new FlightDetails(
                  FlightProvider.values()[flightNum % FlightProvider.values().length],
                  "FN" + flightNum,
                  dep,
                  arr,
                  4000.0 + 100 * Math.abs(i - j),
                  20 - Math.abs(i - j));
          FlightEdge edge = new FlightEdge(source, target, details);
          graph.addEdge(edge);
          flightNum++;
        }
      }
    }
    return graph;
  }

  public static FlightGraph createBigGraph2() {
    FlightGraph graph = new FlightGraph();
    List<AirportTimeNode> nodes = new ArrayList<>();
    for (String code :
        new String[] {
          "DEL", "BOM", "BLR", "HYD", "MAA", "CCU", "GOI", "PNQ", "AMD", "COK", "IXC", "JAI", "LKO",
          "PAT", "TRV"
        }) {
      AirportTimeNode node =
          new AirportTimeNode(
              code,
              ZonedDateTime.of(2025, 8, 2, 6 + nodes.size(), 0, 0, 0, ZoneId.of("Asia/Kolkata")));
      graph.addNode(node);
      nodes.add(node);
    }
    int flightNum = 200;
    for (int i = 0; i < nodes.size(); i++) {
      for (int j = 0; j < nodes.size(); j++) {
        if (i != j
            && Math.abs(i - j) <= 3) { // Only connect close airports for more realistic graph
          AirportTimeNode source = nodes.get(i);
          AirportTimeNode target = nodes.get(j);
          ZonedDateTime dep = source.getTime();
          ZonedDateTime arr = dep.plusHours(1 + Math.abs(i - j));
          FlightDetails details =
              new FlightDetails(
                  FlightProvider.values()[flightNum % FlightProvider.values().length],
                  "FN" + flightNum,
                  dep,
                  arr,
                  3500.0 + 150 * Math.abs(i - j),
                  15 - Math.abs(i - j));
          FlightEdge edge = new FlightEdge(source, target, details);
          graph.addEdge(edge);
          flightNum++;
        }
      }
    }
    return graph;
  }
}
