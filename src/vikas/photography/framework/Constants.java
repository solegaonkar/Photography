package vikas.photography.framework;

import java.io.File;

public class Constants {
	public static final String	BASE_DIRECTORY	= System.getProperty("user.dir");
	public static final String	LOG_FILE		= BASE_DIRECTORY + File.separator + "logfile.txt";
	public static final String	InputPath		= BASE_DIRECTORY + File.separator + "input";
	public static final String	MonochromePath	= BASE_DIRECTORY + File.separator + "output/monochrome";
	public static final String	EnhancedPath	= BASE_DIRECTORY + File.separator + "output/enhanced";
	public static final String	NeonPath		= BASE_DIRECTORY + File.separator + "output/neon";
	public static final String	PencilPath		= BASE_DIRECTORY + File.separator + "output/pencil";
	public static final String	OriginalsPath	= BASE_DIRECTORY + File.separator + "output/originals";
	public static final String	PlainPath		= BASE_DIRECTORY + File.separator + "output/plain";
}
