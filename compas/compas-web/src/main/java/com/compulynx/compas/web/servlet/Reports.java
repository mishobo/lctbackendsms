package com.compulynx.compas.web.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.sql.Connection;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compulynx.compas.dal.operations.DBOperations;
import com.compulynx.compas.dal.operations.Queryconstants;
//import com.compulynx.compas.models.Reports;
import com.compulynx.compas.models.ReportsDTO;

//import org.springframework.beans.factory.annotation.Autowired;

/**
 * Servlet implementation class Reports
 */
@Component
public class Reports extends HttpServlet {
	private String TOMCAT_HOME = "";
	private String fileName = "";
	private String PATH = "";

	private static final long serialVersionUID = 1L;

	@Autowired
	private DataSource dataSource;

	/**
	 * F Default constructor.
	 */
	public Reports() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	public Reports(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public Connection getDbConnection() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// report path configuration
		TOMCAT_HOME = System.getProperty("catalina.base");
		PATH = TOMCAT_HOME + "/lct_uploads/lct_report/";
		String reportPath = PATH + fileName;
		System.out.println("The official Report Path is here: " + reportPath);

		// System.out.println("Called Servlet Reports");
		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		JasperDesign jasperDesign = null;
		Map<String, String> parameters = new HashMap();
		ServletOutputStream outstream = null;
		Connection connection = null;
		String reportType = request.getParameter("type");
		String exportType = request.getParameter("eType");
		DateTimeFormatter dtft = DateTimeFormat.forPattern("yyyy-MM-dd_HH:mm:ss");
		org.joda.time.DateTime now = new org.joda.time.DateTime();
		String by_now = String.valueOf(dtft.print(now));
		try {
			InitialContext initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:/comp/env");
			System.out.println("This is the WORKING reportPath: " + reportPath.toString());
			DataSource dataSource = (DataSource) context.lookup("jdbc/compasDS");
			connection = dataSource.getConnection();
			
			
			// member utilization report
			if (reportType.equalsIgnoreCase("MUR")) {
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptOutstandingBills.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptMemberUtilization.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "LCT_Member_Utilization.pdf");

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptOutstandingBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptMemberUtilization_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_utilization_report.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("OB")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String orgId = request.getParameter("orgId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptOutstandingBills.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptOutstandingBills.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptOutstandingBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptOutstandingBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_outstanding_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("PB")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String orgId = request.getParameter("orgId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptPendingBills.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptPendingBills.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptPendingBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptPendingBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_pending_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("UB")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String orgId = request.getParameter("orgId");

				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptUnpaidBills.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptUnpaidBills.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptUnpaidBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptUnpaidBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_unpaid_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("SB")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String orgId = request.getParameter("orgId");

				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptSettledBills.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptSettledBills.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptSettledBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptSettledBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_settled_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("RB")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String orgId = request.getParameter("orgId");

				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptRejectedBills.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptRejectedBills.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptRejectedBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptRejectedBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_rejected_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("BS")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String cardNo = request.getParameter("cardNo");
				String orgId = request.getParameter("orgId");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");

				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("memberNo", cardNo);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptMemberStatement.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptMemberStatement.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_member_statement.pdf");

					} else {
						System.out.println("No data");
					}

				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptMemberStatement_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptMemberStatement_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_member_statement.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			
			//judiciary providerwise
			if (reportType.equalsIgnoreCase("JDPT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign =
					// JRXmlLoader.load("lct_report/RptProviderWiseTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptJDCProviderWiseTransaction.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "LCT-judciary_providerwise_bills.pdf");

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					jasperDesign = JRXmlLoader.load(reportPath + "RptJDCProviderWiseBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_providerwise_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}

			if (reportType.equalsIgnoreCase("PT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				String providerId = request.getParameter("ProviderId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("providerId", providerId);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign =
					// JRXmlLoader.load("lct_report/RptProviderWiseTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptProviderWiseTransaction.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign =
					// JRXmlLoader.load("lct_report/RptProviderWiseBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptProviderWiseBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_providerwise_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("CT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String coverId = request.getParameter("SchemeId");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("schemeId", String.valueOf(coverId));
				parameters.put("orgId", String.valueOf(orgId));
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptSchemeWiseTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptSchemeWiseTransaction.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptSchemeWiseBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptSchemeWiseBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_schemewise_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("CN")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptCancelledTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptCancelledTransaction.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptCancelledBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptCancelledBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_cancelled_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			if (reportType.equalsIgnoreCase("AT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptAuthorizedTransaction.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptAuthorizedBills_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_authorized_bills.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			// INDIVIDUAL TRANSACTIONS
			if (reportType.equalsIgnoreCase("ITR")) {
				//String memberNo = request.getParameter("memberNo");
				String billNo = request.getParameter("billNo");
				Connection Individualconnection = null;
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					Individualconnection = dataSource.getConnection();
					preparedStatement = Individualconnection.prepareStatement(Queryconstants.getIndividualTransactions);
						preparedStatement.setString(1, billNo);
						preparedStatement.setString(2, billNo); 
						System.out.println("Individual Report Query: " +preparedStatement.toString());
						System.out.println("Individual invoice Number: " +billNo);
						resultSet = preparedStatement.executeQuery();
						List<ReportsDTO> reports = new ArrayList<ReportsDTO>();
						while (resultSet.next()) {
							ReportsDTO report = new ReportsDTO();
							// report.fromDate = resultSet.getString("");
							report.id = resultSet.getInt("id");
							report.member_no = resultSet.getString("member_no");
							report.programmedesc = resultSet.getString("programmedesc");
							report.bill_no = resultSet.getString("bill_no");
							report.trans_date = resultSet.getString("trans_date");
							report.ser_name = resultSet.getString("ser_name");
							report.trans_status = resultSet.getString("trans_status");
							report.card_no = resultSet.getString("card_no");
							report.acc_no = resultSet.getString("acc_no");
							report.invoice_number=resultSet.getString("invoice_number");
							report.name = resultSet.getString("NAME");
							report.service_amount = resultSet.getBigDecimal("service_amount");
							report.product_name = resultSet.getString("product_name");
							report.mer_name = resultSet.getString("mer_name");
							report.productid = resultSet.getInt("productId");
							report.merchantid = resultSet.getInt("merchantId");
							report.invoice_number = resultSet.getString("invoice_number");
							reports.add(report);
							System.out.println(report);
						}
						JRBeanCollectionDataSource dataSourceIndividual = new JRBeanCollectionDataSource(reports, false);
					// add parameters
					//parameters.put("memberNo", memberNo);
					parameters.put("billNo", billNo);
					if (exportType.equalsIgnoreCase("P")) {
						jasperDesign = JRXmlLoader.load(reportPath + "RptIndividualTransactions.jrxml");
						jasperReport = JasperCompileManager.compileReport(jasperDesign);
						jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSourceIndividual);

						if (jasperPrint.getPages().size() != 0) {
							byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
							outstream = response.getOutputStream();
							response.setContentType("application/pdf");
							response.setContentLength(pdfasbytes.length);
							response.setHeader("Content-disposition", "attachment; filename=" + by_now + " LCT_MemberTrans - "+billNo+".pdf");
							outstream.write(pdfasbytes, 0, pdfasbytes.length);

						} else {
							System.out.println("No data");
						}
					}
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
				finally {
					DBOperations.DisposeSql(Individualconnection, preparedStatement, resultSet);
				}	
			}	
			
			// ALL TRANSACTIONS
			if (reportType.equalsIgnoreCase("ALLSPTR")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				String mer_name = request.getParameter("mer_name");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				parameters.put("mer_name", mer_name);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "SpRptAllTransactions.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "SpRptAllTransactions_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_All_"+mer_name+"_Transactions.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			// ALL TRANSACTIONS
			if (reportType.equalsIgnoreCase("ALLTR")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptAllTransactions.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "RptAllTransactions_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_All_AKUH_Transactions.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			// AKUH ALL TRANSACTIONS
			if (reportType.equalsIgnoreCase("AKT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "AkuhRptAuthorizedTransaction.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "AkuhRptAuthorizedTransaction_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_All_AKUH_Transactions.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
			// AKUH ALL TRANSACTIONS
			if (reportType.equalsIgnoreCase("GRT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "GertRpAllTransactions.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "AkuhRptAuthorizedTransaction_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_All_AKUH_Transactions.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}

			// MPSHA ALL TRANSACTIONS
			if (reportType.equalsIgnoreCase("MPT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedTransaction.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "MpshahRptAllTransactions.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					jasperDesign = JRXmlLoader.load(reportPath + "AkuhRptAuthorizedTransaction_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_All_AKUH_Transactions.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}

			// NAIROBI HOSPITAL
			if (reportType.equalsIgnoreCase("NHT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					jasperDesign = JRXmlLoader.load(reportPath + "NboRpAllTransactions.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					//jasperDesign = JRXmlLoader.load(reportPath + "AkuhRptAuthorizedTransaction_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_All_NAIROBI HOSPITAL_Transactions.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}

			if (reportType.equalsIgnoreCase("RI")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String tDate = request.getParameter("tDt");
				String file_name = request.getParameter("file_name");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTime t = dtf.parseDateTime(tDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("tDt", tDate);
				parameters.put("file_name", file_name);
				// jasperDesign =
				// JRXmlLoader.load("lct_report/RptRemitanceInformation_xlsx.jrxml");
				jasperDesign = JRXmlLoader.load(reportPath + "RptRemitanceInformation_xlsx.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
				if (jasperPrint.getPages().size() != 0) {
					JRXlsxExporter exporter = getCommonXlsxExporter();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
					exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
					exporter.exportReport();

					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setHeader("Content-disposition", "attachment; filename=" + file_name + "x");
					response.setContentLength(baos.size());
					response.getOutputStream().write(baos.toByteArray());

				}
			}
			// claims remitance settlement file
			if (reportType.equalsIgnoreCase("CRI")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String tDate = request.getParameter("tDt");
				String file_name = request.getParameter("file_name");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				DateTime t = dtf.parseDateTime(tDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("tDt", tDate);
				parameters.put("file_name", file_name);
				// jasperDesign =
				// JRXmlLoader.load("lct_report/RptRemitanceInformation_xlsx.jrxml");
				jasperDesign = JRXmlLoader.load(reportPath + "RptClaimRemitanceInformation_xlsx.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
				if (jasperPrint.getPages().size() != 0) {
					JRXlsxExporter exporter = getCommonXlsxExporter();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
					exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
					exporter.exportReport();

					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setHeader("Content-disposition", "attachment; filename=" + file_name + "x");
					response.setContentLength(baos.size());
					response.getOutputStream().write(baos.toByteArray());

				}
			}
			if (reportType.equalsIgnoreCase("PRT")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				String myId = request.getParameter("providerId");
				String orgId = request.getParameter("orgId");
				String status = request.getParameter("st");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				parameters.put("FromDt", fromDate);
				parameters.put("ToDt", toDate);
				parameters.put("myId", myId);
				parameters.put("orgId", orgId);
				parameters.put("status", status);
				// jasperDesign =
				// JRXmlLoader.load("lct_report/ProviderTransactions_xlsx.jrxml");
				jasperDesign = JRXmlLoader.load(reportPath + "ProviderTransactions_xlsx.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
				if (jasperPrint.getPages().size() != 0) {
					JRXlsxExporter exporter = getCommonXlsxExporter();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
					exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
					exporter.exportReport();

					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setHeader("Content-disposition", "attachment; filename=" + by_now + ".xlsx");
					response.setContentLength(baos.size());
					response.getOutputStream().write(baos.toByteArray());

				}
			}
			//audit log
			if (reportType.equalsIgnoreCase("ATR")) {
				String fromDate = request.getParameter("FrDt");
				String toDate = request.getParameter("ToDt");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				// Format for output
				DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
				String orgId = request.getParameter("orgId");
				parameters.put("fromDt", fromDate);
				parameters.put("toDt", toDate);
				parameters.put("orgId", orgId);
				if (exportType.equalsIgnoreCase("P")) {
					jasperDesign = JRXmlLoader.load(reportPath + "AuditTrail.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);

					} else {
						System.out.println("No data");
					}
				} else if (exportType.equalsIgnoreCase("E")) {
					// jasperDesign = JRXmlLoader.load("lct_report/RptAuthorizedBills_xlsx.jrxml");
					//jasperDesign = JRXmlLoader.load(reportPath + "AkuhRptAuthorizedTransaction_xlsx.jrxml");
					jasperReport = JasperCompileManager.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, baos);
						exporter.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, Boolean.TRUE);
						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + by_now + "_All_NAIROBI HOSPITAL_Transactions.xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					}
				}
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private JRXlsxExporter getCommonXlsxExporter() {
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);

		// exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
		// Boolean.TRUE);

		return exporter;
	}

}
