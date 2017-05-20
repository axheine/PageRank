import java.io.IOException;
import java.util.Arrays;

import pagerank.PageRank;

public class Main {
	public static void main(String[] args) throws IOException {
		/*
		 * PageGraph g = WikipediaParser.makeGraphFromFile(Paths.get("test_files"+File.separator+"wiki-zulu.txt"), 
													graph -> 1f/(graph.numberOfVertices()*10));
													*/
		PageRank pr = PageRank.init("alea10-40.txt");
		double[] stats = pr.processPageRank(1);
		
		System.out.println(Arrays.toString(stats));
	}
}
