package unitec.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

import unitec.security.SecurityConstants;

public class CryptUNITEC {
	public static String generateKey() {
		try {
			byte[] key = KeyGenerator.getInstance("DES").generateKey().getEncoded();
			String base64 = Base64Utils.encodeToString(key);
			return base64;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static String encrypt(String text, String key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher ecipher = Cipher.getInstance(SecurityConstants.CRYPT_ALGORITHM);
		ecipher.init(Cipher.ENCRYPT_MODE, buildKey(key));
		
		byte[] enc = ecipher.doFinal(text.getBytes());
		
		return Base64Utils.encodeToString(enc);
	}
	
	public static String decrypt(String enc, String key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		Cipher dcipher = Cipher.getInstance(SecurityConstants.CRYPT_ALGORITHM);
		dcipher.init(Cipher.DECRYPT_MODE, buildKey(key));
		
		byte[] base64 = Base64Utils.decode(enc.getBytes()),
				dec = dcipher.doFinal(base64);
		
		return new String(dec, "UTF8");
	}
	
	private static SecretKey buildKey(String key) {
		byte[] bytes = Base64Utils.decodeFromString(key);
		return new SecretKeySpec(bytes, SecurityConstants.CRYPT_ALGORITHM);
	}
}