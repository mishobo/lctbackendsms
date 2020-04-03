package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.compas.dal.UserGroupDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.CompasResponse;
import com.compulynx.compas.models.GroupType;
import com.compulynx.compas.models.RightsDetail;
import com.compulynx.compas.models.UserGroup;

public class UserGroupDalImpl implements UserGroupDal {
	private DataSource dataSource;

	public UserGroupDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public List<UserGroup> GetGroups() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroups);
			resultSet = preparedStatement.executeQuery();
			List<UserGroup> groups = new ArrayList<UserGroup>();
			while (resultSet.next()) {
				UserGroup objGroup = new UserGroup();
				objGroup.groupId = resultSet.getInt("ID");
				objGroup.groupCode = resultSet.getString("groupcode");
				objGroup.groupName = resultSet.getString("groupName");
				objGroup.active = resultSet.getBoolean("active");
				objGroup.grpTypeId = resultSet.getInt("grouptypeid");
				objGroup.createdBy = resultSet.getInt("createdBy");
				objGroup.count=count;
				objGroup.grpTypeName=resultSet.getString("grptypename");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getGroupById);
				preparedStatement.setInt(1, objGroup.groupId);
				preparedStatement.setInt(2, objGroup.groupId);
				preparedStatement.setInt(3, objGroup.groupId);
				resultSet2 = preparedStatement.executeQuery();
				List<RightsDetail> rightList = new ArrayList<RightsDetail>();
				while (resultSet2.next()) {
					rightList
							.add(new RightsDetail(
									resultSet2.getInt("RightID"),
									resultSet2.getString("RightDisplayName"),
									resultSet2.getBoolean("RightAdd"),
									resultSet2.getBoolean("RightEdit"),
									resultSet2.getBoolean("RightDelete"),
									resultSet2.getBoolean("RightView"),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2.getBoolean("AllowAdd")),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2
													.getBoolean("AllowEdit")),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2
													.getBoolean("AllowDelete")),
									resultSet2.getBoolean("AllowView"), 200));
				}
				objGroup.rights = rightList;
				groups.add(objGroup);
				count++;
			}
			return groups;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public CompasResponse UpdateUserGroup(UserGroup group) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int groupId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			if (group.groupId == 0) {
				if (CheckGroupCode(group.groupCode)) {
					return new CompasResponse(201, "Group code already exists");
				}
				if (!CheckGroupByName(group.groupName)) {
					preparedStatement = connection.prepareStatement(
							Queryconstants.insertUserGroup,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, group.groupCode);
					preparedStatement.setString(2, group.groupName);
					preparedStatement.setInt(3, group.grpTypeId);
					preparedStatement.setBoolean(4,group.active);
					preparedStatement.setInt(5, group.createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));

				} else {
					return new CompasResponse(201,
							"Group Name is Already Exists");
				}
			} else {
				if (CheckGroupNameByCode(group.groupName,group.groupCode)) {
					return new CompasResponse(201, "Group Name already exists");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateUserGroup);
				preparedStatement.setString(1, group.groupName);
				preparedStatement.setInt(2, group.grpTypeId);
				preparedStatement.setBoolean(3,group.active);
				preparedStatement.setInt(4, group.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, group.groupId);
				groupId = group.groupId;

			}
			if (preparedStatement.executeUpdate() > 0) {

				// Dispose
				if (group.groupId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					groupId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteGroupRights);
				preparedStatement.setInt(1, groupId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				// insert rights
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertGroupRights);
				if (group.rights != null) {
					for (int i = 0; i < group.rights.size(); i++) {
						preparedStatement
								.setInt(1, group.rights.get(i).rightId);
						preparedStatement.setInt(2, groupId);
						preparedStatement.setBoolean(3,
								group.rights.get(i).rightView);
						preparedStatement.setBoolean(4,
								group.rights.get(i).rightAdd);
						preparedStatement.setBoolean(5,
								group.rights.get(i).rightEdit);
						preparedStatement.setBoolean(6,
								group.rights.get(i).rightDelete);
						preparedStatement.setInt(7,
								group.rights.get(i).createdBy);
						preparedStatement.setTimestamp(
								8,
								new java.sql.Timestamp(new java.util.Date()
										.getTime()));
						if (preparedStatement.executeUpdate() <= 0) {
							throw new Exception("Failed to Insert Right Id "
									+ group.rights.get(i).rightId);
						}
					}
				}
				connection.commit();
				return new CompasResponse(200, "Records Updated");

			} else {
				connection.rollback();
				return new CompasResponse(202, "Nothing To Update");
			}

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new CompasResponse(201, "Group Name is Already Exists");
			} else {
				return new CompasResponse(404, "Exception Occured");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new CompasResponse(404, "Exception Occured");
		} finally {
			DBOperations.DisposeSql(rs);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckGroupByName(String groupName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupByName);
			preparedStatement.setString(1, groupName);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckGroupCode(String groupCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupByCode);
			preparedStatement.setString(1, groupCode);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public boolean CheckGroupNameByCode(String groupName, String groupCode) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupNameByCode);
			preparedStatement.setString(1, groupName);
			preparedStatement.setString(2, groupCode);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<RightsDetail> GetRights(int rightTypeId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRights);
			//preparedStatement.setInt(1, rightTypeId);
			resultSet = preparedStatement.executeQuery();
			List<RightsDetail> rights = new ArrayList<RightsDetail>();
			while (resultSet.next()) {
				rights.add(new RightsDetail(resultSet.getInt("ID"), resultSet
						.getString("RightDisplayName"), resultSet
						.getBoolean("AllowView"), ((resultSet
						.getBoolean("AllowView") == false) ? false : resultSet
						.getBoolean("AllowAdd")), ((resultSet
						.getBoolean("AllowView") == false) ? false : resultSet
						.getBoolean("AllowEdit")), ((resultSet
						.getBoolean("AllowView") == false) ? false : resultSet
						.getBoolean("AllowDelete")), 200

				));
			}
			return rights;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public List<GroupType> GetGroupTypes() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupTypes);

			resultSet = preparedStatement.executeQuery();
			List<GroupType> grpTypes = new ArrayList<GroupType>();
			while (resultSet.next()) {
				grpTypes.add(new GroupType(resultSet.getInt("ID"), resultSet
						.getString("grptype_name"), 200

				));
			}
			return grpTypes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
