package alex.geometry.camera;

import alex.buffer.render.RenderingCamera;
import alex.buffer.render.RenderableVertex;
import alex.geometry.angle.Angle;
import alex.geometry.angle.rotate.RayRotator;
import alex.geometry.base.Vertex;

/**
 * Represents a movable camera, mostly for controlling the WebGL lookAt() and perspective() functions
 * */
public class Camera extends RenderingCamera{
	
	private Vertex eye = new Vertex(0,0,-1);
	private Vertex aim = new Vertex(0,0,0);
	private Vertex up = new Vertex(0,1,0);
	
	private RayRotator aim_eye, eye_aim;
	
	
	public Camera(){
		this.updateRays();
	}
	
	public Camera(Vertex eye, Vertex aim){
		this.eye = eye;
		this.aim = aim;
		this.updateRays();
	}
	
	public Camera(Vertex eye, Vertex aim, Vertex up){
		this.eye = eye;
		this.aim = aim;
		this.up = up;
		this.updateRays();
	}
	
	public Camera(Camera camera){
		this.eye = camera.eye;
		this.aim = camera.aim;
		this.up = camera.up;
		this.updateRays();
	}
	
	private void updateRays(){
		this.aim_eye = new RayRotator(aim, eye);
		this.eye_aim = new RayRotator(eye, aim);		
	}
	
	public void translate(Vertex offset){
		this.eye.translate(offset);
		this.aim.translate(offset);
	}
	
	
	public void orbitLeft(Angle angle){
		this.aim_eye.rotateLeft(angle);
		
		
		this.updateRays();
		
	}
	
	public void orbitRight(Angle angle){
		this.orbitLeft(angle.negated());
	}
	
	
	public void orbitDown(Angle angle){
		this.aim_eye.rotateDown(angle);
	}
	
	public void orbitUp(Angle angle){
		this.orbitDown(angle.negated());
	}

	
	public void turnLeft(Angle angle){
		this.eye_aim.rotateLeft(angle);
	}
	
	public void turnRight(Angle angle){
		this.turnLeft(angle.negated());
	}
	
	
	public void turnDown(Angle angle){
		this.eye_aim.rotateDown(angle);
	}
	
	public void turnUp(Angle angle){
		this.turnDown(angle.negated());
	}
	
	
	public void moveForth(double dist){
		this.eye_aim.moveForth(dist);
	
	}
	
	public void moveBack(double dist){
		this.moveForth(-dist);
	}
	
	
	public void moveLeft(double dist){
		this.eye_aim.moveLeft(dist);
	}
		
	public void moveRight(double dist){
		this.moveLeft(-dist);
	}
	
	
	public void moveDown(double dist){
		this.eye_aim.moveDown(dist);
	}
	
	public void moveUp(double dist){
		this.moveDown(-dist);
	}
	
	
	public void setEye(Vertex eye){
		this.eye = eye;
		this.updateRays();
	}

	public void setAim(Vertex at){
		this.aim = at;
		this.updateRays();
	}

	public Vertex getAim() {
		return aim;
	}
	
	public Vertex getEye() {
		return eye;
	}
	
	public void setFar(double far){
		this.far = far;
	}


	@Override
	protected RenderableVertex getRenderingEye() {
		return new RenderableVertex(this.eye.x, this.eye.y, this.eye.z);
	}

	@Override
	protected RenderableVertex getRenderingAim() {
		return new RenderableVertex(this.aim.x, this.aim.y, this.aim.z);
	}

	@Override
	protected RenderableVertex getRenderingUp() {
		return new RenderableVertex(this.up.x, this.up.y, this.up.z);
	}
	
	@Override
	public String toString(){
		return this.eye+"=>"+this.aim;
	}
}
