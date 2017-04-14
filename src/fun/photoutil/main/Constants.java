package fun.photoutil.main;

import java.io.File;

public class Constants {
	public static final String	BASE_DIRECTORY	= System.getenv("user.dir");
	public static final String	LOG_FILE		= BASE_DIRECTORY + File.separator + "worklog.txt";
	public static final String	PHOTO_DIRECTORY	= BASE_DIRECTORY + File.separator + "photographs";

}
