package fun.photoutil.main;

import java.io.FileReader;
import java.io.FileWriter;

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

	public String get(String key) {
		return (String) json.get(key);
	}

	@SuppressWarnings("unchecked")
	public void set(String key, String value) {
		json.put(key, value);
		FileWriter jw = null;
		try {
			jw = new FileWriter(Constants.CONFIGURATION_FILE);
			json.writeJSONString(jw);
		} catch (Exception e) {
			CommonUtils.exception(e);
		} finally {
			if (jw != null)
				try {
					jw.close();
				} catch (Exception e) {
				}
		}
	}
}
