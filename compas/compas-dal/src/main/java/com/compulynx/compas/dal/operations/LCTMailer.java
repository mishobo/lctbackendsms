package com.compulynx.compas.dal.operations;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.compulynx.compas.models.ResetUser;

/**
 * @author dkabii
 *
 */
public class LCTMailer 
{
	public boolean sendEmail(String recepientEmail, String subject, String body)
	{
		boolean sent = true;
		System.out.println("Houston, launching mail in 3, 2, 1...");
		String to = recepientEmail;  
	    String from = "lctteam.compulynx@gmail.com";
	    String host = "localhost";//or IP address  
	  
	    //Get the session object 
	    /*
	    Properties properties = System.getProperties();  
	    properties.setProperty("mail.smtp.host", host);  
	    Session session = Session.getDefaultInstance(properties);  
	    */
	    final String username = "lctteam.compulynx@gmail.com";
        final String password = "nomasana.2019";

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS, change to SSL after test
        
        Authenticator authcat = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        Session session = Session.getInstance(prop, authcat);
	      try
	      {  
	         MimeMessage email = new MimeMessage(session);  
	         email.setFrom(new InternetAddress(from));  
	         email.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
	         email.setSubject(subject,"UTF-8");  
	         email.setContent(body, "text/html; charset=utf-8"); 
	  
	         // Send message  
	         Transport.send(email);  
	         System.out.println("message sent successfully....");  
	  
	      }
	      catch (MessagingException mex) 
	      {
	    	  sent = false;
	    	  //mex.printStackTrace();
	      }
		return sent;
	}
}
