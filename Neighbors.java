import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import java.util.Scanner;

public class Neighbors {

    static void findNeighbors(EdgeWeightedDigraph g, int start, double distance) {

        DijkstraSP shortPath = new DijkstraSP(g, start);

        int verticesFound = 0;

        for (int i = 1; i < g.V(); i++) {
            double weight = shortPath.distTo(i), infinity = Double.POSITIVE_INFINITY;

            if (weight <= distance && weight != infinity){ //if weight == infinity then the path does not exist
                System.out.println("path found from vertex " + start + " to vertex " + i + ", weight: " + weight);
                verticesFound++;
            }
        }

        if(verticesFound == 0)
            System.out.println("No paths found in graph within the given distance!");

    }

    static EdgeWeightedDigraph testerGraph(){ //only used for testing purposes
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(6);
        g.addEdge(new DirectedEdge(0,1,2));
        g.addEdge(new DirectedEdge(1,2,11));
        g.addEdge(new DirectedEdge(2,4,4));
        g.addEdge(new DirectedEdge(4,3,12));
        g.addEdge(new DirectedEdge(3,2,8));
        g.addEdge(new DirectedEdge(0,2,5));
        g.addEdge(new DirectedEdge(0,5,12));
        g.addEdge(new DirectedEdge(0,1,1));

        return g;
    }

    public static void main(String[] args) {
        EdgeWeightedDigraph g = testerGraph();

        int start = 0;
        double distance = 0;
        Scanner keyboard = new Scanner(System.in);

        System.out.println("Enter start vertex: ");
        start = keyboard.nextInt();

        System.out.println("Enter distance: ");
        distance = keyboard.nextInt();

        findNeighbors(g, start, distance);
    }
}
