package graph;

/**
 * This class represents an immutable directed labeled edge in directed labeled graph. A such edge contains the label
 * of the edge and the node (child) the edge is pointing to.
 */
public class DirectedLabeledEdge {

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

    }

    /**
     * Returns the node that this directed labeled edge is pointing to
     *
     * @return the node that this edge is pointing to
     */
    public String getChild() {
        return null;
    }

    /**
     * Returns the label of this directed labeled edge
     *
     * @return the label of this edge
     */
    public String getLabel() {
        return null;
    }
}
