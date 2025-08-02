package com.cred.demoproject.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Represents a step in a flight path: the airport-time node and the flight details for the hop. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightPathStep {
  private AirportTimeNode airportTimeNode;
  private FlightDetails flightDetails;
}
