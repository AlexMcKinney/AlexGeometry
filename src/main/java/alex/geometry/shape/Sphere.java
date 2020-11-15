package alex.geometry.shape;

import java.util.ArrayList;
import java.util.List;

import alex.buffer.render.Color;
import alex.buffer.render.triangle.AbsoluteTriangle;
import alex.geometry.angle.Degrees;
import alex.geometry.angle.rotate.RayRotator;
import alex.geometry.base.Vertex;

public class Sphere extends Shape{
	private static final int DEFAULT_LEVEL = 4;
	
	//not completely necessary, but speeds up calculations to remember these directly
	private double radius;
	private Vertex center;
	
	public Sphere(Vertex center, double radius){
		this.radius = radius;
		this.center = center;
		double interval = 360.0 / DEFAULT_LEVEL;
		Color[] colors = new Color[]{Color.RED, Color.BLUE};
		construct_from_radius(center, radius, interval, colors);
		super.construct();
	}

	public Sphere(Vertex center, double radius, Color[] colors){
		this.radius = radius;
		this.center = center;
		double interval = 360.0 / DEFAULT_LEVEL;
		construct_from_radius(center, radius, interval, colors);
		super.construct();
	}
	
	public Sphere(Vertex center, double radius, int level){
		this.radius = radius;
		this.center = center;
		if(level <= 0){
			return;
		}
		if(level < 4){
			System.out.println("WARNING: cannot have Sphere level below 4: "+level+", defaulting to "+DEFAULT_LEVEL);
			level = DEFAULT_LEVEL;
		}
		double interval = 360.0 / level;
		Color[] colors = new Color[]{Color.RED, Color.BLUE};
		construct_from_radius(center, radius, interval, colors);
		super.construct();
	}
	
	public Sphere(Vertex center, double radius, int level, Color[] colors){
		this.radius = radius;
		this.center = center;
		if(level <= 0){
			return;
		}
		double interval = 360.0 / level;
		construct_from_radius(center, radius, interval, colors);
		super.construct();
	}
	
	private void construct_from_radius(Vertex center, double radius, double interval, Color[] colors){
		//System.out.println("construct Sphere interval "+interval);
		List<AbsoluteTriangle> triangles = new ArrayList<AbsoluteTriangle>();
		
		//mesh of Vertices, like the flattened surface of the sphere
		List<List<Vertex>> mesh = new ArrayList<List<Vertex>>();
		
		//an edge up/down
		List<Vertex> edge = new ArrayList<Vertex>();
		
		RayRotator ray = new RayRotator(center, center.plus(0,radius,0));
		for(int i=0; i <= 180; i += interval){
			edge.add(ray.getEndPoint().clone());
			ray.rotateDown(new Degrees(interval));
		}
		
		//generate a mesh
		for(int i=0; i < 360; i += interval){
			List<Vertex> temp = Vertex.cloneList(edge);
			for(Vertex v : temp){
				ray = new RayRotator(center, v);
				ray.rotateLeft(new Degrees(i));
			}
			mesh.add(temp);
		}
		
		//create squares out of neighboring mesh vertices
		for(int i=0; i < mesh.size(); i++){
			List<Vertex> edge1 = mesh.get(i);
			List<Vertex> edge2 = mesh.get((i+1) % mesh.size());
			
			for(int j=0; j < edge1.size()-1; j++){
				Vertex v1 = edge1.get(j);
				Vertex v2 = edge1.get((j+1) % edge1.size());
				Vertex v3 = edge2.get(j);
				Vertex v4 = edge2.get((j+1) % edge2.size());
				
				//the very top & bottom are triangles
				if(j == 0){
					this.addTriangle(new Triangle(v1, v2, v4, colors[(j+i) % colors.length]));
				}
				else if(j == edge1.size()-2){
					this.addTriangle(new Triangle(v1, v3, v4, colors[(j+i) % colors.length]));
				}
				else{
					this.addTriangles(new Rectangle(v1, v2, v4, v3, colors[(j+i) % colors.length]));
				}
			
			}
		}
	}

	@Override
	public Vertex getCenter(){
		return this.center;
	}
	
	/**
	 * Set a Shape equal to another without changing the pointers
	 * Expected to be the same class
	 * */
	public void moveTo(Shape shape){
		this.center.moveTo(shape.getCenter());
	}
}
