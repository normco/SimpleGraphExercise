/**
 * Name: TestVertex
 * Description: JUnit test for Vertex class
 * Author: Norman Kwok
 * Date: 2016-10-31
 */

package com.nkwok.simplegraph;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

/**
 * @author nkwok
 * @version 1.0
 */
public class TestVertex {

	@Test
	public void test_create() {
		
		String vertexName = "Node_A";
		Vertex vertex = new Vertex(vertexName);
		
		assertEquals(vertexName, vertex.getName());
	}

	/**
	 * Test Vertex Serializable by first write Vertices into stream
	 * then read back from stream and compare the returned objects
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void test_vertexSerialization() throws IOException, ClassNotFoundException {
		
		long now = System.currentTimeMillis();
		File temp = File.createTempFile("Vertex" + now, ".tmp");		
		temp.deleteOnExit();
		
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(temp));

		String[] names = {"Node_1", "Node_2"};
		for(String name: names) {
			oos.writeObject(new Vertex(name));
		}
		oos.close();
		
		Vertex readVertex = null;
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(temp));
		
		for(String name: names) {
			readVertex = (Vertex) ois.readObject();
			assertEquals(name, readVertex.getName());
		}
		ois.close();
	}
}
