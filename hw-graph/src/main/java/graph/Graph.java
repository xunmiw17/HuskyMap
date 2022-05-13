package graph;

import java.util.*;

/**
 * This class represents a mutable directed labeled graph composed of nodes and edges. Between a pair of nodes, there
 * could be zero, one, or multiple labeled edges. Besides, no two nodes share the same data.
 */
public class Graph<T, E> {

    private static final boolean DEBUG = false;

    // RI: graph != null, and every node in the graph are not null, and every edge in the graph has non-null child
    //      and label, and the graph must contain a node if the node appears in the edge, and size >= 0
    // AF(this) = a graph with a set of nodes this.graph.keySet(), with each of the node "node" having a set of outgoing
    //              edges this.graph.get(node) and the total number of nodes this.size
    private Map<T, Set<DirectedLabeledEdge<T, E>>> graph;

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
     * @spec.requires node != null
     * @spec.modifies this
     * @spec.effects adds a new node to the graph if it is not already in the graph, otherwise there is no effects.
     *
     * @param node the node to be added to the graph
     */
    public void addNode(T node) {
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
     * @spec.requires parent != null and child != null and label != null
     * @spec.modifies this
     * @spec.effects adds a directed edge from the parent node to the child node with the given label if the graph does
     *              not already contain the edge, otherwise there is no effects.
     *
     * @param parent the parent node
     * @param child the child node
     * @param label the label on the edge
     * @throws IllegalArgumentException if the graph does not contain either parent or child node
     */
    public void addEdge(T parent, T child, E label) {
        if (!containsEdge(parent, child, label)) {
            DirectedLabeledEdge<T, E> edge = new DirectedLabeledEdge<T, E>(child, label);
            graph.get(parent).add(edge);
        }
        checkRep();
    }

    /**
     * Returns all the nodes that are currently in the directed labeled graph
     *
     * @return a set of all nodes that are currently in this graph
     */
    public Set<T> listNodes() {
        checkRep();
        return graph.keySet();
    }

    /**
     * Returns all the directed labeled edges of a given parent node in the directed labeled graph, each edge containing
     * information of the label of the edge and the child node
     *
     * @spec.requires parent != null
     *
     * @param parent the parent node
     * @return a set of directed labeled edges, each containing the label of the edge and the child node
     * @throws IllegalArgumentException if the given parent node is not in the graph
     */
    public Set<DirectedLabeledEdge<T, E>> childrenOf(T parent) {
        checkRep();
        if (!containsNode(parent)) {
            throw new IllegalArgumentException();
        }
        return graph.get(parent);
    }

    /**
     * Returns whether the directed labeled graph contains the given node
     *
     * @spec.requires node != null
     *
     * @param node the node to be checked if it is in the graph
     * @return true if the graph contains the node, false otherwise
     */
    public boolean containsNode(T node) {
        checkRep();
        return graph.containsKey(node);
    }

    /**
     * Returns whether the directed labeled graph contains the given edge
     *
     * @spec.requires parent != null, child != null, and label != null
     *
     * @param parent the parent node
     * @param child the child node
     * @param label the edge label
     * @return true if the graph contains the edge, false otherwise
     * @throws IllegalArgumentException if the graph does not contain either parent or child node
     */
    public boolean containsEdge(T parent, T child, E label) {
        checkRep();
        if (!containsNode(parent) || !containsNode(child)) {
            throw new IllegalArgumentException();
        }
        Set<DirectedLabeledEdge<T, E>> edges = graph.get(parent);
        DirectedLabeledEdge<T, E> newEdge = new DirectedLabeledEdge<T, E>(child, label);
        for (DirectedLabeledEdge<T, E> edge : edges) {
            if (newEdge.equals(edge)) {
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
    public Set<Map.Entry<T, Set<DirectedLabeledEdge<T, E>>>> entrySet() {
        checkRep();
        return Collections.unmodifiableSet(graph.entrySet());
    }

    /**
     * Returns the size or the number of nodes in the directed labeled graph.
     *
     * @return the total number of nodes in the graph
     */
    public int size() {
        checkRep();
        return size;
    }

    /**
     * Returns if the directed labeled graph is empty
     *
     * @return true if the graph is empty, false otherwise
     */
    public boolean isEmpty() {
        checkRep();
        return size == 0;
    }

    private void checkRep() {
        assert graph != null;
        assert size >= 0;
        if (DEBUG) {
            for (T node : graph.keySet()) {
                assert node != null;
                for (DirectedLabeledEdge<T, E> edge : graph.get(node)) {
                    T child = edge.getChild();
                    E label = edge.getLabel();
                    assert child != null;
                    assert label != null;
                    assert graph.containsKey(child);
                }
            }
        }
    }

    /**
     * This inner class represents an immutable directed labeled edge in directed labeled graph. A such edge contains the
     * label of the edge and the node (child) the edge is pointing to.
     */
    public static class DirectedLabeledEdge<T, E> {

        // RI: child is not null and label is not null
        // AF(this) = an outgoing edge with the child node this.child and the edge label this.label
        private T child;

        private E label;

        /**
         * Creates a new directed labeled edge
         *
         * @spec.requires child != null and label != null
         * @spec.effects creates a new directed labeled edge
         *
         * @param child the node that the edge is pointing to
         * @param label the label of the edge
         */
        public DirectedLabeledEdge(T child, E label) {
            this.child = child;
            this.label = label;
        }

        /**
         * Returns the node that this directed labeled edge is pointing to
         *
         * @return the node that this edge is pointing to
         */
        public T getChild() {
            checkRep();
            return child;
        }

        /**
         * Returns the label of this directed labeled edge
         *
         * @return the label of this edge
         */
        public E getLabel() {
            checkRep();
            return label;
        }

        /**
         * Returns whether the two DirectedLabeledEdge's are equal
         *
         * @param obj the object to compare equality
         * @return true if two DirectedLabeledEdge's are equal, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            checkRep();
            if (!(obj instanceof DirectedLabeledEdge<?, ?>)) {
                return false;
            }
            DirectedLabeledEdge<?, ?> other = (DirectedLabeledEdge<?, ?>) obj;
            return this.child.equals(other.child) && this.label.equals(other.label);
        }

        /**
         * Returns the hashcode of this DirectedLabeledEdge
         *
         * @return the hashcode of this
         */
        @Override
        public int hashCode() {
            checkRep();
            return this.child.hashCode() + this.label.hashCode();
        }

        private void checkRep() {
            if (DEBUG) {
                assert child != null;
                assert label != null;
            }
        }
    }
}
