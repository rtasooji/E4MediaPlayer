package parseTags;

import javafx.util.Duration;

public class KeyPair
{
	String tagName;
	Duration time;
	
	public KeyPair(String tag, Duration time)
	{
		setTagName(tag);
		setTime(time);
	}
		
	 public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Duration getTime() {
		return time;
	}

	public void setTime(Duration time) {
		this.time = time;
	}
	
}