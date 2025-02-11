package com.mikebro.nhl;

import com.mikebro.nhl.control.SwitchButton;
import com.mikebro.nhl.format.FormatHelper;
import com.mikebro.nhl.json.Game;
import com.mikebro.nhl.json.Schedule;
import com.mikebro.nhl.service.NHLService;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author mikebro
 */
public class App extends Application {

	private NHLService nhlService;

	private static double sceneWidth = 500.0;
	private static double sceneHeight = 500.0;
	private static double labelX = 20.0;
	private static double labelY = 20.0;
	private static double yIncrement = 30.0;

	@Override
	public void start( Stage primaryStage ) {
		nhlService = Launcher.getNHLService();

		Pane root = new Pane();

		//ToggleButton tb1 = new ToggleButton("Show Scores");
		SwitchButton tb1 = new SwitchButton("Show Scores");
		tb1.setLayoutX( labelX );
		tb1.setLayoutY( labelY );
		labelY += yIncrement;
		root.getChildren().add( tb1 );

		CustomControl cc = new CustomControl( "434 - Minnesota 1 - 4 Los Angeles - Period 3 (05:13)" );
		cc.setLayoutX( labelX );
		cc.setLayoutY( labelY );
		labelY += yIncrement;
		root.getChildren().add( cc );

		CustomControl cc2 = new CustomControl( "my second" );
		cc2.setLayoutX( labelX );
		cc2.setLayoutY( labelY );
		labelY += yIncrement;
		root.getChildren().add( cc2 );

		Schedule schedule = nhlService.getTodaySchedule();
		cc2.setText( String.format( "found %s games", schedule.getGames().size() ) );

		for( Game g : schedule.getGames() ) {
			System.out.println( FormatHelper.buildGameString( g ) );
		}

		Scene scene = new Scene( root, sceneWidth, sceneHeight );

		primaryStage.setTitle( "NHLApp" );
		primaryStage.setScene( scene );
		primaryStage.show();
	}




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
}
