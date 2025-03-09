package com.mikebro.nhl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mikebro.nhl.control.GameStatus;
import com.mikebro.nhl.control.SwitchButton;
import com.mikebro.nhl.format.GameStatusHelper;
import com.mikebro.nhl.json.Game;
import com.mikebro.nhl.json.Schedule;
import com.mikebro.nhl.service.NHLService;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.swing.Timer;

/**
 *
 * @author mikebro
 */
public class NHLApp extends Application {

	private static final Log logger = LogFactory.getLog( NHLApp.class );

	private static final String BORDER_STYLE = "-fx-border-color: BLACK; -fx-border-width: 2";

	private NHLService nhlService;

	private static double sceneWidth = 575.0;
	private static double sceneHeight = 500.0;
	private static double labelX = 20.0;
	private static double labelY = 20.0;
	private static double rowHeight = 40.0;
	private static double yIncrement = 30.0;

	private Map<Integer,GameStatus> gameStatusMap;

	private Insets bottomPad = new Insets(0, 0, 0, 0);

	@Override
	public void start( Stage primaryStage ) {

		Pane navigationPane = new Pane();
		navigationPane.setStyle( BORDER_STYLE );
		navigationPane.setLayoutX( labelX );
		navigationPane.setLayoutY( labelY );
		navigationPane.setPrefHeight( rowHeight );
		navigationPane.setPrefWidth( 415 );

		Pane togglePane = new Pane();
		togglePane.setStyle( BORDER_STYLE );
		togglePane.setLayoutX( 425 );
		togglePane.setLayoutY( labelY - 2 );
		togglePane.setPrefHeight( rowHeight );
		togglePane.setPrefWidth( 135 );

		Pane hbox1 = new HBox( navigationPane, togglePane );
		hbox1.setStyle( BORDER_STYLE );
		hbox1.setLayoutX( 10 );
		hbox1.setLayoutY( 10 );
		hbox1.setPadding( bottomPad );

		Pane gameStatusPane = new Pane();
		gameStatusPane.setStyle( BORDER_STYLE );
		gameStatusPane.setPrefHeight( sceneHeight - 65 );
		gameStatusPane.setPrefWidth( sceneWidth - 20 );

		Pane vbox1 = new VBox( hbox1, gameStatusPane );
		vbox1.setLayoutX( 10 );
		vbox1.setLayoutY( 10 );
		vbox1.setPadding( bottomPad );

		Pane root = new Pane();
		root.getChildren().add( vbox1 );


		Scene scene = new Scene( root, sceneWidth, sceneHeight );
		primaryStage.setTitle( "NHLApp" );
		primaryStage.setScene( scene );
		primaryStage.show();

	}


}
