package Runnables;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edgeDetection.CannyEdgeDetector;

public class EdgeDetector {
	
	public EdgeDetector(){
	}
	
	public static void main(String[] args){
		BufferedImage apple = null, car = null, computer = null, face = null, orange = null, apple_rotated = null, apple_scaled = null, apple_translated = null, otherApple = null;
		try{
			apple = ImageIO.read(new File("images/objects/apple.jpg"));
			car = ImageIO.read(new File("images/objects/car.jpg"));
			computer = ImageIO.read(new File("images/objects/computer.jpg"));
			face = ImageIO.read(new File("images/objects/face.jpg"));
			orange = ImageIO.read(new File("images/objects/orange.jpg"));
			apple_rotated = ImageIO.read(new File("images/objects/apple_rotated.jpg"));
			apple_scaled = ImageIO.read(new File("images/objects/apple_scaled.jpg"));
			apple_translated = ImageIO.read(new File("images/objects/apple_translated.jpg"));
			otherApple = ImageIO.read(new File("images/objects/otherApple.jpg"));
		}
		catch(IOException e){}
		
		CannyEdgeDetector canny = new CannyEdgeDetector();
		BufferedImage appleEdges = canny.processImage(apple);
		BufferedImage carEdges = canny.processImage(car);
		BufferedImage computerEdges = canny.processImage(computer);
		BufferedImage faceEdges = canny.processImage(face);
		BufferedImage orangeEdges = canny.processImage(orange);
		BufferedImage apple_rotatedEdges = canny.processImage(apple_rotated);
		BufferedImage apple_scaledEdges = canny.processImage(apple_scaled);
		BufferedImage apple_translatedEdges = canny.processImage(apple_translated);
		BufferedImage otherAppleEdges = canny.processImage(otherApple);
		
		try{
			File appleFile = new File("images/edges/apple.jpg");
			ImageIO.write(appleEdges,"jpg",appleFile);
			
			File carFile = new File("images/edges/car.jpg");
			ImageIO.write(carEdges,"jpg",carFile);

			File computerFile = new File("images/edges/computer.jpg");
			ImageIO.write(computerEdges,"jpg",computerFile);
			
			File faceFile = new File("images/edges/face.jpg");
			ImageIO.write(faceEdges,"jpg",faceFile);
			
			File orangeFile = new File("images/edges/orange.jpg");
			ImageIO.write(orangeEdges,"jpg",orangeFile);
			
			File apple_rotatedFile = new File("images/edges/apple_rotated.jpg");
			ImageIO.write(apple_rotatedEdges, "jpg", apple_rotatedFile);
			
			File apple_scaledFile = new File("images/edges/apple_scaled.jpg");
			ImageIO.write(apple_scaledEdges, "jpg", apple_scaledFile);
			
			File apple_translatedFile = new File("images/edges/apple_translated.jpg");
			ImageIO.write(apple_translatedEdges, "jpg", apple_translatedFile);
			
			File otherAppleFile = new File("images/edges/otherApple.jpg");
			ImageIO.write(otherAppleEdges, "jpg", otherAppleFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
