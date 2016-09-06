package hiPkg;

//http://stackoverflow.com/questions/10915375/create-a-class-to-connect-to-any-database-using-jdbc

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class ConnectionManager {
	
		
	
	    private static String url = "jdbc:mysql://localhost:3306/healthinsurance";    
	    private static String driverName = "com.mysql.jdbc.Driver";   
	    private static String username = "root";   
	    private static String password = "root";
	    private static Connection con;

	    
	    //default for workbench
	    public ConnectionManager(){}
	    
	    public ConnectionManager(String userIn, String passIn){
	    	username = userIn;
	    	password = passIn;
	    }
	    
	    public ConnectionManager(String urlIn){
	    	url = urlIn;
	    }
	    
	    public Connection getConnection() {
	        try {
	            Class.forName(driverName);
	            try {
	                con = DriverManager.getConnection(url, username, password);
	            } catch (SQLException ex) {
	                // log an exception. from example:
	                System.out.println("Failed to create the database connection."); 
	            }
	        } catch (ClassNotFoundException ex) {
	            // log an exception. for example:
	            System.out.println("Driver not found."); 
	        }
	        return con;
	    }
	}
