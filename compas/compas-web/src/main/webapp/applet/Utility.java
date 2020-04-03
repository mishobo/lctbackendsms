package com.compulynx.compas.commons;

import java.io.PrintStream;
import java.util.ArrayList;

public class Utility
{
  public static byte[] getByteArFromString(String input)
  {
    int j = 0;
    int i = 0;
    byte[] arbyte = null;
    try
    {
      int inpLen = input.length();
      if (inpLen % 2 != 0)
      {
        input = input.concat("0");
        inpLen++;
      }
      arbyte = new byte[inpLen / 2];
      for (i = 0; i < inpLen; i += 2) {
        try
        {
          int k = Integer.parseInt(input.substring(i, i + 2), 16);
          arbyte[j] = Integer.valueOf(k).byteValue();
          j++;
        }
        catch (NumberFormatException e)
        {
          System.out.println("Number Format Exception #1 in getByteArFromString " + 
            e);
          String a = input.substring(i, i + 2);
          int l = 0;
          for (int c = 0; c < 2; c++) {
            if (Character.isLetter(a.charAt(c))) {
              l++;
            } else if (Character.isWhitespace(a.charAt(c))) {
              l = -1;
            } else {
              l = -1;
            }
          }
          arbyte[j] = 
            ((byte)((Character.digit(a.charAt(0), 16) << 4) + Character.digit(a.charAt(1), 16)));
        }
      }
    }
    catch (NumberFormatException ne)
    {
      System.out.println("Number Format Exception #2 in getByteArFromString " + 
        ne);
      arbyte[j] = Byte.parseByte(input.substring(i, i + 2));
    }
    return arbyte;
  }
  
  public static String getStringPadLeft(String inputStr, String padStr, int outStrLen)
  {
    String output = "";
    StringBuffer sbf = new StringBuffer(outStrLen);
    int inpLen = 0;
    try
    {
      inpLen = inputStr.trim().length();
    }
    catch (NullPointerException npe)
    {
      inpLen = 0;
    }
    for (int i = 0; i < outStrLen - inpLen; i++) {
      sbf.append(padStr);
    }
    sbf.append(inputStr.trim());
    output = sbf.toString();
    
    return output;
  }
  
  public static byte[] getByteArrayFromArList(ArrayList<byte[]> a)
  {
    int len = 0;
    byte[] opByte = null;
    for (int i = 0; i < a.size(); i++) {
      len += ((byte[])a.get(i)).length;
    }
    opByte = new byte[len];
    
    int k = 0;
    for (int i = 0; i < a.size(); i++)
    {
      byte[] temp = (byte[])a.get(i);
      for (int j = 0; j < temp.length; j++)
      {
        opByte[k] = temp[j];
        k++;
      }
    }
    return opByte;
  }
  
  public static String GenNewKey(String companyKey, String cardKey)
  {
    StringBuffer newKey = new StringBuffer("");
    
    System.out.println("CardUtility.GenNewKeyKU() ---" + cardKey + "---");
    
    int companySingleDigit = Integer.parseInt(SingleDigitKey(companyKey));
    int cardSingleDigit = Integer.parseInt(SingleDigitKey(cardKey));
    for (int i = 0; i < cardKey.length(); i++)
    {
      int key = 0;
      
      key = Integer.parseInt(companyKey.charAt(i), 16) * 28 + 
        Integer.parseInt(cardKey.charAt(i), 16) * 10;
      switch (i % 2)
      {
      case 0: 
        key *= companySingleDigit;
        key += 14;
        break;
      default: 
        key *= cardSingleDigit;
        key += 25;
      }
      key = key % 16;
      
      newKey.append(Character.toUpperCase(Character.forDigit(key, 16)));
    }
    System.out.println("CardUtility.GenNewKey()  ------ new key" + 
      newKey.toString());
    return newKey.toString();
  }
  
  public static String SingleDigitKey(String key)
  {
    String finalSingleDigit = "";
    
    int keyNum = 0;
    for (int i = 0; i < key.length(); i++) {
      keyNum += Character.getNumericValue(key.charAt(i));
    }
    finalSingleDigit = Integer.toString(keyNum);
    keyNum = 0;
    while (finalSingleDigit.length() > 1)
    {
      for (int i = 0; i < finalSingleDigit.length(); i++) {
        keyNum += Character.getNumericValue(finalSingleDigit.charAt(i));
      }
      finalSingleDigit = Integer.toString(keyNum);
      keyNum = 0;
    }
    return finalSingleDigit;
  }
  
  private static String get16Digit(String cardKey)
  {
    StringBuffer sbuf = new StringBuffer(16);
    for (int i = 0; i < cardKey.length(); i++)
    {
      int key = 0;
      if (!Character.isDigit(cardKey.charAt(i))) {
        key = cardKey.charAt(i);
      } else {
        key = Integer.parseInt(cardKey.substring(i, i + 1));
      }
      sbuf.append(key);
    }
    System.out.println("CardUtility.getKU16Digit() ---" + sbuf.toString() + 
      "---");
    
    return getSubString(sbuf.toString(), 0, 16);
  }
  
  public static String getSubString(String input, int startIndex, int endIndex)
  {
    String output = "";
    try
    {
      if ((input == null) || (input.equals(""))) {
        output = " ";
      } else {
        output = input.substring(startIndex, endIndex);
      }
    }
    catch (NullPointerException npe)
    {
      output = " ";
    }
    catch (StringIndexOutOfBoundsException strEx)
    {
      output = input.substring(startIndex);
    }
    catch (IndexOutOfBoundsException iobe)
    {
      output = input;
    }
    return output;
  }
  
  public static String getString2(String input, String defaultOutput)
  {
    String output = "";
    try
    {
      input = input.trim();
      if ((input == null) || (input.trim().equals("")) || 
        (input.trim().length() == 0)) {
        output = defaultOutput;
      } else {
        output = input;
      }
    }
    catch (NullPointerException npe)
    {
      output = defaultOutput;
    }
    return output;
  }
  
  public static byte[] getByteArFromString2(String input)
  {
    return input.getBytes();
  }
  
  public static String getString(String input, String defaultOutput)
  {
    String output = " ";
    try
    {
      if ((input == null) || (input.trim().equals("")) || 
        (input.trim().length() == 0)) {
        output = defaultOutput;
      } else {
        output = input;
      }
    }
    catch (NullPointerException npe)
    {
      output = defaultOutput;
    }
    return output;
  }
  
  public static String getImageTypeHexToNormal(String imageType)
  {
    String type = "";
    type = imageType.replaceFirst("A", "R").replaceFirst("B", "L");
    return type;
  }
  
  public static int getNumber(String input, int defaultOutput, int startIndex, int endIndex)
  {
    int output = defaultOutput;
    try
    {
      if ((input == null) || (input.equals("")) || (input.length() == 0)) {
        output = defaultOutput;
      } else {
        output = Integer.parseInt(input.substring(startIndex, endIndex).trim());
      }
    }
    catch (NullPointerException npe)
    {
      output = defaultOutput;
    }
    catch (IndexOutOfBoundsException iobe)
    {
      output = Integer.parseInt(input);
    }
    catch (NumberFormatException nfe)
    {
      output = defaultOutput;
    }
    return output;
  }
  
  public static String getStringPadRight(String inputStr, String padStr, int outStrLen)
  {
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
}
