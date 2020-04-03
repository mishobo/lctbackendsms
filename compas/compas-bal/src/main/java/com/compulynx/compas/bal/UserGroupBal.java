/**
 * 
 */
package com.compulynx.compas.bal;

import java.util.List;

import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.GroupType;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.UserGroup;

/**
 * @author Anita
 *
 */
public interface UserGroupBal {

	CompasResponse UpdateUserGroup(UserGroup group);

	List<UserGroup> GetGroups();

	List<GroupType> GetGroupTypes();
	List<RightsDetail> GetRights(int rightTypeId);
}
