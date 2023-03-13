package repository;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import models.Address;
import models.User;

public interface UserRepository {
	public void createUser(User user) throws SQLException;
	public List<User> listUsers(String nameFilter, String titleFilter, Address addressFilter);
	public void updateUser(User user);
	public void patchUser(User user);
	public void deleteUser(int id);
}
