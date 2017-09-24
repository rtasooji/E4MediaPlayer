package client;

public class ReturnStream {
	
	public static String streamValue(StreamOptions stream)
	{
		switch (stream)
		{
		case ACCELERATION:
			return "acc";
		case BLOODVOLUMEPULSE:
			return "bvp";
		case GALVANICSKINRESPONSE:
			return "gsr";
		case INTERBEATINTERVAL:
			return "ibi";
		case SKINTEMPERATURE:
			return "tmp";
		case DEVICEBATTERY:
			return "bat";
		case TAG:
			return "tag";
		default:
			return "";				
		}
	}
}
