package com.mikebro.nhl.format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeFormat {

	private static final DateTimeFormatter MDY = DateTimeFormatter.ofPattern( "M/d/yyyy" );

	public static String getFormattedDate( LocalDate date ) {
		return date.format( MDY );
	}
}
