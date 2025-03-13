package com.mikebro.nhl;

import static com.mikebro.nhl.format.DateTimeFormat.getFormattedDate;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mikebro.nhl.control.GameDayPane;
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
import javafx.scene.layout.StackPane;
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
	private static final Font NAV_LBL_FONT = new Font( "Verdana", 12.0 );
	private static final String BORDER_STYLE = "-fx-border-color: BLACK; -fx-border-width: 2";
	private static final double NAV_LBL_HEIGHT = 40.0;
	private static final double NAV_LBL_WIDTH = 160.0;

	private NHLService nhlService;

	private double sceneWidth = 575.0;
	private double sceneHeight = 500.0;
	private double rowHeight = 40.0;

	private Label goToPrevious;
	private Label goToToday;
	private Label goToNext;

	private SwitchButton showScores;
	private GameDayPane gameDayPane;

	private Insets bottomPad = new Insets(0, 0, 0, 0);
	private Insets bothSidesPad = new Insets(0, 25, 0, 25);

	@Override
	public void start( Stage primaryStage ) {
		nhlService = Launcher.getNHLService();
		Schedule todaySchedule = nhlService.getTodaySchedule();

		goToPrevious = new Label();
		goToPrevious.setText( "<-- " + getFormattedDate( todaySchedule.getPrevDate() ));
		goToPrevious.setFont( NAV_LBL_FONT );
		goToPrevious.setPrefHeight( NAV_LBL_HEIGHT );
		goToPrevious.setPrefWidth( NAV_LBL_WIDTH );
		goToPrevious.setPadding( bothSidesPad );
		goToPrevious.setOnMouseClicked( event -> navigateToPrevious() );

		goToToday = new Label();
		goToToday.setText( "Today" );
		goToToday.setFont( NAV_LBL_FONT );
		goToToday.setPrefHeight( NAV_LBL_HEIGHT );
		goToToday.setPadding( bothSidesPad );
		// goToToday label is hidden until it is needed
		goToToday.setVisible( false );
		goToToday.setOnMouseClicked( event -> navigateToToday() );

		goToNext = new Label();
		goToNext.setText( getFormattedDate( todaySchedule.getNextDate() ) + " -->" );
		goToNext.setFont( NAV_LBL_FONT );
		goToNext.setPrefHeight( NAV_LBL_HEIGHT );
		goToNext.setPrefWidth( NAV_LBL_WIDTH );
		goToNext.setPadding( bothSidesPad );
		goToNext.setOnMouseClicked( event -> navigateToNext() );

		Pane navigationPane = new HBox( goToPrevious, goToToday, goToNext );
		navigationPane.setLayoutX( 20.0 );
		navigationPane.setLayoutY( 20.0 );
		navigationPane.setPrefHeight( rowHeight );
		navigationPane.setPrefWidth( 415 );


		Pane togglePane = new Pane();
		//togglePane.setStyle( BORDER_STYLE );
		togglePane.setLayoutX( 425 );
		togglePane.setLayoutY( 20.0 - 2 );
		togglePane.setPrefHeight( rowHeight );
		togglePane.setPrefWidth( 135 );
		createShowScoresToggle( togglePane );

		Pane hbox1 = new HBox( navigationPane, togglePane );
		hbox1.setStyle( BORDER_STYLE );
		hbox1.setLayoutX( 10 );
		hbox1.setLayoutY( 10 );
		hbox1.setPadding( bottomPad );

		// TODO: the height and width of gameStatusPane should be derived
		// from the dimensions of gameDayPane
		Pane gameStatusPane = new Pane();
		gameStatusPane.setStyle( BORDER_STYLE );
		gameStatusPane.setPrefHeight( sceneHeight - 65 );
		gameStatusPane.setPrefWidth( sceneWidth - 20 );
		gameDayPane = new GameDayPane( todaySchedule, this );
		gameStatusPane.getChildren().add( gameDayPane );

		Pane vbox1 = new VBox( hbox1, gameStatusPane );
		vbox1.setLayoutX( 10 );
		vbox1.setLayoutY( 10 );
		vbox1.setPadding( bottomPad );

		Pane root = new Pane();
		root.getChildren().add( vbox1 );

		// TODO: the minimum height of the main scene should be the height
		// of gameDayPane + 65 or 500
		Scene scene = new Scene( root, sceneWidth, sceneHeight );
		primaryStage.setTitle( "NHLApp - " + getFormattedDate( todaySchedule.getCurrentDate() ) );
		primaryStage.setScene( scene );
		primaryStage.show();

	}


	public SwitchButton getShowScores() {
		return showScores;
	}

	private void createShowScoresToggle( Pane pane ) {
		Label showLabel = new Label();
		showLabel.setText( "Show Scores" );
		showLabel.setFont( new Font( "Verdana", 10.0 ) );
		showLabel.setPrefHeight( 20.0 );
		showLabel.setLayoutX( 10 );
		showLabel.setLayoutY( 10 );
		pane.getChildren().add( showLabel );

		showScores = new SwitchButton( show -> refresh( show ) );
		showScores.setLayoutX( 85 );
		showScores.setLayoutY( 12.5 );
		pane.getChildren().add( showScores );
	}


	public void refresh( boolean showScores ) {
		logger.info( "refresh with showScores " + ( showScores ? "true" : "false" ) );
		gameDayPane.refresh( showScores );
	}


	private void navigateToPrevious() {
		logger.info( "navigate to previous date" );
		goToToday.setVisible( true );
	}

	private void navigateToToday() {
		logger.info( "navigate to today's date" );
		goToToday.setVisible( false );
	}

	private void navigateToNext() {
		logger.info( "navigate to next date" );
		goToToday.setVisible( true );
	}
}
