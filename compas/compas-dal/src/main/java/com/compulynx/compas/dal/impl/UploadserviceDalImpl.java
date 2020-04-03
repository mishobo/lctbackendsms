package com.compulynx.compas.dal.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.compulynx.compas.dal.UploadserviceDal;
import com.compulynx.compas.dal.operations.Queryconstants;
import com.compulynx.compas.models.Subservice;
import com.compulynx.compas.models.CompasResponse;

public class UploadserviceDalImpl implements UploadserviceDal{
	private DataSource dataSource;
	
	public UploadserviceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}
	
	@Override
	public CompasResponse UploadService(String filePath) {
		try {
			ArrayList<ArrayList<Subservice>> list = getSubservicesExcel(filePath);
			if (uploadSubservices(list)) {
				return new CompasResponse(200, "Uploaded Successfully");
			} else {
				return new CompasResponse(201, "Can't upload duplicate services");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new CompasResponse(201, "Error. Could not process uploaded document.");
		}
	}
	//response message dependent on multiple db transations, hence in dal. refactor excel reading to bal higher abs class though
	//check api for overwritten compas response messages
	private ArrayList<ArrayList<Subservice>> getSubservicesExcel(String pathToFile) throws IOException {
		ArrayList<ArrayList<Subservice>> Subservices = new ArrayList<ArrayList<Subservice>>();
		try {
			System.out.println("FileNameInUploadExcel##" + pathToFile);
			FileInputStream file = new FileInputStream(new File(pathToFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			int sheets = workbook.getNumberOfSheets();
			int mstServiceId = -1;
			String query = "";
			for(int i=0;i<sheets;i++)
			{
				XSSFSheet sheet = workbook.getSheetAt(i);
				System.out.println("Working on sheet: "+sheet.getSheetName());
				if(sheet.getSheetName().contains("IP"))
				{
					query = "select id from (SELECT id FROM mst_health_services where ser_name like '%IN%PATIENT "+sheet.getSheetName().substring(3)+"%') as mhs";
					System.out.println(query);
				}
				else
				{
					query = "SELECT id FROM mst_health_services where ser_name like '%"+sheet.getSheetName()+"%' and ser_name not like '%INP%ATIENT%'";
					System.out.println(query);
				}
				PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();
				while(rs.next())
				{
					mstServiceId = rs.getInt("id");
				}
				ArrayList<Subservice> lst = processSheet(workbook, i, mstServiceId);
				Subservices.add(lst);
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
		return Subservices;
	}
	public ArrayList<Subservice> processSheet(XSSFWorkbook workbook, int idxSheet, int mstServiceId)
	{
		ArrayList<Subservice> Subservices = new ArrayList<Subservice>();
		XSSFSheet sheet = workbook.getSheetAt(idxSheet);
		int rows = sheet.getPhysicalNumberOfRows();
		System.out.println("Number of rows>>" + rows);
		DataFormatter df = new DataFormatter();
		//last row contains sample data for test excel file
		for (int r = 1; r < rows; r++) 
		{
			XSSFRow row = sheet.getRow(r);
			XSSFCell cell;
			Subservice Subservice = new Subservice();
			Subservice.sub_service_name = df.formatCellValue(row.getCell(0));
			Subservice.sub_service_code = df.formatCellValue(row.getCell(1));
			Subservice.sub_service_dept = df.formatCellValue(row.getCell(2));
			Subservice.sub_service_section = df.formatCellValue(row.getCell(3));
			Subservice.mst_service_id = mstServiceId;
			Subservices.add(Subservice);			
		}
		System.out.println("Number of rows##" + rows);
		System.out.println(Subservices.get(0));
		System.out.println(Subservices.get(Subservices.size()-1));
		return Subservices;

	}
	public boolean uploadSubservices(ArrayList<ArrayList<Subservice>> listSubservices)
	{
		boolean done = false;
		try 
		{
			Connection connection = dataSource.getConnection();
			//connection.setAutoCommit(false);
			System.out.println("Not using transactions...");
			PreparedStatement preparedStatement = connection.prepareStatement(Queryconstants.insertSubService);
			for(ArrayList<Subservice> serviceLst: listSubservices)
			{
				for(Subservice service: serviceLst)
				{
					preparedStatement.setString(1, service.sub_service_code);
					preparedStatement.setString(2, service.sub_service_name);
					preparedStatement.setString(3, service.sub_service_dept);
					preparedStatement.setString(4, service.sub_service_section);
					preparedStatement.setInt(5, service.mst_service_id);
					preparedStatement.execute();
				}
			}
			//connection.commit();
			done = true;
			if(done)
			{
				System.out.println("sub-services inserted");
			}
			else
			{
				System.out.println("sub-services not inserted");
			}
		}
		catch(SQLException e) { e.printStackTrace(); }
		
		return done;
	}

}
