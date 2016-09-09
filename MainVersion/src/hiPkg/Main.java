package hiPkg;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Main {

	public static void main(String[] args) throws SQLException {
		int user = -1;
		Scanner keyboardIn = new Scanner(System.in);
		do{
			/*Added this in 09/09/16
			 * 
			 * 
			 */
			System.out.println("*******************************************");
			System.out.println("*");
			System.out.println("*"+ "WELCOME TO THE HEALTH INSURANCE CALCULATOR");
			System.out.println("*");
			System.out.println("*******************************************");

			System.out.println("1. New Policy\n2. Admin\n0. Exit");
			user = keyboardIn.nextInt();
			
			if(user == 1){
				boolean uniqueEmail = false;
				String email = "";
				do{
					System.out.println("Please Input email");
					email = keyboardIn.next();
					if(DatabaseManager.isUniqueEmail(email))
						uniqueEmail = true;
					else
						System.out.println("Invalid Email");
				}while(!uniqueEmail);
				System.out.println("Please Enter Contact No");
				String contactNo = keyboardIn.next();
				System.out.println("Please Select Policy Type");
				/*Added this in 09/09/16
				 * 
				 * 
				 */
				System.out.println("Policy Types: GOLD-25% INCREASE PREMIUM CARE +PRIVATE ROOM");
				System.out.println("Policy Type: SILVER-10% INCREASE PRIVATE ROOM");
				System.out.println("Policy Type: Bronze-5% INCREASE BASIC POLICY");

				int policyChosen = 0; 
				ArrayList<PolicyType> policyTypes = DatabaseManager.readPolicyTypes();
				for(int i = 0; i< policyTypes.size(); i++){
					System.out.println(i+1 + ". "+policyTypes.get(i).getName());
				}
				policyChosen = keyboardIn.nextInt();
				ArrayList<Client> policyClients = new ArrayList<Client>();
				System.out.println("Please enter number of customer for policy");
				int clientsNeeded = keyboardIn.nextInt();
				for(int i = 0; i<clientsNeeded; i++){
					policyClients.add(createClientMenu());		
				}
				DatabaseManager.writePolicy(new Policy(policyClients, contactNo, email, policyTypes.get(policyChosen)));
			
		
			Writer writer = null;

			try {
				{
					   Calendar cal = Calendar.getInstance();
					   DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL,
					        DateFormat.MEDIUM);

					    System.out.println(df.format(cal.getTime()));
			    writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream("policyTesterFile.txt"), "utf-8"));
			    writer.write("Conditions: " +policyClients+email+contactNo+policyChosen+df);
			}
			}
				catch (IOException ex) {
			  // report
			} finally {
			   try {writer.close();} catch (Exception ex) {/*ignore*/}
			}
			// Instantiate a Date object
			}
			//launch login
			/*
			 * Added 9/09/16
			 */
			

			loginScreen.Login.main(args);
			
			if(user == 2){
				System.out.println("Enter Admin Username");
				String username = keyboardIn.next();
				System.out.println("Enter Admin Password");
				String pass = keyboardIn.next();
				
				boolean isAdmin = DatabaseManager.isAdmin(username, pass);
				if(isAdmin){
					int adminSelect = -1;
					do{
						System.out.println("1. View Policy. \n2. Edit Policy\n3. Delete Policy\n0. Exit");
						adminSelect = keyboardIn.nextInt();
						if(adminSelect == 1){
							System.out.println("Plase Enter Policy Number");
							int pNo = keyboardIn.nextInt();
							if(DatabaseManager.isPolicyNumber(pNo)){
								System.out.println(DatabaseManager.readPolicy(pNo));
							}
							else
								System.out.println("Policy Not Found");
						}
						//dialogue box popup of one to edit, create a policy and call method from dbm
						if(adminSelect == 2){
							System.out.println("Plase Enter Policy Number");
							int pNo = keyboardIn.nextInt();
							if(DatabaseManager.isPolicyNumber(pNo)){
								Policy orig = DatabaseManager.readPolicy(pNo);
								Policy updated = DatabaseManager.readPolicy(pNo);
								int editMenu = -1;
								do{
									System.out.println("1. Change Policy Type\n2. Edit Email\n3. Edit Contact Number\n4. Add Person to Policy\n5. Delete Client From Policy\n6. Edit Client within Policy\n0. Next" );
									editMenu = keyboardIn.nextInt();
									if(editMenu == 1){
										updated.setType(createPolicyTypeMenu());
									}
									//VALIDATION NEEDED HERE!!!
									if(editMenu == 2){
										System.out.println("Please enter new email address");
										String updatedEmail = keyboardIn.next();
										updated.setEmail(updatedEmail);
									}
									if(editMenu == 3){
										System.out.println("Please enter new Contact Number");
										String updatedContact= keyboardIn.next();
										updated.setContactNo(updatedContact);
									}
									if(editMenu == 4){
										ArrayList<Client> myArray = updated.getClients();
										myArray.add(createClientMenu());
										updated.setClients(myArray);
									}
									if (editMenu == 5){
										System.out.println("Please Select client to Delete");
										for(int i = 0; i<updated.getClients().size(); i++){
											System.out.println((i+1)+". "+updated.getClients().get(i).getFname()+ " "+updated.getClients().get(i).getLname());
											
										}
										System.out.println("0. Exit");
										ArrayList<Client> myArray = updated.getClients();
										int toRemove = keyboardIn.nextInt()-1;
										if(toRemove != -1){
											myArray.remove(toRemove);
											updated.setClients(myArray);
											System.out.println("Client Deleted");
											//delete policy if no clients left
											if(updated.getClients().size()==0){
												DatabaseManager.deletePolicy(pNo);
												System.out.println("Policy Deleted");
												editMenu = 0;
											}
										}
										
									}
									if(editMenu == 6){
										ArrayList<Client> myArray = updated.getClients();
										System.out.println("Please select client to edit");
										for(int i = 0; i<myArray.size(); i++){
											System.out.println((i+1)+ ". " +myArray.get(i).getFname() + " " + myArray.get(i).getLname());
										}
										System.out.println("0. Exit");
										int selectedClientForEdit = keyboardIn.nextInt()-1;
										if(selectedClientForEdit != -1){
											int selectedEdit = -1;
											do{
											System.out.println("1. Edit First Name\n2. Edit Last Name\n3. Edit Age\n4. Edit Conditions\n0. Exit" );
											selectedEdit = keyboardIn.nextInt();
											if(selectedEdit == 1){
												System.out.println("Please Enter First Name");
												myArray.get(selectedClientForEdit).setFname(keyboardIn.next());	
											}
											if(selectedEdit == 2){
												System.out.println("Please Enter Last Name");
												myArray.get(selectedClientForEdit).setLname(keyboardIn.next());	
											}
											if(selectedEdit == 3){
												System.out.println("Please Enter Age");
												myArray.get(selectedClientForEdit).setAge(keyboardIn.nextInt());	
											}
											if(selectedEdit == 4){
												ArrayList<Condition> avCons = DatabaseManager.readConditions();
												ArrayList<Condition> cliCons = new ArrayList<Condition>();
												System.out.println("Please Enter First Name");
												int userConditions = -1;
												do{
													System.out.println("Please Select If Any Relate To You");
													for(int j = 0; j<avCons.size(); j++){
														System.out.println((j+1) + ". "+avCons.get(j).getName());
													}
													System.out.println("0. Next");
													userConditions = keyboardIn.nextInt();
													if(userConditions != 0){
														cliCons.add(avCons.get(userConditions-1));
														avCons.remove(userConditions-1);
														//System.out.println(availCons);
													}	
													
												}while(userConditions != 0);
												myArray.get(selectedClientForEdit).setConditions(cliCons);
											}
												
											}while(selectedEdit!=0);
										}
										updated.setClients(myArray);
									}
								}while(editMenu != 0);
								DatabaseManager.updatePolicy(orig, updated);
							}
							else
								System.out.println("Policy Not Found");
						}
						if (adminSelect == 3){
							System.out.println("Plase Enter Policy Number");
							int pNo = keyboardIn.nextInt();
							if(DatabaseManager.isPolicyNumber(pNo)){
								System.out.println(DatabaseManager.readPolicy(pNo));
								System.out.println("Are you sure you want to delete this policy ?(y/n)");
								if (keyboardIn.next().equals("y"))
									DatabaseManager.deletePolicy(pNo);
									
							}
							else
								System.out.println("Policy Not Found");
							
						}
					}while(adminSelect != 0);
				}
				else
					System.out.println("Invalid Password");
			
			}
		}while(user != 0);
		keyboardIn.close();

	}
	public static boolean validateInput(String input){
		boolean valid = true;
		for(int i = 0; i<input.length(); i++){
			if (!Character.isDigit(input.charAt(i)))
					valid = false;
		}
		return valid;
		
	}
	public static Client createClientMenu() throws SQLException{
		Scanner keyboardIn = new Scanner(System.in);
		//System.out.println("Client Number "+ (numberIn+1));
		System.out.println("Please enter First Name");
		String fName = keyboardIn.next();
		System.out.println("Please enter Last Name");
		String lName = keyboardIn.next();
		System.out.println("Please enter Age");
		int age = keyboardIn.nextInt();
		
		ArrayList<Condition> availCons = DatabaseManager.readConditions();
		ArrayList<Condition> custCons = new ArrayList<Condition>();
		int userConditions = -1;
		
		
		do{
			System.out.println("Please Select If Any Relate To You");
			for(int j = 0; j<availCons.size(); j++){
				System.out.println(j+1 + ". "+availCons.get(j).getName());
			}
			System.out.println("0. Next");
			userConditions = keyboardIn.nextInt();
			if(userConditions != 0){
				//-1 adjustment
				//userConditions--;
				custCons.add(availCons.get(userConditions-1));
				availCons.remove(userConditions-1);
			}	
		//IS THIS WORKING????
			
		}while(userConditions != 0);
		//keyboardIn.close();
		return new Client(fName, lName, age, custCons);
		
		
	}
	public static PolicyType createPolicyTypeMenu() throws SQLException{
		Scanner keyboardIn = new Scanner(System.in);
		System.out.println("Please Select Policy Type");
		int policyChosen = 0; 
		ArrayList<PolicyType> policyTypes = DatabaseManager.readPolicyTypes();
		for(int i = 0; i< policyTypes.size(); i++){
			System.out.println(i+1 + ". "+policyTypes.get(i).getName());
		}
		policyChosen = keyboardIn.nextInt();
		return policyTypes.get(policyChosen-1);
		
	}

}