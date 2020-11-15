package alex.geometry.shape;

import alex.buffer.render.Color;
import alex.geometry.base.Vertex;

public class Rectangle extends Shape{

	protected Rectangle(){}
	
	public Rectangle(Vertex v1, Vertex v2, Vertex v3, Vertex v4){
		construct_from_vertices(v1, v2, v3, v4, Color.BLACK);
		super.construct();
	}
	
	public Rectangle(Vertex v1, Vertex v2, Vertex v3, Vertex v4, Color color){
		construct_from_vertices(v1, v2, v3, v4, color);
		super.construct();
	}
	
	public Rectangle(double x1, double y1, double z1, 
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4){
		construct_from_vertices(x1,y1,z1,x2,y2,z2,x3,y3,z3,x4,y4,z4,Color.BLACK);
		super.construct();
	}

	public Rectangle(double x1, double y1, double z1, 
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4, Color color){
		construct_from_vertices(x1,y1,z1,x2,y2,z2,x3,y3,z3,x4,y4,z4,color);
		super.construct();
	}
	
	private void construct_from_vertices(Vertex v1, Vertex v2, Vertex v3, Vertex v4, Color color){
		this.triangles.add(new Triangle(v1, v3, v2, color));
		this.triangles.add(new Triangle(v1, v3, v4, color));
	}
	
	private void construct_from_vertices(
			double x1, double y1, double z1, 
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4, Color color){
		construct_from_vertices(new Vertex(x1,y1,z1), new Vertex(x2,y2,z2), new Vertex(x3,y3,z3), new Vertex(x4,y4,z4), color);
	}
}
