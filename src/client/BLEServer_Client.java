package client;
import java.io.*;
import java.net.*;

public class BLEServer_Client {
	private static PrintStream ps;
	private static InputStreamReader ir;
	public static BufferedReader br;
	public BLEServer_Client()
	{

	}	
	public static String sendMessage(Socket socket,String message) 
	{
		try
		{
			 ps = SingletonPrintStream.getInstance(socket.getOutputStream());
		
				
			 ir = SingletonInputStream.getInstance(socket.getInputStream());
			 br = SingletonBufferReader.getInstance(ir);
			 
			 ps.println(message);
			 
			String result = br.readLine();
			return result;
		}
		catch (Exception e)
		{
			return ("Connection failed!" + e.toString());
		}

	}
	
	public static String disconnect(Socket socket)
	{
		return sendMessage(socket, "device_disconnect");
	}
	
	public static void closeSocket(Socket socket)
	{
		try {
			ps.close();
			ir.close();
			br.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
