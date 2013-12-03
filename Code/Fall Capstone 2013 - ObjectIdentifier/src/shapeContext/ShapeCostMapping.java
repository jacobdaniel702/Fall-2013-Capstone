package shapeContext;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ShapeCostMapping {
	private ObjectShape unknownShape;
	private ObjectShape knownShape;
	private Map<Point, Map<Point, Double>> shapeContextCost;
	private double[][] costMatrix;
	private List<PointToPointCostMapping> minCostPointMapping;
	
	enum LineType{NONE, HORIZONTAL, VERTICAL}
	
	private static class Line{
		int lineIndex;
		LineType rowType;
		
		Line(int lineIndex, LineType rowType){
			this.lineIndex = lineIndex;
			this.rowType = rowType;
		}
		
		LineType getLineType(){
			return rowType;
		}
		
		int getLineIndex(){
			return lineIndex;
		}
		
		boolean isHorizontal(){
			return rowType == LineType.HORIZONTAL;
		}
	}
	
	public ShapeCostMapping(ObjectShape unknownShape, ObjectShape knownShape){
		this.unknownShape = unknownShape;
		this.knownShape = knownShape;
		
		shapeContextCost = new HashMap<Point, Map<Point, Double>>();
		computeCostMatrix();
		applyHungarianAlgorithm();
	}
	
	private void computeCostMatrix(){
		Map<Point, LogPolarHistogram> unknownHistograms = unknownShape.getPointHistograms();
		Map<Point, LogPolarHistogram> knownHistograms = knownShape.getPointHistograms();
		
		for(Point p: unknownHistograms.keySet()){
			Map<Point, Double> costMap = new HashMap<Point, Double>();
			for(Point q: knownHistograms.keySet()){
				double totalCost = 0.0;
				for(int k = 0; k < unknownHistograms.get(p).getNumBins(); k++){
					//denominator may go to zero
					double numerator = (Math.pow((unknownHistograms.get(p).getBinCount(k)/(1.0*unknownHistograms.get(p).getTotalBinCount()))-
							(knownHistograms.get(q).getBinCount(k)/(1.0*knownHistograms.get(q).getTotalBinCount())),2));
					
					double denominator = ((unknownHistograms.get(p).getBinCount(k)/(1.0*unknownHistograms.get(p).getTotalBinCount()))+
							(knownHistograms.get(q).getBinCount(k)/(1.0*knownHistograms.get(q).getTotalBinCount())));
					
					
					if(denominator != 0){
						totalCost += (numerator/denominator);
					}
					else totalCost += 0;
				}
				costMap.put(q, .5*totalCost);
			}
			shapeContextCost.put(p, costMap);
		}
		
		Iterator<Point> pointIterator = unknownHistograms.keySet().iterator();
		costMatrix = new double[shapeContextCost.size()][shapeContextCost.size()];
		
		for(int row = 0; row < shapeContextCost.size(); row++){
			Iterator<Point> otherPointIterator = knownHistograms.keySet().iterator();
			Point p = pointIterator.next();
			for(int col = 0; col < shapeContextCost.size(); col++){
				Point q = otherPointIterator.next();
				costMatrix[row][col] = shapeContextCost.get(p).get(q);
			}
		}
	}
	
	private void applyHungarianAlgorithm(){
		reduceRows();
		reduceCols();
		List<Line> coveredZeroLines = getMinLines(costMatrix);
		Map<Point, LogPolarHistogram> unknownHistograms = unknownShape.getPointHistograms();
		Map<Point, LogPolarHistogram> knownHistograms = knownShape.getPointHistograms();
		
		while(coveredZeroLines.size() != costMatrix.length){
			double[][] covered = computeCoveredElements(coveredZeroLines);
			addMinUncoveredToCovered(covered);
			subtractMinElement();
			coveredZeroLines = getMinLines(costMatrix);
		}
		int[][] result = findMatching();	
		minCostPointMapping = new ArrayList<PointToPointCostMapping>();
		
		Iterator<Point> pointIterator = unknownHistograms.keySet().iterator();
		
		for(int i = 0; i < result.length; i++){
			Iterator<Point> otherPointIterator = knownHistograms.keySet().iterator();
			Point p = pointIterator.next();
			for(int j = 0; j < result[i].length; j++){
				Point q = otherPointIterator.next();
				if(result[i][j] == 1)
					minCostPointMapping.add(new PointToPointCostMapping(p,q,shapeContextCost.get(p).get(q)));
			}
		}
		
	}

	public double getTotalMatchingCost(){
		double totalCost = 0;
		
		for(PointToPointCostMapping c: minCostPointMapping){
			totalCost += c.getCost();
		}
		
		return totalCost;
	}
	
	public ListIterator<PointToPointCostMapping> getMinCostPointMapping(){
		return minCostPointMapping.listIterator();
	}
	
	private void reduceRows(){
		double minRowCost = Double.MAX_VALUE;
		
		for(int row = 0; row < costMatrix.length; row++){
			for(int col = 0; col < costMatrix[row].length; col++){
				if(costMatrix[row][col] < minRowCost)
					minRowCost = costMatrix[row][col];
			}
			for(int col = 0; col < costMatrix[row].length; col++){
				costMatrix[row][col] -= minRowCost;
			}
			minRowCost = Double.MAX_VALUE;
		}
	}
	
	private void reduceCols(){
		double minColCost = Double.MAX_VALUE;
		
		int col = 0;
		while(col < costMatrix[0].length){
			for(int row = 0; row < costMatrix.length; row++){
				if(costMatrix[row][col] < minColCost)
					minColCost = costMatrix[row][col];
			}
			for(int row = 0; row < costMatrix.length; row++){
				costMatrix[row][col] -= minColCost;
			}
			col++;
			minColCost = Double.MAX_VALUE;
		}
	}
	
	private boolean isZero(int[] array){
		for(int i: array){
			if(i != 0)
				return false;
		}
		return true;
	}
	
	private List<Line> getMinLines(double[][] costMatrix2) {
        final int SIZE = costMatrix2.length;
        int[] zerosPerRow = new int[SIZE];
        int[] zerosPerCol = new int[SIZE];

        // Count the number of 0's per row and the number of 0's per column        
        for (int i = 0; i < SIZE; i++) { 
            for (int j = 0; j < SIZE; j++) { 
                if (costMatrix2[i][j] == 0) { 
                    zerosPerRow[i]++;
                    zerosPerCol[j]++;
                }
            }
        }

        // There should be at most SIZE lines, 
        // initialize the list with an initial capacity of SIZE
        List<Line> lines = new ArrayList<Line>(SIZE);

        LineType lastInsertedLineType = LineType.NONE;

        // While there are 0's to count in either rows or colums...
        while (!isZero(zerosPerRow) && !isZero(zerosPerCol)) { 
            // Search the largest count of 0's in both arrays
            int max = -1;
            Line lineWithMostZeros = null;
            for (int i = 0; i < SIZE; i++) {
                // If exists another count of 0's equal to "max" but in this one has
                // the same direction as the last added line, then replace it with this
                // 
            
                if (zerosPerRow[i] > max || (zerosPerRow[i] == max && lastInsertedLineType == LineType.HORIZONTAL)) {
                    lineWithMostZeros = new Line(i, LineType.HORIZONTAL);
                    max = zerosPerRow[i];
                }
            }

            for (int i = 0; i < SIZE; i++) {
                // Same as above
                if (zerosPerCol[i] > max || (zerosPerCol[i] == max && lastInsertedLineType == LineType.VERTICAL)) {
                    lineWithMostZeros = new Line(i, LineType.VERTICAL);
                    max = zerosPerCol[i];
                }
            }

            // Delete the 0 count from the line 
            if (lineWithMostZeros.isHorizontal()) {
                zerosPerRow[lineWithMostZeros.getLineIndex()] = 0; 
            } else {
                zerosPerCol[lineWithMostZeros.getLineIndex()] = 0;
            }

            // Once you've found the line (either horizontal or vertical) with the greater 0's count
            // iterate over it's elements and substract the 0's from the other lines 

            int index = lineWithMostZeros.getLineIndex(); 
            if (lineWithMostZeros.isHorizontal()) {
                for (int j = 0; j < SIZE; j++) {
                    if (costMatrix2[index][j] == 0) {
                        zerosPerCol[j]--;
                    }
                }
            } else {
                for (int j = 0; j < SIZE; j++) {
                    if (costMatrix2[j][index] == 0) {
                        zerosPerRow[j]--;
                    }
                }                    
            }

            // Add the line to the list of lines
            lines.add(lineWithMostZeros); 
            lastInsertedLineType = lineWithMostZeros.getLineType();
        }
        return lines;
    }
	
	private double[][] computeCoveredElements(List<Line> covered){
		double[][] coveredOrUncovered = new double[costMatrix.length][costMatrix[0].length];
		
		for(Line line : covered){
			for(int i = 0; i < coveredOrUncovered.length; i++){
				int index = line.getLineIndex();
				if(line.isHorizontal())
					coveredOrUncovered[index][i] = coveredOrUncovered[index][i] < 0 ? -2: -1;
				else coveredOrUncovered[i][index] = coveredOrUncovered[i][index] < 0 ? -2 : -1;
			}
		}
		
		return coveredOrUncovered;
	}
	
	private void addMinUncoveredToCovered(double[][] covered){
		double minUncovered = Double.MAX_VALUE;
		
		//find min uncovered element
		for(int i = 0; i < covered.length; i++){
			for(int j = 0; j < covered[i].length; j++){
				if(covered[i][j] == 0 && costMatrix[i][j] < minUncovered)
					minUncovered = costMatrix[i][j];
					
			}
		}
		
		//add min uncovered element to every covered element
		for(int i = 0; i < covered.length; i++){
			for(int j = 0; j < covered[i].length; j++){
				if(covered[i][j] == -1)
					costMatrix[i][j] += minUncovered;
				else if(covered[i][j] == -2)
					costMatrix[i][j] += 2*minUncovered;
			}
		}
		
	}
	
	private void subtractMinElement(){
		double minElement = Double.MAX_VALUE;
		
		for(int i = 0; i < costMatrix.length; i++){
			for(int j = 0; j < costMatrix[i].length; j++){
				if(costMatrix[i][j] < minElement)
					minElement = costMatrix[i][j];
			}
		}
		
		for(int i = 0; i < costMatrix.length; i++){
			for(int j = 0; j < costMatrix[i].length; j++){
				costMatrix[i][j] -= minElement;
			}
		}
	}
	
	private int[][] findMatching(){
		int[] rowZeros = new int[costMatrix.length];
		int[] colZeros = new int[costMatrix[0].length];
		int[][] result = new int[costMatrix.length][costMatrix[0].length];
		
		for(int row = 0; row < costMatrix.length; row++){
			int rowCount = 0;
			for(int col = 0; col < costMatrix[row].length; col++){
				if(costMatrix[row][col] == 0){
					rowCount++;
					colZeros[col] += 1;
				}
			}
			rowZeros[row] = rowCount;
		}
		
		int rowOrCol;
		
		do{
			int minValue = Integer.MAX_VALUE;
			int indexFound = 0;
			rowOrCol = 0;
			
			for(int i = 0; i < rowZeros.length; i++){
				if(rowZeros[i] > 0 && rowZeros[i] < minValue){
					minValue = rowZeros[i];
					indexFound = i;
					rowOrCol = 1;
				}
			}
			
			for(int i = 0; i < colZeros.length; i++){
				if(colZeros[i] > 0 && colZeros[i] < minValue){
					minValue = colZeros[i];
					indexFound = i;
					rowOrCol = 2;
				}
			}
			
			if(rowOrCol == 1){
				for(int col = 0; col< costMatrix[indexFound].length; col++){
					if(costMatrix[indexFound][col] == 0){
						result[indexFound][col] = 1;
						break;
					}
				}
				rowZeros[indexFound] = 0;
				for(int i = 0; i < rowZeros.length; i++){
					if(rowZeros[i] == 0)
						colZeros[i] -= 1;
				}
			}
			
			else if(rowOrCol == 2){
				for(int row = 0; row < costMatrix.length; row++){
					if(costMatrix[row][indexFound] == 0){
						result[row][indexFound] = 1;
						break;
					}
				}
				colZeros[indexFound] = 0;
				for(int i = 0; i < colZeros.length; i++){
					if(colZeros[i] == 0)
						rowZeros[i] -= 1;
				}
			}
		}while(rowOrCol != 0);
		
		return result;
	}
}
