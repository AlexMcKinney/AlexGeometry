package alex.geometry.base;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import alex.buffer.render.RenderableVertex;

public class Vertex{
	//don't print extremely small numbers
	private static final double PRINT_LIMIT = Math.pow(10, -14);
	
	public static final Vertex ORIGIN = new Vertex(0,0,0);
	
	
	public double x, y, z;
	
	
	public Vertex(double x, double y){
		this.x = x;
		this.y = y;
	}

	public Vertex(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vertex(Vertex v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vertex(RenderableVertex v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public RenderableVertex toRenderable(){
		return new RenderableVertex(x, y, z);
	}
	
	public Vertex clone(){
		return new Vertex(this);
	}
	
	public boolean isNAN(){
		return (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z));
	}
	
	public void translate(Vertex offset){
		this.translate(offset.x, offset.y,  offset.z);
	}
	
	public void translate(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public Vertex translated(Vertex v){
		return this.translated(v.x,  v.y,  v.z);
	}
	
	public Vertex translated(double x, double y, double z){
		Vertex ret = this.clone();
		ret.translate(x,y,z);
		return ret;
	}

	//Vertex 3-d vectors, dot product
	public double dot(Vertex t){
		return (this.x*t.x + this.y*t.y + this.z*t.z);
	}
	
	public double dot(Vector t){
		return (this.x*t.x + this.y*t.y + this.z*t.z);
	}
	
	
	//Vertex 3-d vectors, cross product
	public Vertex cross(Vertex b){
		//c1=a2b3-a3b2, c2=a3b1-a1b3, c3=a1b2-a2b1
		return new Vertex(this.y*b.z - this.z*b.y, this.z*b.x - this.x*b.z, this.x*b.y - this.y*b.x);		
	}
	
	//Vertex 3-d vector, length of v = sqrt(v dot v)
	public double length(){
		return Math.sqrt(this.dot(this));
	}
	
	public double dist(Vertex v){
		return Math.sqrt(Math.pow(this.x - v.x, 2) + Math.pow(this.y - v.y, 2) + Math.pow(this.z - v.z, 2));
	}
	
	public Vertex reversed(){
		return new Vertex(-this.x, -this.y, -this.z);
	}
	
	public Vertex plus(Vertex v){
		return this.translated(v);
	} 

	public Vertex plus(Vector vector){
		return this.plus(vector.toVertex());
	}
	
	public Vertex plus(double x, double y, double z){
		return this.translated(x,y,z);
	} 
	
	public Vertex minus(Vertex v){
		return this.plus(-v.x, -v.y, -v.z);
	}
	
	public Vertex minus(Vector v){
		return this.plus(-v.x, -v.y, -v.z);
	}
	
	public Vertex mult(double mult){
		return new Vertex(mult*this.x, mult*this.y, mult*this.z);
	}

	public Vertex movedForth(double dist, Vertex towards){
		Vector unitv = new Vector(towards.minus(this)).normalized();
		return this.plus(unitv.mult(dist));
	}
	
	public static Vertex average(Vertex[] vertices){
		return average(Arrays.asList(vertices));
	}
	
	public static Vertex average(Collection<Vertex> vertices){
		Vertex total = new Vertex(0,0,0);		
		for(Vertex v: vertices){
			total = total.plus(v);
		}
		return total.mult(1.0/vertices.size());
	}

	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if(Math.abs(x) > PRINT_LIMIT || x == 0.0){
			sb.append(x);
		}
		else{
			sb.append("*0");
		}
		sb.append(", ");
		if(Math.abs(y) > PRINT_LIMIT || y == 0.0){
			sb.append(y);
		}
		else{
			sb.append("*0");
		}
		sb.append(", ");
		if(Math.abs(z) > PRINT_LIMIT || z == 0.0){
			sb.append(z);
		}
		else{
			sb.append("*0");
		}
		sb.append(")");
		return sb.toString();
	}
	
	public void moveTo(Vertex v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public static List<Vertex> cloneList(List<Vertex> list){
		if(list == null){
			return list;
		}
		List<Vertex> cloned = new ArrayList<Vertex>();
		for(int i=0; i < list.size(); i++){
			cloned.add(list.get(i).clone());
		}
		return cloned;
	}
}
