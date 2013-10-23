package entities;

public class UnclassifiedObject {
	private FeatureVector featureVector;
	
	public UnclassifiedObject(FeatureVector featureVector){
		this.setFeatureVector(featureVector);
	}

	public FeatureVector getFeatureVector() {
		return featureVector;
	}

	public void setFeatureVector(FeatureVector featureVector) {
		this.featureVector = featureVector;
	}
}
