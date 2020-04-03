import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * 
 */

/**
 * @author Anita Mar 10, 2017
 */
public class cardnumberutil {
	static final String DB_URL = "jdbc:mysql://localhost:3306/lct_compas";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "godisgr8";

	/*
	 * public static void main(String[]args) { try { String filename =
	 * "C:/NewExcelFile.xls" ; HSSFWorkbook workbook = new HSSFWorkbook();
	 * HSSFSheet sheet = workbook.createSheet("FirstSheet"); Connection conn =
	 * null; java.sql.PreparedStatement stmt = null; ResultSet resultset = null;
	 * String sql; conn = (Connection) DriverManager.getConnection(DB_URL, USER,
	 * PASS); sql = "select acc_id,acc_no from emp_acc_dtl";
	 * 
	 * stmt= conn.prepareStatement(sql); resultset = stmt.executeQuery();
	 * HSSFRow rowhead = sheet.createRow((short)0);
	 * rowhead.createCell(0).setCellValue("No.");
	 * rowhead.createCell(1).setCellValue("Name"); int count=0; while
	 * (resultset.next()) { count++; HSSFRow row =
	 * sheet.createRow((short)count);
	 * row.createCell(0).setCellValue(resultset.getInt("acc_id"));
	 * row.createCell(1).setCellValue(resultset.getString("acc_no"));
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * FileOutputStream fileOut = new FileOutputStream(filename);
	 * workbook.write(fileOut); fileOut.close();
	 * System.out.println("Your excel file has been generated!");
	 * 
	 * } catch ( Exception ex ) { System.out.println(ex); } }
	 */
//	public static void main(String[] args) {
//		try {
//			String filename = "C:/NewExcelFile.xls";
//			HSSFWorkbook workbook = new HSSFWorkbook();
//			HSSFSheet sheet = workbook.createSheet("FirstSheet");
//			Connection conn = null;
//			java.sql.PreparedStatement stmt = null;
//			java.sql.PreparedStatement stmt1 = null;
//			ResultSet resultset = null;
//			String sql;
//			String sql1;
//			conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
//			sql = "insert into mst_allocation (programme_id,acc_id,acc_no,basket_id,service_id,service_value,basket_value,service_balance,basket_balance,cycle,created_by,created_on) values (?,?,?,?,?,?,?,?,?,?,?,?)";
//			sql1 = "SELECT acc_id,acc_no,cp.programmeid,voucherid,service_id,cover_limit,basket_value,2017 as cycle "
//					+ " FROM emp_acc_dtl ac ,"
//					+ "customerprogrammedetails cp,programmevoucherdetails pv   "
//					+ "where cp.customerid=ac.emp_mst_id and cp.isactive=1 "
//					+ "and pv.programmeid=cp.programmeid and pv.isactive=1 ";
//			stmt = conn.prepareStatement(sql1);
//			resultset = stmt.executeQuery();
//			/*
//			 * HSSFRow rowhead = sheet.createRow((short) 0);
//			 * rowhead.createCell(0).setCellValue("No.");
//			 * rowhead.createCell(1).setCellValue("Name");
//			 */
//			int count = 0;
//			while (resultset.next()) {
//				System.out.println("saving"+count);
//				count++;
//				stmt1 = conn.prepareStatement(sql);
//				stmt1.setInt(1, resultset.getInt("programmeid"));
//				stmt1.setInt(2, resultset.getInt("acc_id"));
//				stmt1.setString(3, resultset.getString("acc_no"));
//				stmt1.setInt(4, resultset.getInt("voucherid"));
//				stmt1.setInt(5, resultset.getInt("service_id"));
//				stmt1.setDouble(6, resultset.getDouble("cover_limit"));
//				stmt1.setDouble(7, resultset.getDouble("basket_value"));
//				stmt1.setDouble(8, 0);
//				stmt1.setDouble(9, 0);
//				stmt1.setInt(10, resultset.getInt("cycle"));
//				stmt1.setInt(11, 1);
//				stmt1.setTimestamp(12, new java.sql.Timestamp(
//						new java.util.Date().getTime()));
//				stmt1.executeUpdate();
//			}
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//	}

	private static final int BUFFER_SIZE = 4096;

	/**
	 * Downloads a file from a URL
	 * 
	 * @param fileURL
	 *            HTTP URL of the file to be downloaded
	 * @param saveDir
	 *            path of the directory to save the file
	 * @throws IOException
	 */
	// public static void downloadFile(String fileURL, String saveDir)
	// throws IOException {
	// URL url = new URL(fileURL);
	// HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	// int responseCode = httpConn.getResponseCode();
	//
	// // always check HTTP response code first
	// if (responseCode == HttpURLConnection.HTTP_OK) {
	// String fileName = "";
	// String disposition = httpConn.getHeaderField("Content-Disposition");
	// String contentType = httpConn.getContentType();
	// int contentLength = httpConn.getContentLength();
	//
	// if (disposition != null) {
	// // extracts file name from header field
	// int index = disposition.indexOf("filename=");
	// if (index > 0) {
	// fileName = disposition.substring(index + 10,
	// disposition.length() - 1);
	// }
	// } else {
	// // extracts file name from URL
	// fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
	// fileURL.length());
	// }
	//
	// System.out.println("Content-Type = " + contentType);
	// System.out.println("Content-Disposition = " + disposition);
	// System.out.println("Content-Length = " + contentLength);
	// System.out.println("fileName = " + fileName);
	//
	// // opens input stream from the HTTP connection
	// InputStream inputStream = httpConn.getInputStream();
	// String saveFilePath = saveDir + File.separator + fileName;
	//
	// // opens an output stream to save into file
	// FileOutputStream outputStream = new FileOutputStream(saveFilePath);
	//
	// int bytesRead = -1;
	// byte[] buffer = new byte[BUFFER_SIZE];
	// while ((bytesRead = inputStream.read(buffer)) != -1) {
	// outputStream.write(buffer, 0, bytesRead);
	// }
	//
	// outputStream.close();
	// inputStream.close();
	//
	// System.out.println("File downloaded");
	// } else {
	// System.out.println("No file to download. Server replied HTTP code: " +
	// responseCode);
	// }
	// httpConn.disconnect();
	// }

	// public static void main(String[] args) {
	// String memberNo="JDC30001-01";
	// String str1=memberNo.substring(3,memberNo.length());
	// String result[]=str1.split("-");
	// result[0].toString();
	// System.out.println(result[0].toString());
	//
	// }

	
	  public static void main(String[] args) {
	  
	  Connection conn = null; java.sql.PreparedStatement stmt = null;
	  java.sql.PreparedStatement stmt1 = null; java.sql.PreparedStatement stmt2
	 = null; ResultSet resultset = null; ResultSet resultset1 = null; try {
	 Class.forName("com.mysql.jdbc.Driver"); String sql; String sql1; String
	 sql2; System.out.println("Connecting to database..."); conn =
	  (Connection) DriverManager.getConnection(DB_URL, USER, PASS); 
	 sql1 =
	  "select acc_id,emp_mst_id from emp_acc_dtl";
	  
	 stmt1 = conn.prepareStatement(sql1); resultset = stmt1.executeQuery();
	  while (resultset.next()) { sql2 =
	"select emp_mst_id,ref_sub_key from emp_dtl where emp_mst_id=?"; stmt2 =
	  conn.prepareStatement(sql2);
	stmt2.setInt(1,resultset.getInt("emp_mst_id")); resultset1 =
	 stmt2.executeQuery(); sql =
	  "insert into card_mst (acc_id,emp_mst_id,card_no,pj_number,card_serialno,card_status,created_by,created_on) values (?,?,?,?,?,?,?,?)"
	  ; stmt = conn.prepareStatement(sql); 
	  stmt.setInt(1,
	 resultset.getInt("emp_id"));
	 while (resultset1.next()) { stmt.setInt(1,
	  resultset.getInt("acc_id")); stmt.setInt(2,
	 resultset1.getInt("emp_mst_id")); stmt.setString(3, "804400" +
	 generateNumber()); stmt.setString(4,
	  resultset1.getString("ref_sub_key")); stmt.setString(5,"");
	  stmt.setString(6,"I"); stmt.setInt(7,1); stmt.setTimestamp(8, new
	  java.sql.Timestamp( new java.util.Date().getTime()));
	  stmt.executeUpdate(); System.out.println("insertted###"+
	  resultset.getInt("acc_id")); } }
	 
	  // stmt.setString(1, );
	  
	  for (int i = 0; i < 20000; i++) { String cardno = "804400" +
	  generateNumber(); stmt.setString(1, cardno); stmt.executeUpdate();
	  System.out.println(cardno); // Retrieve by column name
	  
	  // Display values
	  
	  } 
	  //rs.close();
	  stmt.close(); conn.close(); } catch (SQLException se) {
	 
	 se.printStackTrace(); } catch (Exception e) {
	 
	  e.printStackTrace(); } finally {
	  
	  try { if (stmt != null) stmt.close(); } catch (SQLException se2) { } try
	  { if (conn != null) conn.close(); } catch (SQLException se) {
	 se.printStackTrace(); } } System.out.println("Goodbye!");
	 
	  }
	 

	public static long generateNumber() {
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		return number;// (long) (Math.random() * 100000 + 4444300000L);
	}

	public int getRandomNumber(int min, int max) {
		return (int) Math.floor(Math.random() * (max - min + 1)) + min;
	}
}
