package alex.geometry.base;

/**
 * A constant vector force
 * */
public class Force{
	private Vector vector;
	private boolean active = true;
	
	
	public Force(double x, double y, double z){
		this.vector = new Vector(x,y,z);
	}
	
	public Force(Vector vector){
		this.vector = vector;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Force plus(Force force){
		return new Force(this.vector.plus(force.vector));
	}
	
	public Vertex asVertex(){
		return new Vertex(this.vector.x, this.vector.y, this.vector.z);
	}
	
	public Force reversed(){
		return new Force(this.vector.reversed());
	}
	
	public Force mult(double d){
		return new Force(this.vector.mult(d));
	}

	public Vector getEffect(Vertex point) {
		if(this.active == true){
			return this.vector;
		}
		else{
			return new Vector(0,0,0);
		}
	}

	public Force getNewForce(Vertex point, Force oldForce) {
		return new Force(oldForce.vector.plus(this.getEffect(point)));
	}
	
	@Override
	public String toString(){
		return this.asVertex().toString();
	}
}
