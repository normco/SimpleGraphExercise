/**
 * Name: ShortestPathGraph
 * Description: ShortPathGraph Class for to find the shortest path from Source Node to Destination Node.
 * 
 * Author: Norman Kwok
 * Date: 2016-10-31
 */
package com.nkwok.simplegraph;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author nkwok
 * @version 1.0
 *
 */

public class ShortestPathGraph extends Graph{

	private static final long serialVersionUID = 1L;
	
	public ShortestPathGraph(Set<Edge> edges) {
		super(edges);
	}

	/**
	 * findShortestPath - Find the shortest path using Dijkstra's Algorithm
	 *  1. Assign node 0 for source and MAX to others and initially visited set to empty
	 *  2. Add source to unvisited set
	 *  3. while unvisited set is not empty
	 *  4.     For the current node and add its neighbors to unsettled set if it is not yet settled
	 *  5.     select the vertex of unsettled set with the minimum distance 
	 *  6.     remove current vertex from unsettled set and add the current vertex into settled set
	 *  7.     if new shortest path found, update the vertex distance
	 *  
	 * @param fromName - source node name
	 * @param destName - destination node name
	 * @return list of map of vertex and calculated weights if found; otherwise, it is null
	 */

	public LinkedList<Map<Vertex, Integer>> findShortestPath(String fromName, String destName) {
		
		// it is used to hold the parent vertex (neighbor-node, parent)
		Map<Vertex, Vertex> previous = new HashMap<>();
		// it is used to hold the calculated distance from source to the node 
		Map<Vertex, Integer> calcuatedWeight = new HashMap<>();
		
		Comparator<Vertex> cmp =
			new Comparator<Vertex>() {
				public int compare(Vertex v1, Vertex v2) {
					int w1 = calcuatedWeight.get(v1) == null ? Integer.MAX_VALUE : calcuatedWeight.get(v1);
					int w2 = calcuatedWeight.get(v2) == null ? Integer.MAX_VALUE : calcuatedWeight.get(v2);
					return Integer.compare(w1,  w2);
				}
			};
		
		// it is used to hold the settled vertices
		Set<String> settledNodes = new HashSet<>();
		// Make a PriorityQueue with 50 initial capacities - just a random one
		Queue<Vertex> unsettledPQueue = new PriorityQueue<>(50, cmp);
		
		Vertex fromVertex = null;
		Vertex destVertex = null;
		
		synchronized(this) {
			fromVertex = getVertex(fromName);
			destVertex = getVertex(destName);
			
			if (fromVertex == null || destVertex == null) {
				return null;
			}
			
			// Start with the source node with 0 weight
			unsettledPQueue.add(fromVertex);
			calcuatedWeight.put(fromVertex, 0);
			
			// Loop until unsettled queue is done
			// unsettled queue will be added while walking the neighbors from the current node
			// if it is not yet settled
			while (! unsettledPQueue.isEmpty()) {
				
				// The first one in queue should be the smallest
				Vertex node = unsettledPQueue.poll();
				
				settledNodes.add(node.getName());

				// Find all unsettled neighbors, update the weight and put found neighbors into unsettled queue
				getAdjacencyListWithWeight(node.getName())
				.forEach(k -> {
					k.forEach((u, v) -> {
						if (! settledNodes.contains(u.getName())) {
							int targetweight =
									(calcuatedWeight.get(u) == null) ? Integer.MAX_VALUE : calcuatedWeight.get(u);

							int calcweight = calcuatedWeight.get(node) + v;
							if (targetweight > calcweight) {
								calcuatedWeight.put(u, calcweight);
								previous.put(u, node);
								unsettledPQueue.add(u);
							}
						}
					});
				});
			}
		}
		
		// Build result into a LinkedList with Vertex and calculated weight
		LinkedList<Map<Vertex, Integer>> shortestPath = new LinkedList<>();
		final Vertex destNode = destVertex;
		Vertex destination = destNode;

		// Check if the destination path has reached
		if (previous.get(destNode) == null) {
			return null;
		}

		// build the path list from destination to source
		// first add the destination node
		shortestPath.add(new HashMap<Vertex, Integer>() {
			{ put(destNode, calcuatedWeight.get(destNode)); }
		});
		// then loop thru the previous nodes until it reached the top
		while (previous.get(destination) != null) {
			destination = previous.get(destination);
			final Vertex fromNode = destination;
			shortestPath.add(new HashMap<Vertex, Integer>() {
				{	put(fromNode, calcuatedWeight.get(fromNode)); }
			});
		}
		
		// Order the path from source to destination
		Collections.reverse(shortestPath);
		return shortestPath;
	}
	
	/**
	 * pathToString - Take a list of path map with calculated weight and converted to printable string
	 * 
	 * @param foundPath - LinkedList of map of reachable paths with weight
	 * @return String with format node-name1 (weight1) -> node-name2 (weight2) ...
	 */
	
	public String pathToString(LinkedList<Map<Vertex, Integer>> foundPath) {
		if (foundPath == null) {
			return "";
		}

        return foundPath.stream()
            	.flatMap(x -> x.entrySet().stream())
            	.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue(), (e1, e2) -> e1, LinkedHashMap::new))
            	.entrySet().stream()
            	.map(p -> p.getKey().getName() + " (" + p.getValue() + ")")
            	.collect(Collectors.joining(" -> "));
	}
	
	/**
	 * A quick demo instead of JUnit
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		List<Edge> setupEdges = new ArrayList<Edge>();
		Set<Edge> edges = new HashSet<Edge>();

		String[] names = {"Node_0", "Node_1", "Node_2", "Node_3", "Node_4", "Node_5", "Node_6", "Node_7", "Node_8", "Node_9", "Node_10"};
		
		setupEdges.add(new Edge("Edge 0_1", names[0], names[1], 1));
		setupEdges.add(new Edge("Edge 0_2", names[0], names[2], 1));
		setupEdges.add(new Edge("Edge 0_4", names[0], names[4], 1));
		setupEdges.add(new Edge("Edge 0_10", names[0], names[10], 7));
		setupEdges.add(new Edge("Edge 1_10", names[1], names[10], 5));
		setupEdges.add(new Edge("Edge 2_6", names[2], names[6], 186));
		setupEdges.add(new Edge("Edge 2_7", names[2], names[7], 103));
		setupEdges.add(new Edge("Edge 3_7", names[3], names[7], 183));
		setupEdges.add(new Edge("Edge 4_9", names[4], names[9], 2));
		setupEdges.add(new Edge("Edge 5_8", names[5], names[8], 250));
		setupEdges.add(new Edge("Edge 7_9", names[7], names[9], 1));
		setupEdges.add(new Edge("Edge 8_9", names[8], names[9], 84));
		setupEdges.add(new Edge("Edge 9_10", names[9], names[10], 1));

		edges = new HashSet<>(setupEdges);

		ShortestPathGraph sp = new ShortestPathGraph(edges);
        
		Instant startTime = Instant.now();
		LinkedList<Map<Vertex, Integer>> foundPath = sp.findShortestPath("Node_0", "Node_10");
		String result = sp.pathToString(foundPath);
		if (foundPath != null) {
	        result = foundPath.stream()
	            	.flatMap(x -> x.entrySet().stream())
	            	.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue(), (e1, e2) -> e1, LinkedHashMap::new))
	            	.entrySet().stream()
	            	.map(p -> p.getKey().getName() + " (" + p.getValue() + ")")
	            	.collect(Collectors.joining(" -> "));
	        
	        System.out.println("Shortest Path found in " + ChronoUnit.MILLIS.between(startTime, Instant.now()) + " ms");
	        System.out.println(result);
		}

		startTime = Instant.now();
		List<List<String>> connPaths = sp.showConnectivity(names[0], names[names.length - 1]);
        System.out.println("\nShow paths in " + ChronoUnit.MILLIS.between(startTime, Instant.now()) + " ms");
		
		for(List<String> subList: connPaths) {
			String marker = "";
			for(String path: subList) {
				System.out.print(marker + path);
				marker = " -> ";
			}
			System.out.println();
		}
	}
}
