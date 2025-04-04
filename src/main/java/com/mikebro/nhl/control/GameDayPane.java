package com.mikebro.nhl.control;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.swing.Timer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.mikebro.nhl.Launcher;
import com.mikebro.nhl.NHLApp;
import com.mikebro.nhl.format.GameStatusHelper;
import com.mikebro.nhl.json.Game;
import com.mikebro.nhl.json.Schedule;
import com.mikebro.nhl.service.NHLService;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class GameDayPane extends Pane {

	private static final Logger logger = LogManager.getLogger( GameDayPane.class );

	private NHLService nhlService;
	private Schedule schedule;
	private LocalDate gameDate;
//	private LocalDate prevDate;
//	private LocalDate nextDate;
	private Timer timer;
	private Map<Integer,GameStatus> gameStatusMap;
	private GameDayPane prevDayPane;
	private GameDayPane nextDayPane;

	private double sceneWidth = 575.0;
	private double sceneHeight = 400.0;
	private double labelX = 20.0;
	private double labelY = 5.0;
	private double yIncrement = 30.0;


	public double getSceneWidth() {
		return sceneWidth;
	}

	public double getSceneHeight() {
		return sceneHeight;
	}

	public GameDayPane getPrevDayPane() {
		return prevDayPane;
	}

	public void setPrevDayPane(GameDayPane prevDayPane) {
		this.prevDayPane = prevDayPane;
	}

	public GameDayPane getNextDayPane() {
		return nextDayPane;
	}

	public void setNextDayPane(GameDayPane nextDayPane) {
		this.nextDayPane = nextDayPane;
	}

	public LocalDate getGameDate() {
		return this.gameDate;
	}

	public LocalDate getPrevDate() {
		return ( schedule != null ? schedule.getPrevDate() : null );
	}

	public LocalDate getNextDate() {
		return ( schedule != null ? schedule.getNextDate() : null );
	}


	public GameDayPane( LocalDate requestedDate, NHLApp app ) {
		super();
		this.gameDate = requestedDate;
		nhlService = Launcher.getNHLService();
		schedule = nhlService.getSchedule( getGameDate() );
		gameStatusMap = new HashMap<>();

		if( schedule.getGames().size() == 0 ) {
			GameStatus stat = new GameStatus( "No games" );
			stat.setLayoutX( labelX );
			stat.setLayoutY( labelY );
			labelY += yIncrement;
			this.getChildren().add( stat );
		}

		for( Game game : schedule.getGames() ) {
			GameStatus stat = new GameStatus( GameStatusHelper.buildGameString( game, app.getShowScores().getState() ) );
			stat.setTooltip( GameStatusHelper.buildToolTip( game ) );
			gameStatusMap.put( game.getId(), stat );
			stat.setLayoutX( labelX );
			stat.setLayoutY( labelY );
			labelY += yIncrement;
			this.getChildren().add( stat );
		}

		timer = new Timer( 10000, event -> refreshSchedule() );
		timer.setInitialDelay( 1000 );
		wakeup();

		if( sceneHeight > (labelY + yIncrement) ) {
			// height is good
		} else {
			sceneHeight = labelY + yIncrement; 
		}
	}

	private void refreshSchedule() {
		logger.info( "refresh Schedule for {}", getGameDate() );
		CompletableFuture.runAsync( () -> schedule = nhlService.getSchedule( getGameDate() ) );
	}


	/**
	 * Using the currently stored Schedule details, redraw the gameday information for this pane.
	 * Call this method when navigating to prev/next game dates so that the next displayed information
	 * matches the current setting of the show-scores control.
	 * 
	 * @param showScores
	 */
	public void redrawPane( boolean showScores ) {
		Platform.runLater( () -> {
			hibernate();
			for( Game game : schedule.getGames() ) {
				GameStatus stat = gameStatusMap.get( game.getId() );
				stat.setText( GameStatusHelper.buildGameString( game, showScores ) );
			}
			wakeup();
		});
	}


	/**
	 * Stop the internal refresh timer for this GameDayPane
	 */
	public void hibernate() {
		timer.stop();
	}

	/**
	 * Restart the internal refresh timer for this GameDayPane.<br>
	 * Timer will only be allowed to start if this pane is for today's date, since otherwise the data will not be changing.
	 */
	public void wakeup() {
		if( LocalDate.now().equals( getGameDate() ) ) {
			timer.restart();
		}
	}
}
