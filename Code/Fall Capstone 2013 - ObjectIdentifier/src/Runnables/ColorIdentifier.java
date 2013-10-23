package Runnables;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

import javax.imageio.ImageIO;

import knn.KNearestNeighbor;
import entities.Classification;
import entities.ClassifiedObject;
import entities.FeatureVector;
import entities.TrainingSet;
import entities.UnclassifiedObject;

public class ColorIdentifier {
	private TrainingSet trainingSet;
	private KNearestNeighbor KNNClassifier;
	
	public ColorIdentifier(){
		this.trainingSet = new TrainingSet();
		populateTrainingData();
		this.KNNClassifier = new KNearestNeighbor(trainingSet);
	}
	
	public static void main(String[] args) {
		ColorIdentifier colorIdentifier = new ColorIdentifier();
		FeatureVector unknown = new FeatureVector();
		unknown.addFeature(new entities.Color("Color",colorIdentifier.getAverageRGB(new File("images/colors/unclassified/maroon.jpg"))));
		UnclassifiedObject u = new UnclassifiedObject(unknown);
		ListIterator<ClassifiedObject> KNearestNeighbors = colorIdentifier.KNNClassifier.classify(u);
		System.out.println("The most similar colors are:");
		while(KNearestNeighbors.hasNext()){
			ClassifiedObject c = KNearestNeighbors.next();
			System.out.println("Color: " + c.getClassification().getName() + "\tDistance: " + colorIdentifier.KNNClassifier.getDistanceBetweenObjects(c, u));
		}
	}
	
	private void populateTrainingData(){
		//add black
		FeatureVector black = new FeatureVector();
		black.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/black.jpg"))));
		ClassifiedObject c = new ClassifiedObject(new Classification("Black"),black);
		trainingSet.addObject(c);
		
		//add brown
		FeatureVector brown = new FeatureVector();
		brown.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/brown.jpg"))));
		c = new ClassifiedObject(new Classification("Brown"),brown);
		trainingSet.addObject(c);
		
		//add indigo
		FeatureVector indigo = new FeatureVector();
		indigo.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/indigo.jpg"))));
		c = new ClassifiedObject(new Classification("Indigo"),indigo);
		trainingSet.addObject(c);
		
		//add gray
		FeatureVector gray = new FeatureVector();
		gray.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/gray.jpg"))));
		c = new ClassifiedObject(new Classification("Gray"),gray);
		trainingSet.addObject(c);
		
		//add green
		FeatureVector green = new FeatureVector();
		green.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/green.jpg"))));
		c = new ClassifiedObject(new Classification("Green"),green);
		trainingSet.addObject(c);
		
		//add lavender
		FeatureVector lavender = new FeatureVector();
		lavender.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/lavender.jpg"))));
		c = new ClassifiedObject(new Classification("Lavender"),lavender);
		trainingSet.addObject(c);
		
		//add orange
		FeatureVector orange = new FeatureVector();
		orange.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/orange.jpg"))));
		c = new ClassifiedObject(new Classification("Orange"),orange);
		trainingSet.addObject(c);
		
		//add red
		FeatureVector red = new FeatureVector();
		red.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/red.jpg"))));
		c = new ClassifiedObject(new Classification("Red"),red);
		trainingSet.addObject(c);
		
		//add yellow
		FeatureVector yellow = new FeatureVector();
		yellow.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/yellow.jpg"))));
		c = new ClassifiedObject(new Classification("Yellow"),yellow);
		trainingSet.addObject(c);
		
		//add white
		FeatureVector white = new FeatureVector();
		white.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/white.jpg"))));
		c = new ClassifiedObject(new Classification("White"),white);
		trainingSet.addObject(c);
		
		//add pink
		FeatureVector pink = new FeatureVector();
		pink.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/pink.jpg"))));
		c = new ClassifiedObject(new Classification("Pink"),pink);
		trainingSet.addObject(c);
		
		//add purple
		FeatureVector purple = new FeatureVector();
		purple.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/purple.jpg"))));
		c = new ClassifiedObject(new Classification("Purple"),purple);
		trainingSet.addObject(c);
		
		//add turquoise
		FeatureVector turquoise = new FeatureVector();
		turquoise.addFeature(new entities.Color("Color",getAverageRGB(new File("images/colors/classified/turquoise.jpg"))));
		c = new ClassifiedObject(new Classification("Turquoise"),turquoise);
		trainingSet.addObject(c);
		
	}
	
	private double[] getAverageRGB(File imgFile){
		BufferedImage img;
		int totalR = 0, totalG = 0, totalB = 0, numPixels = 0;
		
		try {
			img = ImageIO.read(imgFile);
			
			for(int i = 0; i < img.getWidth(); i++){
				for(int j = 0; j < img.getHeight(); j++){
					numPixels++;
					Color c = new Color(img.getRGB(i, j));
					
					totalR += c.getRed();
					totalG += c.getGreen();
					totalB += c.getBlue();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new double[]{totalR/numPixels, totalG/numPixels, totalB/numPixels};
	}
}
