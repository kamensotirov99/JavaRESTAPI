import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class DBInitializer {
	
	private static final String DB_NAME = "userDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "pass";

    @PostConstruct
    public void initialize() {
        try {
			createDatabaseIfNotExists();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    
    public static void createDatabaseIfNotExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        }
    }
}