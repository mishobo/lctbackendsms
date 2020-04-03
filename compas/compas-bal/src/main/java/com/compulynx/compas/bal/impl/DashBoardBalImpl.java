package com.compulynx.compas.bal.impl;

import java.util.List;

import com.compulynx.compas.bal.DashBoardBal;
import com.compulynx.compas.dal.impl.DashBoardDalImpl;
import com.compulynx.compas.models.DashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DashBoardBalImpl implements DashBoardBal{

	@Autowired
	DashBoardDalImpl dashBoardDal;

	public DashBoard GetDashBoardCount() {
		
		return dashBoardDal.GetDashBoardCount();
	}
	
	public List<DashBoard> GetDashBoardCountDetail() {
	
		return dashBoardDal.GetDashBoardCountDetail();
	}


	public List<DashBoard> GetTransChartDetail() {
		return dashBoardDal.GetTransChartDetail();
	}
	@Override
	public List<DashBoard> GetDashBoardAmountDetail() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetDashBoardAmountDetail();
	}


	@Override
	public List<DashBoard> GetFlowChartCountDetail() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetFlowChartCountDetail();
	}


	@Override
	public List<DashBoard> GetDashBoardCollectionDetail() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetDashBoardCollectionDetail();
	}


	/* (non-Javadoc)
	 * @see com.compulynx.compas.bal.DashBoardBal#GetDashBoardAgentDetail()
	 */
	@Override
	public List<DashBoard> GetDashBoardAgentDetail() {
		// TODO Auto-generated method stub
		return dashBoardDal.GetDashBoardAgentDetail();
	}

	@Override
	public boolean isUserAKUH(int userId) {
		return dashBoardDal.isUserAKUH(userId);
	}

}
