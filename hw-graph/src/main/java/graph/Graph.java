package graph;

import java.util.Set;

/**
 * This class represents a mutable directed labeled graph composed of nodes and edges. Between a pair of nodes, there
 * could be zero, one, or multiple labeled edges. Besides, no two nodes share the same data.
 */
public class Graph {

    /**
     * Creates a new empty directed labeled graph
     *
     * @spec.effects creates a new empty directed labeled graph
     */
    public Graph() {

    }

    /**
     * Adds a new node to the directed labeled graph if the node is not already present in the graph.
     *
     * @spec.requires node != null and node.length() is larger than 0
     * @spec.modifies this
     * @spec.effects adds a new node to the graph if it is not already in the graph, otherwise there is no effects.
     *
     * @param node the node to be added to the graph
     */
    public void addNode(String node) {

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
     * @throws IllegalArgumentException if at least one of parent, child, or label is empty or null
     */
    public void addEdge(String parent, String child, String label) {

    }

    /**
     * Returns all the nodes that are currently in the directed labeled graph
     *
     * @return a set of all nodes that are currently in this graph
     */
    public Set<String> listNodes() {
        return null;
    }

    /**
     * Returns all the children of a given parent node in the directed labeled graph.
     *
     * @spec.requires parent != null and parent.length() is larger than 0
     *
     * @param parent the parent node
     * @return a set of nodes which are the children of the given parent node
     * @throws IllegalArgumentException if the given parent node is not in the graph
     */
    public Set<String> childrenOf(String parent) {
        return null;
    }

    /**
     * Returns whether the two given nodes are connected, or there is a path from the given source node to the given
     * destination node
     *
     * @spec.requires src != null and src.length() is larger than 0 and dest != null and dest.length() is larger than 0
     *
     * @param src the source node
     * @param dest the destination node
     * @return true if the source and destination nodes are connected, or there is a path from the source node to the
     *          destination node
     * @throws IllegalArgumentException if the graph does not contain src or dest node
     */
    public boolean isConnected(String src, String dest) {
        return false;
    }

    /**
     * Returns whether the directed labeled graph contains the given node
     *
     * @spec.requires node != null and node.length() is larger than 0
     *
     * @param node the node to be checked if it is in the graph
     * @return true if the graph contains the node, false otherwise
     */
    public boolean containsNode(String node) {
        return false;
    }

    /**
     * Returns the size or the number of nodes in the directed labeled graph.
     *
     * @return the total number of nodes in the graph
     */
    public int size() {
        return -1;
    }

    /**
     * Returns if the directed labeled graph is empty
     *
     * @return true if the graph is empty, false otherwise
     */
    public boolean isEmpty() {
        return false;
    }

}
