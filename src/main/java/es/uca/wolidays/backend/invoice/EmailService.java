package es.uca.wolidays.backend.invoice;

import java.io.ByteArrayOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import es.uca.wolidays.backend.entities.Reserva;
import es.uca.wolidays.backend.entities.Usuario;

@Service
public class EmailService{

	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private PDFService pdfService;
	
	public void sendInvoiceMessage(String toS, String subject, String text, Reserva res, Usuario anf) {
		
		String from = "wolidaysdevelop@gmail.com";
		String[] eachTo = toS.split(",");
		
		ByteArrayOutputStream outputStream = null;
		
		try {
			
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(text);
			
			outputStream = new ByteArrayOutputStream();
			pdfService.generatePDF(outputStream, res, anf);
			byte[] bytes = outputStream.toByteArray();
			
			DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			pdfBodyPart.setFileName("invoice.pdf");
			
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);
			
			InternetAddress iaSender = new InternetAddress(from);
			InternetAddress iaToAnf = new InternetAddress(eachTo[0]);
			InternetAddress iaToHue = new InternetAddress(eachTo[1]);
			
			MimeMessage message = emailSender.createMimeMessage();
			message.setSender(iaSender);
			message.setSubject(subject);
			message.setRecipients(RecipientType.TO, new Address[]{iaToAnf, iaToHue});
			message.setContent(mimeMultipart);
			
			emailSender.send(message);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
}
