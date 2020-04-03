package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.GroupType;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.UserGroup;

public interface UserGroupDal {

	CompasResponse UpdateUserGroup(UserGroup group);

	List<UserGroup> GetGroups();

	List<GroupType> GetGroupTypes();
	List<RightsDetail> GetRights(int rightTypeId);
}
