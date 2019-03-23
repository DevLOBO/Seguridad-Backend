package unitec.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "messages_seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Message {
	private @Id String id;
	private String issuer;
	private String audience;
	
	public Message(String i, String a) {
		this.issuer = i;
		this.audience = a;
	}
}