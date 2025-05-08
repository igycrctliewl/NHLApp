var season = "20242025";
const PRESEASON = "01";
const REGULAR_SEASON = "02";
const PLAYOFFS = "03";

// use this to indicate which part of the season we are using
var part = PLAYOFFS;

function launchReports() {
	var x = document.forms["reports"]["gsRptCk"].checked;
	var gameNumberFields = document.getElementsByName("gameNumber");
	var gameNum = numberFormat( gameNumberFields[0].value );

	if( gameNum == "0000" ) {
		alert( "Please provide a game number" );
		throw( "arg missing" );
	}

	if( document.forms["reports"]["gsRptCk"].checked ) {
		launchGS( gameNum );
	}
	if( document.forms["reports"]["esRptCk"].checked ) {
		launchES( gameNum );
	}
	if( document.forms["reports"]["plRptCk"].checked ) {
		launchPL( gameNum );
	}
	if( document.forms["reports"]["fsRptCk"].checked ) {
		launchFS( gameNum );
	}
	if( document.forms["reports"]["fcRptCk"].checked ) {
		launchFC( gameNum );
	}
	if( document.forms["reports"]["roRptCk"].checked ) {
		launchRO( gameNum );
	}
	if( document.forms["reports"]["tvRptCk"].checked ) {
		launchTV( gameNum );
	}
	if( document.forms["reports"]["thRptCk"].checked ) {
		launchTH( gameNum );
	}
	if( document.forms["reports"]["ssRptCk"].checked ) {
		launchSS( gameNum );
	}
	if( document.forms["reports"]["scRptCk"].checked ) {
		launchSC( gameNum );
	}
	if( document.forms["reports"]["soRptCk"].checked ) {
		launchSO( gameNum );
	}
}

function numberFormat( i ) {
	var fmt = "" + i;
	for( ; fmt.length < 4; fmt = "0" + fmt );
	return fmt;
}




function launchGS( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/GS" + part + gameNo + ".HTM" );
}

function launchES( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/ES" + part + gameNo + ".HTM" );
}

function launchPL( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/PL" + part + gameNo + ".HTM" );
}

function launchFS( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/FS" + part + gameNo + ".HTM" );
}

function launchFC( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/FC" + part + gameNo + ".HTM" );
}

function launchRO( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/RO" + part + gameNo + ".HTM" );
}

function launchTH( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/TH" + part + gameNo + ".HTM" );
}

function launchTV( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/TV" + part + gameNo + ".HTM" );
}

function launchSS( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/SS" + part + gameNo + ".HTM" );
}

function launchSC( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/stats/shiftcharts?id=" + season.substring(0,4) + part + gameNo );
}

function launchSO( gameNo ) {
	//var randomnumber = Math.floor((Math.random()*100)+1);
	window.open( "http://www.nhl.com/scores/htmlreports/" + season + "/SO" + part + gameNo + ".HTM" );
}


function selectAll() {
	document.forms["reports"]["gsRptCk"].checked = document.forms["reports"]["allCk"].checked;
	document.forms["reports"]["esRptCk"].checked = document.forms["reports"]["allCk"].checked;
	document.forms["reports"]["plRptCk"].checked = document.forms["reports"]["allCk"].checked;
	document.forms["reports"]["fsRptCk"].checked = document.forms["reports"]["allCk"].checked;
	document.forms["reports"]["fcRptCk"].checked = document.forms["reports"]["allCk"].checked;
	document.forms["reports"]["roRptCk"].checked = document.forms["reports"]["allCk"].checked;
	document.forms["reports"]["tvRptCk"].checked = document.forms["reports"]["allCk"].checked;
	document.forms["reports"]["thRptCk"].checked = document.forms["reports"]["allCk"].checked;
	//document.forms["reports"]["ssRptCk"].checked = document.forms["reports"]["allCk"].checked;
	//document.forms["reports"]["scRptCk"].checked = document.forms["reports"]["allCk"].checked;
	//document.forms["reports"]["soRptCk"].checked = document.forms["reports"]["allCk"].checked;
}

