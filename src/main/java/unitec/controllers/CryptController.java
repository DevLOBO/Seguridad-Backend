package unitec.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import unitec.models.CryptInfo;
import unitec.services.CryptService;

@RestController
@RequestMapping("/")
public class CryptController {
	@Autowired
	CryptService cs;
	
	@PostMapping("/encrypt")
	public CryptInfo encryptMessage(@RequestBody String msg) {
		return cs.encryptMsg(msg);
	}
	
	@PostMapping("/decrypt")
	public CryptInfo decryptMessage(@RequestBody CryptInfo cryptInfo) {
		return cs.decryptMsg(cryptInfo.getKey(), cryptInfo.getImage());
	}
}