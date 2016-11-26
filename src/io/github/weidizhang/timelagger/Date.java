package io.github.weidizhang.timelagger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date {

	public static void setDate(String date) {
		String cmd = "date -s \"$(date +'" + date + " %H:%M')\"";
		if (Util.isWindows()) {
			cmd = "cmd /C date " + date;
		}
		
		try {
			Runtime.getRuntime().exec(cmd).waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getDateFormat() {
		String dateFormat = "yyyyMMdd";
		
		if (Util.isWindows()) {
			try {
				Process dateProc = Runtime.getRuntime().exec("reg query \"HKEY_CURRENT_USER\\Control Panel\\International\" /v sShortDate");
				BufferedReader buffReader = new BufferedReader(new InputStreamReader(dateProc.getInputStream()));
				
				String recvLine = null;
				while ((recvLine = buffReader.readLine()) != null) {
					recvLine = recvLine.trim();
					if (recvLine.startsWith("sShortDate")) {
						recvLine = recvLine.replace(" ", "");
						
						String winDateFormat = recvLine.substring(recvLine.indexOf("SZ") + 2);
						
						switch (winDateFormat) {
							case "yy/MM/dd":
								dateFormat = "yy-MM-dd";
								break;
								
							case "dd-MM-yy":
								dateFormat = "dd-MM-yy";
								break;
						
							default:
								dateFormat = "MM-dd-yy";
								break;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return dateFormat;
	}
	
	public static void correctDate(Calendar calendar, int delay) {
		// Doing it this way instead of storing old date because that would not work if actual date changes.
		// (e.g. it is 11:59pm when this is ran and/or very long delay)
		
		calendar.add(Calendar.SECOND, delay);
		
		String dateFormat = getDateFormat();
		SimpleDateFormat sdFormat = new SimpleDateFormat(dateFormat);
		Date.setDate(sdFormat.format(calendar.getTime()));
	}
}
