package alex.geometry.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import alex.buffer.render.Color;
import alex.geometry.base.Vertex;
public class Prism extends Shape{

	//assumed to be in a precise order
	public Prism(Vertex nbl, Vertex ntl, Vertex ntr, Vertex nbr, 
			Vertex fbl, Vertex ftl, Vertex ftr, Vertex fbr){
		
		Color[] colors = Color.colors6;
		construct_from_corners(nbl, ntl, ntr, nbr, fbl, ftl, ftr, fbr, colors);
		super.construct();
	}
	
	//assumed to be in a precise order
	public Prism(Vertex nbl, Vertex ntl, Vertex ntr, Vertex nbr, 
			Vertex fbl, Vertex ftl, Vertex ftr, Vertex fbr, Color color){
		
		Color[] colors = new Color[]{color, color, color, color, color, color};
		construct_from_corners(nbl, ntl, ntr, nbr, fbl, ftl, ftr, fbr, colors);
		super.construct();
	}
	
	//assumed to be opposite sides
	public Prism(Rectangle side1, Rectangle side2, Color color){
		//Color[] colors = new Color[]{color, color, color, color, color, color};
		Color[] colors = Color.makeSpectrum(color, new double[]{1.0, 0.9, 0.8, 0.7, 0.6, 0.5});
		construct_from_sides(side1, side2, colors);
		super.construct();
	}
	
	//assumed to be opposite sides
	public Prism(Rectangle side1, Rectangle side2, Color[] colors){
		construct_from_sides(side1, side2, colors);
		super.construct();
	}
	
	//assumed to be opposite sides
	public Prism(Rectangle side1, Rectangle side2){
		Color[] colors = Color.colors6;
		construct_from_sides(side1, side2, colors);
		super.construct();
	}
	
	//assumed to be a cube
	public Prism(Vertex center, double radius){
		Color[] colors = Color.colors6;
		construct_from_center(center, radius, colors);
		super.construct();
	}
	
	//assumed to be a cube
	public Prism(Vertex center, double radius, Color color){
		Color[] colors = new Color[]{color, color, color, color, color, color};
		construct_from_center(center, radius, colors);
		super.construct();
	}
	
	//assumed to be a cube
	public Prism(Vertex center, double radius, Color[] colors){
		construct_from_center(center, radius, colors);
		super.construct();
	}
	
	public Prism(Vertex center, double length, double width, double height, Color color){
		Color[] colors = new Color[]{color, color, color, color, color, color};
		construct_from_center(center, length, width, height, colors);
		super.construct();
	}
	
	public Prism(Vertex center, double length, double width, double height){
		Color[] colors = Color.colors6;
		construct_from_center(center, length, width, height, colors);
		super.construct();
	}
	
	public Prism(Vertex center, double length, double width, double height, Color[] colors){
		construct_from_center(center, length, width, height, colors);
		super.construct();
	}
	
	//!!!try to interpret this as 2 points in parallel planes
	public Prism(Vertex v1, Vertex v2, double radius, Color color){
		Color[] colors = new Color[]{color, color, color, color, color, color};
		construct_from_sides(v1, v2, radius, colors);
		super.construct();
	}

	public Prism(Vertex v1, Vertex v2, double radius){
		Color[] colors = Color.colors6;
		construct_from_sides(v1, v2, radius, colors);
		super.construct();
	}
	
	/**
	 * Creates a List of Triangles given the 2 points in the middle of opposing squares and its radius
	 * */
	private void construct_from_sides(Vertex v1, Vertex v2, double radius, Color[] colors){
		
		//start each Rectangle as parallel to x-axis then transform
		
		Rectangle side1 = new Rectangle(
				v1.plus(-radius, -radius, 0),
				v1.plus(-radius, radius, 0),
				v1.plus(radius, radius, 0),
				v1.plus(radius, -radius, 0)
				);

		Rectangle side2 = new Rectangle(
				v2.plus(-radius, -radius, 0),
				v2.plus(-radius, radius, 0),
				v2.plus(radius, radius, 0),
				v2.plus(radius, -radius, 0)
				);
		
		//see how it works without transform
		
		construct_from_sides(side1, side2, colors);
	}
	
	/**
	 * Creates a List of Triangles given 2 rectangular sides, assumed to be opposite sides
	 * */
	private void construct_from_sides(Rectangle side1, Rectangle side2, Color[] colors){
		side1.setColor(colors[0]);
		side2.setColor(colors[1]);
		
		this.addTriangles(side1);
		this.addTriangles(side2);
		
		List<Vertex> list = new ArrayList<Vertex>(side1.getVertices());
		for(int i=0; i < list.size(); i++){
			//can guess which points to connect to
			Vertex v1 = list.get(i);
			Vertex v2 = list.get((i+1)%list.size());
			
			Vertex near1 = getNearest(v1, side2.getVertices()).get(0);
			Vertex near2 = getNearest(v2, side2.getVertices()).get(0);
			
			Rectangle r = new Rectangle(v1, near1, near2, v2, colors[(i+2)%colors.length]);
			this.addTriangles(r);
		}
	}
	
	/**
	 * Creates a List of Triangles given 8 corners of rectangular sides, assumed to be in a specific order
	 * */
	private void construct_from_corners(Vertex nbl, Vertex ntl, Vertex ntr, Vertex nbr, 
			Vertex fbl, Vertex ftl, Vertex ftr, Vertex fbr, Color[] colors){
		this.addTriangles(Arrays.asList(
				new Triangle(nbl, ntl, nbr, colors[0]), new Triangle(ntl, nbr, ntr, colors[0]),
				new Triangle(fbl, ftl, fbr, colors[1]), new Triangle(ftl, fbr, ftr, colors[1]),
				new Triangle(ntl,ftl,ntr, colors[2]), new Triangle(ftl,ntr,ftr, colors[2]),
				new Triangle(nbl,fbl,nbr, colors[3]), new Triangle(fbl,nbr,fbr, colors[3]),
				new Triangle(fbl,nbl,ftl, colors[4]), new Triangle(nbl,ftl,ntl, colors[4]),
				new Triangle(fbr,nbr,ftr, colors[5]), new Triangle(nbr,ftr,ntr, colors[5])
				));
	}
	
	/**
	 * Creates a List of Triangles given the center of a cube and its radius
	 * */
	private void construct_from_center(Vertex center, double radius, Color[] colors){
		construct_from_center(center, 2.0*radius, 2.0*radius, 2.0*radius, colors);
	}
	
	/**
	 * Creates a List of Triangles given the center of a cube and its sizes
	 * */
	private void construct_from_center(Vertex center, double length, 
			double width, double height, Color[] colors){
		
		//length:Z, width:X, height:Y
		double x = width/2.0, y = height/2.0, z = length/2.0; 
		Vertex nbl = center.plus(-x,-y,-z);
		Vertex ntl = center.plus(-x,y,-z);
		Vertex ntr = center.plus(x,y,-z);
		Vertex nbr = center.plus(x,-y,-z);
		Vertex fbl = center.plus(-x,-y,z);
		Vertex ftl = center.plus(-x,y,z);
		Vertex ftr = center.plus(x,y,z);
		Vertex fbr = center.plus(x,-y,z);
		
		construct_from_corners(nbl, ntl, ntr, nbr, fbl, ftl, ftr, fbr, colors);
	}
	
	public static List<Vertex> getNearest(final Vertex v, Collection<Vertex> list){
		List<Vertex> sorted = new ArrayList<Vertex>(list);
		
		Collections.sort(sorted,new Comparator<Vertex>(){
			@Override
			public int compare(Vertex o1, Vertex o2) {
				double d1 = v.dist(o1);
				double d2 = v.dist(o2);
				if(d1 < d2){
					return -1;
				}
				else if(d2 < d1){
					return 1;
				}
				return 0;
			}
		});
		
		return sorted;
	}
}
