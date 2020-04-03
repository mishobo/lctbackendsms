/**
 * 
 */
package com.compulynx.compas.models;

import java.util.List;

/**
 * @author Anita
 *
 */
public class LoginSession {
	public int sessionId;
	public int linkId;
	public int userGroupId;
	public int userTypeId;
	public String linkName;
	public String sessionName;
	public String sessionFullName;
	public String linkExtInfo;
	public int userClassId;

	public void setLinkExtInfo(String linkExtInfo) {
		this.linkExtInfo = linkExtInfo;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}
	
	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}

	public void setSessionFullName(String sessionFullName) {
		this.sessionFullName = sessionFullName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public void setUserClassID(int userClassID) {
		this.userClassId = userClassID;
	}

	public List<Rights> rightsHeaderList;
	public int respCode;

	public LoginSession() {
		super();
	}

	public void setRightsList(List<Rights> rightsList) {
		this.rightsHeaderList = rightsList;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}



	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public LoginSession(int respCode) {
		super();
		this.respCode = respCode;
	}
}
