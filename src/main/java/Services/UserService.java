package Services;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import models.User;
import repository.UserRepository;

@Path("/users")
public class UserService {
	private UserRepository userRepository;
	
	 public UserService(UserRepository userRepository) {
	        this.userRepository = userRepository;
	    }
	
	 public void createUser(User user) {
		 try {
			userRepository.createUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	
	
}
