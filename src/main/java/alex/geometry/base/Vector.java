package alex.geometry.base;

public class Vector {
	public double x, y, z;

	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector(Vertex v){
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector(Vertex v1, Vertex v2){
		this.x = v2.x - v1.x;
		this.y = v2.y - v1.y;
		this.z = v2.z - v1.z;
	}
	
	public Vertex toVertex(){
		return new Vertex(x, y, z);
	}
	
	public Vector normalized(){
		if(x == 0.0 && y == 0.0 && z == 0.0){
			return new Vector(x, y, z);
		}
		else{
			return this.mult(1.0 / this.length());
		}
	}

	public Vector reversed(){
		return new Vector(-this.x, -this.y, -this.z);
	}
	
	public Vector plus(Vector v){
		return new Vector(this.x + v.x, this.y + v.y, this.z + v.z);
	} 

	public Vector plus(double x, double y, double z){
		return new Vector(this.x + x, this.y + y, this.z + z);
	} 
	
	public Vector minus(Vector v){
		return this.plus(v.reversed());
	}
	
	public Vector mult(double mult){
		return new Vector(mult*this.x, mult*this.y, mult*this.z);
	}
		
	public double length(){
		return Math.sqrt(this.dot(this));
	}
	
	public double dot(Vector t){
		return (this.x*t.x + this.y*t.y + this.z*t.z);
	}
	
	public double dot(Vertex t){
		return (this.x*t.x + this.y*t.y + this.z*t.z);
	}
	
	//Vector 3-d vectors, cross product
	public Vector cross(Vector v){
		//c1=a2b3-a3b2, c2=a3b1-a1b3, c3=a1b2-a2b1
		return new Vector(this.y*v.z - this.z*v.y, this.z*v.x - this.x*v.z, this.x*v.y - this.y*v.x);		
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(x).append(", ").append(y).append(", ").append(z).append("}");
		return sb.toString();
	}
}
