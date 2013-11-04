package edgeDetection;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class CannyEdgeDetector {

	private GaussianFilter gaussFilter;
	private BufferedImage initialImage;
	private BufferedImage blurImage;
	
	public CannyEdgeDetector(BufferedImage img){
		gaussFilter = new GaussianFilter();
		initialImage = img;
		blurImage = new BufferedImage(img.getWidth(),img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		applyGaussFilter();
	}
	
	private void applyGaussFilter(){		
		WritableRaster raster = blurImage.getRaster();
		int[] iArray = new int[1];
		
		for(int i = 0; i < initialImage.getHeight(); i++){
			for(int j = 0; j < initialImage.getWidth(); j++){
				int averagePixelValue = getAveragePixelValue(i,j);
				iArray[0] = averagePixelValue;
				raster.setPixel(i,j,iArray);
				
			}
		}
	}
	
	public BufferedImage getBlurredImage(){
		return blurImage;
	}
	
	private int getAveragePixelValue(int row, int col){
		double sum = 0.0;
		
		for(int i = 2; i >=0; i--){
			for(int j = 2; j >=0; j--){
				if(row-i>=0 && col-j >=0){
					sum += (gaussFilter.getCoefficient()*gaussFilter.getValue(2-i,2-j))*
							convertToGrayScale(new Color(initialImage.getRGB(row,col)));
				}
				else if(row+i<initialImage.getHeight() && col+j<initialImage.getWidth()){
					sum += (gaussFilter.getCoefficient()*gaussFilter.getValue(2+i,2+j))*
							convertToGrayScale(new Color(initialImage.getRGB(row,col)));
				}
			}
		}
		
		return (int)Math.ceil(sum);
	}
	
	private int convertToGrayScale(Color rgb){
		int grayScale = (int) Math.ceil(.299*rgb.getRed() + .587*rgb.getGreen() 
				+ .114*rgb.getBlue());
		return grayScale;
	}
}
