package fun.photoutil.main;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Configuration {
	private static final JSONObject json = readConf();

	private static JSONObject readConf() {
		JSONParser parser = new JSONParser();
		FileReader jr = null;
		try {
			jr = new FileReader(Constants.CONFIGURATION_FILE);
			return (JSONObject) parser.parse(jr);
		} catch (Exception e) {
			CommonUtils.exception(e);
			return null;
		} finally {
			if (jr != null)
				try {
					jr.close();
				} catch (Exception e) {
				}
		}
	}
}
