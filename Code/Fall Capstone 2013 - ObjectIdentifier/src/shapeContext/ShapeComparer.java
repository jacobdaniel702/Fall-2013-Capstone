package shapeContext;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class ShapeComparer {
	private ObjectShape unknownShape;
	private ObjectShape knownShape;
	private Map<Point, Map<Point, Double>> shapeContextCost;
	
	public ShapeComparer(ObjectShape unknownShape, ObjectShape knownShape){
		this.unknownShape = unknownShape;
		this.knownShape = knownShape;
		
		shapeContextCost = new HashMap<Point, Map<Point, Double>>();
		computeCostMatrix();
	}
	
	private void computeCostMatrix(){
		Map<Point, LogPolarHistogram> unknownHistograms = unknownShape.getPointHistograms();
		Map<Point, LogPolarHistogram> knownHistograms = knownShape.getPointHistograms();
		
		for(Point p: unknownHistograms.keySet()){
			Map<Point, Double> costMap = new HashMap<Point, Double>();
			for(Point q: knownHistograms.keySet()){
				double totalCost = 0.0;
				for(int k = 0; k < unknownHistograms.get(p).getNumBins(); k++){
					totalCost += 
							(Math.pow(unknownHistograms.get(p).getBinCount(k)-knownHistograms.get(q).getBinCount(k),2))/
							(unknownHistograms.get(p).getBinCount(k)+knownHistograms.get(q).getBinCount(k));
				}
				costMap.put(q, .5*totalCost);
			}
			shapeContextCost.put(p, costMap);
		}
	}
}
