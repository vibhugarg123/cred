package com.cred.demoproject.domains;

import com.cred.demoproject.enums.FlightProvider;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetails {
  private FlightProvider provider;
  private String flightNumber;
  private ZonedDateTime departureTime;
  private ZonedDateTime arrivalTime;
  private double fare;
  private int seatAvailability;
}
