package unitec.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AuthorityMixin {
	@JsonCreator
	public AuthorityMixin(@JsonProperty("authority") String role) {
		
	}
}