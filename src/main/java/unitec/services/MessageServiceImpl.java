package unitec.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unitec.exceptions.IncorrectIssuerException;
import unitec.models.Message;
import unitec.repositories.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	MessageRepository mr;
	
	@Override
	public String saveMessage(String issuer, String audience) {
		Message msg = new Message(issuer, audience);
		return mr.save(msg).getId();
	}

	@Override
	public void verifyIssuer(String id, String issuer) throws IncorrectIssuerException {
		Message msg = mr.findById(id).orElseThrow(() -> new RuntimeException("Not exist the message"));
		
		if (msg.getIssuer().equals(issuer))
			throw new IncorrectIssuerException("Username " + issuer + " is not the original issuer");
	}
}
