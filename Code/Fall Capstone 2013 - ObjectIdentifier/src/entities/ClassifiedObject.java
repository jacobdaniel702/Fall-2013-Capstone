package entities;

public class ClassifiedObject {
	private Classification classification;
	private FeatureVector featureVector;
	
	public ClassifiedObject(Classification classification, FeatureVector featureVector){
		this.classification = classification;
		this.featureVector = featureVector;
	}
	
	public Classification getClassification() {
		return classification;
	}
	public void setClassification(Classification classification) {
		this.classification = classification;
	}
	
	public FeatureVector getFeatureVector() {
		return featureVector;
	}
	public void setFeatureVector(FeatureVector featureVector) {
		this.featureVector = featureVector;
	}
}
