package com.mikebro.nhl;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.mikebro.nhl.service.NHLService;

import javafx.application.Application;

/**
 *
 * @author mikebro
 */
public class Launcher {

	private static NHLService nhlService;
	public static NHLService getNHLService() {
		return Launcher.nhlService;
	}


	public static void main( String[] args ) {
		System.out.println( ">>>> Launcher.main()" );
		AbstractApplicationContext context = new AnnotationConfigApplicationContext( "com.mikebro.nhl.service.*" );

		nhlService = null;
		try {
			nhlService = (NHLService) context.getBean( NHLService.class );
		} catch ( Exception e ) {
			System.out.println( e.getMessage() );
		}

		Application.launch( App.class, args );

		context.close();
		System.out.println( "<<<< Launcher.main exit" );
	}
}
