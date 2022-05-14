/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder.scriptTestRunner;

import graph.Graph;
import pathfinder.datastructures.Path;
import pathfinder.dijkstra.Dijkstra;

import java.io.*;
import java.util.*;

/**
 * This class implements a test driver that uses a script file format
 * to test an implementation of Dijkstra's algorithm on a graph.
 */
public class PathfinderTestDriver {

    private final Map<String, Graph<String, Double>> graphs = new HashMap<String, Graph<String, Double>>();
    private final PrintWriter output;
    private final BufferedReader input;

    // Leave this constructor public
    public PathfinderTestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    // Leave this method public
    public void runTests() throws IOException {
        String inputLine;
        while((inputLine = input.readLine()) != null) {
            if((inputLine.trim().length() == 0) ||
                    (inputLine.charAt(0) == '#')) {
                // echo blank and comment lines
                output.println(inputLine);
            } else {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if(st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            switch(command) {
                case "CreateGraph":
                    createGraph(arguments);
                    break;
                case "AddNode":
                    addNode(arguments);
                    break;
                case "AddEdge":
                    addEdge(arguments);
                    break;
                case "ListNodes":
                    listNodes(arguments);
                    break;
                case "ListChildren":
                    listChildren(arguments);
                    break;
                case "FindPath":
                    findPath(arguments);
                    break;
                default:
                    output.println("Unrecognized command: " + command);
                    break;
            }
        } catch(Exception e) {
            String formattedCommand = command;
            formattedCommand += arguments.stream().reduce("", (a, b) -> a + " " + b);
            output.println("Exception while running command: " + formattedCommand);
            e.printStackTrace(output);
        }
    }

    private void createGraph(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        graphs.put(graphName, new Graph<String, Double>());
        output.println("created graph " + graphName);
    }

    private void addNode(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

    private void addNode(String graphName, String nodeName) {
        Graph<String, Double> graph = graphs.get(graphName);
        graph.addNode(nodeName);
        output.println("added node " + nodeName + " to " + graphName);
    }

    private void addEdge(List<String> arguments) {
        if(arguments.size() != 4) {
            throw new CommandException("Bad arguments to AddEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);
        String edgeLabel = arguments.get(3);

        addEdge(graphName, parentName, childName, edgeLabel);
    }

    private void addEdge(String graphName, String parentName, String childName,
                         String edgeLabel) {
        Graph<String, Double> graph = graphs.get(graphName);
        double edgeLabelDouble = Double.parseDouble(edgeLabel);
        graph.addEdge(parentName, childName, edgeLabelDouble);
        output.println(String.format("added edge %.3f", edgeLabelDouble) + " from " + parentName + " to " + childName + " in " + graphName);
    }

    private void listNodes(List<String> arguments) {
        if(arguments.size() != 1) {
            throw new CommandException("Bad arguments to ListNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        Graph<String, Double> graph = graphs.get(graphName);
        Set<String> nodes = graph.listNodes();
        List<String> sortedList = new ArrayList<>(nodes);
        Collections.sort(sortedList);
        output.print(graphName + " contains:");
        for (int i = 0; i < sortedList.size() - 1; i++) {
            String node = sortedList.get(i);
            output.print(" " + node + " ");
        }
        if (sortedList.size() > 0) {
            output.print(" " + sortedList.get(sortedList.size() - 1));
        }
        output.println();
    }

    private void listChildren(List<String> arguments) {
        if(arguments.size() != 2) {
            throw new CommandException("Bad arguments to ListChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        Graph<String, Double> graph = graphs.get(graphName);
        Set<Graph.DirectedLabeledEdge<String, Double>> edges = graph.childrenOf(parentName);
        List<Graph.DirectedLabeledEdge<String, Double>> sortedList = new ArrayList<>(edges);
        Collections.sort(sortedList, new Comparator<Graph.DirectedLabeledEdge<String, Double>>() {
            @Override
            public int compare(Graph.DirectedLabeledEdge<String, Double> o1, Graph.DirectedLabeledEdge<String, Double> o2) {
                if (o1.getChild().compareTo(o2.getChild()) != 0) {
                    return o1.getChild().compareTo(o2.getChild());
                }
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        output.print("the children of " + parentName + " in " + graphName + " are:");
        for (int i = 0; i < sortedList.size() - 1; i++) {
            Graph.DirectedLabeledEdge<String, Double> edge = sortedList.get(i);
            output.print(" " + edge.getChild() + String.format("(%.3f) ", edge.getLabel()));
        }
        if (sortedList.size() > 0) {
            output.print(" " + sortedList.get(sortedList.size() - 1).getChild() + String.format("(%.3f)", sortedList.get(sortedList.size() - 1).getLabel()));
        }
        output.println();
    }

    private void findPath(List<String> arguments) {
        if(arguments.size() != 3) {
            throw new CommandException("Bad arguments to AddNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String src = arguments.get(1);
        String dest = arguments.get(2);
        findPath(graphName, src, dest);
    }

    private void findPath(String graphName, String src, String dest) {
        Graph<String, Double> graph = graphs.get(graphName);
        // Equal-node path
        if (src.equals(dest)) {
            output.println("path from " + src + " to " + dest + ":");
            output.println("total cost: 0.000");
            return;
        }
        // One or both nodes is not in the graph
        if (!graph.containsNode(src)) {
            output.println("unknown: " + src);
            if (!graph.containsNode(dest)) {
                output.println("unknown: " + dest);
            }
            return;
        } else if (!graph.containsNode(dest)) {
            output.println("unknown: " + dest);
            return;
        }
        Path<String> path = Dijkstra.dijkstra(graph, src, dest);
        output.println("path from " + src + " to " + dest + ":");
        // No path found between two nodes
        if (path.getCost() == 0.0) {
            output.println("no path found");
            return;
        }
        Iterator<Path<String>.Segment> itr = path.iterator();
        while (itr.hasNext()) {
            Path<String>.Segment segment = itr.next();
            String start = segment.getStart();
            String end = segment.getEnd();
            double cost = segment.getCost();
            output.println(start + " to " + end + String.format(" with weight %.3f", cost));
        }
        output.println("total cost: " + String.format("%.3f", path.getCost()));
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }

        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}
