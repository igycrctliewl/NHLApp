package com.mikebro.nhl;

import static com.mikebro.nhl.format.DateTimeFormat.getFormattedDate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mikebro.nhl.control.GameDayPane;
import com.mikebro.nhl.control.SwitchButton;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author mikebro
 */
public class NHLApp extends Application {

	private static final Log logger = LogFactory.getLog( NHLApp.class );
	private static final Font NAV_LBL_FONT = new Font( "Verdana", 12.0 );
	private static final String BORDER_STYLE = "-fx-border-color: BLACK; -fx-border-width: 0";
	private static final double NAV_LBL_HEIGHT = 40.0;
	private static final double NAV_LBL_WIDTH = 160.0;

	private double sceneWidth = 575.0;
	private double sceneHeight = 500.0;
	private double rowHeight = 40.0;

	private Label goToPrevious;
	private Label goToToday;
	private Label goToNext;

	private SwitchButton showScores;
	private Pane gameStatusPane;
	private GameDayPane gameDayPane;
	private Map<LocalDate,GameDayPane> gameDayPaneMap;
	private Stage mainApplicationStage;

	private Insets bottomPad = new Insets(0, 0, 0, 0);
	private Insets bothSidesPad = new Insets(0, 25, 0, 25);

	@Override
	public void start( Stage primaryStage ) {
		mainApplicationStage = primaryStage;

		/* When we call GameDayPane constructor, we pass this object.
		 * This object is used to obtain the current setting of the show-scores toggle.
		 * Therefore, show-scores toggle must be created before we call the constructor.
		 */
		Pane togglePane = new Pane();
		//togglePane.setStyle( BORDER_STYLE );
		togglePane.setLayoutX( 425 );
		togglePane.setLayoutY( 20.0 - 2 );
		togglePane.setPrefHeight( rowHeight );
		togglePane.setPrefWidth( 135 );
		createShowScoresToggle( togglePane );

		gameDayPane = new GameDayPane( LocalDate.now(), this );
		final GameDayPane pane = gameDayPane;
		CompletableFuture.runAsync( () -> preloadPrevNextDay( pane ) );

		gameDayPaneMap = Collections.synchronizedMap( new HashMap<>() );
		gameDayPaneMap.put( gameDayPane.getGameDate(), gameDayPane );

		goToPrevious = new Label();
		goToPrevious.setText( "<-- " + getFormattedDate( gameDayPane.getPrevDate() ));
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
		goToToday.setVisible( false );
		goToToday.setOnMouseClicked( event -> navigateToToday() );

		goToNext = new Label();
		goToNext.setText( getFormattedDate( gameDayPane.getNextDate() ) + " -->" );
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


		Pane hbox1 = new HBox( navigationPane, togglePane );
		hbox1.setStyle( BORDER_STYLE );
		hbox1.setLayoutX( 10 );
		hbox1.setLayoutY( 10 );
		hbox1.setPadding( bottomPad );

		// TODO: the height and width of gameStatusPane should be derived
		// from the dimensions of gameDayPane
		gameStatusPane = new Pane();
		gameStatusPane.setStyle( BORDER_STYLE );
		gameStatusPane.setPrefHeight( sceneHeight - 65 );
		gameStatusPane.setPrefWidth( sceneWidth - 20 );
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
		setAppTitle();
		mainApplicationStage.setScene( scene );
		mainApplicationStage.show();
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
		showLabel.setStyle( BORDER_STYLE );
		pane.getChildren().add( showLabel );

		showScores = new SwitchButton( show -> refresh( show ) );
		showScores.setLayoutX( 85 );
		showScores.setLayoutY( 12.5 );
		showScores.setStyle( BORDER_STYLE );
		pane.getChildren().add( showScores );

		// Link "show scores" label with the toggle switch
		showLabel.setOnMouseClicked( showScores.getOnMouseClicked() );
	}


	public void refresh( boolean showScores ) {
		logger.info( "refresh with showScores " + ( showScores ? "true" : "false" ) );
		gameDayPane.refresh( showScores );
	}


	private void refreshForNavigation( LocalDate newGameDate ) {
		logger.info( "navigate to " + newGameDate );

		// attempt to get GameDayPane from map
		// or create a GameDayPane from the previous date
		GameDayPane navigatingToPane = gameDayPaneMap.get( newGameDate );
		if( navigatingToPane == null ) {
			navigatingToPane = new GameDayPane( newGameDate, this );
			gameDayPaneMap.put( newGameDate, navigatingToPane );
		}
		final GameDayPane pane = navigatingToPane;
		CompletableFuture.runAsync( () -> preloadPrevNextDay( pane ) );

		// hibernate the current GameDayPane and activate the new GameDayPane
		gameDayPane.hibernate();
		navigatingToPane.wakeup();

		// update prev/next label text
		goToPrevious.setText( "<-- " + getFormattedDate( navigatingToPane.getPrevDate() ));
		goToToday.setVisible( ! navigatingToPane.getGameDate().equals( LocalDate.now() ) );
		goToNext.setText( getFormattedDate( navigatingToPane.getNextDate() ) + " -->" );

		// display new GameDayPane
		gameStatusPane.getChildren().removeAll( gameDayPane );
		gameDayPane = navigatingToPane;
		setAppTitle();
		gameStatusPane.getChildren().add( gameDayPane );
	}

	private void navigateToPrevious() {
		logger.info( "navigate to previous date" );
		refreshForNavigation( gameDayPane.getPrevDate() );
	}

	private void navigateToToday() {
		logger.info( "navigate to today's date" );
		refreshForNavigation( LocalDate.now() );
	}

	private void navigateToNext() {
		logger.info( "navigate to next date" );
		refreshForNavigation( gameDayPane.getNextDate() );
	}


	private void preloadPrevNextDay( GameDayPane gameDay ) {
		CompletableFuture<Void> prev = CompletableFuture.runAsync( () -> preloadGameDate( gameDay.getPrevDate() ) );
		CompletableFuture<Void> next = CompletableFuture.runAsync( () -> preloadGameDate( gameDay.getNextDate() ) );
		CompletableFuture.allOf( prev, next );
	}

	private void preloadGameDate( LocalDate gameDate ) {
		logger.info( String.format( "preloading %s", gameDate ));
		if( gameDayPaneMap.get( gameDate ) == null ) {
			GameDayPane pane = new GameDayPane( gameDate, this );
			pane.hibernate();
			gameDayPaneMap.put( gameDate, pane );
		}
	}


	private void setAppTitle() {
		mainApplicationStage.setTitle( "NHLApp - " + getFormattedDate( gameDayPane.getGameDate() ) );
	}
}
