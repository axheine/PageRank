package graph;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.IntFunction;

public class WikipediaParser {
	public static Graph makeGraphFromFile(Path path, IntFunction<Graph> factory) throws IOException {
		String[][] lines = readLinesFromFile(path);
		int length = lines.length;
		Graph g = factory.apply(length);
		HashMap<String, Integer> names = new HashMap<>();

		// Pour chaque ligne, ajouter son nom
		for (int i=0; i < length; i++) {
			names.put(lines[i][0], i);
			i++;
		}
		
		// Second passage : pour chaque nom associé à un vertex, ajouter l'edge
		for (int i=0; i < length; i++) {
			for(int j=1; j<lines[i].length; j++) {
				if(names.containsKey(lines[i][j])) {
					g.addEdge(i, names.get(lines[i][j]), 1.0f/length);
				}
			}
		}
		
		
		return g;
	}

	private static String[][] readLinesFromFile(Path path) throws FileNotFoundException, IOException {
		ArrayList<String[]> lines = new ArrayList<>();
		String line;
		try (InputStream fis = new FileInputStream(path.toString());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			while ((line = br.readLine()) != null) {
				lines.add(line.split("\\|"));
			}
		}
		String[][] ret = new String[lines.size()][];
		int i=0;
		for(String[] l : lines) {
			ret[i] = l;
			i++;
		}
		
		return ret;
	}
}
