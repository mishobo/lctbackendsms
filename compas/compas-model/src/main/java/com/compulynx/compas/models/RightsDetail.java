package com.compulynx.compas.models;

public class RightsDetail {
	public int rightId;
	public String rightDisplayName;
	public String rightShortCode;
	public String rightViewName;
	public String rightName;
	public boolean rightAdd;
	public boolean rightEdit;
	public boolean rightDelete;
	public boolean rightView;
	public boolean allowAdd;
	public boolean allowEdit;
	public boolean allowDelete;
	public boolean allowView;
	public int rightMaxWidth;
	public String rightDisplayIcon;
	public int createdBy;
	
	public int respCode;

	public RightsDetail(int respCode) {
		super();
		this.respCode = respCode;
	}

	public RightsDetail(String rightDisplayName, String rightShortCode,
			String rightViewName, String rightName, boolean allowAdd,
			boolean allowEdit, boolean allowDelete, boolean allowView,
			int rightMaxWidth) {
		super();
		this.rightDisplayName = rightDisplayName;
		this.rightShortCode = rightShortCode;
		this.rightViewName = rightViewName;
		this.rightName = rightName;
		this.allowAdd = allowAdd;
		this.allowEdit = allowEdit;
		this.allowDelete = allowDelete;
		this.allowView = allowView;
		this.rightMaxWidth = rightMaxWidth;
		
	}
	
	public RightsDetail(int rightId, String rightDisplayName,
			boolean allowView, boolean allowAdd, boolean allowEdit,
			boolean allowDelete, int respCode) {
		super();
		this.rightId = rightId;
		this.rightDisplayName = rightDisplayName;
		this.allowView = allowView;
		this.allowAdd = allowAdd;
		this.allowEdit = allowEdit;
		this.allowDelete = allowDelete;

		this.respCode = respCode;
	}

	public RightsDetail() {
		super();
	}

	public RightsDetail(int rightId, String rightDisplayName, boolean rightAdd,
			boolean rightEdit, boolean rightDelete, boolean rightView,
			boolean allowAdd, boolean allowEdit, boolean allowDelete,
			boolean allowView, int respCode) {
		super();
		this.rightId = rightId;
		this.rightDisplayName = rightDisplayName;
		this.rightAdd = rightAdd;
		this.rightEdit = rightEdit;
		this.rightDelete = rightDelete;
		this.rightView = rightView;
		this.allowAdd = allowAdd;
		this.allowEdit = allowEdit;
		this.allowDelete = allowDelete;
		this.allowView = allowView;
		this.respCode = respCode;
	}

	public RightsDetail(int rightId, String rightDisplayName, boolean rightAdd,
			boolean rightEdit, boolean rightDelete, boolean rightView) {
		super();
		this.rightId = rightId;
		this.rightDisplayName = rightDisplayName;
		this.rightAdd = rightAdd;
		this.rightEdit = rightEdit;
		this.rightDelete = rightDelete;
		this.rightView = rightView;
	}
	public RightsDetail(int rightId, String rightName, 
			int respCode) {
		super();
		this.rightId = rightId;
		this.rightName = rightName;
		this.respCode = respCode;
	}

}
