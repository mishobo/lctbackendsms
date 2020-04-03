package com.compulynx.compas.dal;

import com.compulynx.compas.models.CompasResponse;

public interface UploadmemberDal {
	CompasResponse UploadMember(String filePath, int orgId, int productId, int programmeId, String createdBy);
}
