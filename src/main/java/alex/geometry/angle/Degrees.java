package alex.geometry.angle;


public class Degrees extends Angle{
	
	public Degrees(double degrees){
		this.angle = degrees;
		this.correctAngle();
	}
	
	public Degrees(Angle angle){
		this.angle = angle.toDegrees();
		this.correctAngle();
	}

	@Override
	public Degrees clone(){
		return new Degrees(this.angle);
	}
	
	@Override
	public Angle reversed() {
		return new Degrees(this.angle + 180.0);
	}
	
	@Override
	public Angle plus(Angle angle) {
		if(angle != null){
			return new Degrees(this.toDegrees() + angle.toDegrees());
		}
		System.out.println("WARNING Degrees::plus() null input");
		return new Degrees(this);
	}
	
	@Override
	public Angle plus(double angle) {
		return new Degrees(this.toDegrees() + angle);
	}

	@Override
	public Angle mult(double factor){
		return new Degrees(this.angle*factor);
	}
	
	@Override
	public double toRadians() {
		return Math.toRadians(this.angle);
	}

	@Override
	public double toDegrees() {
		return this.angle;
	}

	@Override
	protected void correctAngle() {
		while(this.angle < 0.0){
			this.angle += 360.0;
		}
		while(this.angle >= 360.0){
			this.angle -= 360.0;
		}
	}

	@Override
	public void addAngle(Angle angle) {
		this.angle += angle.toDegrees();
		this.correctAngle();
	}

	@Override
	public String toString(){
		return super.toString()+"D";
	}

}
