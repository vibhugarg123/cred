package com.cred.demoproject.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightEdge {
  private AirportTimeNode source;
  private AirportTimeNode target;
  private FlightDetails flightDetails;
}
