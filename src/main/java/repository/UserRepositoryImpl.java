package repository;

import java.util.List;
import java.sql.*;
import java.util.*;

import models.Address;
import models.User;

public class UserRepositoryImpl implements UserRepository{

	private Connection conn;
	
	public UserRepositoryImpl() {
		try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/userdb";
            String username = "root";
            String password = "pass";
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		try {
			createAddressesTable();
			createUsersTable();	
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createUsersTable() throws SQLException {
        try (
             Statement stmt = this.conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users("
            		+ "user_id INT PRIMARY KEY AUTO_INCREMENT,"
            		+ "name VARCHAR(100),"
            		+ "title VARCHAR (300),"
            		+ "work VARCHAR (300),"
            		+ "address_id INT,"
            		+"CONSTRAINT fk_address_id FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON UPDATE CASCADE)";
            stmt.executeUpdate(sql);
        }catch(SQLException e) {
        	e.printStackTrace();
        }
    }
	
	private void createAddressesTable() throws SQLException {
        try (
             Statement stmt = this.conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS addresses("
            		+ "address_id INT PRIMARY KEY AUTO_INCREMENT,"
            		+ "city VARCHAR(100),"
            		+ "postcode INT,"
            		+ "street_name VARCHAR (300),"
            		+ "street_number INT) ";
            stmt.executeUpdate(sql);
            stmt.close();
        }catch(SQLException e) {
        	e.printStackTrace();
        }
    }
	
	public void createUser(User user) throws SQLException {
		//check if the address exists in the database
		String checkAddressSql = "SELECT address_id FROM addresses WHERE city = ? AND postcode = ? AND street_name = ? AND street_number = ?";
        PreparedStatement checkAddressStmt = conn.prepareStatement(checkAddressSql);
        checkAddressStmt.setString(1, user.getAddress().getCity());
        checkAddressStmt.setInt(2, user.getAddress().getPostCode());
        checkAddressStmt.setString(3, user.getAddress().getStreetName());
        checkAddressStmt.setInt(4, user.getAddress().getStreetNumber());
        ResultSet addressResult = checkAddressStmt.executeQuery();

        int addressId;
        if (addressResult.next()) {
            addressId = addressResult.getInt("address_id");
        } else {
            String insertAddressSql = "INSERT INTO addresses (city, postcode, street_name, street_number) VALUES (?, ?, ?, ?)";
            PreparedStatement insertAddressStmt = conn.prepareStatement(insertAddressSql, Statement.RETURN_GENERATED_KEYS);
            insertAddressStmt.setString(1, user.getAddress().getCity());
            insertAddressStmt.setInt(2, user.getAddress().getPostCode());
            insertAddressStmt.setString(3, user.getAddress().getStreetName());
            insertAddressStmt.setInt(4, user.getAddress().getStreetNumber());
            int rowsInserted = insertAddressStmt.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = insertAddressStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    addressId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting address failed, no ID obtained.");
                }
            } else {
                throw new SQLException("Inserting address failed, no rows affected.");
            }
            insertAddressStmt.close();
        }
        addressResult.close();
        checkAddressStmt.close();
		
		
		
        String sql = "INSERT INTO users (name, title, work, address_id) VALUES (?, ?, ?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getTitle());
            stmt.setString(3, user.getTitle());
            stmt.setLong(4, addressId);
            stmt.executeUpdate();
        }
    }

	public Address getAddressById(int id) {
		String query = "SELECT * FROM addresses WHERE address_id = ?";
		PreparedStatement stmt;
		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return new Address(id,
					rs.getString("city"),
					rs.getInt("postcode"),
					rs.getString("street_name"),
					rs.getInt("street_number")
					);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public List<User> listUsers(String nameFilter, String titleFilter, Address addressFilter) {
	    String query ="SELECT u.user_id, u.name, u.title, u.work, a.address_id as address_id, a.city, a.postcode, a.street_name, a.street_number FROM users u JOIN addresses a ON u.address_id = a.address_id";
	    if (nameFilter != null) {
	        query += " AND u.name=?";
	    }
	    if (titleFilter != null) {
	        query += " AND u.title=?";
	    }
	    if (addressFilter != null) {
	        query += " AND a.city=? AND a.postcode=? AND a.street_name=? AND a.street_number=?";
	    }
	    
	    
	    PreparedStatement statement;
		try {
			statement = conn.prepareStatement(query);
			int paramIndex = 1;
	    if (nameFilter != null) {
	        statement.setString(paramIndex++, nameFilter);
	    }
	    if (titleFilter != null) {
	        statement.setString(paramIndex++, titleFilter);
	    }
	    if (addressFilter != null) {
	        statement.setString(paramIndex++, addressFilter.getCity());
	        statement.setInt(paramIndex++, addressFilter.getPostCode());
	        statement.setString(paramIndex++, addressFilter.getStreetName());
	        statement.setInt(paramIndex++, addressFilter.getStreetNumber());
	    }
	    ResultSet result = statement.executeQuery();
	    List<User> userList = new ArrayList<>();
	    while (result.next()) {
	        int userId = result.getInt("user_id");
	        String name = result.getString("name");
	        String title = result.getString("title");
	        String work = result.getString("work");
	        int addressId = result.getInt("address_id");
	        
	        String city = result.getString("city");
	        int postCode = result.getInt("postcode");
	        String streetName = result.getString("street_name");
	        int streetNumber = result.getInt("street_number");
	        Address address = new Address(addressId, city, postCode, streetName, streetNumber);
	        userList.add(new User(userId, name, title, work,address));
	    }
	    return userList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	    
	}

	public void updateUser(User user) {
		String sql = "UPDATE users SET name = ?, title = ?, work = ?, address_id = ? WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getTitle());
            stmt.setString(3, user.getWork());
            stmt.setInt(4, user.getAddress().getId());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        }catch(SQLException e) {
        	e.printStackTrace();
        }
		
	}

	public void patchUser(User user) {
		try {
		String sql = "UPDATE users SET";
        List<Object> params = new ArrayList<>();
        boolean isFirst = true;
        if (user.getName() != null) {
            sql += " name = ?";
            params.add(user.getName());
            isFirst = false;
        }
        if (user.getTitle() != null) {
            if (!isFirst) {
                sql += ",";
            }
            sql += " title = ?";
            params.add(user.getTitle());
            isFirst = false;
        }
        if (user.getAddress() != null) {
            if (!isFirst) {
                sql += ",";
            }
            sql += " address_id = ?";
            params.add(user.getAddress().getId());
        }
        if (user.getWork() != null) {
            if (!isFirst) {
                sql += ",";
            }
            sql += " work = ?";
            params.add(user.getWork());
        }
        sql += " WHERE user_id = ?";
        params.add(user.getId());
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            statement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
		
	}

	public void deleteUser(int id) {
		String sql = "DELETE FROM users WHERE user_id = ?";
		 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            stmt.executeUpdate();
	        }
		 catch(SQLException e) {
			 e.printStackTrace();
		 }
		
	}

}
