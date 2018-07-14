import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Finds the largest connected component for a series of graphs via BFS
 * Richie Ramrati
 */
public class MaxCC {

	public static void main(String[] args) {
		int graphID = 1;
		System.out.println(
				"Largest connected component of graph " + graphID++ +
						": " + largestCC(randomGraph(100, 100, 1)));
		System.out.println(
				"Largest connected component of graph " + graphID++ +
						": " + largestCC(randomGraph(1000, 2000, 17)));
		System.out.println(
				"Largest connected component of graph " + graphID++ +
						": " + largestCC(randomGraph(5000, 2500, 42)));
		System.out.println(
				"Largest connected component of graph " + graphID++ +
						": " + largestCC(randomGraph(250000, 300000, 1)));
	}

	/**
	 * BFS approach to find the longest connected component
	 * @param g
	 * @return the length of the longest connected component in g
	 */
	public static int largestCC(Graph g) {
		int maxCC = 0;
		g.markAll(Graph.UNDISCOVERED);
		for (int n = 0; n < g.getVertexCount(); n++) {
			if (g.getMark(n) == Graph.UNDISCOVERED) {
				Queue<Integer> q = new LinkedList<Integer>();
				q.add(n);
				g.mark(n, Graph.DISCOVERED);
				int count = 0;
				while (!q.isEmpty()) {
					int curVrtx = q.remove();
					g.mark(curVrtx,Graph.DISCOVERED);
					count++;
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
				maxCC = count > maxCC ? count : maxCC;
			}
		}
		return maxCC;
	}

	/**
	 * Creates a random undirected graph with specified numbers of vertices and
	 * edges, using a pseudo-random number generator initialized with a given seed.
	 */
	public static Graph randomGraph(int vertexCt, int edgeCt, int seed) {
		Random rand = new Random(seed);
		Graph grph;
		grph = new Graph(vertexCt);
		while (grph.getEdgeCount() < edgeCt) {
			int a = rand.nextInt(vertexCt);
			int b = rand.nextInt(vertexCt);
			if (!grph.edgeExists(a, b)) {
				grph.addEdge(a, b);
			}
		}
		return grph;
	}

}
