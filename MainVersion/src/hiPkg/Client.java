package hiPkg;

import java.util.ArrayList;

public class Client{// implements Comparable<Client> {
	
	private int clientNo = -1;
	private String fname;
	private String lname;
	private int age;
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private boolean existing = false;
	private int policyNo = -1;
	
	public Client(String fname, String lname, int age, ArrayList<Condition> cons) {
		this.fname = fname;
		this.lname = lname;
		this.age = age;
		conditions = cons;
	}
	public Client(String fname, String lname, int age, ArrayList<Condition> cons, int clientNo) {
		this.fname = fname;
		this.lname = lname;
		this.age = age;
		conditions = cons;
		this.clientNo = clientNo;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(int policyNo) {
		this.policyNo = policyNo;
	}
	public boolean isExisting(){
		return existing;
	}
	//trips exisiting boolean
	public void setExisting(){
		existing = true; 
		
	}
	public ArrayList<Condition> getConditions(){
		return conditions; 
	}
	public void setConditions(ArrayList<Condition> array){
		conditions = array;
	}
	//only needed until GUI set up
	public String toString(){
		String cons = "Conditions\n";
		//enhanced for loop could also be used
		for(int i = 0; i<conditions.size(); i++){
			cons += conditions.get(i).getName()+"\n";
		}
		return "Name : " + fname + " " + lname+"\nAge: " + age+ "\n" + cons; 
	}
	public void setClientNo(int clientNo){
		this.clientNo = clientNo;
		
	}
	public int getClientNo(){
		return clientNo;
	}
//	public boolean equals(Client other){
//		return other.clientNo == clientNo; 
//	}
//	@Override
//	public int compareTo(Client other) {
//		Integer a = new Integer(clientNo);
//		Integer b = new Integer(other.clientNo);
//		return a.compareTo(b);
//		
//	}

	
	

}
