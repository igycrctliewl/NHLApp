package com.mikebro.nhl.format;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class DateTimeFormatTest {

	@Test
	public void testGetFormattedDate() {
		LocalDate testDate = LocalDate.parse( "2008-07-03" );
		assertEquals( "7/3/2008", DateTimeFormat.getFormattedDate( testDate ) ); 
	}

}
