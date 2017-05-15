package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PageGraph implements Graph {
	private final HashSet<Integer>[] edges;
	private final int verticesNumber;
	private int edgesNumber = 0;
	private final String[] names;
	private final float[] probabilities;
	public final int SUPERNODE_INDEX;
	
	@SuppressWarnings("unchecked")
	public PageGraph(int verticesNumber) {
		edges = (HashSet<Integer>[]) new HashSet[verticesNumber+1];
		for(int i=0; i<verticesNumber+1; i++) {
			edges[i] = new HashSet<>();
		}
		
		this.verticesNumber = verticesNumber;
		names = new String[verticesNumber];
		
		probabilities = new float[verticesNumber+1];
		
		SUPERNODE_INDEX = verticesNumber;
	}
	
	
	@Override
	public int numberOfEdges() {
		return edgesNumber;
	}

	@Override
	public int numberOfVertices() {
		return verticesNumber;
	}

	@Override
	public void addEdge(int i, int j, float value) {
		throw new NotImplementedException();
	}
	
	public void addEdge(int i, int j) {
		checkEdgesNumber(i, j);
		edges[i].add(j);
	}

	@Override
	public boolean isEdge(int i, int j) {
		checkEdgesNumber(i, j);
		return edges[i].contains(j);
	}

	@Override
	public float getWeight(int i, int j) {
		throw new NotImplementedException();
	}

	@Override
	public Iterator<Edge> edgeIterator(int i) {
		throw new NotImplementedException();
	}

	@Override
	public void forEachEdge(int i, Consumer<Edge> consumer) {
		throw new NotImplementedException();
	}
	
	public void forEachNeighbor(int i, Consumer<Integer> consumer) {
		checkEdgesNumber(i, 0);
		
		for(int neighbor : edges[i]) {
			consumer.accept(neighbor);
		}
	}

	@Override
	public void setVerticeName(int vertice, String name) {
		names[vertice] = name;
	}

	@Override
	public String getVerticeName(int vertice) {
		checkEdgesNumber(vertice, 0);
		return names[vertice];
	}
	
	private void checkEdgesNumber(int i, int j) {
		if(i > verticesNumber || j > verticesNumber) 
			throw new IllegalArgumentException("illegal edges numbers: "+i+" to "+j);
	}


	@Override
	public int getEdgeNumber(int vertice) {
		checkEdgesNumber(vertice, 0);
		return edges[vertice].size();
	}


	@Override
	public void setVerticeWeight(int vertice, float proba) {
		checkEdgesNumber(vertice, 0);
		probabilities[vertice] = proba;
	}


	@Override
	public float getVerticeWeight(int vertice) {
		checkEdgesNumber(vertice, 0);
		return probabilities[vertice];
	}
	
	
}
