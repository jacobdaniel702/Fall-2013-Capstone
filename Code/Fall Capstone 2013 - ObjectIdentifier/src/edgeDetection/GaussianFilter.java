package edgeDetection;

public class GaussianFilter {
	private int width;
	private int height;
	private double coefficient;
	private int[][] kernal;
	
	public GaussianFilter(){
		width = 5;
		height = 5;
		coefficient = 1.0/159.0;
		kernal = new int[5][5];
		populate();
	}
	
	public double getCoefficient(){
		return coefficient;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getValue(int row, int col){
		return kernal[row][col];
	}
	
	private void populate(){
		kernal[0][0] = 2;
		kernal[0][1] = 4;
		kernal[0][2] = 5;
		kernal[0][3] = 4;
		kernal[0][4] = 2;
		kernal[1][0] = 4;
		kernal[1][1] = 9;
		kernal[1][2] = 12;
		kernal[1][3] = 9;
		kernal[1][4] = 4;
		kernal[2][0] = 5;
		kernal[2][1] = 12;
		kernal[2][2] = 15;
		kernal[2][3] = 12;
		kernal[2][4] = 5;
		kernal[3][0] = 4;
		kernal[3][1] = 9;
		kernal[3][2] = 12;
		kernal[3][3] = 9;
		kernal[3][4] = 4;
		kernal[4][0] = 2;
		kernal[4][1] = 4;
		kernal[4][2] = 5;
		kernal[4][3] = 4;
		kernal[4][4] = 2;
	}
}
