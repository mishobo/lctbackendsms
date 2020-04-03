/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class BeneficiaryFamilyMembers {

	public int memberId;
	public int familyMemId;
	public String famMemberNo;
	public String famMemFirstName;
	public String famMemLastName;
	public String famMemFullName;
	public String famDob;
	public String famGender;
	public String famMemNationalId;
	public int relationId;
	public String relationDesc;
	public String familyMemPic;
	public int familyMemBioId;
	public int tempFamilyMemId;
	public boolean editMode;
	public String	iconStyle;
	public boolean cancelled;
	public int createdBy;
	public int respCode;
	public String famPic;
	public boolean isChecked;
	public boolean isNew;
	public String verifyStatus;
	public String approveStatus;
	public String lctCode;
	public String memberType;
	public String idNumber;
	public boolean active;
	public BeneficiaryFamilyMembers(int respCode) {
		super();
		this.respCode = respCode;
	}

	/*public BeneficiaryFamilyMembers(int familyMemId, String famMemFullName,
			String famMemberNo, int relationId, String familyMemPic,int familyMemBioId,
			int respCode, boolean isChecked,String verifyStatus,String approveStatus,String famGender) {
		super();
		this.familyMemId = familyMemId;
		this.famMemFullName = famMemFullName;
		this.famMemberNo = famMemberNo;
		this.relationId = relationId;
		this.familyMemPic = familyMemPic;
		this.familyMemBioId=familyMemBioId;
		this.respCode = respCode;
		this.isChecked=false;
		this.verifyStatus=verifyStatus;
		this.approveStatus=approveStatus;
		this.famGender=famGender;
	}*/
	
	public BeneficiaryFamilyMembers(int familyMemId, String famMemFullName,
			String famMemberNo, int relationId, String familyMemPic,int familyMemBioId,
			int respCode, boolean isChecked,String verifyStatus,String approveStatus,String famGender, String lctCode, String memberType) {
		super();
		this.familyMemId = familyMemId;
		this.famMemFullName = famMemFullName;
		this.famMemberNo = famMemberNo;
		this.relationId = relationId;
		this.familyMemPic = familyMemPic;
		this.familyMemBioId=familyMemBioId;
		this.respCode = respCode;
		this.isChecked=false;
		this.verifyStatus=verifyStatus;
		this.approveStatus=approveStatus;
		this.famGender=famGender;
		this.lctCode = lctCode;
		this.memberType = memberType;
	}

	public BeneficiaryFamilyMembers() {
		super();
		// TODO Auto-generated constructor stub
	}
}
