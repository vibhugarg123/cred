package com.cred.demoproject.algorithms.utils;

import com.cred.demoproject.domains.AirportTimeNode;

/** Utility class for common algorithm logic. */
public class AlgorithmUtils {
  private AlgorithmUtils() {}

  /**
   * Generates a unique visit key for a node and hop count.
   *
   * @param node The AirportTimeNode
   * @param hops The number of hops
   * @return Unique visit key string
   */
  public static String getVisitKey(AirportTimeNode node, int hops) {
    return node.getAirportCode() + "@" + node.getTime() + "#" + hops;
  }
}
