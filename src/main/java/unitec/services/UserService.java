package unitec.services;

import java.util.Date;

import unitec.models.User;

public interface UserService {
	User saveUser(String username, String password, Date dateExpiration);
}