package Runnables;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import knn.KNearestNeighbor;

import edgeDetection.CannyEdgeDetector;
import entities.Classification;

import shapeContext.ObjectShape;

public class ShapeComparer {
	private String[] imgFiles;
	private BufferedImage[] edges;
	private ObjectShape[] trainingSet;
	
	public ShapeComparer(){
		imgFiles = new String[]{"images/objects/trainingSet/apple.jpg","images/objects/trainingSet/car.jpg","images/objects/trainingSet/computer.jpg",
				"images/objects/trainingSet/face.jpg","images/objects/trainingSet/apple2.jpg", "images/objects/trainingSet/apple3.jpg",
				"images/objects/trainingSet/apple4.jpg", "images/objects/trainingSet/car2.jpg", "images/objects/trainingSet/car3.jpg", "images/objects/trainingSet/face2.jpg",
				"images/objects/trainingSet/computer2.jpg"};
		
		edges = new BufferedImage[imgFiles.length];
		trainingSet = new ObjectShape[imgFiles.length];
		
		findEdges();
		initializeObjectShapes();
	}
	
	private void findEdges(){
		CannyEdgeDetector canny = new CannyEdgeDetector();
		for(int i = 0; i < imgFiles.length; i++){
			try {
				edges[i] = canny.processImage(ImageIO.read(new File(imgFiles[i])));
				File file = new File("images/edges/" + imgFiles[i].split("trainingSet/")[1]);
				ImageIO.write(edges[i],"jpg",file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initializeObjectShapes(){
		
		ObjectShape apple = new ObjectShape(edges[0]);
		apple.setClassification(new Classification("Apple"));
		trainingSet[0] = apple;
		
		ObjectShape car = new ObjectShape(edges[1]);
		car.setClassification(new Classification("Car"));
		trainingSet[1] = car;
		
		ObjectShape computer = new ObjectShape(edges[2]);
		computer.setClassification(new Classification("Computer"));
		trainingSet[2] = computer;
		
		ObjectShape face = new ObjectShape(edges[3]);
		face.setClassification(new Classification("Face"));
		trainingSet[3] = face;
		
		ObjectShape apple2 = new ObjectShape(edges[4]);
		apple2.setClassification(new Classification("Apple"));
		trainingSet[4] = apple2;
		
		ObjectShape apple3 = new ObjectShape(edges[5]);
		apple3.setClassification(new Classification("Apple"));
		trainingSet[5] = apple3;
		
		ObjectShape apple4 = new ObjectShape(edges[6]);
		apple4.setClassification(new Classification("Apple"));
		trainingSet[6] = apple4;
		
		ObjectShape car2 = new ObjectShape(edges[7]);
		car2.setClassification(new Classification("Car"));
		trainingSet[7] = car2;
		
		ObjectShape car3 = new ObjectShape(edges[8]);
		car3.setClassification(new Classification("Car"));
		trainingSet[8] = car3;
		
		ObjectShape face2 = new ObjectShape(edges[9]);
		face2.setClassification(new Classification("Face"));
		trainingSet[9] = face2;
		
		ObjectShape computer2 = new ObjectShape(edges[10]);
		computer2.setClassification(new Classification("Computer"));
		trainingSet[10] = computer2;
	
	}
	
	public static void main(String[] args){
		
		ShapeComparer sc = new ShapeComparer();
		KNearestNeighbor KNN = new KNearestNeighbor(sc.trainingSet);
		
		CannyEdgeDetector canny = new CannyEdgeDetector();
		Scanner scan = new Scanner(System.in);
		String input = null;
		do{
			System.out.println("Enter file name:");
			 input = scan.nextLine();
			 if(input != "q"){
				 try {
					 	String fileName = "images/objects/testData/"+input+".jpg";
						Classification c = KNN.classify(new ObjectShape(canny.processImage(ImageIO.read(new File(fileName)))));
						System.out.println("Classification: " + c.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
			 }
		}while(input != "q");
		scan.close();
	}
}
