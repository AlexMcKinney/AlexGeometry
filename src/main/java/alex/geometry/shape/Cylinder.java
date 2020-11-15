package alex.geometry.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alex.buffer.render.Color;
import alex.geometry.angle.Angle;
import alex.geometry.angle.Degrees;
import alex.geometry.angle.rotate.RayRotator;
import alex.geometry.base.Line;
import alex.geometry.base.Vertex;

/**
 * An approximate cylinder, 2 connected circles
 * */
public class Cylinder extends Shape{

	public Cylinder(Circle top, Circle bottom){
		List<Color> colors = new ArrayList<Color>();
		for(int i=0; i < top.getVertices().size()/2; i++){
			if(i % 2 == 0){
				colors.add(Color.RED);
				//colors.add(Color.RED);
			}
			else{
				//colors.add(Color.BLUE);
				colors.add(Color.BLUE);
			}
		}
		
		construct_from_ends(top, bottom, colors);
		super.construct();
	}
	
	public Cylinder(Vertex v1, Vertex v2, double radius){
		List<Color> sideColors = Arrays.asList(Color.RED, Color.BLUE);
		List<Color> endColors = Arrays.asList(Color.GREEN, Color.YELLOW);
		construct_from_points(v1, v2, radius, sideColors, endColors);
		super.construct();
	}

	public Cylinder(Vertex v1, Vertex v2, double radius, List<Color> sideColors, List<Color> endColors){
		construct_from_points(v1, v2, radius, sideColors, endColors);
		super.construct();
	}
	
	public Cylinder(Vertex v1, Vertex v2, double radius, Color[] sideColors, Color[] endColors){
		construct_from_points(v1, v2, radius, Arrays.asList(sideColors), Arrays.asList(endColors));
		super.construct();
	}
	
	public Cylinder(Vertex v1, Vertex v2, double radius, Color sideColors, Color endColors){
		construct_from_points(v1, v2, radius, Arrays.asList(sideColors, sideColors), Arrays.asList(endColors, endColors));
		super.construct();
	}
	
	public Cylinder(Vertex v1, Vertex v2, double radius, Color color){
		construct_from_points(v1, v2, radius, Arrays.asList(color, color), Arrays.asList(color, color));
		super.construct();
	}
	
	private void construct_from_points(Vertex v1, Vertex v2, double radius, 
			List<Color> sideColors, List<Color> endColors){
		
		Vertex mid = v1.plus(v2).mult(0.5);
		Vertex off = v1.plus(new Line(v1, v2).getRandomNormal());
		
		//arbitrary point on plane at specified distance, rotate to make polygon
		Vertex start1 = mid.movedForth(radius, off);
		int step = 45;
		List<Vertex> list = new ArrayList<Vertex>();
		for(int i=0; i < 360/step; i++){
			Angle angle = new Degrees(step*i);
			Vertex v = RayRotator.rotated(start1, v1, v2, angle);
			list.add(v);
		}
		
		
		//construct polygons and translate them to ends of cylinder
		Circle top = new Circle(Vertex.cloneList(list), endColors.get(0));
		Circle bottom = new Circle(Vertex.cloneList(list), endColors.get(1 % endColors.size()));
		
		top.translate(v1.minus(top.getCenter()));
		bottom.translate(v2.minus(bottom.getCenter()));
		
		construct_from_ends(top, bottom, sideColors);
	}

	private void construct_from_ends(Circle top, Circle bottom, List<Color> colors){
		List<Vertex> list1 = new ArrayList<Vertex>(top.getOuterVertices());
		List<Vertex> list2 = new ArrayList<Vertex>(bottom.getOuterVertices());
				
		for(int i=0; i < list1.size()+1 && i < list2.size()+1; i++){
			Vertex v1a = list1.get(i % list1.size());
			Vertex v1b = list1.get((i+1)%list1.size());
			
			Vertex v2a = list2.get(i % list2.size());
			Vertex v2b = list2.get((i+1)%list2.size());
			
			Color color = colors.get(i % colors.size());
			this.addTriangle(new Triangle(v1a, v2b, v1b, color));
			this.addTriangle(new Triangle(v1a, v2b, v2a, color));
		}
		
		this.addTriangles(top);
		this.addTriangles(bottom);
	}
}
