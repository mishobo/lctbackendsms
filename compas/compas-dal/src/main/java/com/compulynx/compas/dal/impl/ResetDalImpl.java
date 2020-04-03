/**
 * 
 */
package com.compulynx.compas.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;

import com.compulynx.compas.dal.ResetDal;
import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.ResetUser;
import com.compulynx.compas.dal.operations.AES;
import com.compulynx.compas.dal.operations.LCTMailer;
import com.compulynx.compas.dal.operations.GenRandomPassword;

/**
 * @author dkabii
 *
 */
public class ResetDalImpl implements ResetDal {
	private DataSource	dataSource;

	public ResetDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	public ResetUser resetUserPassword(String strUser) {
		boolean done = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResetUser resetUser = new ResetUser();
		System.out.println("Opening DB connection...");
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(Queryconstants.getUserByUsernameOrEmail);
			preparedStatement.setString(1, strUser);
			//preparedStatement.setString(2, strUser);
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()) {
				System.out.println("Good to send...");
				resetUser.setEmail(resultSet.getString("UserEmail"));
				System.out.println("Handling email sending for user"+resetUser.getEmail());
				String strPwd = GenRandomPassword.generatePassword(15);
				String subject = "LCT Password Reset";
				String body = "Password successfully reset.<br/>Password: <b>"+strPwd+"</b><br/>Use it to login and change your password.";
				LCTMailer lctMailer = new LCTMailer();
				if(lctMailer.sendEmail(resetUser.getEmail(),subject,body))
				{
					preparedStatement.close();
					preparedStatement = connection.prepareStatement(Queryconstants.updateUserPassword);
					String encodedRandomPassword = AES.encrypt(strPwd);
					preparedStatement.setString(1, encodedRandomPassword);
					//preparedStatement.setString(1, strPwd);
					preparedStatement.setString(2, resetUser.getEmail());
					int affectedRows = preparedStatement.executeUpdate();
					System.out.println("affected rows: "+affectedRows);
					System.out.println("User Password: "+strPwd);
					if(affectedRows == 1)
					{
						resetUser.setRespCode(200);
						resetUser.setSuccess(true);
					}
					else
					{
						resetUser.setRespCode(400);
						resetUser.setSuccess(false);
					}
				}
				return resetUser;
			} else {
				System.out.println("Email does not exist");
				resetUser.setRespCode(201);
				resetUser.setSuccess(false);
			}
			return resetUser;

		} catch (Exception ex) {
			ex.printStackTrace();
			resetUser = new ResetUser();
			resetUser.setRespCode(404);
			resetUser.setSuccess(false);
			return resetUser;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	
}
