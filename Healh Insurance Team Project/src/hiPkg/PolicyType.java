package hiPkg;

public class PolicyType {
	
	private String name = "";
	private int impact = -1;
	private int id;
	public PolicyType(String name, int impact, int id) {
		this.name = name;
		this.impact = impact;
		this.id = id;
	} 
	public PolicyType(int id){
		this.id = id; 
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImpact() {
		return impact;
	}
	public void setImpact(int impact) {
		this.impact = impact;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	} 
	public String toString(){
		return "Policy Type: " + name;
	}
	

}
