package unitec.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import unitec.models.User;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authManager;
	
	public JWTAuthenticationFilter(AuthenticationManager authManager) {
		this.authManager = authManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = null;

		if (username == null && password == null) {
			try {
				user = new ObjectMapper().readValue(request.getInputStream(), User.class);
				username = user.getUsername();
				password = user.getPassword();
			} catch (IOException e) {
				username = "";
				password = "";
			}
		}
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		return authManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String username = authResult.getName();
		String token = JWT.create()
				.withSubject(username)
				.withClaim("roles", new ObjectMapper().writeValueAsString(authResult.getAuthorities()))
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
		
		Map<String, Object> res = new HashMap<>();
		res.put("username", username);
		res.put("token", token);
		res.put("logged", true);

		response.getWriter().write(new ObjectMapper().writeValueAsString(res));
		response.setStatus(200);
		response.setContentType("application/json");
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<>();
		res.put("logged", false);
		res.put("error", "Username and/or password invalid");
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(res));
		response.setStatus(401);
		response.setContentType("application/json");
	}
}