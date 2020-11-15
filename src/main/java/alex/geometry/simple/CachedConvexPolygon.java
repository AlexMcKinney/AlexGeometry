package alex.geometry.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import alex.geometry.base.CachedPlane;
import alex.geometry.base.Line;
import alex.geometry.base.Vertex;

public class CachedConvexPolygon extends Polygon{
	
	private Vertex center;
	private double maxRadius = 0;
	
	public CachedConvexPolygon(List<Vertex> vertices) {
		super(vertices);
		this.setupCache();
	}
	
	private void setupCache(){
		this.center = Vertex.average(vertices);
		for(Vertex v : vertices){
			double dist = center.dist(v);
			if(dist > maxRadius){
				maxRadius = dist;
			}
		}
	}
	
	public List<Vertex> getClosestVertices(final Vertex v){
		List<Vertex> sorted = new ArrayList<Vertex>(this.vertices);
		Collections.sort(sorted, new Comparator<Vertex>(){
			@Override
			public int compare(Vertex arg0, Vertex arg1) {
				return Double.compare(arg0.dist(v), arg1.dist(v));
			}
		});
		return sorted;
	}
	
	public Vertex getIntersection(Line line){
		CachedPlane plane = new CachedPlane(this.vertices);
		Vertex v = plane.getIntersection(line);
		if(v == null){
			return null;
		}
		//v intersects the infinte plane, maybe not the finite polygon
		
		//quick filter, nowhere near the polygon
		if(v.dist(this.center) > this.maxRadius){
			return null;
		}
		
		List<Vertex> corners = this.getClosestVertices(v);
		Line edge = new Line(corners.get(0), corners.get(1));
		Vertex inter = edge.getClosestPoint(v);
		
		if(center.dist(v) < center.dist(inter)){
			return v;
		}
		else{
			return null;
		}
	}
}
