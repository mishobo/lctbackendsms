package com.compulynx.compas.models;

public class Upmember extends Member{
	public String organization;
	public String principalApplicant;

	@Override
	public String toString() {
		return "Upmember [organization=" + organization +" principalApplicant=" + principalApplicant + ", memberId=" + memberId + ", customerId=" + customerId
				+ ", memberNo=" + memberNo + ", firstName=" + firstName + ", lastName=" + lastName + ", fullName="
				+ fullName + ", dateOfBirth=" + dateOfBirth + ", deviceId=" + deviceId + ", respCode=" + respCode
				+ ", bioId=" + bioId + ", FileName=" + FileName + ", createdBy=" + createdBy + ", memberIdList="
				+ memberIdList + ", vouchers=" + vouchers + ", selected=" + selected + ", memberPic=" + memberPic
				+ ", expanded=" + expanded + ", memberType=" + memberType + ", lctCode=" + lctCode + ", title=" + title
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
				+ ", relation=" + relation + ", principleId=" + principleId + ", principle=" + principle + ",orgId=" + orgId + "]";
	}
}
