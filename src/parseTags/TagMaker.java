package parseTags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import javafx.util.Duration;

public class TagMaker {
	
	public TagMaker()
	{	
	}
	
	public static LinkedList<KeyPair> makeTag(File file)
	{
		LinkedList<KeyPair> tags = new LinkedList<>();
		try
		{
			FileReader filePath = new FileReader(file);
			BufferedReader br = new BufferedReader(filePath);
			String line;
			while((line = br.readLine())!=null)
			{

				String[] parse = line.split(",");
				String time = parse[1];
				double timeD = Double.parseDouble(time);
				Duration milli = new Duration(timeD);
				KeyPair key = new KeyPair(parse[0], milli);
				tags.add(key);
			}
			br.close();
			filePath.close();
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		return tags;
	}

}
