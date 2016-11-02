/**
 * Name: TestShortestPathGraph
 * Description: JUnit test for ShortestPathGraph class
 * 
 * This implementation will take a list of Edges and create the vertices on create.
 * 
 * Author: Norman Kwok
 * Date: 2016-10-31
 */

package com.nkwok.simplegraph;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author nkwok
 * @version 1.0
 *
 */
public class TestShortestPathGraph {
	private Set<Edge> edges = new HashSet<Edge>();
    private String expectedShortestPath = "Node_0 (0) -> Node_4 (1) -> Node_9 (3) -> Node_10 (4)";

	@Before
	public void setUp() throws Exception {
		
		List<Edge> setupEdges = new ArrayList<Edge>();

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
	}

	@Test
	public void test_shortestPathList() {
		
		ShortestPathGraph sp = new ShortestPathGraph(edges);
        
		LinkedList<Map<Vertex, Integer>> foundPath = sp.findShortestPath("Node_0", "Node_10");
		String result = sp.pathToString(foundPath);
		if (foundPath != null) {
	        result = foundPath.stream()
	            	.flatMap(x -> x.entrySet().stream())
	            	.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue(), (e1, e2) -> e1, LinkedHashMap::new))
	            	.entrySet().stream()
	            	.map(p -> p.getKey().getName() + " (" + p.getValue() + ")")
	            	.collect(Collectors.joining(" -> "));
		}

        assertEquals(expectedShortestPath, result);
	}
	
}
