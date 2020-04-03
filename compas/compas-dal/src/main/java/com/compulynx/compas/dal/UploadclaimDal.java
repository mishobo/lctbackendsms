package com.compulynx.compas.dal;

import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.models.Claim;
import com.compulynx.compas.models.OffLCT;
import com.compulynx.compas.models.SmartDebit;
import com.compulynx.compas.models.CompasResponse;

public interface UploadclaimDal {
	public boolean addUploadclaimDocument(List<Claim> doc);

	public CompasResponse UploadClaim(String filePath);

	// off lct
	public boolean addUploadOffLCTDocument(List<OffLCT> doc);

	public CompasResponse UploadOFFLCTClaim(String filePath);

	// smart debit
	public boolean addUploadSmartClaim(List<SmartDebit> doc);
	public CompasResponse UploadSmartClaim(String filePath);
}
