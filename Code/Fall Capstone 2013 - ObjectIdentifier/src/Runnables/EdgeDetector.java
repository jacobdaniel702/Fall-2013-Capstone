package Runnables;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edgeDetection.CannyEdgeDetector;

public class EdgeDetector {
	
	public EdgeDetector(){
	}
	
	public static void main(String[] args){
		EdgeDetector detector = new EdgeDetector();
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("images/objects/apple.jpg"));
		}
		catch(IOException e){}
		
		CannyEdgeDetector canny = new CannyEdgeDetector(img);
		BufferedImage blur = canny.getBlurredImage();
		
		try{
			File outputFile = new File("images/objects/blurredApple.jpg");
			ImageIO.write(blur,"jpg",outputFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private BufferedImage convertToGrayScale(BufferedImage color){
		BufferedImage grayscale = new BufferedImage(color.getWidth(),color.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = grayscale.getGraphics();
		g.drawImage(color,0,0,null);
		g.dispose();
		return grayscale;
	}
}
