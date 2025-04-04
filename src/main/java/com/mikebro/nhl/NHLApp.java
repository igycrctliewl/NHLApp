package com.mikebro.nhl;

import static com.mikebro.nhl.format.DateTimeFormat.getFormattedDate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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

	private static final Logger logger = LogManager.getLogger( NHLApp.class );
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
	private Scene scene;

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
		goToPrevious.setStyle( BORDER_STYLE );
		setPrevDateText( gameDayPane.getPrevDate() );
		goToPrevious.setFont( NAV_LBL_FONT );
		goToPrevious.setPrefHeight( NAV_LBL_HEIGHT );
		goToPrevious.setPrefWidth( NAV_LBL_WIDTH );
		goToPrevious.setPadding( bothSidesPad );
		goToPrevious.setOnMouseClicked( event -> navigateToPrevious() );

		goToToday = new Label();
		goToToday.setStyle( BORDER_STYLE );
		goToToday.setText( "Today" );
		goToToday.setFont( NAV_LBL_FONT );
		goToToday.setPrefHeight( NAV_LBL_HEIGHT );
		goToToday.setPadding( bothSidesPad );
		goToToday.setVisible( false );
		goToToday.setOnMouseClicked( event -> navigateToToday() );

		goToNext = new Label();
		goToNext.setStyle( BORDER_STYLE );
		setNextDateText( gameDayPane.getNextDate() );
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

		gameStatusPane = new Pane();
		gameStatusPane.setStyle( BORDER_STYLE );
		gameStatusPane.setPrefHeight( sceneHeight );
		gameStatusPane.setPrefWidth( sceneWidth - 20 );
		gameStatusPane.getChildren().add( gameDayPane );

		Pane vbox1 = new VBox( hbox1, gameStatusPane );
		vbox1.setLayoutX( 10 );
		vbox1.setLayoutY( 10 );
		vbox1.setPadding( bottomPad );

		Pane root = new Pane();
		root.getChildren().add( vbox1 );

		scene = new Scene( root, sceneWidth, sceneHeight );
		setAppTitle();
		mainApplicationStage.setScene( scene );
		setSizes();
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
		gameDayPane.redrawPane( showScores );
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
		setPrevDateText( navigatingToPane.getPrevDate() );
		goToToday.setVisible( ! navigatingToPane.getGameDate().equals( LocalDate.now() ) );
		setNextDateText( navigatingToPane.getNextDate() );

		// display new GameDayPane
		gameStatusPane.getChildren().removeAll( gameDayPane );
		gameDayPane = navigatingToPane;
		setAppTitle();
		setSizes();
		gameDayPane.redrawPane( showScores.getState() );
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

	private void setPrevDateText( LocalDate prevDate ) {
		goToPrevious.setVisible( prevDate != null );
		goToPrevious.setText( "<-- " + getFormattedDate( prevDate ));
	}

	private void setNextDateText( LocalDate nextDate ) {
		goToNext.setVisible( nextDate != null );
		goToNext.setText( getFormattedDate( nextDate ) + " -->" );
	}

	private void preloadPrevNextDay( GameDayPane gameDay ) {
		CompletableFuture.runAsync( () -> preloadGameDate( gameDay.getPrevDate() ) );
		CompletableFuture.runAsync( () -> preloadGameDate( gameDay.getNextDate() ) );
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


	private void setSizes() {
		// expand the container controls and the main app if necessary for busy NHL schedules
		double statusPaneHeight = Math.max( 330, 330 + gameDayPane.getSceneHeight() - 400 );
		gameStatusPane.setPrefHeight( statusPaneHeight );
		double stageHeight = Math.max( 510, 510 + gameDayPane.getSceneHeight() - 400 );
		mainApplicationStage.setHeight( stageHeight );
	}
}
