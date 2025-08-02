- Start by reading README.md to fully understand the problem statement and architecture goals. 
- We’re building a fast, pluggable in-memory flight search engine. Everything should be extensible, testable, and use SOLID/OCP principles.
- Model the graph: Nodes are (Airport, Timestamp), Edges are flights. Graph must be directional and time-aware. Avoid duplicate nodes for same airport-time.
- Create domain models: AirportTimeNode, FlightDetails, FlightEdge, FlightGraph, FlightPathStep. Use value objects. 
- You have given LocalDateTime, but update it to ZonedDateTime for better timezone handling.
- Implement Interface PathSearchService, should be part of strategies under algorithm package, that uses a pathfinding strategy to find top K paths between two airports.
- The interface can be List<List<FlightPathStep>> findKOptimalPaths(
FlightGraph flightGraph,
AirportTimeNode source,
AirportTimeNode destination,
int maxHops,
int k);
- I am adding the necessary algorithms and structure for 2 algorithms to be implemented under algorithms package. Please go through the structure and complete the implementation.
- We will add  DijkstraFlightPathSearchService and YenFlightPathSearchService. add necessary javadoc wherever required, but use KISS principle and YAGNI.
- Add necessary logging too in your algorithm and update java doc and try rechecking with the algo.
- I am giving you the sample test cases for the algorithms, please go through them and implement the algorithms accordingly.
- I have read the implementation, please remove redundancy and ensure that the code is clean, readable, and follows best practices. 
- Also, I have myself fixed some issues in the code,so please check these as edge cases and consider in Yen implementation too.
- Make strategy pluggable via FlightPathSearchStrategyFactory. The config should decide which algorithm to use: DijkstraFlightPathSearchService or YenFlightPathSearchService and throws InvalidStrategyException 
is strategy is not correct.
- Don’t allow backtracking — once you visit an airport in a path, avoid going back. Validate this in the path validator.
- In test layer, lets create two sample graphs.
- Add unit tests for: direct flights , 2–5 hop paths , invalid flights (timing issues, negative layovers), paths with cycles , paths that exceed max hops
- Write clear Javadoc for every public class and interface. For internal utils, inline comments should explain non-obvious logic and assumptions.