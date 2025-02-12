package com.mikebro.nhl.format;

import com.mikebro.nhl.json.Game;

public class FormatHelper {

	/**
	 * For a given Game object, build a string summarizing the
	 * vital information for the game.  The short ID is a must.
	 * It would be nice to be able to toggle scores on/off
	 */
	public static String buildGameString( Game game, boolean showScores ) {
		StringBuilder builder = new StringBuilder();
		builder.append( game.getId().toString()
				.substring( game.getId().toString().length() - 4 ));
		return builder.toString();
	}
}
