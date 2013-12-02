package Runnables;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import shapeContext.ObjectShape;
import shapeContext.ShapeCostMapping;

public class ShapeComparer {
	public static void main(String[] args){
		BufferedImage apple = null, car = null, computer = null, face = null, orange = null;
		
		try{
			apple = ImageIO.read(new File("images/edges/apple.jpg"));
			car = ImageIO.read(new File("images/edges/car.jpg"));
			computer = ImageIO.read(new File("images/edges/computer.jpg"));
			face = ImageIO.read(new File("images/edges/face.jpg"));
			orange = ImageIO.read(new File("images/edges/orange.jpg"));
		}
		catch(IOException e){}
		
		ObjectShape appleShape = new ObjectShape(apple);
		ObjectShape carShape = new ObjectShape(car);
//		ObjectShape computerShape = new ObjectShape(computer);
//		ObjectShape faceShape = new ObjectShape(face);
//		ObjectShape orangeShape = new ObjectShape(orange);
		
//		ShapeCostMapping appleToApple = new ShapeCostMapping(appleShape, appleShape);
		ShapeCostMapping appleToCar = new ShapeCostMapping(appleShape, carShape);
//		ShapeCostMapping appleToComputer = new ShapeCostMapping(appleShape, computerShape);
//		ShapeCostMapping appleToFace = new ShapeCostMapping(appleShape, faceShape);
//		ShapeCostMapping appleToOrange = new ShapeCostMapping(appleShape, orangeShape);
		
		System.out.println("Cost of matching shapes(smaller is better):");
//		System.out.println("Apple-Apple: "  + appleToApple.getTotalMatchingCost());
		System.out.println("Apple-Car: " + appleToCar.getTotalMatchingCost());
//		System.out.println("Apple-Computer: " + appleToComputer.getTotalMatchingCost());
//		System.out.println("Apple-Face: " + appleToFace.getTotalMatchingCost());
//		System.out.println("Apple-Orange: " + appleToOrange.getTotalMatchingCost());
	}
}
