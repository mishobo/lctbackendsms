/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;



/**
 * @author Anita
 *
 */
public class LoginUser {
	public int userId;
	public int respCode;
	public int userTypeId;
	public String respMessage;
	public int userBioID;
	public boolean firstTimeLogin;
	public String UserGroupId;
	public List<Programme> programmes;
	
	public LoginUser(int userId, int respCode) {
		super();
		this.userId = userId;
		this.respCode = respCode;
	}
	
	public LoginUser(int userId, int userTypeId, int respCode, boolean firstTimeLogin, String UserGroupId) {
		super();
		this.userId = userId;
		this.respCode = respCode;
		this.userTypeId = userTypeId;
		this.firstTimeLogin = firstTimeLogin;
		this.UserGroupId=UserGroupId;
	}
	
//	public LoginUser(int userId,int userBioID, int respCode) {
//		super();
//		this.userId = userId;
//		this.userBioID = userBioID;
//		this.respCode = respCode;
//	}
	public LoginUser(int userId, int respCode,String respMessage,List<Programme> programmes) {
		super();
		this.userId = userId;
		this.respCode = respCode;
		this.respMessage=respMessage;
		this.programmes=programmes;
	}

	public LoginUser(int respCode, String respMessage) {
		super();
		this.respCode = respCode;
		this.respMessage = respMessage;
	}

	public LoginUser(int respCode) {
		super();
		this.respCode = respCode;
	}

	public LoginUser() {
		super();
	}
}
