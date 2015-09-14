package com.bonioctavianus.parsepushdemo.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateToDay {
	
	// helper method untuk mengambil waktu saat ini
	public static String setDateNotif(Calendar cal) {
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.applyPattern("EEEE, d MMM yyyy - hh:mm:ss");
		String strDate = sdf.format(date);
		return strDate;
	}
}