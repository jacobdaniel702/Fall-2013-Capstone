package shapeContext;

public class Bin {
	private double r;
	private double theta;
	public int count;
	
	public Bin(double r, double theta){
		this.r = r;
		this.theta = theta;
		count = 0;
	}

	public double getR(){
		return r;
	}
	
	public double getTheta(){
		return theta;
	}
	
	public void increment(){
		count++;
	}
	
	public int getCount(){
		return count;
	}
}
