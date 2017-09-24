package client;


import java.net.Socket;

public class SingletonSocket {
	private static Socket instance = null;
	
	protected SingletonSocket()
	{
		// empty construct
	}
	public static Socket getInstance(String url, int port)
	{
		if (instance == null)
		{
			instance = run(url,port);
		}
		return instance;
	}
	
	private static Socket run(String server,int port)
	{
		try
		{
			Socket socket = new Socket(server, port);
			socket.setTcpNoDelay(true);
			return socket;
		}
		catch(Exception e)
		{
			System.out.println(e);
			return null;
		}
	}

	
}
