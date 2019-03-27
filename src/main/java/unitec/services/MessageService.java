package unitec.services;

import unitec.exceptions.IncorrectAudienceException;

public interface MessageService {
	String saveMessage(String issuer, String audience);
	void verifyAudience(String id, String issuer) throws Exception, IncorrectAudienceException;
}