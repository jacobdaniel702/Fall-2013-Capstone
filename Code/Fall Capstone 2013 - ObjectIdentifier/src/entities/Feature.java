package entities;

public class Feature {
	private String name;
	private Object attribute;
	
	public Feature(String name, Object attribute){
		this.name = name;
		this.attribute = attribute;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getAttribute() {
		return attribute;
	}
	public void setAttribute(Object attribute) {
		this.attribute = attribute;
	}
}
