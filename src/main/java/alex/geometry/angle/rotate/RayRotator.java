package alex.geometry.angle.rotate;

import alex.geometry.angle.Angle;
import alex.geometry.angle.Degrees;
import alex.geometry.angle.Radians;
import alex.geometry.base.Vector;
import alex.geometry.base.Vertex;

/**
 * Holds 2 Vertices (start => end) and calculates the effect of movement and rotation
 * "end" is the one that moves from rotation
 * */
public class RayRotator {
	//Angles currently turned
	//0 left-right is towards +X axis
	protected Angle left_right = new Degrees(0);
	protected Angle down_up = new Degrees(0);
	
	//offset from origin, initial center of the shape
	protected Vertex start;
	protected Vertex end;
	
	
	public RayRotator(Vertex start, Vertex end){
		this.construct(start, end);
	}
			
	public RayRotator(RayRotator rr){
		this.left_right = rr.left_right.clone();
		this.down_up = rr.down_up.clone();
		this.start = rr.start;
		this.end = rr.end;
	}
	
	private void construct(Vertex start, Vertex end){
		this.start = start;
		this.end = end;
		this.left_right = getAlignment(start, end)[0];
		
		this.down_up = getAngle(start,new Vertex(end.x, start.y, end.z),end);
		if(start.y > end.y){
			this.down_up = down_up.negated();
		}
	}
	
	/**
	 * Get the starting point of the ray
	 * */
	public Vertex getStartPoint(){
		return this.start;
	}
	
	/**
	 * Get the end point of the ray
	 * */
	public Vertex getEndPoint(){
		return this.end;
	}
	
	/**
	 * Move the start and end points by this offset
	 * */
	public void translateBy(double x, double y, double z){
		this.start.translate(x, y, z);
		this.end.translate(x, y, z);
	}

	/**
	 * Move the start and end points by this offset
	 * */
	public void translateBy(Vertex offset){
		this.translateBy(offset.x, offset.y, offset.z);
	}
	
	/**
	 * Move the start point to the given position,
	 *  and translate the end point by the difference
	 * */
	public void translateTo(Vertex start){
		Vertex offset = this.start.minus(start);
		this.start.translate(offset);
		this.end.translate(offset);
	}
	
	/**
	 * Move only the end point by this offset
	 * */
	public void translateEndBy(double x, double y, double z){
		this.end.translate(x, y, z);
	}
	
	/**
	 * Move only the end point by this offset
	 * */
	public void translateEndBy(Vertex offset){
		this.translateEndBy(offset.x, offset.y, offset.z);
	}
	
	/**
	 * Move only the start point by this offset
	 * */
	public void translateStartBy(double x, double y, double z){
		this.start.translate(x, y, z);
	}
	
	/**
	 * Move only the start point by this offset
	 * */
	public void translateStartBy(Vertex offset){
		this.translateStartBy(offset.x, offset.y, offset.z);
	}
	
	/**
	 * Move both the ray points "forwards" by the given amount
	 * 	with (start => end) as the movement vector
	 * */
	public void moveForth(double dist){
		this.translateTo(this.start.movedForth(-dist, this.end));
	}
	
	/**
	 * Move both the ray points "left" across the XZ-plane 
	 * 	by the given amount, with (start => end) as the normal vector
	 * */
	public void moveLeft(double dist){
		this.translateTo(this.start.movedForth(dist, this.getLeftRight()[0]));
	}
	
	/**
	 * Move both the ray points down the Y-axis by the given amount
	 * */
	public void moveDown(double dist){
		this.end.y -= dist;
		this.start.y -= dist;
	}
		
	/**
	 * Rotate the end point around the Y-axis at the start point
	 * */
	public void rotateLeft(Angle angle){
		this.left_right.addAngle(angle);
		this.end.moveTo(rotatedY(end, start, angle));
	}

	/**
	 * Rotate the end point around an arbitrary axis in the XZ-plane
	 * 	with (start => end) as the normal vector used to make a tangent line 
	 * */
	public void rotateDown(Angle angle){
		Vertex[] lr = this.getLeftRight();
		this.turnDown(angle, lr[0], start);
	}
	
	/**
	 * Rotate the end point around an arbitrary axis in the XZ-plane
	 * 	with the given 2 points as a line
	 * */
	public void turnDown(Angle angle, Vertex p1, Vertex p2){
		this.down_up.addAngle(angle);
		this.end.moveTo(rotated(this.end, p1, p2, angle));		
	}
	
	/**
	 * Get 2 points that make a line perpendicular to the (start => end) ray
	 * 	that have the same value on the Y-axis 
	 * */
	public Vertex[] getLeftRight(){
		Vertex temp = this.start.plus(1,0,0);
		temp = rotatedY(temp, start, this.left_right);
		return new Vertex[]{
			rotatedY(temp, start, new Degrees(90)),
			rotatedY(temp, start, new Degrees(-90))
		};
	}
	
	/**
	 * Get the point on this line that the given point is closest to
	 * */
	public Vertex getIntersection(Vertex v){
		double h = this.start.dist(v);
		double y = this.getDist(v);
		//x^2 + y^2 = h^2
		double move = Math.sqrt(Math.pow(h, 2) - Math.pow(y, 2));
		return start.movedForth(move, end);
	}
	
	/**
	 * Get the distance from the given point to this line
	 * */
	public double getDist(Vertex v){
		return this.start.dist(v) * Math.sin(getAngle(v, start, end).toRadians());
	}
	
	@Override
	public String toString(){
		return this.getStartPoint()+ " => " + this.getEndPoint()+ " "+this.left_right+" & "+this.down_up;
	}
	
	/******************************************************************************
	* Return the angle between 3 points
	******************************************************************************/
	public static Angle getAngle(Vertex a, Vertex b, Vertex c){
		//System.out.println("RR::getAngle() "+a+" "+b+" "+c);
		if(a.equals(b) || b.equals(c)){
			System.out.println("ERROR getAngleP() two vertices overlap "+a+b+c);
			return new Radians(0);
		}
		
		//vector b=>a
		Vector v = new Vector(a.minus(b));
		//vector b=>c
		Vector w = new Vector(c.minus(b));
		
		//angle = (A*B) / (|A| |B|)
		double top = v.dot(w);
		double bot = v.length() * w.length();
		if(bot == 0.0){
			return new Degrees(0);
		}
		double value = top/bot;
		if(value > 1.0){
			value = 1.0;
		}
		if(value < -1.0){
			value = -1.0;
		}
		return new Radians(Math.acos(value));
	}
	

	/**
	 * Rotates a RenderingVertex by the Z-axis around another RenderingVertex (clockwise from -Z direction)
	 * */
	public static Vertex rotatedZ(Vertex v, Vertex around, Angle ang){
		double dist = Math.sqrt(Math.pow(around.x - v.x,2) + Math.pow(around.y - v.y,2) + 0*Math.pow(around.z - v.z,2));
		Angle a = new Radians(ang), b = new Radians(0);
		if(dist != 0.0){
			b = new Radians(Math.asin((around.y - v.y) / dist));
		}
		double xmult=1.0, ymult=-1.0;
		if(v.x < around.x){
			a = a.negated();
			xmult *= -1.0;
		}
		Angle ab = new Radians(a);
		ab.addAngle(b);
		
		double dx = dist * Math.cos(ab.toRadians()), dy = dist * Math.sin(ab.toRadians());
		double x2 = around.x + dx*xmult;
		double y2 = around.y + dy*ymult;
		double z2 = v.z;
		return new Vertex(x2,y2,z2);
	}

	/**
	 * Rotates a RenderingVertex by the Y-axis around another RenderingVertex (clockwise from +Y direction)
	 * */
	public static Vertex rotatedY(Vertex v, Vertex around, Angle ang){
		double dist = Math.sqrt(Math.pow(around.x - v.x,2) + 0*Math.pow(around.y - v.y,2) + Math.pow(around.z - v.z,2));
		Angle a = new Radians(ang), b = new Radians(0);
		if(dist != 0.0){
			b = new Radians(Math.asin((around.z - v.z) / dist));
		}
		double xmult=1.0, zmult=-1.0;
		if(v.x < around.x){
			a = a.negated();
			xmult *= -1.0;
		}
		Angle ab = new Radians(a);
		ab.addAngle(b);
		
		double dx = dist * Math.cos(ab.toRadians()), dz = dist * Math.sin(ab.toRadians());
		double x2 = around.x + dx*xmult;
		double z2 = around.z + dz*zmult;
		double y2 = v.y;
		return new Vertex(x2,y2,z2);
	}

	/**
	 * Rotates a RenderingVertex by the X-axis around another RenderingVertex (clockwise from +X direction)
	 * */
	public static Vertex rotatedX(Vertex v, Vertex around, Angle ang){
		double dist = Math.sqrt(Math.pow(around.z - v.z,2) + Math.pow(around.y - v.y,2) + 0*Math.pow(around.x - v.x,2));
		Angle a = new Radians(ang), b = new Radians(0);
		if(dist != 0.0){
			b = new Radians(Math.asin((around.y - v.y) / dist));
		}
		double zmult=1.0, ymult=-1.0;
		if(v.z < around.z){
			a = a.negated();
			zmult *= -1.0;
		}
		Angle ab = new Radians(a);
		ab.addAngle(b);
		
		double dz = dist * Math.cos(ab.toRadians()), dy = dist * Math.sin(ab.toRadians());
		double z2 = around.z + dz*zmult;
		double y2 = around.y + dy*ymult;
		double x2 = v.x;
		return new Vertex(x2,y2,z2);
	}

	/**
	* Rotate a point around another, using two other points as references to the origin
	* @param deg Degrees to turn
	* @param rotate The RenderingVertex to rotate
	* @param around The RenderingVertex to rotate around
	* @param align1 The RenderingVertex to consider the source point
	* @param align2 The RenderingVertex to consider the target point
	* @return Rotated point
	*/
	public static Vertex rotatedUp(Angle ang, Vertex rotate, Vertex around, Vertex align1, Vertex align2){
		//simply align with an axis on origin, rotate along that, then undo
		Vertex offset = new Vertex(around);
		Angle[] angles = getAlignment(align1.minus(offset), align2.minus(offset));
		Vertex a = around.minus(offset);
		
		//align, rotate, then undo
		Vertex t = rotate.minus(offset);
		t = rotatedY(t, a, angles[0].negated());
		t = rotatedZ(t, a, ang);
		t = rotatedY(t, a, angles[0]).plus(offset);
		return t;
	}
	
	public static Vertex rotatedUp(Vertex rotate, Vertex around, Angle ang){
		return rotatedUp(ang, rotate, around, rotate, around);
	}

	/**
	 * Get a RenderingVertex rotated around an arbitrary axis
	 * @param rotate The RenderingVertex to rotate
	 * @param line1 A RenderingVertex that defines one point on the axis
	 * @param line2 A RenderingVertex that defines another point on the axis
	 * @param angle The angle to rotate
	 * @return a rotated copy of the RenderingVertex
	 * */
	public static Vertex rotated(Vertex rotate, Vertex line1, Vertex line2, Angle angle){
		//println("rotated() "+rotate+" by "+angle+" over "+line1+" & "+line2+"");
		Vector uvw = new Vector(line2, line1).normalized();
		
		//point xyz, line through abc, with direction unit vector uvw
		double x = rotate.x, y = rotate.y, z = rotate.z;		
		double a = line1.x, b = line1.y, c = line1.z;
		double u = uvw.x, v = uvw.y, w = uvw.z;		
		double u2 = u*u, v2 = v*v, w2 = w*w;
		
		double bv = b*v, cw = c*w, ux = u*x, vy = v*y, wz = w*z, cv = c*v, bw = b*w, wy = w*y, vz = v*z;
		double au = a*u, cu = c*u, aw = a*w, wx = w*x, uz = u*z, bu = b*u, av = a*v, vx = v*x, uy = u*y;
				
		double sin = Math.sin(angle.toRadians());
		double cos = Math.cos(angle.toRadians());
		
		double xxx = (a*(v2 + w2) - u*(bv + cw - ux - vy - wz))*(1.0 - cos) + x*cos + (-cv + bw - wy + vz)*sin;
		double yyy = (b*(u2 + w2) - v*(au + cw - ux - vy - wz))*(1.0 - cos) + y*cos + (cu - aw + wx - uz)*sin;
		double zzz = (c*(u2 + v2) - w*(au + bv - ux - vy - wz))*(1.0 - cos) + z*cos + (-bu + av - vx + uy)*sin;
		
		//println("return "+new RenderingVertex(xxx, yyy, zzz));
		return new Vertex(xxx, yyy, zzz);
	}

	/******************************************************************************
	* Return angles needed for a vector to be parallel to axes (x,y,z).
	* Vector is expressed by given 2 points
	* @param start The first point in the vector
	* @param end The second point in the vector
	* @return an array holding (xAngle, yAngle, zAngle)
	******************************************************************************/
	public static Angle[] getAlignment(Vertex start, Vertex end){
		//println("RenderingVertex::getAlignmentP() "+start+" "+end);
		Angle angx = new Degrees(0), angy = new Degrees(0), angz = new Degrees(0);
		
		//sometimes points on top of each other cause problems with x/z alignment
		double limit = 1.0E-15;
		if(Math.abs(start.x - end.x) < limit && Math.abs(start.z - end.z) < limit){
			if(start.y > end.y){
				angy = new Degrees(180);
			}
			return new Angle[]{angx, angy, angz};
		}		
		
		Vertex a, b, c;
	
		//angle from being parallel to y-axis
		a = new Vertex(start.x, start.y+1, start.z);
		b = new Vertex(start);
		c = new Vertex(end);
		angy = getAngle(a, b, c);

		//angle from being parallel to x-axis
		a = new Vertex(start.x+1, 0, start.z);
		b = new Vertex(start.x, 0, start.z);
		c = new Vertex(end.x, 0, end.z);
		angx = getAngle(a, b, c);
		
		//angle from being parallel to z-axis
		a = new Vertex(start.x, 0, start.z+1);
		b = new Vertex(start.x, 0, start.z);
		c = new Vertex(end.x, 0, end.z);
		angz = getAngle(a, b, c);
		
		//arc-cosine only returns 0 -180, may need to account for that
		if(start.x > end.x){
			angz = angz.negated();
		}
		if(start.z < end.z){
			angx = angx.negated();
		}		
		
		return new Angle[]{angx, angy, angz};
	}
}
