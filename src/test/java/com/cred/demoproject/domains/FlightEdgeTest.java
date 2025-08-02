package com.cred.demoproject.domains;

import static org.junit.jupiter.api.Assertions.*;

import com.cred.demoproject.utils.TestDataUtils;
import org.junit.jupiter.api.Test;

class FlightEdgeTest {
  @Test
  void testGettersAndSetters() {
    AirportTimeNode source = TestDataUtils.createAirportTimeNode("DEL");
    AirportTimeNode target = TestDataUtils.createAirportTimeNode("BOM");
    FlightDetails details = TestDataUtils.createFlightDetails();
    FlightEdge edge = new FlightEdge();
    edge.setSource(source);
    edge.setTarget(target);
    edge.setFlightDetails(details);
    assertEquals(source, edge.getSource());
    assertEquals(target, edge.getTarget());
    assertEquals(details, edge.getFlightDetails());
  }
}
