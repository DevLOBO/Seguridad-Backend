package unitec.services;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import unitec.exceptions.IncorrectAudienceException;
import unitec.models.CryptInfo;
import unitec.models.User;
import unitec.utils.CryptUNITEC;
import unitec.utils.EmailUtil;
import unitec.utils.ImageCreator;
import unitec.utils.JWTUtils;

@Service
public class CryptServiceImpl implements CryptService {
	@Autowired
	JavaMailSenderImpl sender;
	
	@Autowired
	UserService us;
	
	@Autowired
	MessageService ms;
	
	@Override
	public CryptInfo encryptMsg(CryptInfo cryptInfo) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, AddressException, MessagingException {
		Date date = new Date(System.currentTimeMillis()  + (cryptInfo.getTime() * 60000));
		String id = ms.saveMessage(cryptInfo.getUsername(), cryptInfo.getTo()),
				key = CryptUNITEC.generateKey(),
				encMsg = CryptUNITEC.encrypt(cryptInfo.getMessage(), key),
				token = JWTUtils.createWithKeyAndExpirationTimeAndMessage(cryptInfo.getUsername(), cryptInfo.getTo(), key, id, date);
		byte[] img = ImageCreator.createImageRandom(),
				encImg = ImageCreator.hideText(img, encMsg);
		encMsg = ImageCreator.encodeImg(encImg);
		
		String password = Long.toString(System.currentTimeMillis());
		User user = us.saveUser(cryptInfo.getTo(), password, date);
		
		EmailUtil email = new EmailUtil(sender);
		email.sendEmailWithAttachment(user.getUsername(), password, encImg, token, date);
		
		return CryptInfo.builder().key(token).image(encMsg).date(date).build();
	}

	@Override
	public CryptInfo decryptMsg(CryptInfo cryptInfo) throws Exception, IncorrectAudienceException, Exception {
		String id = JWTUtils.getMessage(cryptInfo.getKey());
		ms.verifyAudience(id, cryptInfo.getUsername());
		
		byte[] decImg = ImageCreator.decodeImg(cryptInfo.getImage());
		String key = JWTUtils.getKey(cryptInfo.getKey()),
				encMsg = ImageCreator.retrieveText(decImg),
				decMsg = CryptUNITEC.decrypt(encMsg, key),
				user = JWTUtils.getIssuer(cryptInfo.getKey());
		
		return CryptInfo.builder().message(decMsg).username(user).build();
	}
}