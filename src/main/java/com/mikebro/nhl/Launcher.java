package com.mikebro.nhl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.mikebro.nhl.service.NHLService;
import com.mikebro.nhl.service.TeamNamesService;

import javafx.application.Application;

/**
 *
 * @author mikebro
 */
public class Launcher {

	private static final Log logger = LogFactory.getLog( Launcher.class );

	private static NHLService nhlService;
	public static NHLService getNHLService() {
		return Launcher.nhlService;
	}

	private static TeamNamesService teamNamesService;
	public static TeamNamesService getTeamNamesService() {
		return Launcher.teamNamesService;
	}


	public static void main( String[] args ) {
		logger.info( "Launcher.main()" );

		AbstractApplicationContext context = new AnnotationConfigApplicationContext( "com.mikebro.nhl.service.*" );

		nhlService = null;
		try {
			nhlService = (NHLService) context.getBean( NHLService.class );
		} catch ( Exception e ) {
			logger.error( "Unable to find bean for NHLService" );
			logger.error( e.getMessage(), e );
		}

		teamNamesService = null;
		try {
			teamNamesService = (TeamNamesService) context.getBean( TeamNamesService.class );
		} catch ( Exception e ) {
			logger.error( "Unable to find bean for TeamNamesService" );
			logger.error( e.getMessage(), e );
		}

		Application.launch( NHLApp.class, args );

		context.close();
		logger.info( "Launcher.main exit" );
	}
}
