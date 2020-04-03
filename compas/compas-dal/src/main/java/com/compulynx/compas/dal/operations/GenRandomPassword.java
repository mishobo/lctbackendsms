package com.compulynx.compas.dal.operations;

import java.security.SecureRandom;
import java.util.Random;

public class GenRandomPassword {
	static char[] SPECIAL_CHARS = (new String("^$*.[]{}()?-\"!@#%&/\\,><':;|_~`")).toCharArray();
    static char[] LOWER = (new String("abcdefghijklmnopqrstuvwxyz")).toCharArray();
    static char[] UPPER = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).toCharArray();
    static char[] DIGITS = (new String("0123456789")).toCharArray();
    static char[] ALL_CHARS = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`")).toCharArray();
	public static String generatePassword(int length)
	{
		String pwd = "";
		Random rand = new SecureRandom();
   
        assert length >= 4;
        char[] password = new char[length];

        //ensure password has lower, upper, digit and special characters
        password[0] = LOWER[rand.nextInt(LOWER.length)];
        password[1] = UPPER[rand.nextInt(UPPER.length)];
        password[2] = DIGITS[rand.nextInt(DIGITS.length)];
        password[3] = SPECIAL_CHARS[rand.nextInt(SPECIAL_CHARS.length)];

        //fill rest of pwd with random characters
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        //shuffling
        for (int i = 0; i < password.length; i++) 
        {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }

        pwd = new String(password);
	        
        return pwd;
	}
}
