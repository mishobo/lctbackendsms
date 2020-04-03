/**
 * 
 */
package com.compulynx.compas.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.bal.UserGroupBal;
import com.compulynx.compas.dal.impl.UserGroupDalImpl;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.GroupType;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.UserGroup;

/**
 * @author Anita
 *
 */
@Component
public class UserGroupBalImpl implements UserGroupBal{

	@Autowired
	UserGroupDalImpl userGroupDal;
	public CompasResponse UpdateUserGroup(UserGroup userGroup) {
		return userGroupDal.UpdateUserGroup(userGroup);
	}

	public List<RightsDetail> GetRights(int rightTypeId) {
		return userGroupDal.GetRights(rightTypeId);
	}


	public List<UserGroup> GetGroups() {
		// TODO Auto-generated method stub
		 return userGroupDal.GetGroups();
	}


	@Override
	public List<GroupType> GetGroupTypes() {
		// TODO Auto-generated method stub
		return userGroupDal.GetGroupTypes();
	}

}
