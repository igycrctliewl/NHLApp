package com.mikebro.nhl;

import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author mikebro
 */
public class Version {

	/**
	 * When invoked, simply replies with the current version, as described in pom.xml
	 */
	public static void main( String[] args ) {
		try {
			final Properties properties = new Properties();
			properties.load( Version.class.getClassLoader().getResourceAsStream("app.properties") );
			System.out.printf( "NHLApp version: %s%n", properties.getProperty("app.version"));
		} catch( IOException e ) {
			System.err.println( "Error reporting version" );
		}
	}
}
