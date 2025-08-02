package com.cred.demoproject.domains;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportTimeNode {
  private String airportCode;
  private ZonedDateTime time;
}
