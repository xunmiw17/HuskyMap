package graph;

import java.util.*;

/**
 * This class represents a mutable directed labeled graph composed of nodes and edges. Between a pair of nodes, there
 * could be zero, one, or multiple labeled edges. Besides, no two nodes share the same data.
 */
public class Graph {

    private static final boolean DEBUG = true;

    // RI: graph != null, and every node in the graph are not empty or null, and every edge in the graph has non-null and
    //      non-empty child and label, and the graph must contain a node if the node appears in the edge, and size >= 0
    // AF(this) = a graph with a set of nodes this.graph.keySet(), with each of the node "node" having a set of outgoing
    //              edges this.graph.get(node) and the total number of nodes this.size
    private Map<String, Set<DirectedLabeledEdge>> graph;

    private int size;

    /**
     * Creates a new empty directed labeled graph
     *
     * @spec.effects creates a new empty directed labeled graph
     */
    public Graph() {
        graph = new HashMap<>();
        size = 0;
        checkRep();
    }

    /**
     * Adds a new node to the directed labeled graph if the node is not already present in the graph
     *
     * @spec.requires node != null and node.length() is larger than 0
     * @spec.modifies this
     * @spec.effects adds a new node to the graph if it is not already in the graph, otherwise there is no effects.
     *
     * @param node the node to be added to the graph
     */
    public void addNode(String node) {
        if (!graph.containsKey(node)) {
            graph.put(node, new HashSet<>());
            size++;
        }
        checkRep();
    }

    /**
     * Adds a new directed edge to the directed labeled graph from the given parent node to the given child node with
     * the given label.
     *
     * @spec.requires parent != null and parent.length() is larger than 0 and child != null and child.length() is larger
     *                  than 0 and label != null and label.length() is larger than 0
     * @spec.modifies this
     * @spec.effects adds a directed edge from the parent node to the child node with the given label if the graph does
     *              not already contain the edge, otherwise there is no effects.
     *
     * @param parent the parent node
     * @param child the child node
     * @param label the label on the edge
     * @throws IllegalArgumentException if the graph does not contain either parent or child node
     */
    public void addEdge(String parent, String child, String label) {
        if (!containsEdge(parent, child, label)) {
            DirectedLabeledEdge edge = new DirectedLabeledEdge(child, label);
            graph.get(parent).add(edge);
        }
    }

    /**
     * Returns all the nodes that are currently in the directed labeled graph
     *
     * @return a set of all nodes that are currently in this graph
     */
    public Set<String> listNodes() {
        return graph.keySet();
    }

    /**
     * Returns all the directed labeled edges of a given parent node in the directed labeled graph, each edge containing
     * information of the label of the edge and the child node
     *
     * @spec.requires parent != null and parent.length() is larger than 0
     *
     * @param parent the parent node
     * @return a set of directed labeled edges, each containing the label of the edge and the child node
     * @throws IllegalArgumentException if the given parent node is not in the graph
     */
    public Set<DirectedLabeledEdge> childrenOf(String parent) {
        if (!containsNode(parent)) {
            throw new IllegalArgumentException();
        }
        return graph.get(parent);
    }

    // /**
    //  * Returns whether the two given nodes are connected, or there is a path from the given source node to the given
    //  * destination node
    //  *
    //  * @spec.requires src != null and src.length() is larger than 0 and dest != null and dest.length() is larger than 0
    //  *
    //  * @param src the source node
    //  * @param dest the destination node
    //  * @return true if there is a path from the source node to the destination node, or they are the same node
    //  * @throws IllegalArgumentException if the graph does not contain src or dest node
    //  */
    // public boolean isConnected(String src, String dest) {
    //     throw new RuntimeException("not yet implemented");
    // }

    /**
     * Returns whether the directed labeled graph contains the given node
     *
     * @spec.requires node != null and node.length() is larger than 0
     *
     * @param node the node to be checked if it is in the graph
     * @return true if the graph contains the node, false otherwise
     */
    public boolean containsNode(String node) {
        return graph.containsKey(node);
    }

    /**
     * Returns whether the directed labeled graph contains the given edge
     *
     * @spec.requires parent != null and parent.length() is larger than 0, child != null and child.length() is larger than 0,
     *              and label != null and label.length() is larger than 0
     *
     * @param parent the parent node
     * @param child the child node
     * @param label the edge label
     * @return true if the graph contains the edge, false otherwise
     * @throws IllegalArgumentException if the graph does not contain either parent or child node
     */
    public boolean containsEdge(String parent, String child, String label) {
        if (!containsNode(parent) || !containsNode(child)) {
            throw new IllegalArgumentException();
        }
        Set<DirectedLabeledEdge> edges = graph.get(parent);
        for (DirectedLabeledEdge edge : edges) {
            if (edge.getChild().equals(child) && edge.getLabel().equals(label)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a set view of all the nodes and their children and edge labels in the current directed labeled graph
     *
     * @return a set view of all the nodes and their children and edge labels in the current graph
     */
    public Set<Map.Entry<String, Set<DirectedLabeledEdge>>> entrySet() {
        return Collections.unmodifiableSet(graph.entrySet());
    }

    /**
     * Returns the size or the number of nodes in the directed labeled graph.
     *
     * @return the total number of nodes in the graph
     */
    public int size() {
        return size;
    }

    /**
     * Returns if the directed labeled graph is empty
     *
     * @return true if the graph is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    private void checkRep() {
        if (DEBUG) {
            assert graph != null;
            assert size >= 0;
            for (String node : graph.keySet()) {
                assert node != null && node != "";
                for (DirectedLabeledEdge edge : graph.get(node)) {
                    String child = edge.getChild();
                    String label = edge.getLabel();
                    assert child != null && child != "";
                    assert label != null && label != "";
                    assert containsNode(child);
                }
            }
        }
    }

    /**
     * This inner class represents an immutable directed labeled edge in directed labeled graph. A such edge contains the
     * label of the edge and the node (child) the edge is pointing to.
     */
    public static class DirectedLabeledEdge {

        // RI: child is not null and empty, and label is not null and empty
        // AF(this) = an outgoing edge with the child node this.child and the edge label this.label
        private String child;

        private String label;

        /**
         * Creates a new directed labeled edge
         *
         * @spec.requires child != null and child.length() is larger than 0 and label != null and label.length() is larger than 0
         * @spec.effects creates a new directed labeled edge
         *
         * @param child the node that the edge is pointing to
         * @param label the label of the edge
         */
        public DirectedLabeledEdge(String child, String label) {
            this.child = child;
            this.label = label;
        }

        /**
         * Returns the node that this directed labeled edge is pointing to
         *
         * @return the node that this edge is pointing to
         */
        public String getChild() {
            return child;
        }

        /**
         * Returns the label of this directed labeled edge
         *
         * @return the label of this edge
         */
        public String getLabel() {
            return label;
        }
    }

}
