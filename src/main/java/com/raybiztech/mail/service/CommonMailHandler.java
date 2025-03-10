/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.mail.service;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.mail.util.EmailPOJO;
import com.raybiztech.mailtemplates.business.AccountEmail;
import com.raybiztech.payslip.utility.AES256Encryption;

@Component
public class CommonMailHandler implements MessageHandler {

	private static final Logger LOGGER = Logger
			.getLogger(CommonMailHandler.class);

	@Autowired
	PropBean propBean;
	
	@Autowired
	DAO dao;

	public PropBean getPropBean() {
		return propBean;
	}

	public void setPropBean(PropBean propBean) {
		this.propBean = propBean;
	}

	@Override
	public void handle(EmailPOJO email) {
		String username="";
		String pwd = "";
		
		// If part is for authenticating form account mail coming from Accountmail table 
		// which is usefull for Invoice reminder from mail during sending mail.
		if(email.getBody().contains("unique for Account mail only")){
			AccountEmail mail = dao.get(AccountEmail.class).get(0);
			AES256Encryption aes256Encryption = new AES256Encryption(mail.getEmail(), mail.getSaltKey());
			username = mail.getEmail();
			pwd = aes256Encryption.decrypt(mail.getPassword());
			email.setBody(email.getBody().replace("unique for Account mail only", ""));
		}else{
			username = (String) propBean.getPropData().get(
					"mail.fromMailId");
			pwd = (String) propBean.getPropData().get(
					"mail.mailIdPassword");
		}
		

		final String fromMailId = username;
		final String mailIdPassword = pwd;
		String host = (String) propBean.getPropData().get("mail.host");
		String port = (String) propBean.getPropData().get("mail.port");
		String sslTrust = (String) propBean.getPropData().get("mail.sslTrust");

		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.starttls.enable", false);
			props.put("mail.smtp.ssl.trust", sslTrust);
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.transport.protocol", "smtp");
			
			props.put("mail.smtp.starttls.required", true);	

			System.out.println("host ="+host);
			System.out.println("Port ="+port);
			System.out.println("SSL Trust ="+sslTrust);
			
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(fromMailId,
									mailIdPassword);
						}
						
					});
			System.out.println("Password auth done");

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromMailId));

			String to = email.getTo();
			String cc = null;
			String bcc = null;
			if (email.getCc() != null) {
				cc = email.getCc();
				message.addRecipients(javax.mail.Message.RecipientType.CC,
						InternetAddress.parse(cc));
			}
			if (email.getBcc() != null) {
				bcc = email.getBcc();
				message.addRecipients(javax.mail.Message.RecipientType.BCC,
						InternetAddress.parse(bcc));
			}
			System.out.println("To ="+to+"  cc= "+cc+"    bcc"+bcc);

			message.addRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(to));

			message.setSubject(email.getSubject());

			message.setContent(email.getBody(), "text/html");

			System.out.println("Email body ="+email.getBody());
			// LOGGER.warn("GET PATH "+email.getPath());

			if (email.getPath() != null) {

				Multipart multipart = new MimeMultipart();

				MimeBodyPart messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(email.getPath());
				messageBodyPart.setDataHandler(new DataHandler(source));
				int strartIndex = email.getPath().lastIndexOf("/");
				String fileName = email.getPath().substring(strartIndex + 1);
				// LOGGER.warn("fileName "+fileName);
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);
				message.setText(email.getBody());

				MimeBodyPart messageBodyPart1 = new MimeBodyPart();
				messageBodyPart1.setContent(email.getBody(), "text/html");
				multipart.addBodyPart(messageBodyPart1);

				message.setContent(multipart);
			}
			System.out.println("Before sending a mail final");
			Transport.send(message);
			LOGGER.debug("Mail sent Sucessfully");
		} catch (MessagingException e) {
			LOGGER.error(e.getMessage(), e);

		}
	}
}
