package unitec.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "users_seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private @Id String id;
	private String username;
	private String password;
	private Date dateExpiration;
	private boolean enabled;
	
	public User(String u, String p) {
		this.password = new BCryptPasswordEncoder().encode(p);
		this.username = u;
		this.enabled = true;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("DECRYPTER"));
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return isExpired();
	}
	
	private boolean isExpired() {
		if (dateExpiration.compareTo(new Date()) < 0)
			return false;
		
		return true;
	}
}