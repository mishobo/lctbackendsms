package com.compulynx.compas.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: SJ
 * Date: 10/27/13
 * Time: 7:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralUtility {

    public static String getStringPadLeft(String inputStr, String padStr, int outStrLen) {

        String output = "";
        StringBuffer sbf = new StringBuffer(outStrLen);
        int inpLen = 0;

        try {
            inpLen = inputStr.trim().length();
        } catch (NullPointerException npe) {
            inpLen = 0;
        }
        for (int i = 0; i < outStrLen - inpLen; i++) {
            sbf.append(padStr);
        }
        sbf.append(inputStr.trim());
        output = sbf.toString();

        return output;
        
        
    }

    public static String getSubString(String input, String defaultOutput,
                                      int startIndex, int endIndex) {
        String output = "";

        try {
            if (input.trim() == null || input.trim().equals("")) {
                output = defaultOutput;
            } else {
                output = input.trim().substring(startIndex, endIndex).trim();
            }
        } catch (NullPointerException npe) {
            output = defaultOutput;
        } catch (StringIndexOutOfBoundsException strEx) {
            output = input.substring(startIndex);
            //output = defaultOutput;
        } catch (IndexOutOfBoundsException iobe) {
            output = input;
        }

        return output;
    }

    public static String getStringPadRight(String inputStr, String padStr,
                                           int outStrLen) {

        String output = "";
        StringBuffer sbf = new StringBuffer(outStrLen);
        int inpLen = inputStr.trim().length();
        sbf.append(getString2(inputStr, ""));
        for (int i = 0; i < outStrLen - inpLen; i++) {
            sbf.append(padStr);
        }
        output = sbf.toString();

        return output;
    }

    public static String getString2(String input, String defaultOutput) {

        String output = "";

        // code by
        try {
            input = input.trim();
            if (input == null || input.trim().equals("") || input.trim().length() == 0) {
                output = defaultOutput;
            } else {
                output = input;
            }
        } catch (NullPointerException npe) {
            output = defaultOutput;
        }
        return output;
    }

    public static String getDate1(String dateStr, String dateFormat) {
        String formattedDate = "";
        Date d = null;
        DateFormat readFormat = new SimpleDateFormat( "EEE MMM dd hh:mm:ss Z yyyy");
        DateFormat writeFormat = new SimpleDateFormat( "yyyyMMdd");
        try {
            if (dateStr.equals("00000000") || dateStr.equals("FFFFFFFF")) {
                formattedDate = "19000101";
            }
            Date date = null;
            try {
                date = readFormat.parse( dateStr );
            } catch ( ParseException e ) {
                e.printStackTrace();
            }
            SimpleDateFormat df = new SimpleDateFormat(dateFormat);
            d = readFormat.parse(dateStr);
            if( date != null ) {
                formattedDate = writeFormat.format( date );
            }
        } catch (ParseException e) {
            System.out.println("getDate1 exception " + e);
            e.printStackTrace();
        }
        return formattedDate;
    }
    
    public static String splitMemberNo(String memberNo){
    	String result=memberNo.substring(3,memberNo.length());
    	return result;
    	
    }
    public static String splitPJ(String memberNo){
    	System.out.println("Heloooo "+memberNo);
    	String str1=memberNo.substring(3,memberNo.length());
    	String result[]=str1.split("-");
    	return result[0].toString();
    	
    }
}
