package shapeContext;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import entities.Classification;
import entities.Shape;
import entities.Vector;

public class ObjectShape {

	private Shape shape;
	private ListIterator<Point> sample;
	private final int SAMPLESIZE = 350;
	private Map<Point,Map<Point,Double>> pointDistances;
	private Map<Point,Map<Point,Double>> pointAngles;
	private Map<Point, LogPolarHistogram> pointHistograms;
	private Classification classification;
	
	public ObjectShape(BufferedImage edges){
		shape = new Shape();
		
		WritableRaster raster = edges.getRaster();
		
		for(int i = 0; i < edges.getHeight(); i++){
			for(int j = 0; j < edges.getWidth(); j++){
				if(raster.getPixel(i, j, new int[1])[0] == 255)
					shape.add(new Point(i,j));
			}
		}
		
		shape.sample(SAMPLESIZE);
		sample = shape.getSample();
		pointDistances = new HashMap<Point, Map<Point, Double>>();
		pointAngles = new HashMap<Point, Map<Point, Double>>();
		
		calculatePointDistances();
		normalizePointDistances();
		calculatePointAngles();
		
		pointHistograms = new HashMap<Point, LogPolarHistogram>();
		createHistograms();
	}
	
	public void setClassification(Classification classification){
		this.classification = classification;
	}
	
	public Classification getClassification(){
		return classification;
	}
	
	public Map<Point, LogPolarHistogram> getPointHistograms(){
		return pointHistograms;
	}
	
	private void calculatePointDistances(){
		while(sample.hasNext()){
			ListIterator<Point> otherSample = shape.getSample();
			Point p = sample.next();
			HashMap<Point,Double> distances = new HashMap<Point,Double>();
			while(otherSample.hasNext()){
				Point q = otherSample.next();
				if(p.distance(q) != 0 )
					distances.put(q, p.distance(q));
			}
			pointDistances.put(p, distances);
		}
	}
	
	private void normalizePointDistances(){
		double sum = 0.0;
		double meanDistance = 0.0;
		Map<Point,Double> distances = null;
		
		for(Point p: pointDistances.keySet()){
			distances = pointDistances.get(p);
			for(Point q: distances.keySet()){
				sum += distances.get(q);
			}
		}
		
		meanDistance = sum/(pointDistances.size()*distances.size());
		
		for(Point p: pointDistances.keySet()){
			Map<Point,Double> nonNormalizedDistances = pointDistances.get(p);
			for(Point q: nonNormalizedDistances.keySet()){
				double nonNormalizedDistance = nonNormalizedDistances.get(q);
				nonNormalizedDistances.put(q, nonNormalizedDistance/meanDistance);
			}
			pointDistances.put(p, nonNormalizedDistances);
		}
	}
	
	private void calculatePointAngles(){
		sample = shape.getSample();
		while(sample.hasNext()){
			ListIterator<Point> otherSample = shape.getSample();
			Point p = sample.next();
			HashMap<Point,Double> angles = new HashMap<Point,Double>();
			while(otherSample.hasNext()){
				Point q = otherSample.next();
				if(p.distance(q) != 0){
					Vector tangentVector = findAverageVector(p);
					double anglePQ = 0;
					try{
						anglePQ = Math.atan((q.y-p.y)/(q.x-p.x));
					}
					catch(ArithmeticException ae){
						anglePQ = Math.PI/2;
					}
					Map<Point, Double> r = pointDistances.get(p);
					Vector pq = new Vector(pointDistances.get(p).get(q),anglePQ);
					
					double angle = Math.acos(((tangentVector.getXComponent()*pq.getXComponent())+
					(tangentVector.getYComponent()+pq.getYComponent()))/
					(tangentVector.getMagnitude()*pq.getMagnitude()));
					
					angles.put(q, angle);
				}
			}
			pointAngles.put(p, angles);
		}
	}
	
	private Vector findAverageVector(Point p){
		ArrayList<Vector> vectors = new ArrayList<Vector>();
		
		for(Point q: pointDistances.get(p).keySet()){
			double angle = 0;
			try{
				angle = Math.atan((q.y-p.y)/(q.x-p.x));
			}
			catch(ArithmeticException ae){
				angle = Math.PI/2;
			}
			
			vectors.add(new Vector(pointDistances.get(p).get(q),angle));
		}
		
		double xComponent = 0.0, yComponent = 0.0;
		
		for(Vector v: vectors){
			xComponent += v.getMagnitude()*Math.cos(v.getDirection());
			yComponent += v.getMagnitude()*Math.sin(v.getDirection());
		}
		
		double averageMagnitude = Math.sqrt(Math.pow(xComponent, 2)+Math.pow(yComponent, 2));
		double averageAngle = Math.atan(yComponent/xComponent);
		
		return new Vector(averageMagnitude,averageAngle);
	}
	
	private void createHistograms(){
		sample = shape.getSample();
		while(sample.hasNext()){
			Point p = sample.next();
			LogPolarHistogram pHistogram = new LogPolarHistogram();
			Map<Point, Double> distances = pointDistances.get(p);
			Map<Point, Double> angles = pointAngles.get(p);
			
			for(Point q: distances.keySet()){
				pHistogram.addPoint(distances.get(q), angles.get(q));
			}
			pointHistograms.put(p, pHistogram);
		}
	}
}
