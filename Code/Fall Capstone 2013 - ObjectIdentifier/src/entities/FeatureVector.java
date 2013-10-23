package entities;

import java.util.ArrayList;
import java.util.ListIterator;

public class FeatureVector{
	private ArrayList<Feature> features;
	
	public FeatureVector(){
		features = new ArrayList<Feature>();
	}
	
	public void addFeature(Feature feature){
		features.add(feature);
	}
	
	public ListIterator<Feature> getFeatures(){
		return features.listIterator();
	}
}
