package Runnables;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import shapeContext.ObjectShape;
import shapeContext.ShapeCostMapping;

public class ShapeComparer {
	public static void main(String[] args){
		BufferedImage apple = null, apple_rotated = null, apple_scaled = null, apple_translated = null, otherApple = null, face  =null;
		
		try{
			apple = ImageIO.read(new File("images/edges/apple.jpg"));
			apple_rotated = ImageIO.read(new File("images/edges/apple_rotated.jpg"));
			apple_scaled = ImageIO.read(new File("images/edges/apple_scaled.jpg"));
			apple_translated = ImageIO.read(new File("images/edges/apple_translated.jpg"));
			otherApple = ImageIO.read(new File("images/edges/otherApple.jpg"));
			face = ImageIO.read(new File("images/edges/face.jpg"));
		}
		catch(IOException e){}
		
		ObjectShape appleShape = new ObjectShape(apple);
		ObjectShape appleShape2 = new ObjectShape(apple);
//		ObjectShape apple_rotatedShape = new ObjectShape(apple_rotated);
//		ObjectShape apple_scaledShape = new ObjectShape(apple_scaled);
//		ObjectShape apple_translatedShape = new ObjectShape(apple_translated);
//		ObjectShape otherAppleShape = new ObjectShape(otherApple);
//		ObjectShape faceShape = new ObjectShape(face);
		
		ShapeCostMapping appleToApple = new ShapeCostMapping(appleShape, appleShape2);
//		ShapeCostMapping appleToApple_Rotated = new ShapeCostMapping(appleShape, apple_rotatedShape);
//		ShapeCostMapping appleToApple_Scaled = new ShapeCostMapping(appleShape, apple_scaledShape);
//		ShapeCostMapping appleToApple_Translated = new ShapeCostMapping(appleShape, apple_translatedShape);
//		ShapeCostMapping appleToOtherApple = new ShapeCostMapping(appleShape, otherAppleShape);
//		ShapeCostMapping appleToFace = new ShapeCostMapping(appleShape, faceShape);
		
		
		System.out.println("Cost of matching shapes(smaller is better):");
		System.out.println("Apple-Apple: "  + appleToApple.getTotalMatchingCost());
//		System.out.println("Apple-Apple_Rotated: " + appleToApple_Rotated.getTotalMatchingCost());
//		System.out.println("Apple-Apple_Scaled: " + appleToApple_Scaled.getTotalMatchingCost());
//		System.out.println("Apple-Apple_Translated: " + appleToApple_Translated.getTotalMatchingCost());
//		System.out.println("Apple-OtherApple: " + appleToOtherApple.getTotalMatchingCost());
//		System.out.println("Apple-Face: " + appleToFace.getTotalMatchingCost());
	}
}
