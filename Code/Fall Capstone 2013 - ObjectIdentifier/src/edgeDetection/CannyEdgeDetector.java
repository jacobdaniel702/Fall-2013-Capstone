package edgeDetection;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CannyEdgeDetector {

	private GaussianFilter gaussFilter;
	private SobelFilter sobelFilter;
	private BufferedImage initialImage;
	private BufferedImage blurImage;
	private double[][] gradientMagnitude;
	private double[][] gradientAngle;
	
	public CannyEdgeDetector(BufferedImage img){
		gaussFilter = new GaussianFilter();
		sobelFilter = new SobelFilter();
		initialImage = img;
		blurImage = new BufferedImage(img.getWidth(),img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		gradientMagnitude = new double[blurImage.getHeight()][blurImage.getWidth()];
		gradientAngle = new double[blurImage.getHeight()][blurImage.getWidth()];
		applyGaussFilter();
		computeGradient();
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
	
	private void computeGradient(){
		for(int i = 0; i < blurImage.getHeight(); i++){
			for(int j = 0; j < blurImage.getWidth(); j++){
				int[] pixelGradient = computeSobelConvolution(i,j);
				gradientMagnitude[i][j] = Math.sqrt(Math.pow(pixelGradient[0], 2) + Math.pow(pixelGradient[1], 2));
				gradientAngle[i][j] = Math.atan2(pixelGradient[1], pixelGradient[0]);
			}
		}
		
		BufferedImage gradientImage = new BufferedImage(blurImage.getWidth(), blurImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = gradientImage.getRaster();
		int[] iArray = new int[1];
		
		for(int i = 0; i < gradientMagnitude.length; i++){
			for(int j = 0; j < gradientMagnitude[0].length; j++){
				iArray[0] = (int)gradientMagnitude[i][j];
				raster.setPixel(i, j, iArray);
			}
		}
		
		try{
			File outputFile = new File("images/objects/gradientFace.jpg");
			ImageIO.write(gradientImage,"jpg",outputFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private int[] computeSobelConvolution(int row, int col){
		int gx = 0 , gy = 0;
		WritableRaster raster = blurImage.getRaster();
		for(int i = 1; i >=0; i--){
			for(int j = 1; j >=0; j--){
				if(row-i >=0 && col-j >=0){
					gx += sobelFilter.getHorizontalValue(1-i, 1-j)*raster.getPixel(row, col, new int[1])[0];
					gy += sobelFilter.getVerticalValue(1-i, 1-j)*raster.getPixel(row,col,new int[1])[0];
				}
				else if(row+i<blurImage.getHeight() && col+j<blurImage.getWidth()){
					gx += sobelFilter.getHorizontalValue(1+i, 1+j)*raster.getPixel(row, col, new int[1])[0];
					gy += sobelFilter.getVerticalValue(1+i, 1+j)*raster.getPixel(row, col, new int[1])[0];
				}
			}
		}
		
		return new int[]{gx,gy};
	}
	
	public BufferedImage getBlurredImage(){
		return blurImage;
	}
	
	private int getAveragePixelValue(int row, int col){
		double sum = 0.0;
		
		for(int i = 2; i >=0; i--){
			for(int j = 2; j >=0; j--){
				if(row-i >=0 && col-j >=0){
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
