package shapeContext;

public class LogPolarHistogram {
	final int RADIALSEPARATIONS = 5;
	final int ANGULARSEPARATIONS = 12;
	
	private Bin[][] bins;
	
	public LogPolarHistogram(){
		bins = new Bin[RADIALSEPARATIONS][ANGULARSEPARATIONS];
		initializeBins();
	}
	
	private void initializeBins(){
		for(int i = 0; i < RADIALSEPARATIONS; i++){
			for(int j = 0; j < ANGULARSEPARATIONS; j++){
				bins[i][j] = new Bin(.125*Math.pow(2, i),2*(j+1)*Math.PI/ANGULARSEPARATIONS);
			}
		}
	}
	
	public void addPoint(double r, double theta){
		for(int i = 0; i < bins.length; i++){
			for(int j = 0; j < bins[i].length; j++){
				if(bins[i][j].getR() > r && bins[i][j].getTheta() > theta){
					bins[i][j].increment();
					return;
				}
			}
		}
	}
	
	public int getNumBins(){
		return RADIALSEPARATIONS*ANGULARSEPARATIONS;
	}
	
	public int getBinCount(int k){
		int row = k/ANGULARSEPARATIONS;
		int col = k%12;
		
		return bins[row][col].count;
	}
	
	public int getTotalBinCount(){
		int count = 0;
		
		for(int i = 0; i < bins.length; i++){
			for(int j = 0; j < bins[i].length; j++){
				count += bins[i][j].getCount();
			}
		}
		
		return count;
	}
}
