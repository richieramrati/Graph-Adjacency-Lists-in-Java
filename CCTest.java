import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Program that uses a list of random graphs with n vertices but a different number of edges to measure the
 * effect of number of edges on the size of the maximum connected component.
 * Richie Ramrati
 */

/**
 * The test results support the trend that the number of edges translates to an increase
 * in the size of the maximum connected components. As the 10000 vertex graph increases
 * the number of edges by 2000 with each graph up until 2*N, the size of the largest
 * connected component increases and the number of connected components decline.  I did
 * not observe a case where there was a single connected component. The maximum number of
 * edges in a graph of size N, would be N(N-1)/2 for an undirected graph but achieving a
 * single connected component would not require nearly as many given the growth rate of the
 * largest connected component at only size 2N. Given that the number of edges needed to
 * form a connected a single connected component was not linear (2N), nor was it quadratic
 * (N(N-1)/2), I would estimate the number of edges needed would be log-linear such that it
 * needs some approximation of N*log(N) edges.
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
