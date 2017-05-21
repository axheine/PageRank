package Main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import graph.MatrixParser;
import graph.PageGraph;
import graph.WikipediaParser;
import pagerank.PageRank;

public class Main {
	
	/**
	 * Run the test mode of pageRank algorithm
	 * @param string 
	 * 			file to parse
	 * @param k 
	 * 			runs of the algorithm before return a value
	 */
	private static void runTestMode(String fileName, int k) {
		PageGraph g = null;
		try {
			g = MatrixParser.makeGraphFromMatrixFile(Paths.get(fileName), 
					graph -> 1f/(graph.numberOfVertices()*10));
		} catch (IOException e) {
			System.out.println("Error while parsing file" + e);
		}
		
		PageRank pr = new PageRank(g);
		double[] prResult = pr.processPageRank(k);
		double[] fileAnswer = MatrixParser.getResultStat();
		
		for (int i = 0; i < prResult.length; i++) {
			double difference = Math.abs(prResult[i] * 100 / fileAnswer[i]);
			
			System.out.println("Vertix " + i + " found " + prResult[i] + " with difference of " + difference + "%");
		}
	}
	
	/**
	 * Runs normal mode of the page rank algorithm
	 * @param string
	 * @param x
	 */
	private static void runNormalMode(String fileName, int x) {
		PageGraph g = null;
		try {
			g = WikipediaParser.makeGraphFromFile(Paths.get(fileName), 
					graph -> 1f/(graph.numberOfVertices()*10));
		} catch (IOException e) {
			System.out.println("Error while parsing file" + e);
		}
		PageRank pr = new PageRank(g);
		pr.processPageRank(1);
		
		String[] names = new String[x];
		double[] stats = new double[x];
		
		for(int i = 0; i < g.numberOfVertices(); i++) {
			String verticeName = g.getVerticeName(i);
			double verticeWeight = g.getVerticeWeight(i);
			
			for(int j = 0; j < x; j++) {
				if(stats[j] < verticeWeight) {
					stats[j] = verticeWeight;
					names[j] = verticeName;
					break;
				}
			}
		}
		
		for (int i = 0; i < x; i++) {
			System.out.println(names[i] + " : " + stats[i]);
		}
		
	}
	
	private static void usage() {
		System.out.println("Use :");
		System.out.println("\tPageRankCalculator test fileToParse numberOfRuns");
		System.out.println("\tor");
		System.out.println("\tPageRankCalculator fileToParse numberOfPageToShow");
	}
	
	public static void main(String[] args) throws IOException {
		/*
		 * 
													*/
		/*
		double[] stats = pr.processPageRank(1);
		
		System.out.println(Arrays.toString(stats));*/
		
		
		if (args.length == 3 && args[0].equals("test")) {
			runTestMode(args[1], Integer.parseInt(args[2]));
		} else if (args.length == 2) {
			runNormalMode(args[0], Integer.parseInt(args[1]));
		} else {
			usage();
		}
	}	
}
