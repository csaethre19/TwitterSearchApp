package twitterSearch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.Cycle;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.SymbolGraph;

/**
 * Provides functionality to create a graph based on Twitter followers specified
 * by user query. Supports functionality to draw users connected by edges in
 * graphical representation.
 * 
 * @author Charlotte Saethre
 *
 */
public class GraphTool {
	private List<String> edges;
	private List<String> followers;
	private Graph graph;
	private SymbolGraph sg;
	private final String FILE_PATH = "src/twitterSearch/Resources/graph.txt";

	/**
	 * Constructs a symbol graph given a list of <code>edges</code> and a list of
	 * <code>followers</code>
	 * 
	 * @param edges     list of edges
	 * @param followers list of followers
	 */
	public GraphTool(List<String> edges, List<String> followers) {
		this.edges = edges;
		this.followers = followers;
		writeToTxtFile();

		// Construct symbol graph
		String delimiter = " ";
		sg = new SymbolGraph(FILE_PATH, delimiter);
		graph = sg.graph();
	}

	/**
	 * Writes data provided by list of edges to text file to be used to create
	 * symbol graph.
	 */
	private void writeToTxtFile() {
		try {
			PrintWriter writer = new PrintWriter(FILE_PATH);
			for (String edge : edges) {
				writer.println(edge);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows graphical representation of followers and their connections.
	 */
	public void drawGraph() {
		if (graph == null) 
			throw new NullPointerException("graph is null.");
		// Set canvas and pen radius
		StdDraw.setCanvasSize(700, 700);
		StdDraw.setPenRadius(.01);

		// Coordinates List
		List<Coordinate> coordinates = Arrays.asList(new Coordinate(.15, .75), new Coordinate(.35, .85),
				new Coordinate(.65, .85), new Coordinate(.85, .75), new Coordinate(.15, .5), new Coordinate(.85, .5),
				new Coordinate(.15, .25), new Coordinate(.35, .15), new Coordinate(.65, .15), new Coordinate(.85, .25));

		// Filling Symbol Table
		ST<String, Coordinate> st = new ST<>();
		for (int i = 0; i < followers.size(); i++) {
			st.put(followers.get(i), coordinates.get(i));
		}

		// Draw edges
		for (int v = 0; v < graph.V(); v++) {
			for (int w : graph.adj(v)) {
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.line(st.get(sg.nameOf(v)).getX(), st.get(sg.nameOf(v)).getY(), st.get(sg.nameOf(w)).getX(),
						st.get(sg.nameOf(w)).getY());
			}
		}

		// Detecting cycle
		Cycle cycle = new Cycle(graph);
		if (cycle.hasCycle()) {
			List<Integer> cycleIndexes = new ArrayList<>();
			for (Integer c : cycle.cycle()) {
				cycleIndexes.add(c);
			}
			for (int i = 0; i < cycleIndexes.size() - 1; i++) {
				double x1 = st.get(sg.nameOf(cycleIndexes.get(i))).getX();
				double y1 = st.get(sg.nameOf(cycleIndexes.get(i))).getY();
				double x2 = st.get(sg.nameOf(cycleIndexes.get(i + 1))).getX();
				double y2 = st.get(sg.nameOf(cycleIndexes.get(i + 1))).getY();
				StdDraw.setPenColor(StdDraw.MAGENTA);
				StdDraw.line(x1, y1, x2, y2);
			}
		}

		// Drawing Followers Nodes
		for (String key : st.keys()) {
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.filledCircle(st.get(key).getX(), st.get(key).getY(), .1);
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.circle(st.get(key).getX(), st.get(key).getY(), .1);
			StdDraw.text(st.get(key).getX(), st.get(key).getY(), key);
		}

		StdDraw.show();
	}

	// Helper class to store coordinate information to draw nodes
	private class Coordinate {
		private double x;
		private double y;

		public Coordinate(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return this.x;
		}

		public double getY() {
			return this.y;
		}
	}

	/********** TEST CLIENT **********/
	public static void main(String[] args) {
		PersonSearch ps = new PersonSearch(TwitterAuth.getTwitterInstance(), "SiLvErFoX_69_");
		GraphTool graphTool = new GraphTool(ps.getEdges(), ps.getFollowers());
		graphTool.drawGraph();

	}
}
