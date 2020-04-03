package com.compulynx.compas.dal;

import com.compulynx.compas.models.CompasResponse;

public interface UploadserviceDal {
	CompasResponse UploadService(String filePath);
}
