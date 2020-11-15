package alex.geometry.simple;

import java.util.List;

import alex.geometry.base.Vertex;

public class Polygon {
	protected List<Vertex> vertices;

	
	public Polygon(List<Vertex> vertices) {
		this.vertices = vertices;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}
}
