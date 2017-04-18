package vikas.photography.framework;

import java.io.File;
import java.text.DateFormat;

public class PhotographyConstants {
	public static final String	BASE_DIRECTORY		= System.getProperty("user.dir");
	public static final String	LOG_FILE			= BASE_DIRECTORY + File.separator + "logfile.txt";
	public static final String	InputPath			= BASE_DIRECTORY + File.separator + "input";
	public static final String	CONFIGURATION_FILE	= BASE_DIRECTORY + File.separator + "config.json";
	public static final String	MonochromePath		= BASE_DIRECTORY + File.separator + "monochrome";
	public static final String	EnhancedPath		= BASE_DIRECTORY + File.separator + "enhanced";

	public static final String	FileNameFormat		= "/yyyyMM/yyyyMMdd";
}
