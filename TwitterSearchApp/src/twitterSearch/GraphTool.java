package twitterSearch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.SymbolGraph;

/**
 * 
 * @author Charlotte Saethre
 *
 */
public class GraphTool {
	private List<String> edges;
	private Graph graph;
	private final String FILE_PATH = "src/twitterSearch/Resources/graph.txt";
	
	public GraphTool(List<String> edges) {
		this.edges = edges;
		writeToTxtFile();
		
		// Construct symbol graph
		String delimiter = " ";
		SymbolGraph sg = new SymbolGraph(FILE_PATH, delimiter);
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
	 * 
	 * @return
	 */
	public boolean hasCycle() {
		return false; // TODO
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getCyclePath() {
		return null; // TODO
	}
	
	public static void main(String[] args) {
		PersonSearch ps = new PersonSearch(TwitterAuth.getTwitterInstance(), "char_saethre");
		List<String> edges = ps.getEdges();
		GraphTool graphTool = new GraphTool(edges);
	}
}
