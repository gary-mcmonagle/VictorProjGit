//Author: Team Victor
//Desc: Create an object which can be written to database
//Are setters needed?
//Needs own class as written to arraylist in customer class
//Date 20/8/16


package hiPkg;

public class Condition {
	
	private int id;
	private String name;
	private int degree;
	
	public Condition(int id,String name, int degree) {
		this.name = name;
		this.degree = degree;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public int getID(){
		return id;
	}
	public String toString(){
		return "Name: " + name + "Degree: " + degree;
	}
}