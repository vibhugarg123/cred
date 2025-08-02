package com.cred.demoproject.domains;

import static org.junit.jupiter.api.Assertions.*;

import com.cred.demoproject.utils.TestDataUtils;
import org.junit.jupiter.api.Test;

class AirportTimeNodeTest {

  @Test
  void testEqualsAndHashCode() {
    AirportTimeNode node1 = TestDataUtils.createAirportTimeNode("DEL");
    AirportTimeNode node2 = TestDataUtils.createAirportTimeNode("DEL");
    assertEquals(node1, node2);
    assertEquals(node1.hashCode(), node2.hashCode());
  }

  @Test
  void testNotEquals() {
    AirportTimeNode node1 = TestDataUtils.createAirportTimeNode("DEL");
    AirportTimeNode node2 = TestDataUtils.createAirportTimeNode("DEL");
    node2.setTime(node2.getTime().plusHours(1));
    assertNotEquals(node1, node2);
  }

  @Test
  void testGettersAndSetters() {
    AirportTimeNode node = new AirportTimeNode();
    node.setAirportCode("BOM");
    node.setTime(TestDataUtils.createAirportTimeNode("BOM").getTime());
    assertEquals("BOM", node.getAirportCode());
    assertEquals(TestDataUtils.createAirportTimeNode("BOM").getTime(), node.getTime());
  }
}
