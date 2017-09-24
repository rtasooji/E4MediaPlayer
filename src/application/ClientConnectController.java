package application;

import java.net.Socket;

import client.BLEServer_Client;
import client.SingletonSocket;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class ClientConnectController {

	public static String Address= "";
	public static int Port = 0;
    @FXML
    private Button next;

    @FXML
    private TextField serverAddress;

    @FXML
    private TextField serverPort;

    @FXML
    private Button connect;

    @FXML
    private RadioButton radioButton;
    
    @FXML
    private Text textInfo;

    @FXML
    void HandleConnectButtonAction(ActionEvent event) {
    	String url = serverAddress.getText();
    	String port = serverPort.getText();
    	if (url.isEmpty() || port.isEmpty())
    	{
    		textInfo.setText("Enter Address/Port");
    	}
    	try
    	{
    		int portNum = Integer.parseInt(port);
    		Socket socket = SingletonSocket.getInstance(url, portNum);
    		// 
    		boolean isConnected = connectEmpatica(socket);

    		radioButton.setSelected(isConnected);
        	if ( radioButton.isSelected() && textInfo.getText().equals("connected to device"))
        	{
        		next.setDisable(false);
        		Address = url;
        		Port = portNum;
        	}
    	}
    	catch(Exception e)
    	{
    		textInfo.setText("Enter valid port");
    	}

    }
    @FXML
    void HandleNextButtonAction(ActionEvent event) {
    	try
    	{
    		FXMLLoader fxmlLoader = new FXMLLoader(application.Main.class.getResource("SelectFile.fxml"));
    		Parent root2 = (Parent) fxmlLoader.load();
    		Scene scene2 = new Scene(root2,600,600);
    		Stage stage2 = new Stage();
    		stage2.initStyle(StageStyle.DECORATED);
    		stage2.setTitle("Select File");
    		stage2.setScene(scene2);
    		stage2.show();
    	}
    	catch(Exception e)
    	{
    		textInfo.setText(e.getMessage());
    	}
    }
    private boolean connectEmpatica(Socket socket) {
		// check the connection is established
		if (socket == null)
		{
			textInfo.setText("connection failed!");
		}
		String send = "server_status";
		// should receive "R server_status OK"
		String received = BLEServer_Client.sendMessage(socket, send);
		String check = received.substring(received.lastIndexOf(" ") + 1);
		if ( check.toLowerCase().equals("ok"))
		{
			textInfo.setText("connected to server.");
			
			// ask for device lists
			send = "device_list";
			
			// should receive "R device_list <NUMBER_OF_DEVICES> | <DEVICE_INFO_1> | <DEVICE_INFO_2>"
			// Example for <DEVICE_INFO> : "9ff167 Empatica E4 available"
			received = BLEServer_Client.sendMessage(socket, send);
			
			String[] result = received.split(" ");
			
			// TODO: only works with the first device ID, If more device is connected implementation required.
			if (!result[2].equals("0"))
			{
				send = "device_connect " + result[4];
				received = BLEServer_Client.sendMessage(socket, send);  
				check = received.substring(received.lastIndexOf(" ") + 1);
				if (check.toLowerCase().equals("ok"))
				{
					// connection stablished between empatica and client ready to receive data
					textInfo.setText("connected to device");
					return true;
					
				}
			}
			else
			{
				textInfo.setText("no device found!");
			}	
		}
		return false;
	}
}

