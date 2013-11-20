package shapeContext;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import entities.Shape;

public class ObjectShape {

	private Shape shape;
	private ListIterator<Point> sample;
	private final int SAMPLESIZE = 200;
	private Map<Point,Map<Point,Double>> pointDistances;
	
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
		
		calculatePointDistances();
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
}
