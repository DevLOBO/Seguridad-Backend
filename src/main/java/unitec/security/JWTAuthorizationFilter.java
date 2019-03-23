package unitec.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import unitec.utils.JWTUtils;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		SecurityContextHolder.getContext().setAuthentication(getAuth(request));
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuth(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if (token != null) {
			String username = JWTUtils.getIssuer(token);
			List<? extends GrantedAuthority> roles = new ArrayList<>();
			
			try {
				roles = Arrays.asList(new ObjectMapper()
						.addMixIn(SimpleGrantedAuthority.class, AuthorityMixin.class)
						.readValue(JWTUtils.getClaim(token, "roles").getBytes(), SimpleGrantedAuthority[].class));
			} catch (IOException e) {
				roles = Collections.emptyList();
			}
			
			System.out.println(username + " es el usuario autorizado");
			
			if (username != null)
				return new UsernamePasswordAuthenticationToken(username, null, roles);
			
			return null;
		}

		return null;
	}
}