import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class PHPMyAdmin {
	
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = null;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:8889/jdbc","root", "12345678");
			System.out.println("Database is connected !");
			
			Statement st = conn.createStatement();
			st.execute(String.format("INSERT INTO a (k, v) VALUES ('%s', '%s')", String.valueOf((new Date()).getTime()), String.valueOf((new Date()).getTime())));
			
			ResultSet r = st.executeQuery("SELECT * FROM a");
			while (r.next()) {
				System.out.println(String.format("id: %d, k: %s, v: %s", r.getLong("id"), r.getString("k"), r.getString("v")));
			}
			conn.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
