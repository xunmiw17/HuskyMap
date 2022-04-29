package graph.junitTests;

import graph.Graph;
import org.junit.BeforeClass;

public class GraphTest {

    private static Graph graph = null;

    @BeforeClass
    public static void setupBeforeTests() throws Exception {
        graph = new Graph();
    }


}
