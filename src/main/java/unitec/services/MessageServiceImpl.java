package unitec.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unitec.exceptions.IncorrectAudienceException;
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
	public void verifyAudience(String id, String audience) throws Exception, IncorrectAudienceException {
		Message msg = mr.findById(id).orElseThrow(() -> new Exception("Not exist the message"));
		
		if (!msg.getAudience().equals(audience))
			throw new IncorrectAudienceException("Username " + audience + " is not the original target user");
	}
}
