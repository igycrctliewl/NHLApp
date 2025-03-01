package com.mikebro.nhl.format;

import java.time.format.DateTimeFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mikebro.nhl.Launcher;
import com.mikebro.nhl.json.Game;
import com.mikebro.nhl.json.PeriodDescriptor;
import com.mikebro.nhl.service.TeamNamesService;

public class FormatHelper {

	private static final Log logger = LogFactory.getLog( FormatHelper.class );
	private static final DateTimeFormatter TIME_DISPLAY = DateTimeFormatter.ofPattern( "h:mma" );
	private static final TeamNamesService teamNamesService = Launcher.getTeamNamesService();

	/**
	 * For a given Game object, build a string summarizing the
	 * vital information for the game.  The short ID is a must.
	 * It would be nice to be able to toggle scores on/off
	 */
	public static String buildGameString( Game game, boolean showScores ) {
		StringBuilder builder = new StringBuilder();
		builder.append( game.getId().toString()
				.substring( game.getId().toString().length() - 4 ));
		builder.append( " - " );

		if( showScores ) {
			builder.append( teamNamesService.getPlaceName( game.getAwayTeam().getAbbrev() ) );
			builder.append( currentGameScore( game ) );
			builder.append( teamNamesService.getPlaceName( game.getHomeTeam().getAbbrev() ) );
			builder.append( " - " );
			builder.append( currentGameState( game, showScores ) );
		} else {
			builder.append( teamNamesService.getPlaceName( game.getAwayTeam().getAbbrev() ) );
			builder.append( " vs " );
			builder.append( teamNamesService.getPlaceName( game.getHomeTeam().getAbbrev() ) );
			builder.append( " - " );
			builder.append( currentGameState( game, showScores ) );
		}

		return builder.toString();
	}


/*
 * gameScheduleState = OK
 *     gameState = FUT
 *     gameState = PRE
 *     gameState = LIVE
 *         periodType = REG
 *     gameState = OFF
 * gameScheduleState = PPD
 *     gameState = FUT
 */

	private static String currentGameState( Game game, boolean showScores ) {
		String stateOfPlay;
		switch( game.getGameScheduleState() ) {
		case "OK":
			stateOfPlay = gameScheduleStateOK( game, showScores );
			break;
		case "PPD":
			stateOfPlay = "PPD";
			break;
		default:
			// log the unknown scheduleState value
			logger.error( String.format( "Unknown gameScheduleState value >%s< for game %s", game.getGameScheduleState(), game.getId() ) );
			stateOfPlay = "?";
			break;
		}
		return stateOfPlay;
	}

	private static String gameScheduleStateOK( Game game, boolean showScores ) {
		String stateOfPlay;
		switch( game.getGameState() ) {
		case "FUT":
			stateOfPlay = TIME_DISPLAY.format( game.getStartTime() );
			break;
		case "PRE":
			stateOfPlay = "Pre-Game";
			break;
		case "LIVE":
		case "CRIT":
			stateOfPlay = gamePeriodState( game, showScores );
			break;
		case "OFF":
		case "FINAL":
			stateOfPlay = "Final";
			// append OT or SO tag for games in extra-time
			if( game.getPeriodDescriptor().getNumber() > 3 && showScores ) {
				stateOfPlay = stateOfPlay.concat( " (" + game.getPeriodDescriptor().getPeriodType() + ")" );
			}
			break;
		default:
			// log the unknown gameState value
			logger.error( String.format( "Unknown gameState value >%s< for game %s", game.getGameState(), game.getId() ) );
			stateOfPlay = "?";
			break;
		}
		return stateOfPlay;
	}

	private static String gamePeriodState( Game game, boolean showScores ) {
		String stateOfPlay;
		PeriodDescriptor desc = game.getPeriodDescriptor();
		switch( desc.getPeriodType() ) {
		case "REG":
		case "OT":
			if( showScores ) {
				stateOfPlay = "Period " + desc.getNumber().toString();
				if( game.getClock().isInIntermission() ) {
					stateOfPlay = stateOfPlay + " (Intermission)";
				} else {
					stateOfPlay = stateOfPlay + " (" + game.getClock().getTimeRemaining() + ")";
				}
			} else {
				stateOfPlay = "In-progress";
			}
			break;
		default:
			// log the unknown periodType value
			logger.error( String.format( "Unknown periodType value >%s< for game %s", desc.getPeriodType(), game.getId() ) );
			stateOfPlay = "?";
			break;
		}
		return stateOfPlay;
	}

	private static String currentGameScore( Game game ) {
		String scoreString;
		switch( game.getGameScheduleState() ) {
		case "OK":
			switch( game.getGameState() ) {
			case "LIVE":
			case "CRIT":
			case "OFF":
			case "FINAL":
				StringBuilder builder = new StringBuilder();
				builder.append( " " );
				builder.append( game.getAwayTeam().getScore().toString() );
				builder.append( " - " );
				builder.append( game.getHomeTeam().getScore().toString() );
				builder.append( " " );
				scoreString = builder.toString();
				break;
			case "FUT":
			case "PRE":
			default:
				scoreString = " vs ";
				break;
			}
			break;
		case "PPD":
		default:
			scoreString = " vs ";
			break;
		}
		return scoreString;
	}

}
