package com.compulynx.compas.dal.operations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("twilio")
public class TwilioConfiguration {
	
	private String accountSid = "AC711399fd53e2bbead3e1fa49c2cd045a";
	private String authToken = "7c6741ada26e45ed01b71a94e2b83361";
	private String trialNumber = "+12058283581";
	
	
	public TwilioConfiguration() {}

	public String getAccountSid() {
		return accountSid;
	}

	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getTrialNumber() {
		return trialNumber;
	}

	public void setTrialNumber(String trialNumber) {
		this.trialNumber = trialNumber;
	}

	

	

}
