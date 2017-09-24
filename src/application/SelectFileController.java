package application;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;



import client.StreamOptions;
import client.ReturnStream;
import client.SingletonSocket;
import client.BLEServer_Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import parseTags.KeyPair;
import parseTags.TagMaker;

public class SelectFileController {

	Stage stage = new Stage();
	MediaPlayer player;
	String tags = "";
	String markTime = "";
	private boolean isStreaming = false;
    @FXML
    private MenuButton mediaType;

    @FXML
    private MenuItem movieItem;

    @FXML
    private MenuItem gameItem;

    @FXML
    private Button saveBut;

    @FXML
    private Button playBut;

    @FXML
    private Text saveLocation;

    @FXML
    private Text textInfo;

    @FXML
    private Text bvp;

    @FXML
    private Text gsr;

    @FXML
    private Text temp;

    @FXML
    private Text ibi;

    @FXML
    private Text acc;


    @FXML
    private Button saveTag;
    @FXML
    private TextField tagName;
    
    @FXML
    private Button stopStreamBut;
    
    @FXML
    void setGameItemOnAction(ActionEvent event) {
    	mediaType.setText("Game");

    }

    @FXML
    void setTagName(ActionEvent event)
    {
    	tags += tagName.getText() +"," + textInfo.getText() + "\n";
    	tagName.setText("");
    }
    
    @FXML 
    void setExportTag(ActionEvent event)
    {
    	saveFile("tag.csv", tags);
    }
    
    @FXML
    void setMovieItemOnAction(ActionEvent event) {
    	mediaType.setText("Movie");
    }

    @FXML
    void setSaveButtonOnAction(ActionEvent event) {
    	DirectoryChooser fileChooser = new DirectoryChooser();
    	fileChooser.setTitle("Save location");
    	File defaultDir = null;
    	if ( saveLocation.getText().equals(""))
    	{
        	defaultDir = new File("d:");
    	}
    	else
    	{
    		defaultDir = new File(saveLocation.getText());
    	}
    	fileChooser.setInitialDirectory(defaultDir);
    	File file = fileChooser.showDialog(stage);
    	if ( file!=null)
    	{
        	saveLocation.setText(file.getPath());
    	}    	
    }
    

    @FXML
    void setStopStream(ActionEvent event) {
    	stopStream();
    }

    
    @FXML
    void setPause(ActionEvent event) {
    	Status currStatus = player.getStatus();
    	if (currStatus == Status.PLAYING) {
    		player.pause();
    	}
    	else if(currStatus == Status.PAUSED) {
    		player.play();
    	}
    }
    @FXML
    void setPlayButtonOnAction(ActionEvent event) 
    {
    		Group root2 = new Group();
    		Media media = null;

    		if (mediaType.getText().equals("Game"))
    		{
    			String path = new File("src/media/Game.mp4").getAbsolutePath();
    			media = new Media(new File(path).toURI().toString());
    			textInfo.setText("Game file is playing");
    			final ObservableMap<String, Duration> markers = media.getMarkers();
    			File file = new File("src/parseTags/GameTags.csv");
    			LinkedList<KeyPair> tags = TagMaker.makeTag(file);
    			for (int i= 0; i<tags.size();i++)
    			{
    				KeyPair key = tags.get(i);
    				markers.put(key.getTagName(), key.getTime());
    			}		
    		}
    		else if ( mediaType.getText().equals("Movie"))
    		{
    			String path = new File("src/media/Movie.mp4").getAbsolutePath();
    			media = new Media(new File(path).toURI().toString());
    			textInfo.setText(("Movie file is playing"));
    			final ObservableMap<String, Duration> markers = media.getMarkers();
    			File file = new File("src/parseTags/MovieTags.csv");
    			LinkedList<KeyPair> tags = TagMaker.makeTag(file);
    			for (int i= 0; i<tags.size();i++)
    			{
    				KeyPair key = tags.get(i);
    				markers.put(key.getTagName(), key.getTime());
    			}

    		}
    		else
    		{
    			textInfo.setText("select type");
    		}
    		if (media != null)
    		{
        	    player = new MediaPlayer(media);
        		MediaView view = new MediaView(player);
        		root2.getChildren().add(view);
        		Scene scene = new Scene(root2, 400,400,Color.BLACK);
        		stage.setScene(scene);
        		stage.show();      		
        		player.play();	
        		player.setOnReady(new Runnable() 
        		{
					@Override
					public void run() {
						int w = player.getMedia().getWidth();
						int h = player.getMedia().getHeight();
						List<Screen> screens = Screen.getScreens();
						if (screens.size() > 1)
						{
							
							
							Screen screen = screens.get(1);
							Rectangle2D bounds = screen.getVisualBounds();
							
							stage.setX(bounds.getMinX() );
			                stage.setY(bounds.getMinY() );

						}
		                stage.setMinWidth(w);
						stage.setMinHeight(h);
						
						stage.setMaximized(true);
						
						setStream("ON");
						DataStore store = new DataStore();
						Thread t = new Thread(store);
						t.start();	
					}
				});
        		player.setOnMarker(new EventHandler<MediaMarkerEvent>() {
					
					@Override
					public void handle(MediaMarkerEvent event) {
						Date date = new Date();			
						double time = date.getTime();
						markTime += event.getMarker().getKey() + ',' + time + "\n";	
					}
				});
        		stage.setOnCloseRequest((closeEvent) -> {
        			player.stop();

        			});
        		player.setOnEndOfMedia(new Runnable() {
					
					@Override
					public void run() {
						
						stage.close();
						//setStream("OFF");
						stopStream();
					}
				});
        		player.setOnStopped(new Runnable() {
					
					@Override
					public void run() {
						
						//setStream("OFF");
						stopStream();
					}
				});   		
        		player.currentTimeProperty().addListener(new ChangeListener<Duration>() 
        		{
					@Override
					public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
							Duration currValue) {
						double time = currValue.toMillis();
						//textInfo.setText(String.valueOf(time));
					}
        		});
    		}
    }  
    private void setStream(String value)
    {
    	// set EDA on
		String send = "device_subscribe "+ ReturnStream.streamValue(StreamOptions.GALVANICSKINRESPONSE)+" "+value;
		Socket socket = SingletonSocket.getInstance(ClientConnectController.Address,ClientConnectController.Port);

		String received = BLEServer_Client.sendMessage(socket, send);

		//set acc on
		send = "device_subscribe "+ ReturnStream.streamValue(StreamOptions.ACCELERATION)+" "+value;
		received = BLEServer_Client.sendMessage(socket, send);

		//set bvp on
		send = "device_subscribe "+ ReturnStream.streamValue(StreamOptions.BLOODVOLUMEPULSE)+" "+value;
		received = BLEServer_Client.sendMessage(socket, send);

		//set ibi on
		send = "device_subscribe "+ ReturnStream.streamValue(StreamOptions.INTERBEATINTERVAL)+" "+value;
		received = BLEServer_Client.sendMessage(socket, send);
		
		//set skin Temperature on
		send = "device_subscribe "+ ReturnStream.streamValue(StreamOptions.SKINTEMPERATURE)+" "+value;
		received = BLEServer_Client.sendMessage(socket, send);
    }   
    private void checkResult(String received,Text text)
    {
		String check = received.substring(received.lastIndexOf(" ") + 1);
		if (check.toLowerCase().equals("ok"))
		{
			text.setText("on");
		}
		else if (check.toLowerCase().equals("off"))
		{
			text.setText("off");
		}
		else
		{
			text.setText("problem streaming");
		}
    }   
    private void stopStream()
    {
		Socket socket = SingletonSocket.getInstance(ClientConnectController.Address, ClientConnectController.Port);
		textInfo.setText(BLEServer_Client.disconnect(socket));
    } 
    private class DataStore implements Runnable 
    {
    	StringBuilder acceleration = new StringBuilder();
    	StringBuilder bloodVolumePulse = new StringBuilder();
    	StringBuilder galvanicSkinResponse = new StringBuilder();
    	StringBuilder interbeatInterval = new StringBuilder();
    	StringBuilder skinTemperature = new StringBuilder();
    	StringBuilder wrongInput = new StringBuilder();
    	String line;	
    	LinkedList<String> lines = new LinkedList<String>();
		@Override
		public void run() {
			Socket socket = SingletonSocket.getInstance(ClientConnectController.Address, ClientConnectController.Port);    	
			try
	    	{
	    		while ((line = BLEServer_Client.br.readLine()) != null)
	    		{
	    			
	    			System.out.println(line);
	    			String[] lineArray = line.split(" ");
	    			parse(lineArray[0],lineArray);
	    		}
	    		saveFile("galvanicSkinResponse.csv", galvanicSkinResponse.toString());
	    		saveFile("acceleration.csv", acceleration.toString());
	    		saveFile("bloodVolumePulse.csv", bloodVolumePulse.toString());
	    		saveFile("interbeatInterval.csv", interbeatInterval.toString());
	    		saveFile("skinTemperature.csv", skinTemperature.toString());
	    		saveFile("wrongInput.csv", wrongInput.toString());
	    		saveFile("markTime.csv",markTime);
	    		BLEServer_Client.disconnect(socket);
	    		BLEServer_Client.closeSocket(socket); 		
	    	}
	    	catch(Exception e)
	    	{
	    		textInfo.setText(e.getMessage());
	    	}	
		}		
		public void parse(String args,String[] line)
		{
			Date date = new Date();
			long time = date.getTime();
			String result  ="";
			switch (args) {
			case "E4_Acc":
			    result = line[0] + "," +  time + "," + line[1] + "," + line[2] + "," + line[3] + "," + line[4];
				acceleration.append(result).append("\n");
				break;
			case "E4_Bvp":
			    result = line[0] + "," +  time + "," + line[1] + "," + line[2];
				bloodVolumePulse.append(result).append("\n");
				break;
			case "E4_Gsr":
				result = line[0] + "," +  time + "," + line[1] + "," + line[2];
				galvanicSkinResponse.append(result).append("\n");
				break;
			case "E4_Temperature":
				result = line[0] + "," +  time + "," + line[1] + "," + line[2];
				skinTemperature.append(result).append("\n");
				break;
			case "E4_Ibi":
				result = line[0] + "," +  time + "," + line[1] + "," + line[2];
				interbeatInterval.append(result).append("\n");
				break;
			default:
				for (int i = 0 ; i < line.length;i++)
				{
					result += line[i];
				}
				wrongInput.append(result).append("\n");
				break;
			}
		}	
	    private void saveFile(String fileName,String content)
	    {
	    	try
	    	{
	        	FileWriter fw = new FileWriter(saveLocation.getText()+"/"+fileName);
	        	BufferedWriter bw = new BufferedWriter(fw);
	        	bw.write(content);
	        	bw.close();
	        	fw.close();
	    	}
	    	catch(Exception e)
	    	{
	    		textInfo.setText(e.getMessage());
	    	}

	    }
		
    }  
    // DELTE
    private void saveFile(String fileName,String content)
    {
    	try
    	{
        	FileWriter fw = new FileWriter(saveLocation.getText()+"/"+fileName);
        	BufferedWriter bw = new BufferedWriter(fw);
        	bw.write(content);
        	bw.close();
        	fw.close();
    	}
    	catch(Exception e)
    	{
    		textInfo.setText(e.getMessage());
    	}

    }
   
}
