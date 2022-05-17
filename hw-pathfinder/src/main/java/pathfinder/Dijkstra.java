package pathfinder;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

/**
 * A Dijkstra class is a utility class which contains a static method dijkstra, which finds the shortest path in a graph
 * given the source and destination nodes.
 */
public class Dijkstra {

    // This class does not represent an ADT

    /**
     * Returns the shortest path in the given graph from the source node to the destination node.
     *
     * @spec.requires graph != null, src != null and dest != null
     *
     * @param graph the graph to find the shortest path
     * @param src the source node
     * @param dest the destination nodee
     * @param <T> the type of the node
     * @return a path representing the shortest path from the source node to the destination node
     */
    // Output: src.next, ..., dest
    public static <T> Path<T> dijkstra(Graph<T, Double> graph, T src, T dest) {
        Map<T, T> backRef = new HashMap<>();
        Map<T, Double> costs = new HashMap<>();
        // Initialize costs to be all infinity
        for (T node : graph.listNodes()) {
            costs.put(node, Double.MAX_VALUE);
        }
        Set<T> known = new HashSet<>();
        PriorityQueue<Graph.DirectedLabeledEdge<T, Double>> pq = new PriorityQueue<>(new Comparator<Graph.DirectedLabeledEdge<T, Double>>() {
            @Override
            public int compare(Graph.DirectedLabeledEdge<T, Double> o1, Graph.DirectedLabeledEdge<T, Double> o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });

        pq.add(new Graph.DirectedLabeledEdge<T, Double>(src, 0.0));
        costs.put(src, 0.0);

        // {{ Inv: the nodes being removed from pq has a fixed shortest path. There might exist repeated nodes
        //          if the "after node" has a shorter cost than the "prev node" }}
        while (!pq.isEmpty()) {
            Graph.DirectedLabeledEdge<T, Double> minEdge = pq.remove();
            T node = minEdge.getChild();
            if (node.equals(dest)) {
                break;
            }
            if (known.contains(node)) {
                continue;
            }

            for (Graph.DirectedLabeledEdge<T, Double> edge : graph.childrenOf(node)) {
                T child = edge.getChild();
                Double label = edge.getLabel();
                if (!known.contains(child)) {
                    if (costs.get(node) + label < costs.get(child)) {
                        pq.add(edge);
                        costs.put(child, costs.get(node) + label);
                        backRef.put(child, node);
                    }
                }
            }

            known.add(node);
        }

        // Process & construct the shortest path from src to dest
        List<Graph.DirectedLabeledEdge<T, Double>> list = new ArrayList<>();
        T node = dest;
        T prev = backRef.get(node);
        while (prev != null) {//node != src) {
            double cost = costs.get(node) - costs.get(prev);
            list.add(new Graph.DirectedLabeledEdge<>(node, cost));
            node = prev;
            prev = backRef.get(prev);
        }
        Collections.reverse(list);

        Path<T> path = new Path<T>(src);
        for (int i = 0; i < list.size(); i++) {
            Graph.DirectedLabeledEdge<T, Double> edge = list.get(i);
            path = path.extend(edge.getChild(), edge.getLabel());
        }
        return path;
    }
}
