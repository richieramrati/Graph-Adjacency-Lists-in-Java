import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Determines the weight of MST for a series of graphs via Kruskals
 * Richie Ramrati
 */
public class MST {
    public static void main(String[] args) {
        int graphID = 1;
        Graph graphFromFile = null;
        try {
            graphFromFile = readGraphFromFile("src/weighted-graph.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Graph " + graphID++ + " Weight: " + KruskalMST(graphFromFile));
        System.out.println("Graph " + graphID++ + " Weight: " + KruskalMST(randomWeightedGraph(100,300,10,1)));
        System.out.println("Graph " + graphID++ + " Weight: " + KruskalMST(randomWeightedGraph(300000,5000000,1000,2)));
    }


    /**
     * Using the number of connected components of vertices, build the MST with a union-find approach.
     * Repeatedly considers the edge with the lowest weight and tests whether its two endpoints lie
     * within the same connected component.
     *
     * @param g
     * @return weight of MST
     */
    public static double KruskalMST(Graph g) {
        double totalWeight = 0;
        Graph.Edge[] edgeArray = g.getAllEdges();
        Arrays.sort(edgeArray, (a, b) -> Double.compare(a.weight,b.weight));
            for (int i = 0 ; i < edgeArray.length; i++) {
                Graph.Edge e = edgeArray[i];
                int f = e.from;
                int t = e.to;
                if (root(g, f) != root(g, t)) {
                    totalWeight = totalWeight + e.weight;
                    union(g, f, t);
                }
            }
        return totalWeight;
    }

    /**
     * Finds the root vertex of connected component
     * @param g
     * @param vertex
     * @return the root of vertex
     */
    private static int root(Graph g, int vertex){
        int parent = g.getParent(vertex);
        if (parent == -1) {
            return vertex;
        }
        return root(g,parent);
    }

    /**
     * Given two endpoints that are in different components, we insert an edge merging
     * the two components into one using a ranking system that attaches smaller connected
     * component as a child
     *
     * @param g
     * @param v1 vertex 1
     * @param v2 vertex 2
     */
    private static void union(Graph g, int v1, int v2){
        int v1Rank = rank(g, v1, 0);
        int v2Rank = rank(g, v2,0);
        if (v1Rank > v2Rank) {
            int r2 = root(g,v2);
            g.setParent(r2, v1);
        } else {
            int r1 = root(g,v1);
            g.setParent(r1,v2);
        }
    }

    /**
     * Rank corresponds to degree away from root vertex
     * @param g
     * @param v vertex
     * @param r current rank
     * @return
     */
    private static int rank(Graph g, int v, int r) {
        int val = g.getParent(v);
        if (val == -1) {
            return r;
        }
        int rank = r + 1;
        return rank(g,val,rank);
    }

    /**
     * @param fileName string corresponding to a text file
     * @return connected, weighted, undirected graph defined by file
     * @throws FileNotFoundException
     */
    private static Graph readGraphFromFile(String fileName) throws FileNotFoundException {
        Graph g = null;
        File f = new File(fileName);
        if(!f.isFile()) {
            System.out.println("Bad File Path");
            System.exit(0);
            return g;
        } else {
            int vrtxCount, edgeCount;
            Scanner fScan = new Scanner(f);
            vrtxCount = fScan.nextInt();
            edgeCount = fScan.nextInt();
            g = new Graph(vrtxCount);
            String s = "";
            while (fScan.hasNext()){
                s = s + fScan.next() + " ";
            }
            fScan.close();
            Scanner strScan = new Scanner(s);
            int numEdge = 0;
            while (numEdge < edgeCount) {
                g.addEdge(strScan.nextInt(), strScan.nextInt(), strScan.nextDouble());
                numEdge++;
            }
        }
        return g;
    }


    public static Graph randomWeightedGraph(int v, int e, int maxWeight, long seed) {
        Random rand = new Random(seed);
        Graph g = new Graph(v);
        while (g.getEdgeCount() < e) {
            int x = rand.nextInt(v);
            int y = rand.nextInt(v);
            if (x == y || g.edgeExists(x, y))
                continue;
            g.addEdge(x, y, 1 + rand.nextInt(maxWeight));
        }
        return g;
    }
}