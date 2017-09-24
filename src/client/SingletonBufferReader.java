package client;

import java.io.BufferedReader;
import java.io.Reader;

public class SingletonBufferReader {
	private static BufferedReader instance = null;
	
	public SingletonBufferReader()
	{
		
	}
	public static BufferedReader getInstance(Reader in)
	{
		if (instance == null)
		{
			instance = new BufferedReader(in);
		}
		return instance;
	}

}
