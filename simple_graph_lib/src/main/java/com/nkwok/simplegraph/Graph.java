/**
 * Name: Graph
 * Description: Graph Class for Graph which has a set of Vertices and set of Edges, and also implemented Serializable
 * 				for storage or transmission.
 * 				
 * 
 * Author: Norman Kwok
 * Date: 2016-10-31
 */
package com.nkwok.simplegraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * @author nkwok
 * @version 1.0
 * 
 * Change History:
 *
 */
public class Graph implements Serializable {
	
	private static final long serialVersionUID = 1L;
	final private Set<Vertex> vertices;
	final private Set<Edge> edges;
	final private boolean biDirection;

	/**
	 * Constructor with edges and direction flag.  This assume the caller will create all the edges according.
	 * It will validate the Vertices and Edges with this implementation.
	 * 
	 * @param edges - set of edges
	 */

	public Graph(Set<Edge> edges, boolean biDirection) {

		if (edges == null) {
			throw new IllegalArgumentException("null edges");
		}
		synchronized(this) {
			this.vertices = new HashSet<>();
			this.edges = new HashSet<>(edges);
			this.biDirection = biDirection;
			
			for(Edge edge: edges) {
				if (getVertex(edge.getFromVertex()) == null) {
					vertices.add(new Vertex(edge.getFromVertex()));
				}
				if (getVertex(edge.getToVertex()) == null) {
					vertices.add(new Vertex(edge.getToVertex()));
				}
			}
		}
	}
	
	/**
	 * Constructor with set of Edges Objects and default with direction graph
	 * 
	 * @param edges - set of edges
	 */
	public Graph(Set<Edge> edges) {
		this(edges, false);
	}
	
	/**
	 * Constructor with no edges with direction flag
	 * 
	 * @param edges - set of edges
	 */
	public Graph(boolean biDirection) {
		synchronized(this) {
			this.vertices = new HashSet<>();
			this.edges = new HashSet<>();
			this.biDirection = biDirection;
		}
	}

	/**
	 * Constructor with no edges with default direction graph
	 * 
	 */
	public Graph() {
		this(false);
	}

	/**
	 * getNumberOfVertices - return totals entries of vertices graph
	 * 
	 * @return int - totals of vertices in the graph
	 */
	public synchronized int getNumbersOfVertices() {
		return vertices.size();
	}
	
	/**
	 * getNumbersOfEdges - return the totals entries of the edges in graph
	 * 
	 * @return int - totals of vertices in the graph
	 */
	public synchronized int getNumbersOfEdges() {
		return edges.size();
	}

	/**
	 * getVertices - return a copy of vertices in graph
	 * 
	 * @return the vertices
	 */
	public synchronized Set<Vertex> getVertices() {
		return new HashSet<>(vertices);
	}

	/**
	 * getEdges - return a copy of edges in graph
	 * @return the edges
	 */
	public synchronized Set<Edge> getEdges() {
		return new HashSet<Edge>(edges);
	}
	
	/**
	 * isBiDirection - return true/false of the biDirection flag
	 * @return the biDirection
	 */
	public boolean isBiDirection() {
		return biDirection;
	}

	/**
	 * addVertex - add a vertex in graph
	 * @param vertex
	 */
	private void addVertex(Vertex vertex) {
		if (vertex != null && ! vertices.contains(vertex)) {
			synchronized(this) {
				vertices.add(vertex);
			}
		}
	}

	/**
	 * biDirectionEdgeName - helper function to create the Edge name if biDirection is set by
	 * concating the 'to' vertex name with 'from' vertex name separated with a semi-colon.
	 * @param edge
	 * @return
	 */
	private String biDirectionEdgeName(Edge edge) {
		return edge.getToVertex() + " : " + edge.getFromVertex();
	}

	/**
	 * addEdges - Add the edge in graph.  The implementation will check if the vertex exist.  It will create
	 * 				the vertex if it is not in the graph.  If biDirection is defined, the implementation will
	 * 				create the reverse edge with the same weight.
	 * @param edge
	 */
	public void addEdge(Edge edge) {

		if (edge != null && ! edges.contains(edge)) {
			synchronized(this) {
				if (getVertex(edge.getFromVertex()) == null) {
					vertices.add(new Vertex(edge.getFromVertex()));
				}
				if (getVertex(edge.getToVertex()) == null) {
					vertices.add(new Vertex(edge.getToVertex()));
				}
				edges.add(new Edge(edge.getName(), edge.getFromVertex(), edge.getToVertex(), edge.getWeight()));
				
				if (biDirection) {
					edges.add(new Edge(biDirectionEdgeName(edge),
							edge.getToVertex(), edge.getFromVertex(), edge.getWeight()));
				}
			}
		}
	}

	/**
	 * removeVetex -inner function to remove a vertex in graph
	 * 
	 * @param vertex
	 * @return true - remove success, false if not
	 */
	private boolean removeVertex(Vertex vertex) {
		if (vertex!= null && vertices.contains(vertex)) {
			synchronized(this) {
				return vertices.remove(vertex);
			}
		}
		return true;
	}
	
	/**
	 * removeEdge - Remove an edge in graph.It will remove the reverse edge if biDirection flag is set.
	 * 				This implementation will remove vertex if there is no edge is connected to it
	 * 
	 * @param edge
	 * @return true - remove success, false if not
	 */
	public boolean removeEdge(Edge edge) {
		
		if (edge != null && edges.contains(edge)) {
			boolean found = false;
			
			synchronized(this) {
				boolean result = edges.remove(edge);
				if (biDirection) {
					Edge biEdge = new Edge(biDirectionEdgeName(edge), edge.getToVertex(), edge.getFromVertex(), edge.getWeight());
					result &= edges.remove(biEdge);
				}
				for(Vertex vertex: vertices) {
					for(Edge aEdge: edges) {
						if ((vertex.getName().compareTo(aEdge.getFromVertex()) == 0)
								|| (vertex.getName().compareTo(aEdge.getToVertex()) == 0)) {
							found = true;
							break;
						}
					}
					if (! found) {
						removeVertex(vertex);
					}
				}
				return result;
			}
		}
		return true;
	}
	
	/**
	 * getVertex - return a vertex object with the given vertex name
	 * 
	 * @param nodeName
	 * @return vertex object if found; otherwise, null
	 */
	public synchronized Vertex getVertex(final String nodeName) {
		if (nodeName == null) {
			return null;
		}
		return vertices
			.stream()
			.filter(k -> k.getName().equals(nodeName))
			.findAny()
			.orElse(null);
	}
	
	/**
	 * getAdjacencyList - return a list of neighbors' vertices connected to the vertex
	 * 
	 * @param sourceNode
	 * @return list of neighbors' vertices from the source node
	 */
	public List<Vertex> getAdjacencyList(String sourceNode) {	
		if (sourceNode == null) {
			return new ArrayList<>();
		}
		List<Vertex> adjacencyList = edges
				.stream()
				.filter(k -> k.getFromVertex().equals(sourceNode))
				.map(k -> getVertex(k.getToVertex()))
				.collect(Collectors.toList());

		return adjacencyList;
	}
	
	/**
	 * getAdjacencyListWithWeight - return a list of neighbors' vertices connected to the vertex with weight
	 * 
	 * @param sourceNode
	 * @return list of map of neighbors' vertices with its weight
	 */
	public List<Map<Vertex, Integer>> getAdjacencyListWithWeight(String sourceNode) {
		List<Map<Vertex, Integer>> adjacencyList = new ArrayList<>();
		
		if (sourceNode == null) {
			return adjacencyList;
		}
		
		for(Edge edge: edges) {
			if (edge.getFromVertex().compareTo(sourceNode) == 0) {
				Map<Vertex, Integer> amap = new HashMap<>();
				amap.put(getVertex(edge.getToVertex()), edge.getWeight());
				adjacencyList.add(amap);
			}
		}
		return adjacencyList;
	}
	
	/**
	 * showConnectivity - walk the path from starting Node to ending Node and collect all the reachable nodes
	 * 						in a list
	 * 
	 * @param startNode
	 * @param endNode
	 * @return list of List of nodes name
	 */
	public List<List<String>> showConnectivity(String startNode, String endNode) {
		
		List<List<String>> resultList = new LinkedList<>();
		if (startNode == null || getVertex(endNode) == null) {
			return resultList;
		}
		if (endNode == null || getVertex(startNode) == null) {
			return resultList;
		}

		List<String> pathList = new LinkedList<>();
		
		showConnectivity(startNode, endNode, pathList, resultList);
		return resultList;
	}

	/**
	 * show Connectivity - Recursive Helper method to walk the node neighbor's nodes until the end node has
	 * 						reached or does not have any neighbors.
	 * 
	 * @param startNode - source node
	 * @param endNode - destination node
	 * @param pathList - walking paths
	 * @param connList - List of List of nodes from start to end
	 */
	private void showConnectivity(String startNode, String endNode, List<String> pathList, List<List<String>> connList) {

		pathList.add(startNode);
		
		if (startNode.compareToIgnoreCase(endNode) == 0) {
			List<String> findPathList = new LinkedList<>(pathList);
			connList.add(findPathList);

			pathList.remove(endNode);
			return;
		}
		
		List<Vertex> startNodeNeigbhors = getAdjacencyList(startNode);
		
		for(Vertex v : startNodeNeigbhors) {
			if (pathList.contains(v.getName())) {
				continue;
			}
			showConnectivity(v.getName(), endNode, pathList, connList);
			pathList.remove(v.getName());
		}		
	}
}
