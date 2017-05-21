package pagerank;

import java.util.Objects;

import graph.PageGraph;

public class PageRank {
	private final PageGraph g;
	
	public PageRank(PageGraph g) {
		this.g = Objects.requireNonNull(g);
	}
	
	public double[] processPageRank(int k) {
		
		int n = g.numberOfVertices();
		double eps = g.getEpsilon();
		System.out.println(n);
		double[] stats = new double[n];
		double[] tmp = new double[n];
		
		for (int u = 0; u < n; u++) {
			
			if(u == g.SUPERNODE_INDEX) {
				g.setVerticeWeight(u, 0);
				System.out.println("proba for " + u + " set to " + 0);
			} else {
				g.setVerticeWeight(u, 1.0/n);
				System.out.println("proba for " + u + " set to " + 1.0/n);
			}
			
		}
		
		for (int j = 0; j < k; j++) { // nombre de tours demandés par l'algo
			for (int i = 0; i < n; i++) {
				System.out.println("Final proba for " + i + " : " + g.getVerticeWeight(i));
				tmp[i] = g.getVerticeWeight(i);
			}
			for (int u = 0; u < n; u++) { // pour chaque sommets
				final int nbParentNeighbors = g.getEdgeNumber(u);
				final double parentProb = g.getVerticeWeight(u);
				final int currentParent = u;
				
				System.out.println("====================== current parent : " + u + " =====================");
				g.forEachNeighbor(u, (s) -> { // pour chacun des voisins du sommet
					double tmpProba = 0;
					if (currentParent == g.SUPERNODE_INDEX) { // si i est le super node
						System.out.println(currentParent + " (currentParent) is superNode");
						tmpProba = 1.0/n;
					} else if (s == g.SUPERNODE_INDEX) { // si le voisin est le super node 
						System.out.println(s + " child of " + currentParent + " is superNode");
						tmpProba = eps;
					} else { //sinon on est sur un noeud normal
						System.out.println(s + " child of " + currentParent + " is nothing");
						tmpProba = (1.0 - eps)/nbParentNeighbors;
					}  
					double sonProba = g.getVerticeWeight(s);
					double newProba = sonProba * parentProb + tmpProba;
					System.out.println("\tnew proba for " + s + " = " + newProba);
					g.setVerticeWeight(s, newProba);
				});
			}
		}
		
		for (int i = 0; i < n; i++) {
			System.out.println("Final proba for " + i + " : " + g.getVerticeWeight(i));
			stats[i] = g.getVerticeWeight(i);
		}
		
		return stats;
	}
	
	
}
