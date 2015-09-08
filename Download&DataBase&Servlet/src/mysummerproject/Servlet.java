package mysummerproject;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Servlet extends HttpServlet {

	public doGet(HttpServletRequest req, HttpServletResponse res) 
				throws Exception {
		
		java.sql.Statement stat = null;
		Connection db = null;
		
		try {
			db = ConnectionToDataBase.getConnectionToDataBase();
			stat = db.createStatement();
			ResultSet rs = stat.executeQuery("select * from downloadList");
					
			res.setContentType("text/html;charset=utf-8");
			
			PrintWriter printWriter = req.getWriter();
			
			while(rs.next()) {
				
				printWriter.println("<html><head>");
				printWriter.println("<title>Servlet</title></head><body>");
				printWriter.println("<H1>rs.getString(\"name\")</H1>");
				printWriter.println("<H1>rs.getString(\"size\")</H1>");
				printWriter.println("<H1>rs.getString(\"path\")</H1>");
				printWriter.println("<H1>rs.getString(\"extension\")</H1>");
				printWriter.println("<H1>**********************</H1>");
				printWriter.println("</body></html>");
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
}
