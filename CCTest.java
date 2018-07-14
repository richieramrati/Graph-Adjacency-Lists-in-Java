import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Program that uses a list of random graphs with n vertices but a different number of edges to measure the
 * effect of number of edges on the size of the maximum connected component.
 * Richie Ramrati
 */

public class CCTest {

    public static void main(String[] args) {
        int vrtxCnt = 10000;
        ArrayList<Graph> graphs = genGraphs(vrtxCnt, 10, vrtxCnt*2);
        int graphID = 1;
        for (Graph g : graphs) {
            System.out.print("Graph " + graphID++ + " has " + g.getEdgeCount() + " edges. " +
                    "The size of the largest connected component is " + MaxCC.largestCC(g) + ". "
                    + "The number of connected components is " + numCC(g) + ", which means");
            System.out.println(MaxCC.largestCC(g) == g.getVertexCount() ?
                    " the graph is connected, meaning the  vertices are contained in a single connected component."
                    : " there are multiple connected components.");
            System.out.println("");
        }
    }


    /**
     * @param vrtxCnt number of vertices
     * @param factor incremental difference between the number of edges in each graph
     * @param edgeCeil maximum number of edges being tested
     * @return a list of graphs with a various number of edges
     */
    private static ArrayList<Graph> genGraphs(int vrtxCnt, int factor, int edgeCeil) {
        ArrayList<Graph> graphList = new ArrayList<Graph>();
        for (int i = vrtxCnt/factor; i <= edgeCeil; i = i + vrtxCnt/factor ) {
            graphList.add(MaxCC.randomGraph(vrtxCnt,i,15));
        }
        return graphList;
    }

    /**
     * @param g
     * @return the number of connected components in a graph
     */
    private static int numCC (Graph g) {
        g.markAll(Graph.UNDISCOVERED);
        int numCC = 0;
        for (int n = 0; n < g.getVertexCount(); n++) {
            if (g.getMark(n) == Graph.UNDISCOVERED) {
                numCC++;
                Queue<Integer> q = new LinkedList<Integer>();
                q.add(n);
                g.mark(n, Graph.DISCOVERED);
                while (!q.isEmpty()) {
                    int curVrtx = q.remove();
                    g.mark(curVrtx,Graph.DISCOVERED);
                    int[] neighbor = g.getConnectedVertices(curVrtx);
                    if (neighbor.length == 0) {
                        continue;
                    }
                    for (int k : neighbor) {
                        if (g.getMark(k) == Graph.UNDISCOVERED) {
                            g.mark(k, Graph.DISCOVERED);
                            q.add(k);
                        }
                    }
                }
            }
        }
        return numCC;
    }
}
