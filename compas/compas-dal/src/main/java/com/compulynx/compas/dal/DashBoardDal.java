package com.compulynx.compas.dal;

import java.util.List;

import com.compulynx.compas.models.DashBoard;

public interface  DashBoardDal {
	DashBoard GetDashBoardCount();
	
	List<DashBoard> GetDashBoardCountDetail();
	List<DashBoard> GetTransChartDetail();
	List<DashBoard> GetDashBoardAmountDetail() ;
	List<DashBoard> GetFlowChartCountDetail();
	List<DashBoard> GetDashBoardCollectionDetail();
	List<DashBoard> GetDashBoardAgentDetail();
	boolean IsUserAKUH(int userId);
}
