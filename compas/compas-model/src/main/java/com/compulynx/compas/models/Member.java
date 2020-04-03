package com.compulynx.compas.models;

import java.util.List;

import com.compulynx.compas.models.android.ApiProgramme;


public class Member {

	public int memberId;
	public int customerId;
	public String memberNo;
	public String firstName;
	public String lastName;
	public String fullName;
	public String dateOfBirth;
	public String deviceId;
	public int respCode;
	public String bioId;
	public String FileName;
	public int createdBy;
	public List<Member> memberIdList;
	public List<Voucher> vouchers;
	public List <Service> memberServices;
	public boolean selected;
	public String memberPic;
	public boolean expanded;
	public String memberType;
	public String lctCode; //for tracking employer and insurance changes
	//For Jubilee
	public String title;
	public String otherName;
	public String idPassPortNo;
	public String gender;
	public String maritalStatus;
	public String weight;
	public String height;
	public String nhifNo;
	public String employerName;
	public String occupation;
	public String nationality;
	public String postalAdd;
	public String physicalAdd;
	public String homeTelephone ;
	public String officeTelephone;
	public String cellPhone;
	public String email ;
	public String cardStatus;
	public String uploadStatus;
	public int accType;
	public String cardNumber;
	public String serialNumber;
	public String expiryDate;
	public String status;
	public List<Programme> programmes;
	//public List<Programme> programmes;
	public String binRange;
	public int programmeId;
	public String programmeDesc;
	public List<BioImage> imageList;
	public List<BioImage> bioImages;
	//public String bioImages;
	public String bioImage;
	public double cardBalance;
	public String cardSerialNo;
	public String cardPin;
	public int familySize;
	public int bnfGrpId;
	public String bnfStatus;
	public String verifyStatus;
	public String approveStatus;
	public String bioStatus;
	public String memType;
	public List<BeneficiaryFamilyMembers> familyMemList;
	
	public boolean isChecked;
	public String serialNo;

	public List<BulkSelectionIds> verifySelection;
	public List<BulkSelectionIds> famVerifySelection;
	public String userName;
	public String userEmail;
	public int topupCount;
	public String bnfGrpName;
	public int productId;
	public boolean active;
	public int accNo;
	public String accNumber;
	public int accId;
	public double accBalance;
	public int orgId;
	public List<ApiProgramme> memberProgrammes;
	public String fromDate;
	public String toDate;
	public int count;
	public String nationalId;
	public String scheme;
	public boolean schemeActive;
	public boolean schemeExpired;
	public boolean hasSpouse;
	public String relation;
	public int principleId;
	public String principle;
	public int voucherId;
	
	//AKUH
	public String insuranceCode;
	public String schemeCode;
	public String outpatientStatus;

	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Member(int respCode) {
		super();
		this.respCode = respCode;
	}
	public String getInsuranceCode() {
		return insuranceCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public String getSchemeCode() {
		return schemeCode;
	}
	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}
	public String getOutpatientStatus()
	{
		return outpatientStatus;
	}
	public void setOutpatientStatus(String status)
	{
		outpatientStatus = status;
	}
	@Override
	public String toString() {
		return "Member [memberId=" + memberId + ", customerId=" + customerId + ", memberNo=" + memberNo + ", firstName="
				+ firstName + ", lastName=" + lastName + ", fullName=" + fullName + ", dateOfBirth=" + dateOfBirth
				+ ", deviceId=" + deviceId + ", respCode=" + respCode + ", bioId=" + bioId + ", FileName=" + FileName
				+ ", createdBy=" + createdBy + ", memberIdList=" + memberIdList + ", vouchers=" + vouchers
				+ ", selected=" + selected + ", memberPic=" + memberPic + ", expanded=" + expanded + ", title=" + title
				+ ", otherName=" + otherName + ", idPassPortNo=" + idPassPortNo + ", gender=" + gender
				+ ", maritalStatus=" + maritalStatus + ", weight=" + weight + ", height=" + height + ", nhifNo="
				+ nhifNo + ", employerName=" + employerName + ", occupation=" + occupation + ", nationality="
				+ nationality + ", postalAdd=" + postalAdd + ", physicalAdd=" + physicalAdd + ", homeTelephone="
				+ homeTelephone + ", officeTelephone=" + officeTelephone + ", cellPhone=" + cellPhone + ", email="
				+ email + ", cardStatus=" + cardStatus + ", uploadStatus=" + uploadStatus + ", accType=" + accType
				+ ", cardNumber=" + cardNumber + ", serialNumber=" + serialNumber + ", expiryDate=" + expiryDate
				+ ", status=" + status + ", programmes=" + programmes + ", binRange=" + binRange + ", programmeId="
				+ programmeId + ", programmeDesc=" + programmeDesc + ", imageList=" + imageList + ", bioImages="
				+ bioImages + ", bioImage=" + bioImage + ", cardBalance=" + cardBalance + ", cardSerialNo="
				+ cardSerialNo + ", cardPin=" + cardPin + ", familySize=" + familySize + ", bnfGrpId=" + bnfGrpId
				+ ", bnfStatus=" + bnfStatus + ", verifyStatus=" + verifyStatus + ", approveStatus=" + approveStatus
				+ ", bioStatus=" + bioStatus + ", memType=" + memType + ", familyMemList=" + familyMemList
				+ ", isChecked=" + isChecked + ", serialNo=" + serialNo + ", verifySelection=" + verifySelection
				+ ", famVerifySelection=" + famVerifySelection + ", userName=" + userName + ", userEmail=" + userEmail
				+ ", topupCount=" + topupCount + ", bnfGrpName=" + bnfGrpName + ", productId=" + productId + ", active="
				+ active + ", accNo=" + accNo + ", accNumber=" + accNumber + ", accId=" + accId + ", accBalance="
				+ accBalance + ", orgId=" + orgId + ", memberProgrammes=" + memberProgrammes + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", count=" + count + ", nationalId=" + nationalId + ", scheme=" + scheme
				+ ", schemeActive=" + schemeActive + ", schemeExpired=" + schemeExpired + ", hasSpouse=" + hasSpouse
				+ ", relation=" + relation + ", principleId=" + principleId + ", principle=" + principle
				+ ", insuranceCode=" + insuranceCode + ", schemeCode=" + schemeCode + ", outpatientStatus="+outpatientStatus+"]";
	}
	
}
