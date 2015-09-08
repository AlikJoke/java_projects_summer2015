package mysummerproject;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
	
	private String[] pars(File fileList) {
			
			final String[] ext = {"mp3", "mp4", "jpeg", "jpg", "png", "gif", "txt", "pdf", "doc", "docx"};
			String fileName = fileList.getName();
			String fileExtension = "";
			
			for(int i = 0; i < ext.length; i++) {
				if (fileList.getName().contains(ext[i])) {
					
					int index = fileList.getName().indexOf(ext[i]);
					fileName = fileList.getName().substring(0, index - 1);
					if (ext[i].length() == 3) 
						fileExtension = fileList.getName().substring(index, index + 3);
					else 
						fileExtension = fileList.getName().substring(index, index + 4);
				}
			}
			String[] NameExt = {fileName, fileExtension};
			return NameExt;
		}
	
	private void insertToTable(PreparedStatement prepareStatement, File[] fileList) throws SQLException {
		
		for (int u = 0; u < fileList.length; u++) {
			
			if (fileList[u].isFile() && !(fileList[u].isHidden())) {
				
				String fileName = pars(fileList[u])[0];
				String fileExtension = pars(fileList[u])[1];
				
				int index = fileList[u].getPath().indexOf(fileName);
				String filePath = fileList[u].getPath().substring(0, index);
				
				prepareStatement.setString(1, fileName);
				prepareStatement.setString(2, filePath);
				prepareStatement.setInt(3, (int) fileList[u].length());
				prepareStatement.setString(4, fileExtension);
				
				prepareStatement.execute();
				}
		}
	}
	
	public void actionWithDownloadList() {
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			
			connection = ConnectionToDataBase.getConnectionToDataBase();
			
			File file = new File("/home/darkside/workspace/Download&DataBase&Servlet");
			File[] fileList = file.listFiles();
			
			String addToTable = "insert into downloadList (name, path, size, extension) values (?, ?, ?, ?)";
			String delete = "delete from downloadList";
			
			statement = connection.createStatement();
			PreparedStatement prepareStatement = connection.prepareStatement(addToTable);
			
			statement.executeUpdate(delete);
			insertToTable(prepareStatement, fileList);
			
			ResultSet rs3 = statement.executeQuery("select * from downloadList");
			
			while (rs3.next()) {
				
				System.out.println(rs3.getString("name"));
				System.out.println(rs3.getString("size"));
				System.out.println(rs3.getString("path"));
				System.out.println(rs3.getString("extension"));
				System.out.println("**********************");
			}
		} catch (Exception e) {
			
			System.out.print(e.getMessage());
		} finally {
			
			if (connection != null) {
				
				try {
					connection.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
			
			if (statement != null) {
				
				try {
					statement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		}
		
			
	}
}
