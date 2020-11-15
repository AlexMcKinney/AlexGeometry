package alex.geometry.base;

public class LineSegment {
	private Vertex v1, v2;

	public LineSegment(Vertex v1, Vertex v2) {
		super();
		this.v1 = v1;
		this.v2 = v2;
	}

	public Vertex getV1() {
		return v1;
	}

	public Vertex getV2() {
		return v2;
	}
}
