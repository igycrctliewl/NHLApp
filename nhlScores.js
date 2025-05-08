function writeln( dataline ) {
	document.write( dataline + "<br>" );
}


var showScoresOn = true;
function isShowScoresOn() {
	return showScoresOn;
}

function toggleShowScores() {
	var showScoresFields = document.getElementsByName("showScores");
	showScoresOn = showScoresFields[0].checked;
	rebuildGameList();
}

function padNumber( number ) {
	var numberString = number.toString();
	if( numberString.length == 1 ) {
		numberString = "0" + numberString;
	}
	return numberString;
}


function Get(yourUrl){
    var Httpreq = new XMLHttpRequest();
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}


function eachGame( game, index ) {
	var season = game.gamePk.toString().substring( 0, 4 );
	var seasonPart = game.gamePk.toString().substring( 4, 6 );
	var gameNumber = game.gamePk.toString().substring( 6, 10 );
	var visitor = game.teams.away.team;
	var home = game.teams.home.team;

	if( isShowScoresOn() ) {
		htmlBuilder = htmlBuilder.concat( openGameAnchor( gameNumber ) );
		htmlBuilder = htmlBuilder.concat( gameNumber );
		htmlBuilder = htmlBuilder.concat( " - " );
		htmlBuilder = htmlBuilder.concat( visitor.name );
		htmlBuilder = htmlBuilder.concat( deriveScore( game ) );
		htmlBuilder = htmlBuilder.concat( home.name );
		htmlBuilder = htmlBuilder.concat( " - " );
		htmlBuilder = htmlBuilder.concat( deriveGameStatus( game ) );
		htmlBuilder = htmlBuilder.concat( closeGameAnchor() );
	} else {
		htmlBuilder = htmlBuilder.concat( openGameAnchor( gameNumber ) );
		htmlBuilder = htmlBuilder.concat( gameNumber );
		htmlBuilder = htmlBuilder.concat( " - " );
		htmlBuilder = htmlBuilder.concat( visitor.name );
		htmlBuilder = htmlBuilder.concat( " vs " );
		htmlBuilder = htmlBuilder.concat( home.name );
		htmlBuilder = htmlBuilder.concat( closeGameAnchor() );
	}
}


function clickGame( gameNo ) {
	gameNumberFields = document.getElementsByName("gameNumber")
	gameNumberFields[0].value = gameNo;
}


function openGameAnchor( gameNumber ) {
	var gameNo = parseInt( gameNumber );
	var startAnchor = "<p class=\"mb-compact w3-hover-pale-blue\" onclick=\"clickGame( " + gameNo.toString() + " )\">";
	return startAnchor;
}

function closeGameAnchor() {
	var endAnchor = "</p>";
	return endAnchor;
}


function deriveScore( game ) {
	var scoreTag;
	switch( game.status.statusCode ) {
	case "1":
	case "2":
	case "8":
	case "9":
		scoreTag = " vs ";
		break;
	case "3":
	case "4":
	case "5":
	case "6":
	case "7":
		scoreTag = " " + game.teams.away.score + " - " + game.teams.home.score + " ";
		break;
	}
	return scoreTag;
}


function deriveGameStatus( game ) {
	var statusTag;
	switch( game.status.statusCode ) {
	case "1":
	case "8":
		var gameTime = new Date( game.gameDate );
		statusTag = gameTime.toLocaleTimeString( 'en-US', { timeZoneName: 'short' } ).replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
		break;
	case "9":
		statusTag = "PPD";
		break;
	case "2":
		statusTag = "Pre-Game";
		break;
	case "3":
	case "4":
		statusTag = "Period " + game.linescore.currentPeriod + " (" + game.linescore.currentPeriodTimeRemaining + ")";
		break;
	case "5":
	case "6":
	case "7":
		statusTag = "Final";
		if( game.linescore.currentPeriod > 3 ) {
			statusTag = statusTag + " (" + game.linescore.currentPeriodOrdinal + ")";
		}
		break;
	}
	return statusTag;
}


var today = new Date();
var year = today.getFullYear().toString();
var month = ( today.getMonth() + 1 ).toString();
var day = today.getDate().toString();

month = padNumber( month );
day = padNumber( day );
var todayISO = ( year + "-" + month + "-" + day );

var nhlURL = "https://statsapi.web.nhl.com/api/v1/schedule?startDate=" + todayISO + "&endDate=" + todayISO + "&hydrate=linescore";
var schedule = JSON.parse( Get( nhlURL ) );


var htmlBuilder = "";

function rebuildGameList() {
	checkedTag = isShowScoresOn() ? " checked " : "";
	htmlBuilder = "";
	htmlBuilder = htmlBuilder + "<h2>Today's Games</h2>";
	htmlBuilder = htmlBuilder + "<div class=\"w3-container\">";
	htmlBuilder = htmlBuilder + "<input class=\"w3-check\" type=\"checkbox\" name=\"showScores\"";
	htmlBuilder = htmlBuilder + checkedTag;
	htmlBuilder = htmlBuilder + "onclick=\"toggleShowScores()\">";
	htmlBuilder = htmlBuilder + "\n<label>Show scores</label>";

	var gameDate = schedule.dates[0];
	gameDate.games.forEach( eachGame );

	htmlBuilder = htmlBuilder + "</div>";

	document.getElementById("gameList").innerHTML = htmlBuilder;
}


/* Game state codes
 * 1 - Preview
 * 2 - Preview
 * 3 - Live
 * 7 - Final
 *
 *
 * Game status codes
 * 1 - Scheduled
 * 2 - Pre-Game
 * 3 - In Progress
 * 7 - Final
 */
