package cn.eid.sample.idspsdk.common;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;



public class CommonUse {
	
	public static long FAILED_COUNT = 0;
	
	//ÈÝÆ÷ÁÐ±í
	public static final String CONTAINER_USER_RSA1024 	= "{4A7A26B1-ABA5-48ef-8B6A-24A4BA42E787}";     //¸öÈËRSA1024ÃÜÔ¿ÈÝÆ÷
	public static final String CONTAINER_CARRIER 		= "{9BBA36A4-4E5D-44e3-8833-CC4DBFEA0886}";    //ÔØÌåÃÜÔ¿ÈÝÆ÷
	public static final String CONTAINER_MIC2_RSA2048 	= "{CBB503C3-E0C6-4ae9-95C9-2E7D39BEBCF4}";     //MIC2 RSA2048ÃÜÔ¿ÈÝÆ÷
	public static final String CONTAINER_INTERNEL 		= "{229992D8-407B-4d75-AED7-0F4D34AE5F88}";     //ÄÚ²¿ÃÜÔ¿ÈÝÆ÷
	public static final String CONTAINER_USER_SM2 		= "{A677D82B-35F5-4094-BC41-883EC0F67D79}";    	//¸öÈËSM2Ö¤Êé
	public static final String CONTAINER_MIC3_RSA2048 	= "{9C0DEC8F-B3FD-4f7c-8081-D1736A78BE6A}"; 	//MIC3 RSA2048ÃÜÔ¿ÈÝÆ÷
	public static final String CONTAINER_MIC3_SM 		= "{DA8F8CD9-02C6-4912-9A49-78C21F687ABF}";     //MIC3 SM2ÃÜÔ¿ÈÝÆ÷
	
	public enum AsymmAlg {
		
		RSA1024		(0x0001),
		RSA1280		(0x0002),
		RSA2048		(0x0004),
		SM2			(0x0008);
		
		private final int value;
		AsymmAlg(int value) {
            this.value = value;
        }
		public int getValue() {
			return value;
		}		
	}
	
	public enum SymmAlg {
		
		SM1			(0x0001),
		SSF33		(0x0002),
		DES			(0x0004),
		TDES_2KEY	(0x0008),
		TDES_3KEY	(0x0010);
		
		private final int value;
		SymmAlg(int value) {
            this.value = value;
        }
		public int getValue() {
			return value;
		}		
	}
	
	public enum HashAlg {
		
		SM3			(0x0001),
		SHA11		(0x0002),
		SHA256		(0x0004);
		
		private final int value;
		HashAlg(int value) {
            this.value = value;
        }
		public int getValue() {
			return value;
		}		
	}
	
	public static void ExpectEqual(SpannableStringBuilder tv, int expected, int actual,
			String tag) {

		if (actual == expected) {
			
			tv.append("º¯Êý£¨" + tag + "£© ----- [ PASSED ]\n");
			
			
		} else {
			
			int start = tv.length();
			tv.append("º¯Êý£¨" + tag + "£© ----- [ FAILED ");
			tv.append("´íÎóÂë£º£¨" + String.format("0x%x", actual) + "£©]\n");
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
			int end = tv.length();
			tv.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			FAILED_COUNT++;
			
		}
	
	}

	
	public static void ExpectEqual(SpannableStringBuilder tv, long expected, long actual,
			String tag) {

		if (actual == expected) {
			
			tv.append("º¯Êý£¨" + tag + "£© ----- [ PASSED ]\n");
			
		} else {
			
			int start = tv.length();
			tv.append("º¯Êý£¨" + tag + "£© ----- [ FAILED ");
			tv.append("´íÎóÂë£º£¨" + String.format("0x%x", actual) + "£© ]\n");
			int end = tv.length();
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
			tv.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			FAILED_COUNT++;
			
		}
		
	}

	public static void ExpectEqual(SpannableStringBuilder tv, boolean expected,
			boolean actual, String tag) {

		if (actual == expected) {
			
			tv.append("º¯Êý£¨" + tag + "£© ----- [ PASSED ]\n");
			
		} else {
			
			int start = tv.length();
			tv.append("º¯Êý£¨" + tag + "£© ----- [ FAILED ]\n");
			int end = tv.length();
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
			tv.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			FAILED_COUNT++;
			
		}
	
	}
	

	public static void NotExpEqual(SpannableStringBuilder tv, int expected, int actual,
			String tag) {

		if (actual == expected) {
			
			int start = tv.length();
			tv.append("º¯Êý£¨" + tag + "£© ----- [ FAILED ]\n");
			int end = tv.length();
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
			tv.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			FAILED_COUNT++;

		} else {	
			
			tv.append("º¯Êý£¨" + tag + "£© ----- [ PASSED ]\n");

		}
		
	}

	public static void NotExpEqual(SpannableStringBuilder tv, long expected, long actual,
			String tag) {

		if (actual == expected) {
			
			int start = tv.length();
			tv.append(String.format("º¯Êý£¨" + tag + "£© ----- [ FAILED  ret:0x%x ]\n", actual));
			int end = tv.length();
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
			tv.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			FAILED_COUNT++;

		} else {
			
			tv.setSpan(null, 0, 0, 0);
			tv.append("º¯Êý£¨" + tag + "£© ----- [ PASSED ]\n");
	
		}
		
	}

	public static void NotExpEqual(SpannableStringBuilder tv, boolean expected,
			boolean actual, String tag) {

		if (actual == expected) {
			
			int start = tv.length();
			tv.append("º¯Êý£¨" + tag + "£© ----- [ FAILED ]\n");
			int end = tv.length();
			ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED); 
			tv.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			FAILED_COUNT++;

		} else {
			
			tv.append("º¯Êý£¨" + tag + "£© ----- [ PASSED ]\n");
			
		}
		
	}
	
	
	
}
