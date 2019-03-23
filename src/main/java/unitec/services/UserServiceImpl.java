package unitec.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import unitec.models.User;
import unitec.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	UserRepository ur;
	
	@Override
	public User saveUser(String username, String password, Date dateExpiration) {
		User user = ur.findByUsername(username).orElse(new User(username, password));
		user.setDateExpiration(dateExpiration);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		return ur.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(username + " intentó autenticarse");
		return ur.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + " no encontrado"));
	}
}