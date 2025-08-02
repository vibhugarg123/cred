package com.cred.demoproject.domains;

import static org.junit.jupiter.api.Assertions.*;

import com.cred.demoproject.utils.TestDataUtils;
import org.junit.jupiter.api.Test;

class FlightDetailsTest {
  @Test
  void testGettersAndSetters() {
    FlightDetails details = TestDataUtils.createFlightDetails();
    assertEquals(com.cred.demoproject.enums.FlightProvider.INDIGO, details.getProvider());
    assertEquals("6E123", details.getFlightNumber());
    assertNotNull(details.getDepartureTime());
    assertNotNull(details.getArrivalTime());
    assertEquals(5000.0, details.getFare());
    assertEquals(10, details.getSeatAvailability());
  }
}
