package shapeContext;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import entities.Shape;
import entities.Vector;

public class ObjectShape {

	private Shape shape;
	private ListIterator<Point> sample;
	private final int SAMPLESIZE = 200;
	private Map<Point,Map<Point,Double>> pointDistances;
	private Map<Point,Map<Point,Double>> pointAngles;
	
	public ObjectShape(BufferedImage edges){
		shape = new Shape();
		
		WritableRaster raster = edges.getRaster();
		
		for(int i = 0; i < edges.getHeight(); i++){
			for(int j = 0; j < edges.getWidth(); j++){
				if(raster.getPixel(i, j, new int[1])[0] == 255)
					shape.add(new Point(i,j));
			}
		}
		
		sample = shape.getSample(SAMPLESIZE);
		pointDistances = new HashMap<Point, Map<Point, Double>>();
		pointAngles = new HashMap<Point, Map<Point, Double>>();
		
		calculatePointDistances();
		normalizePointDistances();
	}
	
	private void calculatePointDistances(){
		while(sample.hasNext()){
			ListIterator<Point> otherSample = sample;
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
	
	private Vector findAverageVector(Point p){
		ArrayList<Vector> vectors = new ArrayList<Vector>();
		
		for(Point q: pointDistances.get(p).keySet()){
			double angle = Math.atan((q.y-p.y)/(q.x-p.x));
			vectors.add(new Vector(1,angle));
		}
		
		//add vectors to approximate tangent vector at p
	}
}
