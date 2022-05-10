package graph.junitTests;

import graph.Graph;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * GraphTest is a test of the Graph class
 */
public class GraphTest {

    @Rule public Timeout globalTimeout = Timeout.seconds(10); // 10 seconds max per method tested

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

    @Test(expected = IllegalArgumentException.class)
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

    @Test(expected = IllegalArgumentException.class)
    public void testContainsEdgeWithParentNotInGraph() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");

        graph.containsEdge("n3", "n1", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsEdgeWithChildNotInGraph() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");

        graph.containsEdge("n1", "n3", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeWithParentNotInGraph() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");

        graph.addEdge("n3", "n1", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEdgeWithChildNotInGraph() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");

        graph.addEdge("n1", "n3", "e1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testChildrenOfParentNotInGraph() {
        Graph graph = new Graph();
        graph.addNode("n1");
        graph.addNode("n2");

        graph.childrenOf("n3");
    }

    @Test
    public void testHugeGraph() {
        Graph graph = new Graph();
        for (int i = 1; i <= 10000; i++) {
            graph.addNode("n" + i);
        }
        for (int i = 1; i < 10000; i++) {
            int next = i + 1;
            graph.addEdge("n" + i, "n" + next, "en" + i);
        }
        for (int i = 1; i <= 5000; i++) {
            int next = i * 2;
            graph.addEdge("n" + i, "n" + next, "en2" + i);
        }
        assertEquals(10000, graph.size());
        for (int i = 1; i <= 10000; i++) {
            assertTrue(graph.containsNode("n" + i));
        }
        for (int i = 2; i <= 5000; i++) {
            assertEquals(2, graph.childrenOf("n" + i).size());
        }
        for (int i = 1; i < 10000; i++) {
            int next = i + 1;
            assertTrue(graph.containsEdge("n" + i, "n" + next, "en" + i));
            assertFalse(graph.containsEdge("n" + next, "n" + i, "en" + i));
        }
        for (int i = 2; i <= 5000; i++) {
            int next = i * 2;
            assertTrue(graph.containsEdge("n" + i, "n" + next, "en2" + i));
            assertFalse(graph.containsEdge("n" + next, "n" + i, "en2" + i));
        }
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

    @Test
    public void testEdgeEquals() {
        Graph.DirectedLabeledEdge e1 = new Graph.DirectedLabeledEdge("n1", "e1");
        Graph.DirectedLabeledEdge e2 = new Graph.DirectedLabeledEdge("n1", "e1");
        Graph.DirectedLabeledEdge e3 = new Graph.DirectedLabeledEdge("n1", "e2");
        Graph.DirectedLabeledEdge e4 = new Graph.DirectedLabeledEdge("n2", "e1");
        Object o1 = new Object();

        assertTrue(e1.equals(e2));
        assertTrue(e2.equals(e1));
        assertTrue(e1.equals(e1));
        assertFalse(e1.equals(e3));
        assertFalse(e2.equals(e4));
        assertFalse(e1.equals(o1));
        assertFalse(e1.equals(null));
    }

    @Test
    public void testEdgeHashCode() {
        Graph.DirectedLabeledEdge e1 = new Graph.DirectedLabeledEdge("n1", "e1");
        Graph.DirectedLabeledEdge e2 = new Graph.DirectedLabeledEdge("n1", "e1");

        Graph.DirectedLabeledEdge e3 = new Graph.DirectedLabeledEdge("n50", "e50");
        Graph.DirectedLabeledEdge e4 = new Graph.DirectedLabeledEdge("n50", "e50");

        Graph.DirectedLabeledEdge e5 = new Graph.DirectedLabeledEdge("n4", "e4");
        Graph.DirectedLabeledEdge e6 = new Graph.DirectedLabeledEdge("n4", "e4");

        Graph.DirectedLabeledEdge e7 = new Graph.DirectedLabeledEdge("FrankWuNode", "FrankWuEdge");
        Graph.DirectedLabeledEdge e8 = new Graph.DirectedLabeledEdge("FrankWuNode", "FrankWuEdge");

        assertTrue(e1.hashCode() == e2.hashCode());
        assertTrue(e3.hashCode() == e4.hashCode());
        assertTrue(e5.hashCode() == e6.hashCode());
        assertTrue(e7.hashCode() == e8.hashCode());
    }
}
