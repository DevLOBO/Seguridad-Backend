package unitec.utils;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import unitec.security.SecurityConstants;

public class JWTUtils {
	public static String createWithClaimAndExpirationTime(String name, String key, String value) {
		return JWT.create()
				.withSubject(name)
				.withClaim(key, value)
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
	}
	
	public static String createWithKeyAndExpirationTime(String name, String value, Long time) {
		return JWT.create()
				.withSubject(name)
				.withClaim("key", value)
				.withExpiresAt(new Date(System.currentTimeMillis() + time))
				.sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
	}
	
	public static String getSubject(String token) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getSubject();
	}
	
	public static String getKey(String token) {
		return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
				.verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
				.getClaim("key").asString();
	}
}