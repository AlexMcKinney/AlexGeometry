package alex.geometry.angle;

/**
 * Abstract parent class that allows trigonometric functions to have an "angle"
 * parameter without having to look up whether it should be in radians or degrees.
 * Also, you can "add" Angles without needing to keep track of which unit it is.
 * 
 * Presumably, in other functions you can input Radians or Degrees objects, 
 * and the method will use the Angle abstract converter to get which unit is needed.
 * 
 * Automatically keeps all angles positive and within proper range 
 * */
public abstract class Angle{
	
	protected double angle;
	
	
	public abstract Angle clone();
	
	/**
	 * Returns anonymous angle in radian units
	 * */
	public abstract double toRadians();

	/**
	 * Returns anonymous angle in degree units
	 * */
	public abstract double toDegrees();
	
	/**
	 * Return TRUE if the angle is undefined
	 * */
	public boolean isNaN(){
		return Double.isNaN(this.angle);
	}
	
	/**
	 * Equivalent to returning a new Angle plus 180 degrees
	 * */
	public abstract Angle reversed();
	
	/**
	 * Returns an Angle with 360 - <this_angle>
	 * */
	public Angle negated(){
		return new Degrees(-this.toDegrees());
	}
	
	/**
	 * Returns a new angle, as this angle plus another one
	 * */
	public abstract Angle plus(Angle angle);
	
	public Angle minus(Angle angle){
		return this.plus(angle.negated());
	}
	
	/**
	 * Returns a new angle, as this angle plus another one, 
	 * assumes double to be of THIS type's units
	 * */
	public abstract Angle plus(double angle);
	
	public abstract Angle mult(double factor);
	
	/**
	 * Add an angle to this one--does not return a copy
	 * */
	public abstract void addAngle(Angle angle);
	
	/**
	 * Keeps the angle positive, and within 0 - 360 or 0 - 2PI
	 * */
	protected abstract void correctAngle();
	
	@Override
	public String toString(){
		return this.angle+"";
	}
	
}
