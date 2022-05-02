package graph.junitTests;

import graph.Graph;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * GraphTest is a test of the Graph class
 */
public class GraphTest {

    private static Graph graph = new Graph();

    @Test
    public void testSizeWithEmptyGraph() {
        Graph graph = new Graph();

        assertEquals(graph.size(), 0);
    }

    @Test
    public void testSizeWithOneNode() {
        Graph graph = new Graph();
        graph.addNode("n1");

        assertEquals(graph.size(), 1);
    }

    @Test
    public void testSizeWithMultipleNodes() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "e1");

        assertEquals(graph.size(), 3);
    }

    @Test
    public void testIsEmptyWithEmptyGraph() {
        Graph graph = new Graph();

        assertTrue(graph.isEmpty());
    }

    @Test
    public void testIsEmptyWithOneNode() {
        Graph graph = new Graph();
        graph.addNode("n1");

        assertFalse(graph.isEmpty());
    }

    @Test
    public void testIsEmptyWithMultipleNodes() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");

        assertFalse(graph.isEmpty());
    }

    @Test
    public void testContainsNodeWithEmptyGraph() {
        Graph graph = new Graph();

        assertFalse(graph.containsNode("n1"));
    }

    @Test
    public void testContainsNodeWithOneNode() {
        Graph graph = new Graph();
        graph.addNode("n1");

        assertTrue(graph.containsNode("n1"));
        assertFalse(graph.containsNode("n2"));
    }

    @Test
    public void testContainsNodeWithMultipleNodesAndEdges() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "e1");
        graph.addEdge("n1", "n3", "e2");
        graph.addEdge("n3", "n2", "e3");

        assertTrue(graph.containsNode("n1"));
        assertTrue(graph.containsNode("n2"));
        assertTrue(graph.containsNode("n3"));
        assertFalse(graph.containsNode("n4"));
    }

    @Test
    public void testContainsEdgeWithEmptyGraph() {
        Graph graph = new Graph();

        assertFalse(graph.containsEdge("n1", "n2", "e1"));
    }

    @Test
    public void testContainsEdgeWithOneEdge() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addEdge("n1", "n2", "e1");

        assertTrue(graph.containsEdge("n1", "n2", "e1"));
        assertFalse(graph.containsEdge("n1", "n2", "e2"));
        assertFalse(graph.containsEdge("n2", "n1", "e1"));
    }

    @Test
    public void testContainsEdgeWithMultipleEdges() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "e1");
        graph.addEdge("n1", "n3", "e2");
        graph.addEdge("n3", "n2", "e3");

        assertTrue(graph.containsEdge("n1", "n2", "e1"));
        assertTrue(graph.containsEdge("n1", "n3", "e2"));
        assertTrue(graph.containsEdge("n3", "n2", "e3"));
        assertFalse(graph.containsEdge("n2", "n1", "e1"));
        assertFalse(graph.containsEdge("n1", "n3", "e1"));
        assertFalse(graph.containsEdge("n2", "n3", "e3"));
    }

    @Test
    public void testContainsSelfEdge() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addEdge("n1", "n1", "e1");

        assertTrue(graph.containsEdge("n1", "n1", "e1"));
    }

    @Test
    public void testIsConnected() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "e1");
        graph.addEdge("n1", "n3", "e2");
        graph.addEdge("n3", "n2", "e3");

        assertTrue(graph.isConnected("n1", "n2"));
        assertTrue(graph.isConnected("n1", "n3"));
        assertTrue(graph.isConnected("n3", "n2"));
        assertFalse(graph.isConnected("n2", "n3"));
        assertFalse(graph.isConnected("n2", "n1"));
        assertFalse(graph.isConnected("n3", "n1"));
    }

    @Test
    public void testIsConnectedSameNode() {
        Graph graph = new Graph();
        graph.addNode("n1");

        assertTrue(graph.isConnected("n1", "n1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedForNotExistingSourceNode() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "e1");
        graph.addEdge("n1", "n3", "e2");
        graph.addEdge("n3", "n2", "e3");

        graph.isConnected("n4", "n1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsConnectedForNotExistingDestNode() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "e1");
        graph.addEdge("n1", "n3", "e2");
        graph.addEdge("n3", "n2", "e3");

        graph.isConnected("n1", "n4");
    }

    @Test
    public void testDirectedLabeledEdge() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");
        graph.addNode("n3");
        graph.addEdge("n1", "n2", "e1");
        graph.addEdge("n2", "n3", "e2");

        Set<Graph.DirectedLabeledEdge> childrenOfN1 = graph.childrenOf("n1");
        for (Graph.DirectedLabeledEdge edge : childrenOfN1) {
            assertEquals("n2" ,edge.getChild());
            assertEquals("e1" ,edge.getLabel());
        }

        Set<Graph.DirectedLabeledEdge> childrenOfN2 = graph.childrenOf("n2");
        for (Graph.DirectedLabeledEdge edge : childrenOfN2) {
            assertEquals("n3" ,edge.getChild());
            assertEquals("e2" ,edge.getLabel());
        }
    }

}
