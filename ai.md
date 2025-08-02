# Flight Booking Aggregator System (High-Level Design)

## 1. Overview
Aggregator integrates with 10 flight providers (Indigo, Air India, etc.)
Supports search and booking. Search can return paths with up to 5 hops (flights from different airlines). Bookings must be fastest by time.

## 2. Non-Functional Requirements
- Search: p99 latency < 1 min, extensible, highly available, fault tolerant
- Book: Reliable, consistent, idempotent, durable, eventual consistent notifications

## 3. Estimations
- 500M users, 90% DAU
- Booking: 10K RPS
- Search: 100K RPS
- 100 airports, 10,000 flights (~10GB data)

## 4. High-Level Architecture
- **In-Memory Graph:** Airports as nodes (with time), flights as edges (with time, seats, fare)
- **Search Service:** Finds optimal K paths using min-heap/priority queue (duration, fare)
- **Provider API Layer:** Handles unreliable provider APIs (10 RPS per provider, retries, circuit breakers)
- **Booking Service:** Handles booking, ensures idempotency, distributed transactions for multi-hop
- **Cache/DB:** Stores flight data, booking status
- **Notification Service:** Eventual consistency for booking notifications

## 5. Improvements & Key Gaps
- Real-time seat/fare sync
- Data consistency (stale data handling)
- Scalability (shard graph by region/time)
- Extensibility (easy onboarding for new providers)

## 6. High-Level Architecture Diagram
```
+-------------------+      +-------------------+      +-------------------+
| Provider API Layer| ---> | In-Memory Graph   | ---> | Search Service    |
+-------------------+      +-------------------+      +-------------------+
                                                        |
                                                        v
+-------------------+      +-------------------+      +-------------------+
| Booking Service   | ---> | Cache/DB          | ---> | Notification Svc  |
+-------------------+      +-------------------+      +-------------------+
```

## 7. Search Workflow / Flow Chart
1. User requests search (A->B, date/time)
2. Search Service queries In-Memory Graph for optimal paths (≤5 hops)
3. Search Service calls Provider API Layer for real-time data
4. Returns fastest paths to user

## 8. Booking Sequence Diagram
```
User -> Search Service: Search request
Search Service -> In-Memory Graph: Find paths
Search Service -> Provider API Layer: Get real-time data
Search Service -> User: Return paths
User -> Booking Service: Book request
Booking Service -> Provider API Layer: Book flights
Provider API Layer -> Booking Service: Booking result
Booking Service -> Cache/DB: Save booking
Booking Service -> Notification Svc: Send notification
```

## 9. Queue Topics & Functionalities (UML Mention)
- **SearchRequestTopic:** Producer: User, Consumer: Search Service
- **BookingRequestTopic:** Producer: User, Consumer: Booking Service
- **BookingNotificationTopic:** Producer: Booking Service, Consumer: Notification Service

## 10. UML (Queue Topics)
```
+-------------------+      +-------------------+      +-------------------+
| User              | ---> | SearchRequest     | ---> | Search Service    |
+-------------------+      +-------------------+      +-------------------+

+-------------------+      +-------------------+      +-------------------+
| Booking Service   | ---> | BookingNotification| ---> | Notification Svc |
+-------------------+      +-------------------+      +-------------------+
```

## 11. In-Memory Graph Visualization

The flight search graph is modeled as:
- **Nodes:** Each node is an airport at a specific time (e.g., DELHI@07:00).
- **Edges:** Each edge is a flight connecting two airport-time nodes, with flight details (provider, flight number, departure/arrival time, fare, seat availability).

**Example Visualization:**

```
[DELHI@07:00]
   |--(IX201, 07:00-09:00, Indigo, ₹5000)--> [BOM@09:00]
   |--(AI101, 07:30-10:00, Air India, ₹5200)--> [BLR@10:00]

[BOM@09:00]
   |--(6E202, 09:30-11:00, Indigo, ₹4000)--> [BLR@11:00]
   |--(AI102, 09:45-12:00, Air India, ₹4500)--> [HYD@12:00]
```

**Diagram:**

```
[DELHI@07:00]
   |-----------------------------|
   |                             |
(IX201, 07:00-09:00)      (AI101, 07:30-10:00)
   |                             |
[BOM@09:00]                [BLR@10:00]
   |
(6E202, 09:30-11:00)
   |
[BLR@11:00]
```

- Each node is an airport at a specific time.
- Each edge is a flight with all relevant details.
- Traversal finds optimal paths (up to 5 hops).

## 12. K Optimal Path Traversal Algorithm

To find k optimal paths (e.g., fastest or cheapest) in the flight graph:

### Approach
- Use a min-heap (priority queue) to always expand the path with the lowest cost (duration, fare, or custom metric).
- Track paths and prune those exceeding 5 hops.
- Use a variant of Yen’s algorithm for k shortest paths.

### Steps
1. **Initialize:**
   - Start from the source node (airport, time).
   - Push initial paths into the min-heap.
2. **Expand:**
   - Pop the path with the lowest cost from the heap.
   - For each outgoing edge (flight), create a new path and push into the heap.
   - Track visited nodes and hops.
3. **Collect:**
   - When a path reaches the destination, add to result set.
   - Continue until k paths are found or heap is empty.

### Pseudocode
```
function findKOptimalPaths(graph, source, destination, k):
    minHeap = PriorityQueue()
    minHeap.push([source], cost=0)
    results = []
    while minHeap not empty and len(results) < k:
        path, cost = minHeap.pop()
        if path ends at destination:
            results.append(path)
            continue
        if hops(path) >= 5:
            continue
        for edge in graph.outgoingEdges(path.lastNode):
            newPath = path + [edge.target]
            newCost = cost + edge.metric
            minHeap.push(newPath, newCost)
    return results
```

- **Metric:** Can be duration, fare, or a weighted combination.
- **Heap:** Ensures always expanding the most promising path.
- **Yen’s Algorithm:** For true k shortest paths, use Yen’s algorithm to avoid duplicate paths.

## 13. Key Insights & Limitations of K Optimal Path Algorithm

### Key Insights
- The min-heap based traversal efficiently finds the lowest-cost paths by always expanding the most promising path first.
- Limiting hops to 5 ensures the search space remains manageable and relevant for flight bookings.
- Yen’s algorithm helps avoid duplicate paths and finds true k shortest paths.
- The graph structure allows for fast in-memory traversal, suitable for high read (search) traffic.

### Limitations
- **Scalability:** As the number of airports, flights, and users grows, the graph and heap can become very large, impacting memory and CPU usage.
- **Real-Time Updates:** Frequent changes in flight schedules, seat availability, and fares require efficient graph updates and cache invalidation.
- **Provider Latency:** Real-time data fetches from providers can slow down search responses, especially if provider APIs are unreliable.
- **Path Diversity:** Yen’s algorithm may return paths with minor variations, not always maximally diverse in airlines or layover airports.
- **Resource Usage:** High concurrency can lead to contention for memory and CPU, especially during peak search times.

### Improvements for Optimality & Performance
- **Graph Sharding:** Partition the graph by region, time window, or user segment to reduce memory footprint and parallelize search.
- **Incremental Updates:** Use event-driven updates to only modify affected nodes/edges, minimizing full graph rebuilds.
- **Provider Data Caching:** Cache provider responses for short intervals to reduce API calls and improve latency.
- **Heuristic Pruning:** Apply heuristics to prune unlikely paths early (e.g., minimum layover time, avoid backtracking).
- **Parallel Search:** Distribute search requests across multiple nodes/services to handle high RPS.
- **Batching & Precomputation:** Precompute popular routes and cache k optimal paths for frequent queries.
- **Hybrid Algorithms:** Combine A* (with heuristics) and Yen’s for faster convergence on optimal paths.
- **Resource-Aware Scheduling:** Dynamically allocate resources based on search load and graph size.

## 14. Advanced Optimization: Incremental Updates, Path Diversity, Heuristic Pruning

### 1. Incremental Updates
**Problem:**
- Flight schedules, fares, and seat availability change frequently. Rebuilding the entire graph for every update is inefficient and slow.

**Solution:**
- Use event-driven updates: When a flight changes, only update the affected nodes/edges in the graph.
- Maintain a mapping from flight IDs to graph edges for quick lookup and modification.

**Detailed Pseudocode:**
```
# Assume: graph.edges is a list of all edges
# flightIdToEdgeMap is a dictionary mapping flightId to edge

function getEdgeByFlightId(flightId):
    # Fast lookup using a map
    return flightIdToEdgeMap.get(flightId)

function onFlightUpdate(flightId, newDetails):
    edge = getEdgeByFlightId(flightId)
    if edge:
        # Update only the affected edge
        edge.update(newDetails)
    else:
        # If flight is new, add a new edge and update the map
        newEdge = createEdgeFromDetails(newDetails)
        graph.edges.append(newEdge)
        flightIdToEdgeMap[flightId] = newEdge

function createEdgeFromDetails(details):
    # Create a new edge object from flight details
    edge = Edge(
        source=details.sourceAirport,
        target=details.targetAirport,
        departureTime=details.departureTime,
        arrivalTime=details.arrivalTime,
        flightId=details.flightId,
        airline=details.airline,
        fare=details.fare,
        seatAvailability=details.seatAvailability
    )
    return edge
```
- This approach ensures only the relevant part of the graph is updated, minimizing latency and resource usage.

### 2. Path Diversity
**Problem:**
- Yen’s algorithm may return k paths that are very similar (e.g., only differing by a single hop or airline), reducing the value of alternatives for users.

**Solution:**
- Add diversity constraints: Penalize paths that share too many common nodes/edges or airlines.
- Use a diversity score in the path cost metric.

**Pseudocode:**
```
function calculateDiversityScore(path, existingPaths):
    score = 0
    for p in existingPaths:
        sharedNodes = countSharedNodes(path, p)
        sharedAirlines = countSharedAirlines(path, p)
        score += sharedNodes * NODE_PENALTY + sharedAirlines * AIRLINE_PENALTY
    return score

function findKOptimalDiversePaths(graph, source, destination, k):
    results = []
    while len(results) < k:
        path = findNextBestPath(graph, source, destination, results)
        diversityScore = calculateDiversityScore(path, results)
        if diversityScore < DIVERSITY_THRESHOLD:
            results.append(path)
    return results
```

### 3. Heuristic Pruning
**Problem:**
- The search space can be huge, especially with many airports and flights. Many paths are unlikely to be chosen (e.g., long layovers, backtracking).

**Solution:**
- Apply heuristics to prune paths early:
    - Minimum/maximum layover time
    - No backtracking (do not revisit airports)
    - Maximum total travel time

**Pseudocode:**
```
function isValidPath(path):
    for i in range(1, len(path)):
        layover = path[i].departureTime - path[i-1].arrivalTime
        if layover < MIN_LAYOVER or layover > MAX_LAYOVER:
            return False
    if hasBacktracking(path):
        return False
    if totalTravelTime(path) > MAX_TRAVEL_TIME:
        return False
    return True

function findKOptimalPathsWithPruning(graph, source, destination, k):
    minHeap = PriorityQueue()
    minHeap.push([source], cost=0)
    results = []
    while minHeap not empty and len(results) < k:
        path, cost = minHeap.pop()
        if not isValidPath(path):
            continue
        if path ends at destination:
            results.append(path)
            continue
        for edge in graph.outgoingEdges(path.lastNode):
            newPath = path + [edge.target]
            newCost = cost + edge.metric
            minHeap.push(newPath, newCost)
    return results
```

## 15. Essential Metrics to Track

Tracking the following metrics is critical for monitoring, optimizing, and scaling the flight booking aggregator system:

### Search Metrics
- **Search Request Rate:** Number of search requests per second (RPS).
- **Search Latency (p50, p99):** Time taken to return search results (median, 99th percentile).
- **Cache Hit Rate:** Percentage of search queries served from cache vs. real-time computation.
- **Search Error Rate:** Percentage of failed or errored search requests.
- **Average Search Result Diversity:** Average number of unique airlines/routes returned per search.

### Booking Metrics
- **Booking Request Rate:** Number of booking requests per second.
- **Booking Success Rate:** Percentage of successful bookings vs. attempted bookings.
- **Booking Latency:** Time taken to complete a booking transaction.
- **Booking Error Rate:** Percentage of failed or errored booking requests.
- **Idempotency Failure Rate:** Percentage of duplicate or conflicting booking attempts.

### Provider/API Metrics
- **Provider API Latency:** Average and p99 response time from each provider.
- **Provider API Error Rate:** Percentage of failed API calls per provider.
- **Provider Availability:** Uptime and reliability of each provider's API.

### System Health Metrics
- **Graph Update Latency:** Time taken to update the in-memory graph after flight data changes.
- **Queue Backlog:** Number of pending messages in search/booking/notification queues.
- **Resource Utilization:** CPU, memory, and network usage of search and booking services.
- **Node Availability:** Uptime and health of nodes running graph/search/booking services.

### User Experience Metrics
- **Time to First Result:** Time from search initiation to first result shown to user.
- **Abandonment Rate:** Percentage of users who start a search but do not complete a booking.
- **Repeat Search Rate:** Percentage of users who perform multiple searches for the same route.

- harish.bohara@gmail.com
---