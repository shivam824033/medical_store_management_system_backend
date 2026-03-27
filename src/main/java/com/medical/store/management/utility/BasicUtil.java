package com.medical.store.management.utility;

import java.time.LocalDateTime;
import java.util.Date;

public class BasicUtil {
	
	public static final String getResultString(Object string) {	
		if(null!=string) {
			String res = (String) string;
			return res;
		}		
		return null;
	}
	
	public static final int getResultInteger(Object string) {	
		if(null!=string) {
			int res = Integer.parseInt(string.toString());
			return res;
		}		
		return 0;
	}
	
	public static final long getResultLong(Object string) {	
		if(null!=string) {
			long res = Long.parseLong(string.toString());
			return res;
		}		
		return 0;
	}
	
	public static final Double getResultDouble(Object string) {	
		if(null!=string) {
			Double res = Double.parseDouble(string.toString());
			return res;
		}		
		return 0.0;
	}
	
	public static final Date getResultDate(Object string) {	
		if(null!=string) {	
			Date res = java.sql.Timestamp.valueOf(string.toString());
			return res;
		}		
		return null;
	}


}
