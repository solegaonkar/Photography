package fun.photography.framework;

import java.util.Calendar;

public class CommonUtils {
	public static final String	TIME_FORMAT_1	= "%04d%02d%02d%02d%02d%02d";
	public static final String	TIME_FORMAT_2	= "%04d%02d%02d %02d%02d%02d";
	public static final String	TIME_FORMAT_3	= "%04d/%02d/%02d %02d:%02d:%02d";

	/**
	 * Log to the console
	 * 
	 * @param log
	 */
	public static void info(String log) {
		System.out.printf("%s : %s : %s\n", CommonUtils.getTimeStamp(CommonUtils.TIME_FORMAT_3),
				CommonUtils.getStackElementInfo(3), log);
	}

	public static String getTimeStamp(String format) {
		Calendar c = Calendar.getInstance();
		return String.format(format, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));

	}

	/**
	 * Get the info for a particular point in the trace.
	 * 
	 * @param n
	 * - e.g. 2 for the current line, 3 for the caller.
	 * @return
	 */
	public static String getStackElementInfo(int n) {
		StackTraceElement[] sta = Thread.currentThread().getStackTrace();
		return String.format("%s : %s(%d)", sta[n].getFileName(), sta[n].getMethodName(), sta[n].getLineNumber());
	}
}
