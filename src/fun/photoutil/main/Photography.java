package fun.photoutil.main;

import fun.photoutil.ui.SplashScreen;
import vikas.photography.framework.Constants;

/**
 * The main entry point for the application. It triggers everything required for the application.
 * 
 * @author vs0016025
 *
 */
public class Photography {

	public static void main(String[] args) {
		try {
			CommonUtils.init(Constants.LOG_FILE);
			SplashScreen.splash();

		} catch (Exception e) {
			CommonUtils.exception(e);
		}
	}
}
