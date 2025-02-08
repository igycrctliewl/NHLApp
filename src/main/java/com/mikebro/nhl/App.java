package com.mikebro.nhl;

import java.util.Date;

import javax.swing.Timer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author mikebro
 */
public class App extends Application {

	@Override
	public void start( Stage primaryStage ) {

		Button btn = new Button();
		btn.setText( "Say 'Hello World'" );
		btn.setLayoutX( 10 );
		btn.setLayoutY( 100 );
		btn.setOnAction( new EventHandler<ActionEvent>() {
			@Override
			public void handle( ActionEvent event ) {
				System.out.println( "Hello World!" );
			}
		} );

		Button btn2 = new Button();
		btn2.setText( "Oh, just have text" );
		btn2.setLayoutX( 150 );
		btn2.setLayoutY( 100 );
		btn2.setOnAction( new EventHandler<ActionEvent>() {
			@Override
			public void handle( ActionEvent event ) {
				System.out.println( "Button2 has spoken" );
			}
		} );

		CustomControl cc = new CustomControl();
		cc.setLayoutX(20);
		cc.setLayoutY(300);

		CustomControl cc2 = new CustomControl( "my second" );
		cc2.setLayoutX(20);
		cc2.setLayoutY(350);

		Pane root = new Pane();
		//StackPane root = new StackPane();
		root.getChildren().add( btn );
		root.getChildren().add( btn2 );
		root.getChildren().add( cc );
		root.getChildren().add( cc2 );

		Timer timer = new Timer(5000, ae -> cc2.setText( (new Date()).toString() ) );
		timer.start();

		Scene scene = new Scene( root, 300, 500 );

		primaryStage.setTitle( "Hello World!" );
		primaryStage.setScene( scene );
		primaryStage.show();
	}


	public class CustomControl extends HBox {
		private TextField textField;
		private Button button;

		public CustomControl( String customString ) {
			super(5);
			createText( customString );
			createButton( customString );
			this.getChildren().addAll( textField, button );
		}

		public CustomControl() {
			super(5);
			createText( "custom" );
			createButton( "custom" );
			this.getChildren().addAll( textField, button );
		}

		public void setText( String newText ) {
			textField.setText( newText );
		}


		private void createText( String textString ) {
			textField = new TextField();
			textField.setText( textString.concat( " text" ) );
			textField.setFont( new Font( "Verdana", 14.0 ) );
			textField.setPrefHeight( 25.0 );
			textField.setPrefWidth( 150.0 );
			System.out.printf( "textfield height: %s  width: %s%n", textField.getHeight(), textField.getWidth() );
			//this.textField.setPadding( padding );
		}

		private void createButton( String textString ) {
			button = new Button();
			button.setText( textString.concat( " button" ) );
			button.setPrefHeight( 25.0 );
			button.setPrefWidth( 115.0 );
			System.out.printf( "button height: %s  width: %s%n", button.getHeight(), button.getWidth() );
			//this.button.setPadding( padding );
			button.setOnAction( event -> System.out.println( "cc message: ".concat( textField.getText() ) ) );
		}
	}
}
