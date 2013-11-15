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
		BufferedImage apple = null, car = null, computer = null, face = null, orange = null;
		try{
			apple = ImageIO.read(new File("images/objects/apple.jpg"));
			car = ImageIO.read(new File("images/objects/car.jpg"));
			computer = ImageIO.read(new File("images/objects/computer.jpg"));
			face = ImageIO.read(new File("images/objects/face.jpg"));
			orange = ImageIO.read(new File("images/objects/orange.jpg"));
		}
		catch(IOException e){}
		
		CannyEdgeDetector canny = new CannyEdgeDetector();
		BufferedImage appleEdges = canny.processImage(apple);
		BufferedImage carEdges = canny.processImage(car);
		BufferedImage computerEdges = canny.processImage(computer);
		BufferedImage faceEdges = canny.processImage(face);
		BufferedImage orangeEdges = canny.processImage(orange);
		
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
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
