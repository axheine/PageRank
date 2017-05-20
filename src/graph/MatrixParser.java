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
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MatrixParser {	
	
	public static PageGraph makeGraphFromMatrixFile(Path path, Function<Graph, Float> eSupplier) throws FileNotFoundException, IOException {
		String[][] lines = readLinesFromFile(path);
		int length = lines.length;
		PageGraph g = new PageGraph(length);
		
		g.setEpsilon(eSupplier.apply(g));
		
		for (int i = 0; i < length - 1; i++) {
			for (int j = 0; j < length - 1; j++) {
				System.out.println(lines[i][j]);
				if (lines[i][j].equals("1")) {
					g.addEdge(i, j);
				}	
			}
		}
		// Ajout des edges "spéciaux" ("ma maman me dit que je suis schpécial!")
		for (int i=0; i < length; i++) {
			g.addEdge(i, g.SUPERNODE_INDEX);
			g.addEdge(g.SUPERNODE_INDEX, i);
			g.addEdge(i, i);
		}
		return g;
	}
	
	private static String[][] readLinesFromFile(Path path) throws FileNotFoundException, IOException {
		List<String[]> lines = new ArrayList<>();
		String line;
		try (InputStream fis = new FileInputStream(path.toString());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			/*lines = br	.lines()
				.limit(br.lines().count() - 2)
				.map((l) -> {
					return l.split(" ");
				}).collect(Collectors.toList());*/
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				lines.add(tokens);
			}
		}
		String[][] ret = new String[lines.size() - 2][];
		for(int i = 0; i < lines.size() -2; i++) {
			ret[i] = lines.get(i);
		}
		return ret;
	}
	
	/*private static List<String> getStatsFromFile(Path path, int n) throws FileNotFoundException, IOException {
		List<String> stats;
		try (	InputStream fis = new FileInputStream(path.toString());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);
			) {
			 stats = br	.lines()
				.skip(n)
				.flatMap((l) -> {
					return l.split(", ");
				})
				.collect(Collectors.toList());
		}
		return stats;
	}*/
}
