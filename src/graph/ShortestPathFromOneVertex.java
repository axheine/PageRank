package graph;

import java.util.Arrays;

import com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.HeapNode;

public class ShortestPathFromOneVertex {
	private final int source;
	private final int[] d;
	private final int[] pi;

	ShortestPathFromOneVertex(int vertex, int[] d, int[] pi) {
		this.source = vertex;
		this.d = d;
		this.pi = pi;
	}

	public void printShortestPathTo(int vertex) {
		StringBuilder sb = new StringBuilder();
		
		while(vertex != source) {
			sb.append(vertex).append(" >-- ");
			vertex = pi[vertex];
		}
		
		sb.append(source);
		sb.reverse();
		System.out.println(sb.toString());
	}
	
	public void printShortestPaths() {
		for (int i = 0; i < d.length; i++) {
			if (i == source) {
				continue;
			}
			printShortestPathTo(i);
		}
	}

	@Override
	public String toString() {
		return source + " " + Arrays.toString(d) + " " + Arrays.toString(pi);
	}
}
