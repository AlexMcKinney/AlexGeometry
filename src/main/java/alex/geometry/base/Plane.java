package alex.geometry.base;

import java.util.List;

/**
 * An infinite plane, suitable for movable vertices
 * */
public class Plane {
	//ax + by + cz + d = 0
	public double a, b, c, d;


	protected Plane(){}
	
	public Plane(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	
	public Plane(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = 0;
	}
	
	public Plane(List<Vertex> points){
		this(points.get(0), points.get(1), points.get(2));
	}
	
	public Plane(Vertex v1, Vertex v2, Vertex v3){
		this.a = (v2.y - v1.y)*(v3.z - v1.z) - (v3.y - v1.y)*(v2.z - v1.z);
		this.b = (v2.z - v1.z)*(v3.x - v1.x) - (v3.z - v1.z)*(v2.x - v1.x);
		this.c = (v2.x - v1.x)*(v3.y - v1.y) - (v3.x - v1.x)*(v2.y - v1.y);
		this.d = -(this.a*v1.x + this.b*v1.y + this.c*v1.z);
	}
		
	public Vector asVector(){
		return new Vector(this.a, this.b, this.c);
	}
	
	//normal vector
	public Vector getNormalVector(){
		return new Vector(this.a, this.b, this.c).normalized();
	}
	
	//normal vector
	public Vector getNormalVectorTo(Vertex v){
		Vector n = this.getNormalVector();
		
		Vertex inter = this.getIntersection(v);
		Vector compare = n.mult(0.00000000001); 
		
		//System.out.println(v+" => "+inter+" ? "+n);
		//System.out.println(inter.plus(compare)+" >< "+inter.minus(compare));
		
		if(v.dist(inter.plus(compare)) < v.dist(inter.minus(compare))){
			return n;
		}
		else{
			return n.reversed();
		}
	}

	public double getDist(Vertex p){
		return Math.abs(this.dot(p) / this.length()); 
	}
	
	public Vertex getIntersection(Vertex v){
		//P = vd / |v|^2
		Vector vec = this.asVector();
		return vec.mult(-d).mult(1.0 / Math.pow(vec.length(), 2)).toVertex();
	}
	
	//shorthand for making a new Vector to operate with
	public double dot(Vertex p){
		return this.a * p.x + this.b * p.y + this.c * p.z + this.d;
	}
	
	//shorthand for making a new Vector to operate with
	public double length(){
		return Math.sqrt(a*a + b*b + c*c);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(a).append("x");
		
		if(b >= 0.0){
			sb.append(" + ").append(b).append("y");
		}
		else{
			sb.append(" - ").append(-b).append("y");
		}
		
		if(c >= 0.0){
			sb.append(" + ").append(c).append("z");
		}
		else{
			sb.append(" - ").append(-c).append("z");
		}
		
		if(d >= 0.0){
			sb.append(" + ").append(d);
		}
		else{
			sb.append(" - ").append(-d);
		}
		
		return sb.toString();
	}
}
