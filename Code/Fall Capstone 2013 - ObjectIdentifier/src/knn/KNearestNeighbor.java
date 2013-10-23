package knn;

import java.util.ArrayList;
import java.util.ListIterator;

import entities.ClassifiedObject;
import entities.Color;
import entities.TrainingSet;
import entities.UnclassifiedObject;

public class KNearestNeighbor {
	static final int K = 3;
	private TrainingSet trainingSet;
	
	public KNearestNeighbor(TrainingSet trainingSet){
		this.trainingSet = trainingSet;
	}
	
	public double getDistanceBetweenObjects(ClassifiedObject classified, UnclassifiedObject unclassified){
		double distance = 0;
		
		Color classifiedFeature = (Color)classified.getFeatureVector().getFeatures().next();
		double r1 = ((double[])classifiedFeature.getAttribute())[0];
		double g1 = ((double[])classifiedFeature.getAttribute())[1];
		double b1 = ((double[])classifiedFeature.getAttribute())[2];
		
		Color unclassifiedFeature = (Color) unclassified.getFeatureVector().getFeatures().next();
		double r2 = ((double[])unclassifiedFeature.getAttribute())[0];
		double g2 = ((double[])unclassifiedFeature.getAttribute())[1];
		double b2 = ((double[])unclassifiedFeature.getAttribute())[2];
			
		distance = Math.sqrt(Math.pow(r1-r2, 2) + Math.pow(g1-g2, 2) + Math.pow(b1-b2, 2));
		return distance;
	}
	
	public ListIterator<ClassifiedObject> classify(UnclassifiedObject unclassified){
		double distance = 0;
		ArrayList<ClassifiedObject> KNearestNeighbors = new ArrayList<ClassifiedObject>();
		ListIterator<ClassifiedObject> lt = trainingSet.getTrainingSet();
		while(lt.hasNext()){
			ClassifiedObject classified = lt.next();
			distance = getDistanceBetweenObjects(classified, unclassified);
			
			if(KNearestNeighbors.size() < K)
				KNearestNeighbors.add(classified);
			else{
				ClassifiedObject furthest = getObjectFurthestFromTarget(KNearestNeighbors, unclassified);
				if(distance < getDistanceBetweenObjects(furthest,unclassified)){
					KNearestNeighbors.remove(furthest);
					KNearestNeighbors.add(classified);
				}
			}	
		}
		
		return KNearestNeighbors.listIterator();
	}
	
	private ClassifiedObject getObjectFurthestFromTarget(ArrayList<ClassifiedObject> neighbors, UnclassifiedObject target){
		ClassifiedObject furthest = neighbors.get(0);
		
		for(int i = 1; i < neighbors.size(); i++){
			if(getDistanceBetweenObjects(neighbors.get(i),target) > getDistanceBetweenObjects(furthest,target))
				furthest = neighbors.get(i);
		}
		
		return furthest;
	}
}
