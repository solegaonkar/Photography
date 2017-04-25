package vikas.photography.framework;

import java.io.File;

public class Constants {
	public static final String	BASE_DIRECTORY	= System.getProperty("user.dir");
	public static final String	InputPath		= BASE_DIRECTORY + File.separator + "images/input";
	public static final String	MonochromePath	= BASE_DIRECTORY + File.separator + "images/monochrome";
	public static final String	EnhancedPath	= BASE_DIRECTORY + File.separator + "images/enhanced";
	public static final String	NeonPath		= BASE_DIRECTORY + File.separator + "images/neon";
	public static final String	PencilPath		= BASE_DIRECTORY + File.separator + "images/pencil";
	public static final String	OriginalsPath	= BASE_DIRECTORY + File.separator + "images/originals";
	public static final String	PlainPath		= BASE_DIRECTORY + File.separator + "images/plain";
	public static final String	DataFile		= BASE_DIRECTORY + File.separator + "db.object";
	public static final float	SaveQuality		= 0.9f;
}
