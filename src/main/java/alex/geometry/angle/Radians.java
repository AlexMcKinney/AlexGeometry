package alex.geometry.angle;


public class Radians extends Angle{
	
	public Radians(double radians){
		this.angle = radians;
		this.correctAngle();
	}
	
	public Radians(Angle angle){
		this.angle = angle.toRadians();
		this.correctAngle();
	}
	
	@Override
	public Radians clone(){
		return new Radians(this.angle);
	}

	@Override
	public Angle plus(Angle angle) {
		if(angle != null){
			return new Radians(this.toRadians() + angle.toRadians());
		}
		System.out.println("WARNING Radians::plus() null input");
		return new Radians(this);
	}
	
	@Override
	public Angle plus(double angle) {
		return new Radians(this.toRadians() + angle);
	}
	
	@Override
	public Angle mult(double factor){
		return new Radians(this.angle*factor);
	}
	
	@Override
	public Angle reversed() {
		return new Radians(this.angle + Math.PI);
	}
	
	@Override
	public double toRadians() {
		return this.angle;
	}

	@Override
	public double toDegrees() {
		return Math.toDegrees(this.angle);
	}

	@Override
	protected void correctAngle() {
		while(this.angle < 0.0){
			this.angle += 2.0*Math.PI;
		}
		while(this.angle >= 2.0*Math.PI){
			this.angle -= 2.0*Math.PI;
		}
	}

	@Override
	public void addAngle(Angle angle) {
		this.angle += angle.toRadians();
		this.correctAngle();
	}

	@Override
	public String toString(){
		//return this.angle+"R";
		return new Degrees(this).toString()+"R";
	}

}
