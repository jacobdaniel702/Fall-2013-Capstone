package entities;

public class Classification {
	private String name;

	public Classification(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		if(!(obj instanceof Classification))
			return false;
		
		Classification c = (Classification)obj;
		return this.name == c.name;
	}
}
