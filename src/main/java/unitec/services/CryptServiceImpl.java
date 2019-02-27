package unitec.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Service;

import unitec.models.CryptInfo;
import unitec.utils.CryptUNITEC;
import unitec.utils.ImageCreator;
import unitec.utils.JWTUtils;

@Service
public class CryptServiceImpl implements CryptService {
	@Override
	public CryptInfo encryptMsg(CryptInfo cryptInfo) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		Date date = new Date(System.currentTimeMillis()  + (cryptInfo.getTime() * 60000));
		String key = CryptUNITEC.generateKey(),
				encMsg = CryptUNITEC.encrypt(cryptInfo.getMessage(), key),
				token = JWTUtils.createWithKeyAndExpirationTime(cryptInfo.getUsername(), key, date);
		byte[] img = ImageCreator.createImageRandom(),
				encImg = ImageCreator.hideText(img, encMsg);
		encMsg = ImageCreator.encodeImg(encImg);
		
		return CryptInfo.builder().key(token).image(encMsg).date(date).build();
	}

	@Override
	public CryptInfo decryptMsg(CryptInfo cryptInfo) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IOException {
		byte[] decImg = ImageCreator.decodeImg(cryptInfo.getImage());
		String key = JWTUtils.getKey(cryptInfo.getKey()),
				encMsg = ImageCreator.retrieveText(decImg),
				decMsg = CryptUNITEC.decrypt(encMsg, key),
				user = JWTUtils.getSubject(cryptInfo.getKey());
		
		return CryptInfo.builder().message(decMsg).username(user).build();
	}
}