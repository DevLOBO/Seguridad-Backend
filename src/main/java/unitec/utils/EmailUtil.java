package unitec.utils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EmailUtil {
	@Autowired
	JavaMailSenderImpl sender;
	
	public void sendEmailWithAttachment(String to, byte[] img, String key, Date date) throws AddressException, MessagingException, UnsupportedEncodingException {
		InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(img));
		System.out.println("Variable exitosa");
		
		MimeMessage message = sender.createMimeMessage();
		System.out.println("Mime exitoso");
		
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject("Mensaje encriptado");
		helper.setText(buildText(key, date), true);
		helper.setFrom("franco.hernandezh@my.unitec.edu.mx");
		helper.addAttachment("encrypted.png", isr);
		System.out.println("Helper exitoso");
		
		sender.send(message);
	}
	
	public String buildText(String key, Date date) {
		String t = "<!DOCTYPE html>\r\n" + 
				"<html lang=\"es\">\r\n" + 
				"\r\n" + 
				"<head>\r\n" + 
				"    <meta charset=\"UTF-8\">\r\n" + 
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
				"    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n" + 
				"    <title></title>\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">\r\n" + 
				"</head>\r\n" + 
				"<div class=\"jumbotron jumbotron-fluid\">\r\n" + 
				"    <div class=\"container\">\r\n" + 
				"        <h1 class=\"display-3\">Mensaje encriptado</h1>\r\n" + 
				"        <p class=\"lead\">Te han enviado un mensaje encriptado. Fecha: " + new Date().toString() + "</p>\r\n" + 
				"        <hr class=\"my-2\">\r\n" + 
				"        <ul><li>Tu llave es: " + key + "</li>\r\n" + 
				"		 <li>Fecha de expiración: " + date.toString() + "</li></ul>" + 
				"        <p>Para poder desencriptar el mensaje, necesitarás la llave y la imagen adjunta en este correo</p>\r\n" + 
				"        <p class=\"lead\">\r\n" + 
				"            <a class=\"btn btn-primary btn-lg\" href=\"https://devlobo.github.io/DevLOBO/Seguridad-Frontend\" role=\"button\">IR AL SISTEMA</a>\r\n" + 
				"        </p>\r\n" + 
				"    </div>\r\n" + 
				"</div>\r\n" + 
				"\r\n" + 
				"<body>\r\n" + 
				"\r\n" + 
				"</body>\r\n" + 
				"\r\n" + 
				"</html>";
		return t;
	}
}