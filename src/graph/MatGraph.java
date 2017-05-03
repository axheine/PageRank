package graph;

import java.util.Iterator;
import java.util.function.Consumer;

public class MatGraph implements Graph {
	private final float[][] mat;
	private final int verticesNumber;
	private int edgesNumber = 0;
	private final String[] names;
	
	public MatGraph(int verticesNumber) {
		if(verticesNumber<=0) throw new IllegalArgumentException("vertices number can't be null");
		
		this.verticesNumber = verticesNumber;
		mat = new float[verticesNumber][verticesNumber];
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
		if(mat[i][j] == 0) {
			edgesNumber++;
		}
		mat[i][j] = value;
	}

	@Override
	public boolean isEdge(int i, int j) {
		checkEdgesNumber(i, j);
		return mat[i][j] != 0;
	}

	@Override
	public float getWeight(int i, int j) {
		checkEdgesNumber(i, j);
		return mat[i][j];
	}

	@Override
	public Iterator<Edge> edgeIterator(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEachEdge(int edgeOrigin, Consumer<Edge> consumer) {
		for(int i=0; i<verticesNumber; i++) {
			float weight = mat[edgeOrigin][i];
			if(weight > 0) {
				consumer.accept(new Edge(edgeOrigin, i, weight));
			}
		}
	}
	
	private void checkEdgesNumber(int i, int j) {
		if(i >= verticesNumber || j >= verticesNumber) 
			throw new IllegalArgumentException("illegal edges numbers: "+i+" to "+j);
	}

	@Override
	public String getVerticeName(int vertice) {
		if(vertice < 0 || vertice >= verticesNumber) throw new IllegalArgumentException();
		
		return names[vertice];
	}

	@Override
	public void setVerticeName(int vertice, String name) {
		if(vertice < 0 || vertice >= verticesNumber) throw new IllegalArgumentException();
		names[vertice] = name;
	}
}
