/**
 * 
 */
package com.compulynx.compas.models;

/**
 * @author Anita
 *
 */
public class Wallet {

	public int walletId;
	public String walletName;
	public boolean isActive;
	public int createdBy;
	public String fileId;
	public String fileType;
	public String fileSize;
	public String recordLength;
	public String fileLength;
	public String systemCode;
	public Wallet(int walletId, String walletName) {
		super();
		this.walletId = walletId;
		this.walletName = walletName;
	}
	public Wallet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Wallet(int walletId, String walletName, String fileId,
			String fileType, String fileSize, String recordLength,
			String fileLength, String systemCode) {
		super();
		this.walletId = walletId;
		this.walletName = walletName;
		this.fileId = fileId;
		this.fileType = fileType;
		this.fileSize = fileSize;
		this.recordLength = recordLength;
		this.fileLength = fileLength;
		this.systemCode = systemCode;
	}
	
}
