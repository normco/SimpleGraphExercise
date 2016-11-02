/**
 * Name: TestEdge
 * Description: JUnit test for Edge class
 * Author: Norman Kwok
 * Date: 2016-10-31
 */

package com.nkwok.simplegraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * 
 * @author nkwok
 * @version 1.0
 *
 */

public class TestEdge {

	@Test
	public void test_create() {
		final String name = "Edge A_B";
		final String from = "Node_A";
		final String to = "Node_B";
		final int weight = 10;
		
		Edge edge = new Edge(name, from, to, weight);
		
		assertEquals(name, edge.getName());
		assertEquals(from, edge.getFromVertex());
		assertEquals(to, edge.getToVertex());
		assertEquals(weight, edge.getWeight());
	}

	/**
	 * Test Edge Serializable by first write Edges into stream
	 * then read back from stream and compare the returned objects
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test_edgeSerialization() throws IOException, ClassNotFoundException {
		
		long now = System.currentTimeMillis();
		File temp = File.createTempFile("Edge" + now, ".tmp");		
		temp.deleteOnExit();
		
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(temp));
		
		Vertex nodeA = new Vertex("Node_A");
		Vertex nodeB = new Vertex("Node_B");
		Vertex nodeC = new Vertex("Node_C");
		
		Edge edgeAB = new Edge("Edge A_B", nodeA.getName(), nodeB.getName(), 10);
		oos.writeObject(edgeAB);
		
		Edge edgeAC = new Edge("Edge A_C", nodeA.getName(), nodeC.getName(), 5);
		oos.writeObject(edgeAC);

		oos.close();
		
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(temp));
		Edge readEdge1 = (Edge) ois.readObject();
		Edge readEdge2 = (Edge) ois.readObject();
		ois.close();

		assertTrue(edgeAB.equals(readEdge1));
		assertTrue(edgeAC.equals(readEdge2));
	}
}
