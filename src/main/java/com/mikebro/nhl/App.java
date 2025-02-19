package com.mikebro.nhl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mikebro.nhl.control.GameStatus;
import com.mikebro.nhl.control.SwitchButton;
import com.mikebro.nhl.format.FormatHelper;
import com.mikebro.nhl.json.Game;
import com.mikebro.nhl.json.Schedule;
import com.mikebro.nhl.service.NHLService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.Timer;

/**
 *
 * @author mikebro
 */
public class App extends Application {

	private static final Log logger = LogFactory.getLog( App.class );

	private NHLService nhlService;

	private static double sceneWidth = 500.0;
	private static double sceneHeight = 500.0;
	private static double labelX = 20.0;
	private static double labelY = 20.0;
	private static double yIncrement = 30.0;

	private Map<Integer,GameStatus> gameStatusMap;

	@Override
	public void start( Stage primaryStage ) {
		nhlService = Launcher.getNHLService();

		Pane root = new Pane();

		Label showLabel = new Label();
		showLabel.setText( "Show Scores" );
		showLabel.setFont( new Font( "Verdana", 10.0 ) );
		showLabel.setPrefHeight( 20.0 );
		showLabel.setLayoutX( 375 );
		showLabel.setLayoutY( labelY - 2 );
		root.getChildren().add( showLabel );


		SwitchButton showScores = new SwitchButton( show -> refresh( show ) );
		showScores.setLayoutX( 450 );
		showScores.setLayoutY( labelY );
		labelY += yIncrement;
		root.getChildren().add( showScores );

		gameStatusMap = new HashMap<>();
		Schedule schedule = nhlService.getTodaySchedule();

		for( Game game : schedule.getGames() ) {
			GameStatus stat = new GameStatus( FormatHelper.buildGameString( game, showScores.getState() ) );
			gameStatusMap.put( game.getId(), stat );
			stat.setLayoutX( labelX );
			stat.setLayoutY( labelY );
			labelY += yIncrement;
			root.getChildren().add( stat );
		}

		Timer timer = new Timer( 15000, event -> refresh( showScores.getState() ) );
		timer.start();

		Scene scene = new Scene( root, sceneWidth, sceneHeight );
		primaryStage.setTitle( "NHLApp" );
		primaryStage.setScene( scene );
		primaryStage.show();
	}


	public void refresh( boolean showScores ) {
		logger.info( "refresh with showScores " + ( showScores ? "true" : "false" ) );
		Schedule schedule = nhlService.getTodaySchedule();
		for( Game game : schedule.getGames() ) {
			GameStatus stat = gameStatusMap.get( game.getId() );
			stat.setText( FormatHelper.buildGameString( game, showScores ) );
		}
	}

}
