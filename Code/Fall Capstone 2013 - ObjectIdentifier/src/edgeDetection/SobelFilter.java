package edgeDetection;

public class SobelFilter {
	private int width; 
	private int height;
	private int[][] horizontalKernal;
	private int[][] verticalKernal;
	
	public SobelFilter(){
		width = 3;
		height = 3;
		horizontalKernal = new int[3][3];
		verticalKernal = new int[3][3];
		populate();
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getHorizontalValue(int row, int col){
		return horizontalKernal[row][col];
	}
	
	public int getVerticalValue(int row, int col){
		return verticalKernal[row][col];
	}
	
	private void populate(){
		horizontalKernal[0][0] = -1;
		horizontalKernal[0][1] = 0;
		horizontalKernal[0][2] = 1;
		horizontalKernal[1][0] = -2;
		horizontalKernal[1][1] = 0;
		horizontalKernal[1][2] = 2;
		horizontalKernal[2][0] = -1;
		horizontalKernal[2][1] = 0;
		horizontalKernal[2][2] = 1;
		
		verticalKernal[0][0] = 1;
		verticalKernal[0][1] = 2;
		verticalKernal[0][2] = 1;
		verticalKernal[1][0] = 0;
		verticalKernal[1][1] = 0;
		verticalKernal[1][2] = 0;
		verticalKernal[2][0] = -1;
		verticalKernal[2][1] = -2;
		verticalKernal[2][2] = -1;
	}
}
