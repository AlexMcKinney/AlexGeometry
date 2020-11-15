package alex.geometry.base;

import java.util.ArrayList;
import java.util.List;

import alex.geometry.AlexMatrix;

/**
 * An infinite plane, only use for static vertices
 * */
public class CachedPlane extends Plane{
	//efficiency in matrix calculations
	private List<Vertex> someVertices = new ArrayList<Vertex>();
	
	
	public CachedPlane(List<Vertex> points){
		this(points.get(0), points.get(1), points.get(2));
	}
	
	public CachedPlane(Vertex v1, Vertex v2, Vertex v3){
		this.a = (v2.y - v1.y)*(v3.z - v1.z) - (v3.y - v1.y)*(v2.z - v1.z);
		this.b = (v2.z - v1.z)*(v3.x - v1.x) - (v3.z - v1.z)*(v2.x - v1.x);
		this.c = (v2.x - v1.x)*(v3.y - v1.y) - (v3.x - v1.x)*(v2.y - v1.y);
		this.d = -(this.a*v1.x + this.b*v1.y + this.c*v1.z);
		this.someVertices.add(v1);
		this.someVertices.add(v2);
		this.someVertices.add(v3);
	}
	
	public List<Vertex> getCachedVertices(){
		return this.someVertices;
	}

	public Vertex getIntersection(Line line){
		//l.n != 0 : expected 1 point intersection
		double dot = line.direction.dot(this.getNormalVector());
		if(Math.abs(dot) < 0.0000000001){
			//parallel or no intersection, just return null
			return null;
		}
		
		Vertex a = line.getFirstVertex(), b = line.getSecondVertex();
		Vertex p0 = this.someVertices.get(0);
		Vertex p1 = this.someVertices.get(1);
		Vertex p2 = this.someVertices.get(2);
		
		double[][] array = new double[3][3];
		//t [v1 - v2, p2 - p1, p3 - p1]^-1 [v1 - p1]
		//u
		//v
				
		array[0][0] = a.x - b.x;
		array[0][1] = p1.x - p0.x;
		array[0][2] = p2.x - p0.x;

		array[1][0] = a.y - b.y;
		array[1][1] = p1.y - p0.y;
		array[1][2] = p2.y - p0.y;
		
		array[2][0] = a.z - b.z;
		array[2][1] = p1.z - p0.z;
		array[2][2] = p2.z - p0.z;
		
		double[][] array2 = new double[3][1];
		
		array2[0][0] = a.x - p0.x;
		array2[1][0] = a.y - p0.y;
		array2[2][0] = a.z - p0.z;
		
		
		AlexMatrix am1 = new AlexMatrix(array);
		AlexMatrix inv = am1.inverse();
		AlexMatrix am2 = new AlexMatrix(array2);
		AlexMatrix tuv = inv.mult(am2);
		
		//intersection is Pa + t*(Pb - Pa)		
		double t = tuv.get(0, 0);
		Vertex mod = b.minus(a).mult(t);
		return a.plus(mod);
	}
	
}
