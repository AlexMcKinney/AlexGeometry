package alex.geometry.shape;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import alex.buffer.render.Color;
import alex.buffer.render.RenderableVertex;
import alex.buffer.render.triangle.TriangleInterface;
import alex.geometry.base.CachedPlane;
import alex.geometry.base.Line;
import alex.geometry.base.Vertex;

public class Triangle extends Shape implements TriangleInterface{
	private Vertex v1, v2, v3;
	private Color color = Color.BLACK;
	
	
	public Triangle(Vertex v1, Vertex v2, Vertex v3){
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		super.construct();
	}
	
	public Triangle(Vertex v1, Vertex v2, Vertex v3, Color color){
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
		super.construct();
	}
	
	public Triangle(TriangleInterface tri){
		this.v1 = new Vertex(tri.getFirstVertex());
		this.v2 = new Vertex(tri.getSecondVertex());
		this.v3 = new Vertex(tri.getThirdVertex());
		this.color = tri.getFirstColor();
		super.construct();
	}
	
	@Override
	public Triangle clone(){
		return new Triangle(this);
	}
	
	@Override
	public void setOpacity(double op){
		this.color.o = op;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	@Override
	public Set<Vertex> getVertices(){
		return new HashSet<Vertex>(Arrays.asList(this.v1, this.v2, this.v3));
	}
	
	@Override
	public List<Triangle> getTriangles(){
		return Arrays.asList(this);
	}
	
	@Override
	public String toString(){
		return "["+this.v1+" "+this.v2+" "+this.v3+"]";
	}
	
	//return the centroid--the intersection of any 2 median lines
	@Override
	public Vertex getCenter(){
		
		try{
			//use intersection line-plane (not line-line) to avoid rounding problems
			Vertex mid12 = Vertex.average(Arrays.asList(v1, v2));
			Vertex mid23 = Vertex.average(Arrays.asList(v2, v3));
			
			//List<Vertex> plane = Arrays.asList(mid12, v3, v3.plus(new Vertex(1,1,1)));
			
			CachedPlane plane = new CachedPlane(mid12, v3, v3.plus(new Vertex(1,1,1)));
			return plane.getIntersection(new Line(v1, mid23));
		}
		catch(Exception ex){
			System.out.println("ERROR Triangle::getCenter() "+ex+" "+this.vertices);
		}
		
		return Vertex.average(this.vertices);
	}
	
	@Override
	public RenderableVertex getFirstVertex() {
		return this.v1.toRenderable();
	}

	@Override
	public RenderableVertex getSecondVertex() {
		return this.v2.toRenderable();
	}

	@Override
	public RenderableVertex getThirdVertex() {
		return this.v3.toRenderable();
	}

	@Override
	public Color getFirstColor() {
		return this.color;
	}

	@Override
	public Color getSecondColor() {
		return this.color;
	}

	@Override
	public Color getThirdColor() {
		return this.color;
	}
}
