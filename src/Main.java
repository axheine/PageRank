import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import graph.PageGraph;
import graph.WikipediaParser;

public class Main {
	public static void main(String[] args) throws IOException {
		PageGraph g = WikipediaParser.makeGraphFromFile(Paths.get("test_files"+File.separator+"wiki-zulu.txt"), 
													graph -> 1f/(graph.numberOfVertices()*10));
		
	}
}
