package alex.geometry.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alex.buffer.render.Color;
import alex.geometry.angle.Degrees;
import alex.geometry.angle.rotate.RayRotator;
import alex.geometry.base.Vertex;

/**
 * Representing an approximated circle, a 2-d shape with any number of edges
 * */
public class Circle extends Shape{

	//convenient to identify the center point where all the triangles meet
	private Vertex centerPointer;
	//need a reliable way to get the outer edges
	private List<Vertex> outer = new ArrayList<Vertex>();
	
	public Circle(List<Vertex> vertices){
		Color color = Color.BLACK;
		construct_from_vertices(vertices, color);
		super.construct();
		this.assignCenterPointer(Vertex.average(vertices));
	}
	
	public Circle(List<Vertex> vertices, Color color){
		construct_from_vertices(vertices, color);
		super.construct();
		this.assignCenterPointer(Vertex.average(vertices));
	}
	
	public Circle(Vertex[] vertices, Color color){
		construct_from_vertices(Arrays.asList(vertices), color);
		super.construct();
		this.assignCenterPointer(Vertex.average(vertices));
	}
	
	//level: number of sub-sections * 4 (level=1 makes a 4-sided polygon)
	public Circle(Vertex center, double radius, int level, Color color){
		construct_from_radius(center, radius, 360/level, color);
		super.construct();
		this.assignCenterPointer(center);
	}
	
	/**
	 * Build an approximate circle (perpendicular to the Y-axis) with a given approximation level
	 * */
	private void construct_from_radius(Vertex center, double radius, 
			int interval, Color color){
		
		//start with a "+" shape, then split each corner into multiple triangles
		Vertex start = center.plus(radius,0,0);
		
		//Rotation current = new Rotation(start, center);
		//Rotation next = new Rotation(start, center);
		//next.rotateLeft(new Degrees(interval));
		RayRotator current = new RayRotator(start.clone(), center.clone());
		RayRotator next = new RayRotator(start.clone(), center.clone());
		next.rotateLeft(new Degrees(interval));
		
		for(int i=0; i <= 360/interval; i++){
			//Vertex v1 = current.getVertex();
			//Vertex v2 = next.getVertex();
			Vertex v1 = current.getEndPoint();
			Vertex v2 = next.getEndPoint();
			if(i == 360/interval){
				v2 = start;
			}
			this.addTriangle(new Triangle(v1, center, v2, color));
			
			//current.rotateLeft(new Degrees(interval));
			//next.rotateLeft(new Degrees(interval));
			
			current.rotateLeft(new Degrees(interval));
			next.rotateLeft(new Degrees(interval));
			
			this.outer.add(v1);
		}
	}

	private void construct_from_vertices(List<Vertex> vertices, Color color){
		Vertex center = Vertex.average(vertices);
		for(int i=0; i < vertices.size(); i++){
			Vertex v1 = vertices.get(i);
			Vertex v2 = vertices.get((i+1)%vertices.size());
			this.addTriangle(new Triangle(v1, center, v2, color));
			
			this.outer.add(v1);
		}
	}
	
	//get vertices except middle point
	public List<Vertex> getOuterVertices(){
		return this.outer;
	}
	
	
	private void assignCenterPointer(Vertex center){
		for(Vertex v : this.vertices){
			if(center.dist(v) < 1.0E-10){
				this.centerPointer = v;
				return;
			}
		}
		System.err.println("WARNING Circle::assignCenterPointer() no center assigned");
	}
	
	public double calcRadius(){
		Vertex c = this.getCenter();
		Vertex v = c;
		for(Vertex temp : this.vertices){
			if(temp != c){
				v = temp;
				break;
			}
		}
		return c.dist(v);
	}
}
