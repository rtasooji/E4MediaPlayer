package application;
	

import client.BLEServer_Client;
import client.SingletonSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


/**
 * 
 * @author Reza Tasooji
 * An application for the "Determining correlation between video stimulus and emotion arousal using a biometric input device" study
 * The application provides:
 * Client for E4 Empatica wristband that stores data streams while playing the media. 
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("ClientConnect.fxml"));
			Scene scene = new Scene(root,600,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Connect to Empatica Server");
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(e->{
				if (ClientConnectController.Address!=null)
				{
					BLEServer_Client.disconnect(SingletonSocket.getInstance(ClientConnectController.Address, ClientConnectController.Port));
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
