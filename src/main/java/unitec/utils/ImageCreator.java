package unitec.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.springframework.util.Base64Utils;

public class ImageCreator {
	public static byte[] createImageRandom() throws IOException {
		SecureRandom rand = new SecureRandom();
		int w = 1000, h = 1000;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w ;x++) {
				int a = rand.nextInt(256),
					r = rand.nextInt(256),
					g = rand.nextInt(256),
					b = rand.nextInt(255) + 1,
					p = (a << 24) | (r << 16) | (g << 8) | b;
				img.setRGB(x, y, p);;
			}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "png", out);
		
		return out.toByteArray();
	}
	
	public static byte[] hideText(byte[] img, String text) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(img));
		byte[] bytes = text.getBytes();
		int cont = 0;
		
		for (int y = 0; y < image.getHeight(); y++)
			for (int x= 0; x < image.getWidth(); x++) {
				int p = image.getRGB(x, y),
						a = (p >> 24) & 0xff,
						r = (p >> 16) & 0xff,
						g = (p >> 8) & 0xff,
						b = 0;
				
				if (cont == bytes.length) {
					p = (a << 24) | (r << 16) | (g << 8) | b;
					image.setRGB(x, y, p);
					
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ImageIO.write(image, "png", out);
					
					return out.toByteArray();
				}
				
				b = bytes[cont];
				p = (a << 24) | (r << 16) | (g << 8) | b;
				image.setRGB(x, y, p);
				cont++;
			}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", bos);
		
		return bos.toByteArray();
	}
	
	public static String retrieveText(byte[] img) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(img));
		StringBuilder sb = new StringBuilder();
		
		for (int y = 0; y < image.getHeight(); y++)
			for (int x = 0; x < image.getWidth(); x++) {
				int p = image.getRGB(x, y),
					b = p & 0xff;
				
				if (b == 0)
					return sb.toString();
				
				sb.append((char) b);
			}
		
		return sb.toString();
	}
	
	public static String encodeImg(byte[] img) {
		return Base64Utils.encodeToString(img);
	}
	
	public static byte[] decodeImg(String img) {
		return Base64Utils.decodeFromString(img);
	}
}