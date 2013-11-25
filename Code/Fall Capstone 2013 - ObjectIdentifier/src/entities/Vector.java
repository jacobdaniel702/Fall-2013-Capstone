package entities;

public class Vector {
	private double magnitude;
	private double direction;
	
	public Vector(double magnitude, double direction){
		this.magnitude = magnitude;
		this.direction = direction;
	}
	
	public double getMagnitude(){
		return magnitude;
	}
	
	public double getDirection(){
		return direction;
	}
	
	public double getXComponent(){
		return magnitude*Math.cos(direction);
	}
	
	public double getYComponent(){
		return magnitude*Math.sin(direction);
	}
}
