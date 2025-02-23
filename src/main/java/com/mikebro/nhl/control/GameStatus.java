package com.mikebro.nhl.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class GameStatus extends HBox {

	private static final Log logger = LogFactory.getLog( GameStatus.class );

	private Label textLabel;

	public GameStatus( String customString ) {
		super(5);
		createText( customString );
		this.getChildren().addAll( textLabel );
	}

	public GameStatus() {
		super(5);
		createText( "custom" );
		this.getChildren().addAll( textLabel );
	}

	public void setText( String newText ) {
		textLabel.setText( newText );
	}


	private void createText( String textString ) {
		textLabel = new Label();
		textLabel.setText( textString );
		textLabel.setTooltip( new Tooltip( "game label tooltip" ) );
		textLabel.setFont( new Font( "Verdana", 16.0 ) );
		textLabel.setPrefHeight( 25.0 );
		textLabel.setPrefWidth( 550.0 );
		//logger.info( String.format( "textfield height: %s  width: %s", textLabel.getHeight(), textLabel.getWidth() ) );
	}

}