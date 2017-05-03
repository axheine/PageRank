package graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.channels.IllegalSelectorException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public interface Graph {
	final static int VERTICE_NOT_VISITED = 0;
	final static int VERTICE_BEING_PROCESSED = 1;
	final static int VERTICE_VISITED = 2;

	public final static int maxGraph = 1000;
	public final static int minGraph = -1000;

	static Random random = new Random(System.currentTimeMillis());

	/**
	 * Le nombre d'arêtes du graphe.
	 * 
	 * @return le nombre d'arêtes du graphe
	 */
	int numberOfEdges();

	/**
	 * Le nombre de sommets du graphe.
	 * 
	 * @return le nombre de sommets du graphe
	 */
	int numberOfVertices();

	/**
	 * Permet d'ajouter une arête orientée et pondérée au graphe.
	 * 
	 * @param i
	 *            la première extrémité de l'arête
	 * @param j
	 *            la seconde extrémité de l'arête
	 * @param value
	 *            le poids de l'arête
	 * @throws IndexOutOfBoundsException
	 *             si i ou j n'est pas un sommet du graphe
	 * @throws IllegalArgumentException
	 *             si value vaut 0 ou si il existe déjà une arête entre i et j
	 */
	void addEdge(int i, int j, int value);

	/**
	 * Teste l'existence d'une arête donnée
	 * 
	 * @param i
	 *            la première extrémité de l'arête
	 * @param j
	 *            la seconde extrémité de l'arête
	 * @return true s'il existe une arête entre i et j; false sinon
	 * @throws IndexOutOfBoundsException
	 *             si i ou j n'est pas un sommet du graphe
	 */
	boolean isEdge(int i, int j);

	/**
	 * Renvoie le poids d'une arête donnée.
	 * 
	 * @param i
	 *            la première extrémité de l'arête
	 * @param j
	 *            la seconde extrémité de l'arête
	 * @return le poids de l'arête entre i et j
	 * @throws IndexOutOfBoundsException
	 *             si i ou j n'est pas un sommet du graphe
	 */
	int getWeight(int i, int j);

	/**
	 * Un itérateur sur tous les voisins d'un sommet donné.
	 * 
	 * @param i
	 *            le sommet à partir duquel partent les arêtes fournies par
	 *            l'itérateur
	 * @return un itérateur sur tous les voisins du sommet i
	 * @throws IndexOutOfBoundsException
	 *             si i n'est pas un sommet du graphe
	 */
	Iterator<Edge> edgeIterator(int i);

	/**
	 * Effectue une action sur tous les voisins (les arêtes) d'un sommet donné.
	 * 
	 * @param i
	 *            le sommet à partir duquel partent les arêtes traitées
	 * @param consumer
	 *            l'acction effectuée sur toutes les arêtes voisines de i
	 * @throws IndexOutOfBoundsException
	 *             si i n'est pas un sommet du graphe
	 */
	void forEachEdge(int i, Consumer<Edge> consumer);
	
	default void forEachEdge(Consumer<Edge> consumer) {
		
		for(int i=0; i<numberOfVertices(); i++) {
			forEachEdge(i, consumer);
		}
	}
	/**
	 * Fournit une réprésentaiuon du graphe au format .dot
	 * 
	 * @return une réprésentaiuon du graphe au format .dot
	 */
	public default String toGraphviz() {
		StringBuilder sb = new StringBuilder().append("digraph G {\n");
		int verticesNumber = numberOfVertices();
		for (int i = 0; i < verticesNumber; i++) {
			final int j = i;
			forEachEdge(i, (edge) -> {
				sb.append(j + " -> " + edge.getEnd() + " [ label=\"" + edge.getValue() + "\"] ;\n");
			});
		}

		return sb.append("}").toString();
	}

	public default void writeToFile(String fileName)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		String viz = toGraphviz();

		File yourFile = new File(fileName);
		yourFile.createNewFile();

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"))) {
			writer.write(viz);
		}
	}
	
	
	/**
	 * Création d'un graphe à partir d'un fichier contenant le nombre de sommets
	 * et sa matrice
	 * 
	 * @param path
	 *            le chemin du fichier contenant la matrice du graphe
	 * @param factory
	 *            une méthode qui étant donné un nombre de sommet n, fabrique et
	 *            renvoie yun graphe vide à n sommet
	 * @return un graphe construit à l'aide de factory et dont les arêtes sont
	 *         données dans le fihier indiqué dans path
	 * @throws IOException
	 */
	public static Graph makeGraphFromMatrixFile(Path path, IntFunction<Graph> factory) throws IOException {
		Graph g = null;
		String line;
		try (InputStream fis = new FileInputStream(path.toString());
				InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				BufferedReader br = new BufferedReader(isr);) {
			int verticesNumber = Integer.parseInt(br.readLine());
			g = factory.apply(verticesNumber);

			int i = 0;
			while ((line = br.readLine()) != null) {
				String[] ends = line.split(" ");
				for (int j = 0; j < verticesNumber; j++) {
					if (!ends[j].equals("0")) {
						g.addEdge(i, j, Integer.parseInt(ends[j]));
					}
				}
				i++;
			}
		}

		return g;
	}

	/**
	 * Création d'un graphe aléatoire avec un nombre de sommets et d'arêtes fixé
	 * 
	 * @param n
	 *            nombre de sommets
	 * @param nbEdges
	 *            nombre d'arêtes
	 * @param wmax
	 *            poids maximal (en valeur absolue) sur les arêtes
	 * @param factory
	 *            une méthode qui étant donné un nombre de sommets n, fabrique
	 *            et renvoie yun graphe vide à n sommets
	 * @return un graphe aléatoire construit à l'aide de factory yant exactement
	 *         n sommets et nbEdges arêtes
	 * @throws IllegalArgumentException
	 *             si le nombre d'arêtes est trop élevé par rapport au nombre de
	 *             sommets
	 */
	public static Graph makeRandomGraph(int verticesNumber, IntFunction<Graph> factory) {
		Graph g = factory.apply(verticesNumber);

		for (int i = 0; i < verticesNumber * verticesNumber * 0.2; i++) {
			g.addEdge(random.nextInt(verticesNumber), random.nextInt(verticesNumber), random.nextInt(20) + 1);
		}

		return g;
	}

	static void depthFirstSearch(Graph g, int vertice, Consumer<Integer> onFirstVisit,
			Consumer<Integer> onFinishedVisiting, int[] visited, boolean throwOnCycle) {
		visited[vertice] = Graph.VERTICE_BEING_PROCESSED;
		if (onFinishedVisiting != null)
			onFirstVisit.accept(vertice);
		g.forEachEdge(vertice, (edge) -> {
			if (visited[edge.getEnd()] == Graph.VERTICE_NOT_VISITED) {
				depthFirstSearch(g, edge.getEnd(), onFirstVisit, onFinishedVisiting, visited);
			} else if (visited[edge.getEnd()] == Graph.VERTICE_BEING_PROCESSED && throwOnCycle) {
				throw new IllegalStateException("Cycle!");
			}
		});
		visited[vertice] = Graph.VERTICE_VISITED;
		if (onFinishedVisiting != null)
			onFinishedVisiting.accept(vertice);
	}

	static void depthFirstSearch(Graph g, int vertice, Consumer<Integer> onFirstVisit,
			Consumer<Integer> onFinishedVisiting, int[] visited) {
		depthFirstSearch(g, vertice, onFirstVisit, onFinishedVisiting, visited, false);
	}
	

	
	public static List<Integer> dfs(Graph g, int s0) {
		ArrayList<Integer> vertices = new ArrayList<>();
		int[] visited = new int[g.numberOfVertices()];
		depthFirstSearch(g, s0, vertices::add, null, visited);
		for (int i = 0; i < g.numberOfVertices(); i++) {
			if (visited[i] == Graph.VERTICE_NOT_VISITED) {
				depthFirstSearch(g, i, vertices::add, null, visited);
			}
		}
		return vertices;
	}

	public static int[][] timedDepthFirstSearch(Graph g, int s0) {
		int[][] times = new int[g.numberOfVertices()][2];
		int[] visited = new int[g.numberOfVertices()];
		LongAdder count = new LongAdder();

		depthFirstSearch(g, s0, (v) -> {
			times[v][0] = count.intValue();
			count.increment();
		}, (v) -> {
			times[v][1] = count.intValue();
			count.increment();
		}, visited);

		for (int i = 0; i < g.numberOfVertices(); i++) {
			if (visited[i] == Graph.VERTICE_NOT_VISITED) {
				depthFirstSearch(g, i, (v) -> {
					times[v][0] = count.intValue();
					count.increment();
				}, (v) -> {
					times[v][1] = count.intValue();
					count.increment();
				}, visited);
			}
		}

		return times;
	}

	public static List<Integer> bfs(Graph g, int s0) {
		ArrayList<Integer> vertices = new ArrayList<>();
		int[] visited = new int[g.numberOfVertices()];
		Queue<Integer> queued = new LinkedList<Integer>();

		queued.add(s0);
		for (int i = 0; i < g.numberOfVertices(); i++) {
			if (visited[i] == Graph.VERTICE_NOT_VISITED) {

				// 1. Mettre le 1e vertice dans la file
				queued.add(i);

				// 2. Tant qu'on a quelque chose dans la file, pour chacun,
				// récupérer
				// les voisins et les ajouter à la fin
				while (!queued.isEmpty()) {
					// System.out.println(queued);
					int vertice = queued.remove();
					if (visited[vertice] == Graph.VERTICE_NOT_VISITED) {
						visited[vertice] = Graph.VERTICE_BEING_PROCESSED;
						vertices.add(vertice);
						g.forEachEdge(vertice, (v) -> queued.add(v.getEnd()));
						visited[vertice] = Graph.VERTICE_VISITED;
					}
				}
			}
		}

		return vertices;
	}

	public static List<Integer> topologicalSort(Graph g, boolean cycleDetect) {
		LinkedList<Integer> vertices = new LinkedList<>();
		int[] visited = new int[g.numberOfVertices()];
		for (int i = 0; i < g.numberOfVertices(); i++) {
			if (visited[i] == Graph.VERTICE_NOT_VISITED) {
				depthFirstSearch(g, i, (v) -> vertices.add(v, 0), null, visited, cycleDetect);
			}
		}
		return vertices;
	}
	
	public static ShortestPathFromOneVertex bellmanFord(Graph g, int source) {
		int verticesNb = g.numberOfVertices();
		int edgesNb = g.numberOfEdges();
		int distances[] = new int[verticesNb];
		int ancestors[] = new int[verticesNb];
		
		for(int i=0; i<verticesNb; i++) {
			if(i != source) {
				distances[i] = Integer.MAX_VALUE;
				ancestors[i] = -1;
			} else {
				ancestors[i] = i;
			}
		}
		
		for(int i=0; i<edgesNb-1; i++) {
			g.forEachEdge((edge) -> {
				int x = edge.getStart();
				int y = edge.getEnd();
				int weight = edge.getValue();
				
				if(InfiniteOperations.add(distances[x], weight) < distances[y]) {
					distances[y] = distances[x] + weight;
					ancestors[y] = x;
				}
			});
		}
		
		g.forEachEdge((edge) -> {
			int x = edge.getStart();
			int y = edge.getEnd();
			int weight = edge.getValue();
			
			if(InfiniteOperations.add(distances[x], weight) < distances[y]) {
				throw new IllegalStateException("Negative loop");
			}
		});
		
		return new ShortestPathFromOneVertex(source, distances, ancestors);
	}

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		/*
		// MatGraph
		Graph g = new MatGraph(4);
		g.addEdge(0, 1, 1);
		g.addEdge(0, 2, 5);
		g.addEdge(0, 3, 4);

		g.addEdge(1, 0, 1);
		g.addEdge(3, 2, 1);
		g.addEdge(2, 0, 1);

		System.out.println(g.toGraphviz());

		g.writeToFile("viz_mat.dot");

		// AdjGraph
		g = new AdjGraph(4);
		g.addEdge(1, 1, 3);
		g.addEdge(3, 2, 5);
		g.addEdge(0, 3, 2);

		g.addEdge(1, 0, 2);
		g.addEdge(1, 2, 1);
		g.addEdge(2, 3, 8);

		System.out.println(g.toGraphviz());

		g.writeToFile("viz_adj.dot");

		// Loading
		g = Graph.makeGraphFromMatrixFile(Paths.get("matrix.mat"), (integer) -> new AdjGraph(integer));
		System.out.println(g.toGraphviz());
		g.writeToFile("viz_loaded_mat.dot");
		
		
		// Random
		g = Graph.makeRandomGraph(5, (integer) -> new AdjGraph(integer));
		
		
		System.out.println(g.toGraphviz());
		System.out.println("Dfs:" + dfs(g, 0));
		System.out.println("Bfs:" + bfs(g, 0));
		int[][] times = timedDepthFirstSearch(g, 0);
		StringBuffer aString = new StringBuffer();
		for (int row = 0; row < times.length; row++) {
			aString.append(row + " : ");
			for (int col = 0; col < times[row].length; col++) {
				aString.append(" " + times[row][col]);
			}
			aString.append("\n");
		}
		System.out.println("Times: \n" + aString.toString());
		
		System.out.println(topologicalSort(g, false));
		g.writeToFile("viz_random.dot");
		*/
		
		Graph g = new MatGraph(6);
		g.addEdge(0, 1, 3);
		g.addEdge(0, 2, 1);
		
		g.addEdge(1, 0, 3);
		g.addEdge(1, 2, 1);
		g.addEdge(1, 3, 2);
		
		g.addEdge(2, 0, 1);
		g.addEdge(2, 1, 1);
		g.addEdge(2, 3, 3);
		g.addEdge(2, 4, 5);
		
		g.addEdge(3, 1, 2);
		g.addEdge(3, 2, 3);
		g.addEdge(3, 4, 1);
		g.addEdge(3, 5, 3);
		
		g.addEdge(4, 2, 5);
		g.addEdge(4, 3, 1);
		g.addEdge(4, 5, 1);
		
		g.addEdge(5, 3, 3);
		g.addEdge(5, 4, 1);
		
		ShortestPathFromOneVertex paths = Graph.bellmanFord(g, 0);
		System.out.println(paths);
		paths.printShortestPathTo(4);
	}
	

	
}
