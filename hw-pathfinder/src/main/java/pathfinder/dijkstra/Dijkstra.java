package pathfinder.dijkstra;

import graph.Graph;
import pathfinder.datastructures.Path;

import java.util.*;

public class Dijkstra {

    // Output: src.next, ..., dest
    public static Path<String> dijkstra(Graph<String, Double> graph, String src, String dest) {
        Map<String, String> backRef = new HashMap<>();
        Map<String, Double> costs = new HashMap<>();
        // Initialize costs to be all infinity
        for (String node : graph.listNodes()) {
            costs.put(node, Double.MAX_VALUE);
        }
        Set<String> known = new HashSet<>();
        PriorityQueue<Graph.DirectedLabeledEdge<String, Double>> pq = new PriorityQueue<>(new Comparator<Graph.DirectedLabeledEdge<String, Double>>() {
            @Override
            public int compare(Graph.DirectedLabeledEdge<String, Double> o1, Graph.DirectedLabeledEdge<String, Double> o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });

        pq.add(new Graph.DirectedLabeledEdge<String, Double>(src, 0.0));
        costs.put(src, 0.0);

        // {{ Inv: the nodes being removed from pq has a fixed shortest path. There might exist repeated nodes
        //          if the "after node" has a shorter cost than the "prev node" }}
        while (!pq.isEmpty()) {
            Graph.DirectedLabeledEdge<String, Double> minEdge = pq.remove();
            String node = minEdge.getChild();
            if (node.equals(dest)) {
                break;
            }
            if (known.contains(node)) {
                continue;
            }

            for (Graph.DirectedLabeledEdge<String, Double> edge : graph.childrenOf(node)) {
                String child = edge.getChild();
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
        List<Graph.DirectedLabeledEdge<String, Double>> list = new ArrayList<>();
        String node = dest;
        String prev = backRef.get(node);
        while (prev != null) {//node != src) {
            double cost = costs.get(node) - costs.get(prev);
            list.add(new Graph.DirectedLabeledEdge<>(node, cost));
            node = prev;
            prev = backRef.get(prev);
        }
        Collections.reverse(list);
        Path<String> path = new Path<String>(src);
        for (int i = 0; i < list.size(); i++) {
            Graph.DirectedLabeledEdge<String, Double> edge = list.get(i);
            path = path.extend(edge.getChild(), edge.getLabel());
        }
        return path;
    }
}
