package com.mikebro.nhl.control;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class CustomControl extends HBox {
	private Label textLabel;

	public CustomControl( String customString ) {
		super(5);
		createText( customString );
		this.getChildren().addAll( textLabel );
	}

	public CustomControl() {
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
		textLabel.setPrefWidth( 450.0 );
		System.out.printf( "textfield height: %s  width: %s%n", textLabel.getHeight(), textLabel.getWidth() );
	}

}