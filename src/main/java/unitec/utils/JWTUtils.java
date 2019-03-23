package unitec.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import unitec.security.SecurityConstants;

public class JWTUtils {
	public static String createWithClaimAndExpirationTime(String name, String key, String value, Date date) {
		return JWT.create()
				.withIssuer(name)
				.withClaim(key, value)
				.withExpiresAt(date)
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
	}
	
	public static String createWithKeyAndExpirationTime(String name, String value, Date date) {
		return JWT.create()
				.withIssuer(name)
				.withClaim("key", value)
				.withExpiresAt(date)
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
	}
	
	public static String createWithKeyAndExpirationTimeAndMessage(String name, String audience, String value, String id, Date date) {
		return JWT.create()
				.withSubject(audience)
				.withIssuer(name)
				.withClaim("key", value)
				.withClaim("message", id)
				.withExpiresAt(date)
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
	}
	
	public static String getClaim(String token, String key) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getClaim("roles").asString();
	}
	
	public static String getIssuer(String token) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getIssuer();
	}
	
	public String getSubject(String token) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getSubject();
	}
	
	public static String getKey(String token) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getClaim("key").asString();
	}
	
	public static String getMessage(String token) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getClaim("message").asString();
	}
}