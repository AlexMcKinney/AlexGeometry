package alex.geometry.shape;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import alex.buffer.Bufferable;
import alex.buffer.render.Color;
import alex.buffer.render.triangle.RenderingTriangle;
import alex.buffer.render.triangle.WrapperTriangle;
import alex.geometry.base.Vertex;

public abstract class Shape implements Bufferable{

	//distance at which 2 Vertexes are considered "equal"
	public static double DOUBLE_LIMIT = Math.pow(2, -1000);

	
	//this should wrap the Triangle pointers and thus not need to be updated
	//changes in the Triangles it wraps will be automatically reflected
	protected List<RenderingTriangle> renderingTriangles = new ArrayList<RenderingTriangle>();
		
	//tessellated representation, with one color per triangle 
	protected List<Triangle> triangles = new ArrayList<Triangle>();
		
	//points in those triangles to be moved or rotated at will, and a color for each
	//should be stored as a Set, but might need to maintain order
	protected LinkedHashSet<Vertex> vertices = new LinkedHashSet<Vertex>();
		
	protected Vertex center;
		
	//a brightness level *** one for each vertex--not one for each Shape
	//protected List<Double> lighting = new ArrayList<Double>();
	
	

	
	/**
	 * Will reconstruct new private Triangles and Vertexes using the given outside Triangles
	 * */
	protected void construct(){
		for(Triangle tri : this.triangles){
			this.renderingTriangles.add(new WrapperTriangle(tri));
			this.vertices.addAll(tri.getVertices());
		}
				
		this.center = Vertex.average(this.vertices);
		/*
		//collect all the vertices in the triangles, and make private pointers
		List<Vertex> all = new ArrayList<Vertex>();
		for(int i=0; i < webGLTriangles.size(); i++){
			//all.addAll(webGLTriangles.get(i).getVertices());
			for(RenderableVertex tr : webGLTriangles.get(i).getVertices()){
				all.add(new Vertex(tr.x, tr.y, tr.z));
			}
		}
		
		this.vertices = makeNewVertexSet(all);
		if(this.vertices.size() < 3){
			println("WARNING Shape::construct() ("+this.getClass()+") too few vertices ("+this.vertices.size()+") made from "+webGLTriangles.size()+" triangles, "+all.size()+" vertices "+all);
		}
		
		//make a replacement for those original pointers with the new private pointers
		List<Vertex> temp = new ArrayList<Vertex>(all);
		for(int i=0; i < temp.size(); i++){
			Vertex v = temp.get(i);
			for(int t=0; t < this.vertices.size(); t++){
				if(Math.abs(v.dist(this.vertices.get(t))) < DOUBLE_LIMIT){
					temp.set(i, this.vertices.get(t));
					break;
				}
			}
		}
		
		this.center = Vertex.average(this.vertices);
				
		//reconstruct new Triangles using new pointers
		for(int i=0; i+2 < temp.size(); i+=3){
			Color color = webGLTriangles.get(i/3).getColors().get(0);
			this.renderingTriangles.add(new AbsoluteTriangle(temp.get(i).toRenderable(), temp.get(i+1).toRenderable(), temp.get(i+2).toRenderable(), color));
		}
		
		//load Lighting
		for(int i=0; i < this.renderingTriangles.size(); i++){
			RenderingTriangle t = this.renderingTriangles.get(i);
			//this.renderingVertices.addAll(t.getVertices());
			this.renderingColors.add(t.getFirstColor());
			this.renderingColors.add(t.getSecondColor());
			this.renderingColors.add(t.getThirdColor());
			
			//default full brightness for each triangle vertex
			this.lighting.add(1.0);
			this.lighting.add(1.0);
			this.lighting.add(1.0);
		}
		*/
	}
	
	protected void addTriangle(Triangle triangle){
		this.triangles.add(triangle);
	}
	
	protected void addTriangles(List<Triangle> triangles){
		this.triangles.addAll(triangles);
	}
	
	protected void addTriangles(Shape shape){
		this.triangles.addAll(shape.getTriangles());
	}

	public void setOpacity(double op){
		for(Triangle tri : this.triangles){
			tri.setOpacity(op);
		}
	}
	
	@Override
	public List<RenderingTriangle> getRenderingTriangles(){
		return this.renderingTriangles;
	}
	
	//need to override to get in a nice order
	public Set<Vertex> getVertices(){
		return this.vertices;
	}
	
	public Vertex getVertex(int index){
		int count=0;
		for(Vertex v: this.vertices){
			if(count == index){
				return v;
			}
			count++;
		}
		return null;
	}
	
	public List<Triangle> getTriangles(){
		return this.triangles;
	}

	public Vertex getCenter(){
		return this.center;
	}
	
	public void setColor(Color color){
		for(Triangle tri : this.triangles){
			tri.setColor(color);
		}
	}
	
	public void translate(Vertex trans){
		for(Vertex v : this.vertices){
			v.translate(trans);
		}
	}
	
	//get a copy of the Vertex on this Shape farthest from the given one
	public Vertex getFarthestFrom(Vertex v){
		List<Vertex> list = new ArrayList<Vertex>(this.vertices);
		
		Vertex far = list.get(0);
		double farDist = far.dist(v);
		
		for(int i=1; i < list.size(); i++){
			Vertex temp = list.get(i);
			double d = temp.dist(v);
			if(d > farDist){
				far = temp;
				farDist = d;
			}
		}
		return far.clone();
	}
	
	//get a copy of the Vertex on this Shape nearest to the given one
	public Vertex getNearestTo(Vertex v){
		List<Vertex> list = new ArrayList<Vertex>(this.vertices);
		
		Vertex near = list.get(0);
		double nearDist = near.dist(v);
		
		for(int i=1; i < list.size(); i++){
			Vertex temp = list.get(i);
			double d = temp.dist(v);
			if(d < nearDist){
				near = temp;
				nearDist = d;
			}
		}
		return near.clone();
	}
	
	/**
	 * Get the nearest collision Vertex with this Shape given a ray, or NULL if none
	 * @param rayStart One vertex of the intersecting ray
	 * @param rayEnd Another vertex of the intersecting ray
	 * @return the Triangle intersection Vertex nearest to the <rayStart>, or NULL if none
	 * */
	/*
	public Vertex getNearestIntersection(final Vertex rayStart, Vertex rayEnd){
		List<Vertex> templist = this.getIntersectionList(rayStart, rayEnd);
		double max = Trig.getDist(rayStart, rayEnd);
		
		List<Vertex> vlist = new ArrayList<Vertex>();
		for(int i=0; i < templist.size(); i++){
			Vertex inter = templist.get(i);
			if(inter != null && Trig.getDist(rayStart, inter) < max && Trig.getDist(rayEnd, inter) < max){
				vlist.add(inter);
			}
		}
		if(vlist.isEmpty()){
			return null;
		}

		Collections.sort(vlist, new Comparator<Vertex>(){
			@Override
			public int compare(Vertex o1, Vertex o2) {
				double d1 = Trig.getDist(rayStart, o1);
				double d2 = Trig.getDist(rayStart, o2);
				if(d1 < d2){
					return -1;
				}
				else if(d2 > d1){
					return 1;
				}
				else{
					return 0;
				}
			}
		});
		return vlist.get(0);
	}
	*/		
	/**
	 * Given a List of Vertexes, construct & return a new set without any overlapping vertices
	 * */
	private static List<Vertex> makeNewVertexSet(List<Vertex> vertices){
		List<Vertex> list = new ArrayList<Vertex>();
		
		for(int i=0; i < vertices.size(); i++){
			Vertex v = vertices.get(i);
			boolean add = true;
			for(int j=0; j < list.size(); j++){
				if(v.dist(list.get(j)) < DOUBLE_LIMIT){
					add = false;
					break;
				}
			}
			if(add == true){
				list.add(v.clone());
			}
		}
		return list;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName()+" Vertices:\n");
		for(Vertex v : this.vertices){
			sb.append(v).append("\n");
		}
		sb.append("\nTriangles:\n");
		for(Triangle tri : this.triangles){
			sb.append(tri).append("\n");
		}
		return sb.toString();
	}
	
	public static List<Triangle> collectTriangles(Shape... shapes){
		List<Triangle> newTriangles = new ArrayList<Triangle>();
		for(Shape shape : shapes){
			newTriangles.addAll(shape.getTriangles());
		}
		return newTriangles;
	}	
	
	protected static boolean between(double min, double test, double max){
		return (test >= min && test <= max);
	}
}
