package entities;

import java.awt.Point;

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
}
