package mysummerproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDataBase {
	
	private static Connection getConnectionToDataBase() {
		
		Connection connection = null;;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://darkside/mysql";
			String user = "root";
			String password = "ushuist";
			
			connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void createDataBase() {
		
		Connection connection = getConnectionToDataBase();
		String sql = "CREATE DATABASE downloads";
		
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		
		createDataBase();
	}

}
