package com.compulynx.compas.models;

import java.util.List;

public class User {
	
	public int userId;
	public String userName;
	public String userFullName;
	public String userPwd;
	public String userEmail;
	public String userPhone;
	public int userGroupId;
	public String groupName;
	public String userSecretQuestion;
	public int userSecretQuestionId;
	public String userSecretAns;
	public boolean active;
	public int createdBy;
	public int respCode;
	public String deviceId;
	public List<Branch> userBranchDetails;
	public boolean userBioLogin;
	public long userLinkedID;
	public long userBioID;
	public int classId;
	public int agentId;
	public int merchantId;
	public int branchId;
	public int userTypeId;
	public int posUserLevel;
	public String className;
	public int count;
	public String userTypeName;
	public int locationId;
	public String newPassword;
	public boolean firstTimeLogin;
	public User(int classId, String className) {
		super();
		this.classId = classId;
		this.className = className;
	}
	public User(int userId, String userName, String userFullName,
			String userEmail, String userPhone, String userPwd, int groupId,
			String groupName,  boolean active, int createdBy,
			int respCode, String newPassword, boolean firstTimeLogin) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userPwd = userPwd;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
		this.userGroupId = groupId;
		this.groupName = groupName;
		//this.userSecretQuestionId = userSecretQuestionId;
		//this.userSecretAns = userSecretAns;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.newPassword = newPassword;
		this.firstTimeLogin=firstTimeLogin;
		
	}
	public User(int userId,String userName, String userFullName, String userPwd,
			int userGroupId,int agentId,int branchId, String userEmail, String userPhone,
			int userSecretQuestionId, String userSecretAns,
			boolean userBioLogin, long userLinkedID, long userBioID,
			boolean active, int createdBy, int respCode, String newPassword, int userTypeId,int count,String userTypeName,int posUserLevel, int merchantId,
			boolean firstTimeLogin) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.userPwd = userPwd;
		this.userGroupId = userGroupId;
		this.agentId=agentId;
		this.branchId=branchId;
		this.userEmail = userEmail;
		this.userPhone = userPhone;
		this.userSecretQuestionId = userSecretQuestionId;
		this.userSecretAns = userSecretAns;
		this.userBioLogin = userBioLogin;
		this.userLinkedID = userLinkedID;
		this.userBioID = userBioID;
		this.active = active;
		this.createdBy = createdBy;
		this.respCode = respCode;
		this.userTypeId=userTypeId;
		this.count=count;
		this.userTypeName=userTypeName;
		this.posUserLevel=posUserLevel;
		this.merchantId=merchantId;
		this.newPassword = newPassword;
		this.firstTimeLogin = firstTimeLogin;
	}
	public User(String userSecretQuestion, int userSecretQuestionId) {
		super();
		this.userSecretQuestion = userSecretQuestion;
		this.userSecretQuestionId = userSecretQuestionId;
	
	}
	

	public User(int respCode) {
		super();
		this.respCode = respCode;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
}