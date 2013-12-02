package shapeContext;

import java.awt.Point;

public class PointToPointCostMapping {
	private Point p;
	private Point q;
	private double cost;
	
	public PointToPointCostMapping(Point p, Point q, double cost){
		this.p = p;
		this.q = q;
		this.cost = cost;
	}
	
	public Point[] getPoints(){
		return new Point[]{p,q};
	}
	
	public double getCost(){
		return cost;
	}
}
