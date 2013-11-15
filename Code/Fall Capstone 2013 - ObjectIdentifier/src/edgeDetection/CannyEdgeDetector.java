package edgeDetection;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class CannyEdgeDetector {

	private GaussianFilter gaussFilter;
	private SobelFilter sobelFilter;
	private BufferedImage initialImage;
	private BufferedImage blurImage;
	private double[][] gradientMagnitude;
	private double[][] gradientAngle;
	private double[][] hysteresisImage;
	private BufferedImage edgeImage;
	private double LOWHYSTERESISTHRESHOLD;
	private double HIGHHYSTERESISTHRESHOLD;
	
	public CannyEdgeDetector(){
		gaussFilter = new GaussianFilter();
		sobelFilter = new SobelFilter();
	}
	
	public BufferedImage processImage(BufferedImage img){
		initialImage = img;
		double meanPixelValue = calculateMeanPixelValue();
		LOWHYSTERESISTHRESHOLD = .5*meanPixelValue;
		HIGHHYSTERESISTHRESHOLD = .8*meanPixelValue;
		blurImage = new BufferedImage(img.getWidth(),img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		gradientMagnitude = new double[blurImage.getHeight()][blurImage.getWidth()];
		gradientAngle = new double[blurImage.getHeight()][blurImage.getWidth()];
		hysteresisImage = new double[blurImage.getHeight()][blurImage.getWidth()];
		edgeImage = new BufferedImage(img.getWidth(),img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		applyGaussFilter();
		computeGradient();
		applyNonMaximumSurpression();
		applyHysteresisThresholding();
		
		return edgeImage;
	}
	
	private double calculateMeanPixelValue(){
		double sum = 0.0;
		
		for(int i = 0; i < initialImage.getHeight(); i++){
			for(int j = 0; j < initialImage.getWidth(); j++){
				sum+=convertToGrayScale(new Color(initialImage.getRGB(i, j)));
			}
		}
		
		return sum/(1.0*initialImage.getHeight()*initialImage.getWidth());
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
//		
//		try{
//			File outputFile = new File("images/objects/blurredApple.jpg");
//			ImageIO.write(blurImage,"jpg",outputFile);
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
	}
	
	private void computeGradient(){
		for(int i = 0; i < blurImage.getHeight(); i++){
			for(int j = 0; j < blurImage.getWidth(); j++){
				int[] pixelGradient = computeSobelConvolution(i,j);
				gradientMagnitude[i][j] = Math.sqrt(Math.pow(pixelGradient[0], 2) + Math.pow(pixelGradient[1], 2));
				gradientAngle[i][j] = Math.atan2(pixelGradient[1], pixelGradient[0]);
			}
		}
		
//		BufferedImage gradientImage = new BufferedImage(blurImage.getWidth(),blurImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//		WritableRaster raster = gradientImage.getRaster();
//		int[] iArray = new int[1];
//		for(int i = 0; i < gradientImage.getHeight(); i++){
//			for(int j = 0; j < gradientImage.getWidth(); j++){
//				iArray[0] = (int) gradientMagnitude[i][j];
//				raster.setPixel(i,j,iArray);
//			}
//		}
//		
//		try{
//			File outputFile = new File("images/objects/gradientApple.jpg");
//			ImageIO.write(gradientImage,"jpg",outputFile);
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
	}
	
	private int[] computeSobelConvolution(int row, int col){
		int gx = 0 , gy = 0;
		WritableRaster raster = blurImage.getRaster();
		for(int i = 1; i >=0; i--){
			for(int j = 1; j >=0; j--){
				if(row-i >=0 && col-j >=0){
					gx += sobelFilter.getHorizontalValue(1-i, 1-j)*raster.getPixel(row-i, col-j, new int[1])[0];
					gy += sobelFilter.getVerticalValue(1-i, 1-j)*raster.getPixel(row-i,col-j,new int[1])[0];
				}
				if(row+i<blurImage.getHeight() && col+j<blurImage.getWidth()){
					gx += sobelFilter.getHorizontalValue(1+i, 1+j)*raster.getPixel(row+i, col+j, new int[1])[0];
					gy += sobelFilter.getVerticalValue(1+i, 1+j)*raster.getPixel(row+i, col+j, new int[1])[0];
				}
			}
		}
		
		return new int[]{gx,gy};
	}
	
	private void applyNonMaximumSurpression(){
		double firstPixel = 0, secondPixel = 0;
		
		for(int i = 0; i < gradientMagnitude.length; i++){
			for(int j = 0; j < gradientMagnitude[0].length; j++){
				if(Math.toDegrees(gradientAngle[i][j]) == 0.0 && i+1 < gradientMagnitude.length && i-1>=0){
					firstPixel = gradientMagnitude[i+1][j];
					secondPixel = gradientMagnitude[i-1][j];
				}
				else if(Math.toDegrees(gradientAngle[i][j]) == 90.0 && j+1 < gradientMagnitude[0].length && j-1>=0){
					firstPixel = gradientMagnitude[i][j+1];
					secondPixel = gradientMagnitude[i][j-1];
				}
				else if(Math.toDegrees(gradientAngle[i][j]) == 45.0 && i+1 < gradientMagnitude.length && j+1 < gradientMagnitude[0].length && i-1>=0 && j-1>=0){
					firstPixel = gradientMagnitude[i+1][j+1];
					secondPixel = gradientMagnitude[i-1][j-1];
				}
				else if(Math.toDegrees(gradientAngle[i][j]) == 135.0 && i+1 < gradientMagnitude.length && j+1 < gradientMagnitude[0].length && j-1>=0 && i-1>=0){
					firstPixel = gradientMagnitude[i+1][j-1];
					secondPixel = gradientMagnitude[i-1][j+1];
				}
				
				if(gradientMagnitude[i][j] < firstPixel || gradientMagnitude[i][j] < secondPixel)
					gradientMagnitude[i][j] = 0;
			}
		}
		
//		BufferedImage nonMaxSurpression = new BufferedImage(blurImage.getWidth(),blurImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
//		WritableRaster raster = nonMaxSurpression.getRaster();
//		int[] iArray = new int[1];
//		for(int i = 0; i < nonMaxSurpression.getHeight(); i++){
//			for(int j = 0; j < nonMaxSurpression.getWidth(); j++){
//				iArray[0] = (int) gradientMagnitude[i][j];
//				raster.setPixel(i,j,iArray);
//			}
//		}
//		
//		try{
//			File outputFile = new File("images/objects/nonMaxSurpressionApple.jpg");
//			ImageIO.write(nonMaxSurpression,"jpg",outputFile);
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}
	}
	
	private void applyHysteresisThresholding(){
		for(int i = 0; i < gradientMagnitude.length; i++){
			for(int j = 0; j < gradientMagnitude[0].length; j++){
				if(gradientMagnitude[i][j] < LOWHYSTERESISTHRESHOLD)
					hysteresisImage[i][j] = 0;
				else if(gradientMagnitude[i][j] > HIGHHYSTERESISTHRESHOLD)
					hysteresisImage[i][j] = 255;
				else if(isValidEdge(i,j))
					hysteresisImage[i][j] = 255;
				else hysteresisImage[i][j] = 0;
			}
		}
		
		WritableRaster raster = edgeImage.getRaster();
		int[] iArray = new int[1];
		
		for(int i = 0; i < hysteresisImage.length; i++){
			for(int j = 0; j < hysteresisImage[0].length; j++){
				iArray[0] = (int)hysteresisImage[i][j];
				raster.setPixel(i, j, iArray);
			}
		}
	}
	
	private boolean isValidEdge(int row, int col){
		boolean isValid = false;
		boolean expandSearch = false;
		
		//search neighbors in a 3x3 region around row,col
		for(int i = row-1; i < row+2; i++){
			for(int j = col-1; j < col+2; j++){
				if(i >= 0 && i < gradientMagnitude.length && j >= 0 && j < gradientMagnitude[0].length){
					if(gradientMagnitude[i][j] > HIGHHYSTERESISTHRESHOLD)
						isValid = true;
					else if(gradientMagnitude[i][j] > LOWHYSTERESISTHRESHOLD)
						expandSearch = true;//search neighbors in 5x5 region
				}
			}
		}
		
		if(!isValid && expandSearch){
			//search neighbors in a 5x5 region around row,col
			for(int i = row-2; i < row+3; i++){
				for(int j = col-2; j < col+3; j++){
					if(i >= 0 && i < gradientMagnitude.length && j >= 0 && j < gradientMagnitude[0].length){
						if(gradientMagnitude[i][j] > HIGHHYSTERESISTHRESHOLD)
							isValid = true;
					}
				}
			}
		}
		
		return isValid;
	}
	
	private int getAveragePixelValue(int row, int col){
		double sum = 0.0;
		
		for(int i = 2; i >=0; i--){
			for(int j = 2; j >=0; j--){
				if(row-i >=0 && col-j >=0){
					sum += (gaussFilter.getCoefficient()*gaussFilter.getValue(2-i,2-j))*
							convertToGrayScale(new Color(initialImage.getRGB(row-i, col-j)));
				}
				if(row+i<initialImage.getHeight() && col+j<initialImage.getWidth()){
					sum += (gaussFilter.getCoefficient()*gaussFilter.getValue(2+i,2+j))*
							convertToGrayScale(new Color(initialImage.getRGB(row+i, col+j)));
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
