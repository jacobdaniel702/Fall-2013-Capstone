package knn;

import java.util.HashMap;
import java.util.Map;

import shapeContext.ObjectShape;
import shapeContext.ShapeCostMapping;

import entities.Classification;

public class KNearestNeighbor {
	static final int K = 3;
	private ObjectShape[] trainingSet;
	
	public KNearestNeighbor(ObjectShape[] trainingSet){
		this.trainingSet = trainingSet;
	}
		
	public Classification classify(ObjectShape unclassified){
		double totalMatchingCost = 0;
		Map<ObjectShape, Double> KNearestNeighbors = new HashMap<ObjectShape, Double>();
		
		
		for(ObjectShape classified: trainingSet){
			ShapeCostMapping costMapping = new ShapeCostMapping(unclassified, classified);
			totalMatchingCost = costMapping.getTotalMatchingCost();
			System.out.println(classified.getClassification().getName()+": " + totalMatchingCost);			
			if(KNearestNeighbors.size() < K)
				KNearestNeighbors.put(classified, totalMatchingCost);
			else{
				ObjectShape furthest = getNeighborFurthestFromTarget(KNearestNeighbors, unclassified);
				if(KNearestNeighbors.get(furthest) > totalMatchingCost){
					KNearestNeighbors.remove(furthest);
					KNearestNeighbors.put(classified, totalMatchingCost);
				}
			}	
		}
		
		Classification result = chooseClassification(KNearestNeighbors);
		
		return result;
	}
	
	private ObjectShape getNeighborFurthestFromTarget(Map<ObjectShape, Double> neighbors, ObjectShape target){
		ObjectShape furthest = neighbors.keySet().iterator().next();
		
		for(ObjectShape neighbor: neighbors.keySet()){
			if(neighbors.get(neighbor) > neighbors.get(furthest)){
				furthest = neighbor;
			}
		}
		
		return furthest;
	}
	
	private Classification chooseClassification(Map<ObjectShape, Double> neighbors){
		System.out.println("Neighbors: ");
		Map<String, Integer> classCount = new HashMap<String, Integer>();
		
		for(ObjectShape neighbor: neighbors.keySet()){
			System.out.println(neighbor.getClassification().getName());
			if(classCount.containsKey(neighbor.getClassification().getName())){
				int count = classCount.get(neighbor.getClassification().getName());
				classCount.put(neighbor.getClassification().getName(), count+1);
			}
			else classCount.put(neighbor.getClassification().getName(), 1);
		}
		
		double closestCost = Double.MAX_VALUE;
		Classification maxInstance = null;
		
		for(ObjectShape neighbor: neighbors.keySet()){
			if(neighbors.get(neighbor) < closestCost){
				closestCost = neighbors.get(neighbor);
				maxInstance = neighbor.getClassification();
			}
		}
		
		int maxOccurrance = classCount.get(maxInstance.getName());
		
		for(String s: classCount.keySet()){
			System.out.println(classCount.get(s));
			if(classCount.get(s) > maxOccurrance){
				maxOccurrance = classCount.get(s);
				maxInstance = new Classification(s);
			}
		}
		
		return maxInstance;
	}
}
