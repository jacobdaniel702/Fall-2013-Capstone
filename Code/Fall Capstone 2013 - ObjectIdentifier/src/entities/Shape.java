package entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class Shape {
	private ArrayList<Point> points;
	private ArrayList<Point> sample;
	
	public Shape(){
		points = new ArrayList<Point>();
	}
	
	public void add(Point point){
		points.add(point);
	}
	
	public ListIterator<Point> getPoints(){
		return points.listIterator();
	}
	
	public void sample(int size){
		Random random = new Random();
		sample = new ArrayList<Point>();
		
		for(int i = 0; i < size; i++){
			int rand = 0;
			do{
				rand = random.nextInt(points.size());
			}while(sample.contains(points.get(rand)));
			sample.add(points.get(rand));
		}
	}
	
	public ListIterator<Point> getSample(){
		return sample.listIterator();
	}
}
