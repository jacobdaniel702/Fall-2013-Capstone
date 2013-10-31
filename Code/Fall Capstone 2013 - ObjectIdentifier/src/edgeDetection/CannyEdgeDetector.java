package edgeDetection;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

public class CannyEdgeDetector {

	private GaussianFilter gaussFilter;
	private BufferedImage initialImage;
	private BufferedImage filterImage;
	
	public CannyEdgeDetector(BufferedImage img){
		gaussFilter = new GaussianFilter();
		initialImage = img;
		applyGaussFilter();
	}
	
	private void applyGaussFilter(){
		for(int i = 0; i < initialImage.getHeight(); i++){
			for(int j = 0; j < initialImage.getWidth(); j++){
				
				int averagePixelValue = getAveragePixelValue(i,j);
			}
		}
	}
	
	private int getAveragePixelValue(int row, int col){
		double sum = 0;
	
		//evaluate middle term(R2C2)
		sum += (gaussFilter.getCoefficient()*gaussFilter.getValue(2,2)) * 
				convertToGrayScale(new Color(initialImage.getRGB(row,col)));
		
		//evaluate R2C1
		if(col-1>=0){
			sum += (gaussFilter.getCoefficient()*gaussFilter.getValue(2,1)) *
					convertToGrayScale(new Color(initialImage.getRGB(row,col-1)));
		}
		
		//evaluate R1C2
		if(row-1>=0){
			sum += (gaussFilter.getCoefficient()*gaussFilter.getValue(1,2)) * 
					convertToGrayScale(new Color(initialImage.getRGB(row-1,col)));
		}
	}
	
	private int convertToGrayScale(Color rgb){
		return (int) Math.round(.299*rgb.getRed() + .587*rgb.getGreen() 
				+ .114*rgb.getBlue());
	}
}
