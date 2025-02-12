package com.mikebro.nhl;

import com.mikebro.nhl.control.CustomControl;
import com.mikebro.nhl.control.SwitchButton;
import com.mikebro.nhl.format.FormatHelper;
import com.mikebro.nhl.json.Game;
import com.mikebro.nhl.json.Schedule;
import com.mikebro.nhl.service.NHLService;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author mikebro
 */
public class App extends Application {

	private NHLService nhlService;

	private static double sceneWidth = 500.0;
	private static double sceneHeight = 500.0;
	private static double labelX = 20.0;
	private static double labelY = 20.0;
	private static double yIncrement = 30.0;


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


		SwitchButton showScores = new SwitchButton();
		showScores.setLayoutX( 450 );
		showScores.setLayoutY( labelY );
		labelY += yIncrement;
		root.getChildren().add( showScores );

		CustomControl cc = new CustomControl( "434 - Minnesota 1 - 4 Los Angeles - Period 3 (05:13)" );
		cc.setLayoutX( labelX );
		cc.setLayoutY( labelY );
		labelY += yIncrement;
		root.getChildren().add( cc );

		CustomControl cc2 = new CustomControl( "my second" );
		cc2.setLayoutX( labelX );
		cc2.setLayoutY( labelY );
		labelY += yIncrement;
		root.getChildren().add( cc2 );

		Schedule schedule = nhlService.getTodaySchedule();
		cc2.setText( String.format( "found %s games", schedule.getGames().size() ) );

		for( Game g : schedule.getGames() ) {
			System.out.println( FormatHelper.buildGameString( g, showScores.getState() ) );
		}

		Scene scene = new Scene( root, sceneWidth, sceneHeight );

		primaryStage.setTitle( "NHLApp" );
		primaryStage.setScene( scene );
		primaryStage.show();
	}
}
