package graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class AdjGraph implements Graph {
	private final ArrayList<LinkedList<Edge>> adj;
	private final int verticesNumber;
	private int edgesNumber = 0;
	private final String[] names;
	
	public AdjGraph(int verticesNumber) {
		adj = new ArrayList<>(verticesNumber);
		for(int i=0; i<verticesNumber; i++) {
			adj.add(new LinkedList<>());
		}
		this.verticesNumber = verticesNumber;
		names = new String[verticesNumber];
	}
	
	@Override
	public int numberOfEdges() {
		return this.edgesNumber;
	}

	@Override
	public int numberOfVertices() {
		return this.verticesNumber;
	}

	@Override
	public void addEdge(int i, int j, float value) {
		if(value <= 0) throw new IllegalArgumentException("weight can't be null or negative");
		checkEdgesNumber(i, j);
		
		LinkedList<Edge> edges = adj.get(i);
		
		Edge newEdge = new Edge(i, j, value);
		if(!isEdge(i, j)) {
			edges.add(newEdge);
		}
	}

	@Override
	public boolean isEdge(int i, int j) {
		checkEdgesNumber(i, j);
		LinkedList<Edge> edges = adj.get(i);
		
		for(Edge edge : edges) {
			if(edge.getEnd() == j) return true;
		}
		return false;
	}

	@Override
	public float getWeight(int i, int j) {
		checkEdgesNumber(i, j);
		LinkedList<Edge> edges = adj.get(i);
		
		for(Edge edge : edges) {
			if(edge.getStart() == i && edge.getEnd() == j) return edge.getValue();
		}
		return 0;
	}

	@Override
	public Iterator<Edge> edgeIterator(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEachEdge(int i, Consumer<Edge> consumer) {
		checkEdgesNumber(i, 0);
		LinkedList<Edge> edges = adj.get(i);
		
		for(Edge edge : edges) {
			consumer.accept(edge);
		}
	}
	
	private void checkEdgesNumber(int i, int j) {
		if(i >= verticesNumber || j >= verticesNumber) 
			throw new IllegalArgumentException("illegal edges numbers: "+i+" to "+j);
	}

	@Override
	public String getVerticeName(int vertex) {
		if(vertex < 0 || vertex >= verticesNumber) throw new IllegalArgumentException();
		
		return names[vertex];
	}

	@Override
	public void setVerticeName(int vertex, String name) {
		if(vertex < 0 || vertex >= verticesNumber) throw new IllegalArgumentException();
		names[vertex] = name;
	}

	@Override
	public int getEdgeNumber(int vertice) {
		return adj.get(vertice).size();
	}
}
