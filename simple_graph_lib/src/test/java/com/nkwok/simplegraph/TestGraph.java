package com.nkwok.simplegraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author nkwok
 * @version 1.0
 *
 */
public class TestGraph {
	
	private Set<Vertex> vertices = new HashSet<Vertex>();
	private Set<Edge> edges = new HashSet<Edge>();
	
	List<Vertex> setupVertices = new ArrayList<Vertex>();
	List<Edge> setupEdges = new ArrayList<Edge>();

	List<Vertex> newVertices = new ArrayList<>();
	List<Edge> newEdges = new ArrayList<>();
	
	@Before
	public void setUp() throws Exception {	

		String[] names = {"Node_1", "Node_2", "Node_3"};
		for(String name: names) {
			setupVertices.add(new Vertex(name));
		}
		vertices = new HashSet<>(setupVertices);
		
		setupEdges.add(new Edge("Edge 1_2", names[0], names[1], 12));
		setupEdges.add(new Edge("Edge 1_3", names[0], names[2], 13));
		setupEdges.add(new Edge("Edge 2_2", names[1], names[2], 22));
		setupEdges.add(new Edge("Edge 2_1", names[1], names[0], 21));
		edges = new HashSet<>(setupEdges);

		Vertex newNode = new Vertex("Node_4");
		newVertices.add(newNode);
		newEdges.add(new Edge("Edge 1_4", names[0], newNode.getName(), 14));
		newEdges.add(new Edge("Edge 2_4", names[1], newNode.getName(), 24));
		newEdges.add(new Edge("Edge 3_4", names[2], newNode.getName(), 34));
	}

	@Test
	public void test_constructor() {
		
		Graph graph = new Graph();
		for(Edge edge: setupEdges) {
			graph.addEdge(edge);
		}
		
		assertTrue(graph.getVertices().containsAll(vertices));
		assertTrue(graph.getEdges().containsAll(edges) && graph.getEdges().size() == edges.size());
	}

	@Test
	public void test_constructorWithBiDirection() {
		
		Graph graph = new Graph(true);
		for(Edge edge: setupEdges) {
			graph.addEdge(edge);
		}

		assertTrue(graph.getVertices().containsAll(vertices));
		assertTrue(graph.getEdges().containsAll(edges) && graph.getEdges().size() == edges.size() * 2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_constructorException()
	{
		new Graph(null);
	}
	
	@Test
	public void test_addEdge() {

		final Set<Edge> innerEdges = new HashSet<Edge>(edges);
		final Graph graph = new Graph(innerEdges);
		
		newEdges.forEach(e -> {graph.addEdge(e); innerEdges.add(e);});

		assertTrue(graph.getEdges().containsAll(innerEdges) && graph.getNumbersOfEdges() == innerEdges.size());
	}
	
	@Test
	public void test_removeEdge() {
		final Set<Edge> innerEdges = new HashSet<Edge>(edges);
		final Graph graph = new Graph(innerEdges);

		newEdges.forEach(graph::addEdge);
		newEdges.forEach(graph::removeEdge);

		assertTrue(graph.getEdges().containsAll(edges) && graph.getNumbersOfEdges() == edges.size());
	}

	@Test
	public void test_getTotalsVertices() {
		Graph graph = new Graph(edges);
		
		assertEquals(vertices.size(), graph.getNumbersOfVertices());
	}
	
	@Test
	public void test_getNumbersOfEdges() {
		final Graph graph = new Graph(edges);
		
		assertEquals(edges.size(), graph.getNumbersOfEdges());
	}
	
	/**
	 * Test Graph Serializable by first write Vertices and Edges into stream
	 * then read back from stream and compare the returned objects
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test_graphSerialization() throws IOException, ClassNotFoundException {
		
		final Graph graph = new Graph(edges);
		
		long now = System.currentTimeMillis();
		File temp = File.createTempFile("Vertex" + now, ".tmp");		
		temp.deleteOnExit();
		
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(temp));

		oos.writeObject(graph);
		oos.close();

		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(temp));
		Graph readGraph = (Graph) ois.readObject();
		ois.close();

		assertEquals(vertices, readGraph.getVertices());
		assertEquals(edges, readGraph.getEdges());
	}
	
	@Test
	public void test_showConnectivity() {
		final Graph graph = new Graph(edges);
		
		List<List<String>> expectedPaths = new LinkedList<>();
		expectedPaths.add(new LinkedList<>(Arrays.asList("Node_1", "Node_3")));
		expectedPaths.add(new LinkedList<>(Arrays.asList("Node_1", "Node_2", "Node_3")));
		
		List<List<String>> connPaths = graph.showConnectivity(setupVertices.get(0).getName(), 
				setupVertices.get(setupVertices.size() - 1).getName());

		
		for(List<String> subList: connPaths) {
			String marker = "";
			for(String path: subList) {
				System.out.print(marker + path);
				marker = " -> ";
			}
			System.out.println();
		}
		assertTrue(expectedPaths.containsAll(connPaths) && expectedPaths.size() == connPaths.size());
	}
}
