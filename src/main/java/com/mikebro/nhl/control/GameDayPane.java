package com.mikebro.nhl.control;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mikebro.nhl.Launcher;
import com.mikebro.nhl.NHLApp;
import com.mikebro.nhl.format.GameStatusHelper;
import com.mikebro.nhl.json.Game;
import com.mikebro.nhl.json.Schedule;
import com.mikebro.nhl.service.NHLService;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class GameDayPane extends Pane {

	private static final Log logger = LogFactory.getLog( GameDayPane.class );

	private NHLService nhlService;
	private LocalDate currentDate;
	private Timer timer;
	private Map<Integer,GameStatus> gameStatusMap;

	private double sceneWidth = 575.0;
	private double sceneHeight = 400.0;
	private double labelX = 20.0;
	private double labelY = 20.0;
	private double yIncrement = 30.0;


	public GameDayPane( Schedule schedule, NHLApp app ) {
		super();
		nhlService = Launcher.getNHLService();
		currentDate = schedule.getCurrentDate();
		gameStatusMap = new HashMap<>();
	
		for( Game game : schedule.getGames() ) {
			GameStatus stat = new GameStatus( GameStatusHelper.buildGameString( game, app.getShowScores().getState() ) );
			stat.setTooltip( GameStatusHelper.buildToolTip( game ) );
			gameStatusMap.put( game.getId(), stat );
			stat.setLayoutX( labelX );
			stat.setLayoutY( labelY );
			labelY += yIncrement;
			this.getChildren().add( stat );
		}

		timer = new Timer( 15000, event -> callRefresh( app.getShowScores().getState() ) );
		timer.start();

		if( sceneHeight > (labelY + yIncrement) ) {
			// height is good
		} else {
			sceneHeight = labelY + yIncrement; 
		}
	}

	public double getSceneWidth() {
		return sceneWidth;
	}

	public double getSceneHeight() {
		return sceneHeight;
	}

	public void callRefresh( boolean showScores ) {
		Platform.runLater( () -> refresh( showScores ) );
	}

	public void refresh( boolean showScores ) {
		logger.info( "refresh with showScores " + ( showScores ? "true" : "false" ) );
		Schedule schedule = nhlService.getSchedule( currentDate );
		for( Game game : schedule.getGames() ) {
			GameStatus stat = gameStatusMap.get( game.getId() );
			stat.setText( GameStatusHelper.buildGameString( game, showScores ) );
		}
	}


	/**
	 * Stop the internal refresh timer for this GameDayPane
	 */
	public void hibernate() {
		timer.stop();
	}

	/**
	 * Restart the internal refresh timer for this GameDayPane
	 */
	public void wakeup() {
		timer.restart();
	}
}
