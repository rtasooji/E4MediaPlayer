package client;

import java.io.OutputStream;
import java.io.PrintStream;

public class SingletonPrintStream {
	private static PrintStream instance = null;
	
	public SingletonPrintStream() {
	}
	
	public static PrintStream getInstance(OutputStream out)
	{
		if (instance == null)
		{
			instance = new PrintStream(out);
		}
		return instance;
	}
}
