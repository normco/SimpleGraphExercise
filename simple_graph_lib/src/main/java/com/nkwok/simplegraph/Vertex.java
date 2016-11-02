/**
 * Name: Vertex
 * Description: Vertex Class for graph and also implemented Serializable for storage or transmission.
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
 */

public class Vertex implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String name;

	public Vertex(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		Vertex other = (Vertex) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Vertex [" + name + "]";
	}
}
