import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import graph.AdjGraph;
import graph.Graph;
import graph.WikipediaParser;

public class Main {
	public static void main(String[] args) throws IOException {
		Graph g = WikipediaParser.makeGraphFromFile(Paths.get("test_files"+File.separator+"wiki-zulu.txt"), 
													v -> new AdjGraph(v),
													graph -> 1f/(graph.numberOfVertices()*10));
		
	}
}
