/**
 * Name: Edge
 * Description: Edge Class for graph and also implemented Serializable for storage or transmission.
 * 
 * Author: Norman Kwok
 * Date: 2016-10-31
 */
package com.nkwok.simplegraph;

import java.io.Serializable;

/**
 * @author nkwok
 * @version 1.0
 * 
 * Change History:
 *
 */
public class Edge implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String name;
	private final String fromVertex;
	private final String toVertex;
	private final int weight;
	
	public Edge(String name, String fromVertex, String toVertex, int weight) {
		this.name = name;
		this.fromVertex = fromVertex;
		this.toVertex = toVertex;
		this.weight = weight;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the fromVertex
	 */
	public String getFromVertex() {
		return fromVertex;
	}

	/**
	 * @return the toVertex
	 */
	public String getToVertex() {
		return toVertex;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return 
			"Edge [name=" + name + ", from=" + fromVertex + ", to=" + toVertex + ", weight=" + weight + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromVertex == null) ? 0 : fromVertex.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((toVertex == null) ? 0 : toVertex.hashCode());
		result = prime * result + weight;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (fromVertex == null) {
			if (other.fromVertex != null)
				return false;
		} else if (!fromVertex.equals(other.fromVertex))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (toVertex == null) {
			if (other.toVertex != null)
				return false;
		} else if (!toVertex.equals(other.toVertex))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

}
