package client;

import java.io.InputStream;
import java.io.InputStreamReader;

public class SingletonInputStream {
	private static InputStreamReader instance = null;
	
	public SingletonInputStream()
	{
		
	}
	public static InputStreamReader getInstance(InputStream in)
	{
		if (instance == null)
		{
			instance = new InputStreamReader(in);
		}
		return instance;
	}

}
