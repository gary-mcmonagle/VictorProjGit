package hiPkg;

import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class Test {


	private static Connection conn = new ConnectionManager("root", "root").getConnection();
	
	
	//method to write an ocustomer object to the db.
	//only works for new customers as change setting needed. Update in sql
	public static int writeClientToDataBase(Client clientIn) throws SQLException{
		//if (myCu.isExisting){
			//write for updating needed
		//else{
		String sql = "insert into client (first_name, last_name, policy_id, age) values (?,?,?,?)";
		//prepare statement and return the auto generated key. This is the auto incremented customerID
		PreparedStatement myStmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS );
		myStmt.setString(1, clientIn.getFname());
		myStmt.setString(2, clientIn.getLname());
		myStmt.setInt(3, clientIn.getPolicyNo());
		myStmt.setInt(4, clientIn.getAge());
		myStmt.executeUpdate();
		ResultSet rs = myStmt.getGeneratedKeys();
		rs.next();
		//return the generated number which will bu used to write to normalized table
		return rs.getInt(1);

	}
	public static void writeClientConditions(int clientId, ArrayList<Condition> conds) throws SQLException{
		for(int i = 0; i<conds.size(); i++){
			String sql = "insert into client_term (client_id, term_id) values (?,?)";
			PreparedStatement myStmt = conn.prepareStatement(sql);
			myStmt.setInt(1,clientId);
			myStmt.setInt(2, conds.get(i).getID());
			myStmt.executeUpdate();

		}
		
	}
	public static Policy readPolicy(int policyNo) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "select policy.contact_no, policy.email, policy_type_id from policy where policy.policy_id = "+policyNo+";";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		String conNo = rs.getString(1);
		String email = rs.getString(2);
		int polType = rs.getInt(3);
		Statement clientStmt = conn.createStatement();
		String clientQuery = "select client.client_id, client.first_name, client.last_name, client.age from client, policy where client.policy_id = policy.policy_id and policy.policy_id = "+policyNo+";";
		ResultSet clientRs = clientStmt.executeQuery(clientQuery);
		ArrayList<Client> myClientArray = new ArrayList<Client>(); 
		while (clientRs.next()){
			ArrayList<Condition> myConditionArray = new ArrayList<Condition>();
			Statement condStmt = conn.createStatement();
			String condQuery = "select term.name, term.factor, term.term_id from term, client, client_term where client.client_id = client_term.client_id and term.term_id = client_term.term_id and client.client_id ="+clientRs.getInt(1)+";";
			ResultSet condRs = condStmt.executeQuery(condQuery);
			while (condRs.next()){
				myConditionArray.add(new Condition(condRs.getInt(3), condRs.getString(1), condRs.getInt(2)));
			}
			myClientArray.add(new Client(clientRs.getString(2), clientRs.getString(3), clientRs.getInt(4), myConditionArray));	
		}
		Statement typeStmt = conn.createStatement();
		String typeQuery = "select policy_type.name, policy_type.percentage_impact, policy_type.policy_type_id from policy, policy_type where policy.policy_type_id = policy_type.policy_type_id and policy.policy_id =" + policyNo+";";
		ResultSet typeRs = clientStmt.executeQuery(typeQuery);
		typeRs.next();		
		PolicyType pt = new PolicyType(typeRs.getString(1), typeRs.getInt(2), typeRs.getInt(3));
		
		return new Policy(myClientArray, conNo, email, pt);
	}
	public static void main(String[] args) throws SQLException{
		
//		Policy p = DatabaseManager.readPolicy(2);
//		
//		Client a = DatabaseManager.readPolicy(2).getClients().get(0);
//		Client b = DatabaseManager.readPolicy(2).getClients().get(0);
//		ArrayList<Client> j = p.getClients();
//		j.remove(0);
//		p.setClients(j);
//		//p.setClients(clients);
//		b.setAge(200);
//		b.setFname("Frank");
//		b.setLname("Hoolahan");
//		Condition c = new Condition(2, "Deaf", 100);
//		ArrayList<Condition> t = b.getConditions();
//		t.add(c);
//		DatabaseManager.updatePolicy(DatabaseManager.readPolicy(2), p);
//		b.setConditions(new ArrayList<Condition>()); 
//		
//		//DatabaseManager.updateClient(b,a);
//		DatabaseManager.updatePolicy(DatabaseManager.readPolicy(2), p);

//		ArrayList<Condition> cs = new ArrayList<Condition>();
//		cs.add(new Condition(1, "a", 100));
//		ArrayList<Client> cls = new ArrayList<Client>(); 
//		cls.add(new Client("Gary", "McMonagle", 23, cs));
//		PolicyType pt = new PolicyType("name", 10, 1)
//		Policy p = new Policy(cls, "0863007837", "gary.mcmonagle@gmail.com", pt);
//		DatabaseManager.writePolicy(p);
//		
//		Policy p = DatabaseManager.readPolicy(4);
//		p.getClients().get(0).setAge(30);
//		p.getType().setId(2);
//		p.setContactNo("0749110168");
//				System.out.println(DatabaseManager.readPolicy(4).getId());
////		System.out.println(p.getType().getId());
//		p.setEmail("gary@hotmail.com");
//		DatabaseManager.updatePolicy(DatabaseManager.readPolicy(4), p);
		
		//DatabaseManager.deletePolicy(4);
		

	}
}
