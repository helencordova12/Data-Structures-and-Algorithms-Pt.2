import edu.princeton.cs.algs4.*;
import java.util.ArrayList;
import java.util.Collections;

public class MinimumSpanningTree {

    static EdgeWeightedGraph prim(EdgeWeightedGraph graph){
        EdgeWeightedGraph mst = new EdgeWeightedGraph(graph.V());

        ArrayList<Edge> edgesMST = new ArrayList<>(); //edges in mst
        ArrayList<Edge> adjacentEdges = new ArrayList<>(); //keeps track of neighboring edges
        ArrayList<Integer> verticesInGraph = new ArrayList<>(); //vertices in mst

        int startVertex = 0, size = 0;
        int addedVertex = startVertex; //keeps track of new vertices added to the mst

        verticesInGraph.add(startVertex);

        while(size != graph.V()-1){

            Iterable<Edge> adjacent = graph.adj(addedVertex);

            if(adjacentEdges.isEmpty())
                adjacent.forEach(adjacentEdges::add);

            for(Edge e: adjacent){
                Edge opp = new Edge(e.other(addedVertex), addedVertex, e.weight());
                if(!inList(adjacentEdges, e) && !inList(adjacentEdges, opp)){
                    adjacentEdges.add(e); //adds all neighboring edges to adjacentEdges
                }
            }

            int index = returnLeastWeightedEdge(adjacentEdges);  //retrieves index of the least weighted adjacent edge
            int v = adjacentEdges.get(index).either(), w = adjacentEdges.get(index).other(v);

            while(verticesInGraph.contains(v) && verticesInGraph.contains(w)){ //this will prevent a cycle from forming
                adjacentEdges.set(index, new Edge(v, w, -1));
                index = returnLeastWeightedEdge(adjacentEdges);
                v = adjacentEdges.get(index).either();
                w = adjacentEdges.get(index).other(v);
            }

            edgesMST.add(adjacentEdges.get(index));

            if(!verticesInGraph.contains(v))
                verticesInGraph.add(v);

            if(!verticesInGraph.contains(w))
                verticesInGraph.add(w);

            addedVertex = w;

            adjacentEdges.set(index, new Edge(v, w, -1)); //changing the weight to -1 will prevent us from adding this edge to the mst again
            size++;

        }

        edgesMST.forEach(mst::addEdge);

        return mst;
    }
    static boolean inList(ArrayList<Edge> adjacentEdges, Edge e){ //1st prim helper method

        for (int i = 0; i < adjacentEdges.size(); i++) {

            int v1 = adjacentEdges.get(i).either(), w1 = adjacentEdges.get(i).other(v1);
            int v2 = e.either(), w2 = e.other(v2);

            if(v1 == v2 && w1 == w2){
                return true; //returns true if edge passed in the argument is found in the arraylist
            }
        }
        return false;
    }

    static int returnLeastWeightedEdge(ArrayList<Edge> edges){ //2nd prim helper method

        Edge least = edges.get(0);
        int index = 0;  //index of least weighted edge

        for (int i = 0; i < edges.size(); i++) {
            if(edges.get(i).weight() != -1){ //the -1 means the edge has already added to the mst or it simply cannot be added to the mst
                least = edges.get(i);
                index = i;
                break;
            }
        }
        for (int i = 0; i < edges.size(); i++) { //this finds the least-weighted adjacent edge (and ensures that it's valid)
            if(edges.get(i).weight() != -1 && edges.get(i).weight() < least.weight()){
                least = edges.get(i);
                index = i;
            }
        }

        return index;
    }

    public static EdgeWeightedGraph kruskal(EdgeWeightedGraph graph){

        EdgeWeightedGraph mst = new EdgeWeightedGraph(graph.V());

        if((graph.E() == 1 && graph.V() == 2) ||(graph.V() == 3 && graph.E() == graph.V()-1)){
            return graph;
        }

        ArrayList<Edge> sortedEdges = new ArrayList<>();
        ArrayList<Edge> edgesMST = new ArrayList<>();
        ArrayList<Integer> verticesInGraph = new ArrayList<>(); //vertices in mst

        graph.edges().forEach(sortedEdges::add);
        Collections.sort(sortedEdges); //sorts edges depending on weight

        int size = 0, index = 0;

        Boolean addNewEdge = false, mstEdgesConnected = false; //the 1st boolean determines whether or not an edge will be added to the mst or not

        Edge current = sortedEdges.get(0);

        while(size != graph.V()-1){

            int v = sortedEdges.get(index).either(), w = sortedEdges.get(index).other(v);

            if(verticesInGraph.isEmpty()){
                verticesInGraph.add(v);
                verticesInGraph.add(w);
                addNewEdge = true;
            }else if (!verticesInGraph.contains(v) || (!verticesInGraph.contains(w))){
                if(!verticesInGraph.contains(v))
                    verticesInGraph.add(v);
                if(!verticesInGraph.contains(w))
                    verticesInGraph.add(w);
                addNewEdge = true;
            }else if(!mstEdgesConnected && !allEdgesTogether(edgesMST, new Edge(v,w,1))){
                mstEdgesConnected = true;
                addNewEdge = true;
            }else if(mstEdgesConnected && verticesInGraph.contains(v) && verticesInGraph.contains(w)) {
                addNewEdge = false;
            }else{
                addNewEdge = false;
            }

            if(sortedEdges.get(index).weight() == -1)
                addNewEdge = false;

            if(addNewEdge){
                edgesMST.add(current);
                Edge temp = new Edge(v,w,-1);
                sortedEdges.set(index, temp);
                size++;

            }
            index++;  //this will allow us to iterate through the list one-by-one until a mst is formed
            current = sortedEdges.get(index);

        }

        edgesMST.forEach(mst::addEdge);

        return mst;
    }

    public static boolean allEdgesTogether(ArrayList<Edge> edges, Edge potential){ //helper method for kruskal's method

        ArrayList<Integer> vertices = new ArrayList<>();
        ArrayList<Edge> edgesToCheck = new ArrayList<>();

        int v = potential.either(), w = potential.other(v);

        for (int i = 0; i < edges.size(); i++) {
            int tempV = edges.get(i).either(), tempW = edges.get(i).other(tempV);

            if(tempV == w)
                vertices.add(tempW);
            else if(tempW == w)
                vertices.add(tempV);
            else
                edgesToCheck.add(edges.get(i));
        }
        for(Edge e: edgesToCheck){
            int tempV = e.either(), tempW = e.other(tempV);
            if(vertices.contains(tempV) || vertices.contains(tempW)){
                return true; //returns true if the edges in the current mst are connected
            }
        }

        return false;
    }

    public static void main(String[] args) {
        EdgeWeightedGraph graph = new EdgeWeightedGraph(5);

        graph.addEdge(new Edge(0, 1, 12));
        graph.addEdge(new Edge(1,2, 2));
        graph.addEdge(new Edge(2,3, 3));
        graph.addEdge(new Edge(3,4, 6));
        graph.addEdge(new Edge(4,0, 70));
        graph.addEdge(new Edge(1,4, 5));
        graph.addEdge(new Edge(1,3, 1));
        graph.addEdge(new Edge(1,2, 1));

        EdgeWeightedGraph mstPrim = prim(graph);
        EdgeWeightedGraph mstKruskal = kruskal(graph);

        System.out.println("- My Prim Output: ");
        Iterable<Edge> edges1 = mstPrim.edges();
        for(Edge e: edges1){
            int v = e.either();
            System.out.println("v: " + v);
            System.out.println("w: " +  e.other(v));
            System.out.println("weight: " + e.weight());
        }
        System.out.println("- My Kruskal Output: ");
        Iterable<Edge> edges2 = mstKruskal.edges();
        for(Edge e: edges2){
            int v = e.either();
            System.out.println("v: " + v);
            System.out.println("w: " +  e.other(v));
            System.out.println("weight: " + e.weight());
        }
        System.out.println("*****************************");
        System.out.println("Sedgewick's Prim Output: ");
        PrimMST graph3 = new PrimMST(graph);
        Iterable<Edge> edges3 = graph3.edges();

        for(Edge e: edges3){
            int v = e.either();
            System.out.println("v: " + v);
            System.out.println("w: " +  e.other(v));
            System.out.println("weight: " + e.weight());
        }
        System.out.println("Sedgewick's Kruskal Output: ");
        KruskalMST graph4 = new KruskalMST(graph);
        Iterable<Edge> edges4 = graph4.edges();

        for(Edge e: edges4){
            int v = e.either();
            System.out.println("v: " + v);
            System.out.println("w: " +  e.other(v));
            System.out.println("weight: " + e.weight());
        }

    }
}

