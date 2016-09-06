package hiPkg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseManager {
	
	private static Connection conn = new ConnectionManager("root", "root").getConnection();
	
	private static int writeClientToDataBase(Client clientIn) throws SQLException{
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
	private static void writeClientConditions(int clientId, ArrayList<Condition> conds) throws SQLException{
		for(int i = 0; i<conds.size(); i++){
			String sql = "insert into client_term (client_id, term_id) values (?,?)";
			PreparedStatement myStmt = conn.prepareStatement(sql);
			myStmt.setInt(1,clientId);
			myStmt.setInt(2, conds.get(i).getID());
			myStmt.executeUpdate();

		}
		
	}
	public static void writePolicy(Policy myPolicy) throws SQLException{
		String sql = "insert into policy (contact_no, email, policy_type_id) values (?,?,?)";
		PreparedStatement myStmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS );
		myStmt.setString(1, myPolicy.getContactNo());
		myStmt.setString(2, myPolicy.getEmail());
		myStmt.setInt(3, myPolicy.getType().getId());
		myStmt.executeUpdate();
		ResultSet rs = myStmt.getGeneratedKeys();
		rs.next();
		for(int i = 0; i<myPolicy.getClients().size(); i++){
			myPolicy.getClients().get(i).setPolicyNo(rs.getInt(1));
			writeClientConditions(writeClientToDataBase(myPolicy.getClients().get(i)), myPolicy.getClients().get(i).getConditions()); 
		}
		
		
	}
	public static Policy readPolicy(int policyNo) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "select policy.contact_no, policy.email, policy_type_id from policy where policy.policy_id = "+policyNo+";";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		String conNo = rs.getString(1);
		String email = rs.getString(2);
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
			Client a = new Client(clientRs.getString(2), clientRs.getString(3), clientRs.getInt(4), myConditionArray, clientRs.getInt(1));
			a.setExisting();
			myClientArray.add(a);	
		}
		Statement typeStmt = conn.createStatement();
		String typeQuery = "select policy_type.name, policy_type.percentage_impact, policy_type.policy_type_id from policy, policy_type where policy.policy_type_id = policy_type.policy_type_id and policy.policy_id =" + policyNo+";";
		ResultSet typeRs = typeStmt.executeQuery(typeQuery);
		typeRs.next();		
		PolicyType pt = new PolicyType(typeRs.getString(1), typeRs.getInt(2), typeRs.getInt(3));
		
		return new Policy(myClientArray, conNo, email, pt, policyNo);
	}
	public static ArrayList<Condition> readConditions() throws SQLException{
		ArrayList<Condition> myArray = new ArrayList<Condition>();
		Statement stmt = conn.createStatement();
		String query = "select term.term_id, term.name, term.factor from term;";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()){
			myArray.add(new Condition(rs.getInt(1), rs.getString(2), rs.getInt(3)));
		}
		return myArray;
	}
	public static ArrayList<PolicyType> readPolicyTypes() throws SQLException{
		ArrayList<PolicyType> myArray = new ArrayList<PolicyType>();
		Statement stmt = conn.createStatement();
		String query = "select policy_type.name, policy_type.percentage_impact, policy_type.policy_type_id from policy_type;";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()){
			myArray.add(new PolicyType(rs.getString(1), rs.getInt(2), rs.getInt(3)));
		}
		return myArray;
	}
	
	public static void updateClient(Client newVersion, Client origVersion) throws SQLException{

		String sql = "";
		if (origVersion.getAge() != newVersion.getAge()){ 
			sql = "UPDATE client SET client.age = ? WHERE client.client_id = ?;";
			PreparedStatement myAgeStmt = conn.prepareStatement(sql);
			myAgeStmt.setInt(1, newVersion.getAge());
			myAgeStmt.setInt(2, origVersion.getClientNo());
			myAgeStmt.executeUpdate();
		}
		if (origVersion.getFname() != newVersion.getFname()){
			sql = "UPDATE client SET client.first_name = ? WHERE client.client_id = ?;";
			PreparedStatement myAgeStmt = conn.prepareStatement(sql);
			myAgeStmt.setString(1, newVersion.getFname());
			myAgeStmt.setInt(2, origVersion.getClientNo());
			myAgeStmt.executeUpdate();
		}
		if (origVersion.getLname() != newVersion.getLname()){
			sql = "UPDATE client SET client.last_name = ? WHERE client.client_id = ?;";
			PreparedStatement myAgeStmt = conn.prepareStatement(sql);
			myAgeStmt.setString(1, newVersion.getLname());
			myAgeStmt.setInt(2, origVersion.getClientNo());
			myAgeStmt.executeUpdate();
		}
		
		if(newVersion.getConditions().size() == origVersion.getConditions().size()){
			int matches = 0;
			for(int i = 0; i<origVersion.getConditions().size(); i++){
				//boolean match = false;
				for (int j = 0; j<origVersion.getConditions().size(); i++){
					if(newVersion.getConditions().get(i).getID() == origVersion.getConditions().get(j).getID()){
						matches++;
						break;
					}
				}
			}
			if (matches == newVersion.getConditions().size())
				return;
		}
		String query = "delete from client_term where client_term.client_id = ? ;";
		PreparedStatement myDelStmt = conn.prepareStatement(query);
		myDelStmt.setInt(1,  origVersion.getClientNo());
		myDelStmt.executeUpdate();
		writeClientConditions(newVersion.getClientNo(), newVersion.getConditions());
		
	}
	//admin priv
	public static void updatePolicy(Policy origPol, Policy newPolicy) throws SQLException{
		String sql = "";
		//newPolicy.setId(origPol.getId());
		if (!(newPolicy.getContactNo().equals(origPol.getContactNo()))){
			sql = "UPDATE policy SET policy.contact_no = ? WHERE policy.policy_id = ?;";
			PreparedStatement myConStmt = conn.prepareStatement(sql);
			myConStmt.setString(1, newPolicy.getContactNo());
			myConStmt.setInt(2, origPol.getId());
			myConStmt.executeUpdate();
		}
		
		if (newPolicy.getType().getId() != origPol.getType().getId()){
			sql = "UPDATE policy SET policy.policy_type_id = ? WHERE policy.policy_id = ?;";
			PreparedStatement myTypeStmt = conn.prepareStatement(sql);
			myTypeStmt.setInt(1, newPolicy.getType().getId());
			myTypeStmt.setInt(2, origPol.getId());
			myTypeStmt.executeUpdate();
		}
		
		if (newPolicy.getEmail() != origPol.getEmail()){
			sql = "UPDATE policy SET policy.email = ? WHERE policy.policy_id = ?;";
			PreparedStatement myEmailStmt = conn.prepareStatement(sql);
			myEmailStmt.setString(1, newPolicy.getEmail());
			myEmailStmt.setInt(2, origPol.getId());
			myEmailStmt.executeUpdate();
		}		
		ArrayList<Client> origList = origPol.getClients();
		ArrayList<Client> newList = newPolicy.getClients();
//		Collections.sort(origList);
//		Collections.sort(newList);
		for(int i = 0; i<newList.size(); i++){
			if(newList.get(i).getClientNo() == -1)
				writeClientToDataBase(newList.get(i));
		}
		//check orig list for updates
		for (int i = 0; i<origList.size(); i++){
			boolean found = false;
			for(int j = 0; i<newList.size(); i++){
				if(origList.get(i).getClientNo() == newList.get(j).getClientNo()){
					found = true;
					updateClient(newList.get(j), origList.get(i));	
				}
			}
			if(!found){
				String query = "delete from client_term where client_term.client_id = ? ;";
				PreparedStatement myCondDelStmt = conn.prepareStatement(query);
				myCondDelStmt.setInt(1,  origList.get(i).getClientNo());
				myCondDelStmt.executeUpdate();
				query = "delete from client where client.client_id = ? ;";
				PreparedStatement myClientDelStmt = conn.prepareStatement(query);
				myClientDelStmt.setInt(1,  origList.get(i).getClientNo());
				myClientDelStmt.executeUpdate();
			}
			
		}

		
		
	}
	public static boolean isUniqueEmail(String emailIn) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "select count(policy.email) from policy where policy.email = '"+emailIn+"';";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		return rs.getInt(1) == 0;
	}
	public static void deletePolicy(int policyNo) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "select client.client_id from client, policy where policy.policy_id = client.policy_id and policy.policy_id ="+policyNo+";";
		ResultSet clientRs = stmt.executeQuery(query);
		while(clientRs.next()){
			query = "delete from client_term where client_term.client_id = ?;";
			PreparedStatement myClientCondDelStmt = conn.prepareStatement(query);
			myClientCondDelStmt.setInt(1, clientRs.getInt(1));
			myClientCondDelStmt.executeUpdate();
			query = "delete from client where client.client_id = ?;";
			PreparedStatement myClientDelStmt = conn.prepareStatement(query);
			myClientDelStmt.setInt(1, clientRs.getInt(1));
			myClientDelStmt.executeUpdate();
		}
		query = "delete from policy where policy.policy_id = ?;";
		PreparedStatement myPolDelStmt = conn.prepareStatement(query);
		myPolDelStmt.setInt(1, policyNo);
		myPolDelStmt.executeUpdate();
		
		
	}
	public static boolean isPolicyNumber(int policyNoIn) throws SQLException{
		Statement stmt = conn.createStatement();
		String query = "select count(policy.policy_id) from policy where policy.policy_id = '"+policyNoIn+"';";
		ResultSet rs = stmt.executeQuery(query);
		rs.next();
		return rs.getInt(1) == 1;
		
	}

}
