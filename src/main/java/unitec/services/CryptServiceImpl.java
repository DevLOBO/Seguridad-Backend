package unitec.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Service;

import unitec.models.CryptInfo;
import unitec.utils.CryptUNITEC;
import unitec.utils.ImageCreator;

@Service
public class CryptServiceImpl implements CryptService {
	@Override
	public CryptInfo encryptMsg(String msg) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		String key = CryptUNITEC.generateKey(),
				encMsg = CryptUNITEC.encrypt(msg, key);
		byte[] img = ImageCreator.createImageRandom(),
				encImg = ImageCreator.hideText(img, encMsg);
		encMsg = ImageCreator.encodeImg(encImg);
		
		return new CryptInfo(key, msg, encMsg);
	}

	@Override
	public CryptInfo decryptMsg(String key, String image) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IOException {
		byte[] decImg = ImageCreator.decodeImg(image);
		String encMsg = ImageCreator.retrieveText(decImg),
				decMsg = CryptUNITEC.decrypt(encMsg, key);
		
		return new CryptInfo(key, decMsg, image);
	}
}