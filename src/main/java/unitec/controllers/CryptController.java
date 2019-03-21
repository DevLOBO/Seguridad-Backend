package unitec.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unitec.models.CryptInfo;
import unitec.services.CryptService;

@RestController
@PreAuthorize("hasAuthority('USER')")
@RequestMapping("/")
public class CryptController {
	@Autowired
	CryptService cs;
	
	@PostMapping("/encrypt")
	public CryptInfo encryptMessage(@RequestBody CryptInfo cryptInfo) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, AddressException, MessagingException {
		return cs.encryptMsg(cryptInfo);
	}
	
	@PostMapping("/decrypt")
	public CryptInfo decryptMessage(@RequestBody CryptInfo cryptInfo) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IOException {
		return cs.decryptMsg(cryptInfo);
	}
}