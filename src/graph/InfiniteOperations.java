package graph;

public class InfiniteOperations {
	public static int add(int v1, int v2) {
		if(v1 == Integer.MAX_VALUE || v2 == Integer.MAX_VALUE) return Integer.MAX_VALUE;
		
		return v1+v2;
	}
	
	
}
