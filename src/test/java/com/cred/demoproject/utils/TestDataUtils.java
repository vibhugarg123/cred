package com.cred.demoproject.utils;

import com.cred.demoproject.domains.AirportTimeNode;
import com.cred.demoproject.domains.FlightDetails;
import com.cred.demoproject.domains.FlightEdge;
import com.cred.demoproject.enums.FlightProvider;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestDataUtils {
  public static AirportTimeNode createAirportTimeNode(String airportCode) {
    ZonedDateTime time = ZonedDateTime.of(2025, 8, 2, 10, 0, 0, 0, ZoneId.of("Asia/Kolkata"));
    return new AirportTimeNode(airportCode, time);
  }

  public static FlightDetails createFlightDetails() {
    ZonedDateTime dep = ZonedDateTime.of(2025, 8, 2, 10, 0, 0, 0, ZoneId.of("Asia/Kolkata"));
    ZonedDateTime arr = dep.plusHours(2);
    return new FlightDetails(FlightProvider.INDIGO, "6E123", dep, arr, 5000.0, 10);
  }

  public static FlightEdge createFlightEdge(String sourceCode, String targetCode) {
    AirportTimeNode source = createAirportTimeNode(sourceCode);
    AirportTimeNode target = createAirportTimeNode(targetCode);
    FlightDetails details = createFlightDetails();
    return new FlightEdge(source, target, details);
  }
}
