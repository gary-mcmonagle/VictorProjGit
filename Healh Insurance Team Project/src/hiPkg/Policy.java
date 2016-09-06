package hiPkg;

import java.util.ArrayList;

public class Policy {
	
	private int id;
	private ArrayList<Client> clients;
	private String contactNo;
	private String email;
	private PolicyType type;
	

	public Policy(ArrayList<Client> clients, String contactNo, String email, PolicyType type) {
		this.clients = clients;
		this.contactNo = contactNo;
		this.email = email;
		this.type = type;
	}
	public Policy(ArrayList<Client> clients, String contactNo, String email, PolicyType type, int id) {
		this.clients = clients;
		this.contactNo = contactNo;
		this.email = email;
		this.type = type;
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<Client> getClients() {
		return clients;
	}
	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public PolicyType getType() {
		return type;
	}
	public void setType(PolicyType type) {
		this.type = type;
	}
	public String toString(){
		return type + "\n" + clients;
	}
	
	
	

}
