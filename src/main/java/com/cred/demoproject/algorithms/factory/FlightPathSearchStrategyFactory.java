package com.cred.demoproject.algorithms.factory;

import com.cred.demoproject.algorithms.strategies.FlightPathSearchService;
import com.cred.demoproject.algorithms.strategies.impl.DijkstraFlightPathSearchService;
import com.cred.demoproject.algorithms.strategies.impl.YenFlightPathSearchService;
import com.cred.demoproject.enums.FlightPathSearchStrategyType;
import com.cred.demoproject.exceptions.InvalidStrategyException;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory for selecting flight path search strategy type. This factory only returns the class of
 * the strategy to use, not an instance. The caller is responsible for instantiating the strategy
 * and providing dependencies.
 */
@Slf4j
public class FlightPathSearchStrategyFactory {
  /**
   * @param strategyType The strategy type (DIJKSTRA, YEN)
   * @return Instance of the selected FlightPathSearchService
   */
  public FlightPathSearchService getStrategyInstance(FlightPathSearchStrategyType strategyType) {
    switch (strategyType) {
      case DIJKSTRA -> {
        log.info("Selected strategy: DIJKSTRA");
        return new DijkstraFlightPathSearchService();
      }
      case YEN -> {
        log.info("Selected strategy: YEN");
        return new YenFlightPathSearchService();
      }
      default -> {
        log.error("Unknown strategy type: {}", strategyType);
        throw new InvalidStrategyException("Unknown strategy type: " + strategyType);
      }
    }
  }
}
