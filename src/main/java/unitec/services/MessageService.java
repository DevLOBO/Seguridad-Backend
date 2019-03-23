package unitec.services;

import unitec.exceptions.IncorrectIssuerException;

public interface MessageService {
	String saveMessage(String issuer, String audience);
	void verifyIssuer(String id, String issuer) throws IncorrectIssuerException;
}