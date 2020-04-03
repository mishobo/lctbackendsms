/**
 * 
 */
package com.compulynx.compas.dal.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.mail.DefaultAuthenticator;

import com.compulynx.compas.dal.impl.CustomerDalImpl;
import com.compulynx.compas.models.CompasProperties;

/**
 * @author Kibet
 *Dec 14, 2016
 */
public class Utility {
	private static final Logger logger = Logger.getLogger(Utility.class
			.getName());

	public static boolean Sendmail(String userName,String status,
			String emailaddress) {
		logger.info("In Send Mail");
		boolean result = false;
		try {
			org.apache.commons.mail.Email email = new org.apache.commons.mail.SimpleEmail();
			/*
			 * email.setHostName("192.168.13.2"); email.setSmtpPort(25);
			 * email.setAuthenticator(new DefaultAuthenticator("sirisha.voleti",
			 * "sairam")); //email.set email.setSSLOnConnect(false);
			 * email.setFrom("sirisha.voleti@compulynx.com");
			 * email.setSubject("iPrint Password"); email.setMsg("Dear " +
			 * userName + " ,your iPrint Password:  " + password);
			 * email.addTo(emailaddress); email.send(); result=true;
			 */
			File configDir = new File(System.getProperty("catalina.base"),
					"conf");
			File configFile = new File(configDir, "email.properties");
			InputStream stream = new FileInputStream(configFile);
			Properties props = new Properties();
			props.load(stream);
			System.out.println("props" + props);
			String Hostname = props.getProperty("HostName");
			String port = props.getProperty("Port");
			String username ="";
			String password ="";
			String to= "";
			String sub ="";
			String strStatus="";
			if(status.equalsIgnoreCase("AP")){
			
				username = props.getProperty("adminUsername");
				password = props.getProperty("adminPassword");
				to = props.getProperty("admin");
				sub = "Beneficiary Approval Done";
				strStatus="Beneficiaries are approved by ";
			}else if(status.equalsIgnoreCase("V")){
				username = props.getProperty("verifyUsername");
				password = props.getProperty("verifyPassword");
				to = props.getProperty("beneficiaryApprover");
				sub = "Beneficiary Verification Done";
				strStatus="Beneficiaries are approved by ";
				strStatus="Beneficiaries are verified by ";
			}else if(status.equalsIgnoreCase("A")){
				strStatus="Topups are approved by ";
			}
			email.setHostName(Hostname);
			email.setSslSmtpPort(port);
			email.setAuthenticator(new DefaultAuthenticator(userName,
					password));
			email.setSSLOnConnect(false);
			email.setFrom(emailaddress);
			email.setSubject(sub);
			email.setMsg(strStatus+ userName);
			email.addTo(to);
			email.send();
			result = true;
			System.out.println("hostname" + Hostname);
			System.out.println("port" + port);
			//System.out.println("from" + bnfApprover);
			//System.out.println("username" + username);
			//System.out.println("sub" + sub);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	 public CompasProperties getCompasProperties() {
	        String path = System.getProperty("catalina.base") +"/conf/application.properties";
	        Properties properties = new Properties();
	        try{
	            FileInputStream file;
	            //load the file handle for main.properties
	            file = new FileInputStream(path.trim());
	            //load all the properties from this file
	            properties.load(file);
	            //we have loaded the properties, so close the file handle
	            file.close();
	            return new CompasProperties(properties.getProperty("settlement.filepath").trim());
	        } catch (Exception ex){
	            ex.printStackTrace();
	        }
	        return null;
	    }
}
