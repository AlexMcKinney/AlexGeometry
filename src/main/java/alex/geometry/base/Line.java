package alex.geometry.base;

public class Line {
	public Vertex offset;
	public Vector direction;
	
	
	public Line(Vertex offset, Vector direction) {
		this.offset = offset;
		this.direction = direction;
	}
	
	public Line(Vertex v1, Vertex v2){
		this.offset = v1;
		this.direction = new Vector(v2.minus(v1));
	}
	
	public Vertex getFirstVertex(){
		return this.offset;
	}
	
	public Vertex getSecondVertex(){
		return this.offset.plus(this.direction);
	}
	
	public Vector getRandomNormal(){
		return new CachedPlane(offset, offset.plus(direction), offset.plus(Math.random(), Math.random(), Math.random())).getNormalVector();
	}
	
	public CachedPlane getRandomPlane(){
		return new CachedPlane(offset, offset.plus(direction), offset.plus(Math.random(), Math.random(), Math.random()));
	}
	
	public double getDist(Vertex v0){
		Vertex v1 = this.getFirstVertex();
		Vertex v2 = this.getSecondVertex();
		//|(x0 - x1)X(x0 - x2)| / |(x2 - x1)|
		return v0.minus(v1).cross(v0.minus(v2)).length() / v2.minus(v1).length();
	}
	
	public Vertex getClosestPoint(Vertex v){
		Vertex p = this.getFirstVertex();
		//x^2 + y^2 = h^2 (find x)
		double move = Math.sqrt(Math.pow(v.dist(p), 2) - Math.pow(this.getDist(v), 2));
		return p.plus(this.direction.normalized().mult(move));
	}
}
