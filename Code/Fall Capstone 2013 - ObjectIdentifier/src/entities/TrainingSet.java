package entities;

import java.util.ArrayList;
import java.util.ListIterator;

public class TrainingSet {
	private ArrayList<ClassifiedObject> classifiedObjects;
	
	public TrainingSet(){
		this.classifiedObjects = new ArrayList<ClassifiedObject>();
	}
	
	public ListIterator<ClassifiedObject> getTrainingSet(){
		return classifiedObjects.listIterator();
	}
	
	public void addObject(ClassifiedObject object){
		classifiedObjects.add(object);
	}
}
