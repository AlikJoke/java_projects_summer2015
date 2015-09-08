package mysummerproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionToDataBase {

	static String url = "jdbc:mysql://darkside/downloads" +
                "?characterEncoding=utf8";
	static String user = "root";
	static String password = "ushuist";
	
	public static Connection getConnectionToDataBase() {
		
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (ClassNotFoundException | SQLException e) {
			
			e.printStackTrace();
		}
		return connection;
	}
	
	private static void createTable() {
		
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			Statement statement = connection.createStatement();
			
			String sqlCreateTable = "CREATE TABLE `downloadList` (" +
		            "  `id` int(11) NOT NULL auto_increment," +
		            "  `name` varchar(130) default NULL," +
		            "  `path` varchar(240) default NULL," +
		            "  `size` int(11) default NULL," +
		            "  `extension` varchar(50) default NULL," +
		            "  PRIMARY KEY  (`id`)" +
		            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
			
			statement.executeUpdate(sqlCreateTable);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		createTable();
	}
}
