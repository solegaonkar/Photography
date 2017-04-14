package fun.photoutil.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;

import javax.swing.JOptionPane;

/**
 * Common utility code that is independent of this application and can be used as is in all other applications.
 * 
 * @author vs0016025
 *
 */
public class CommonUtils {
	public static final String		TIME_FORMAT_1	= "%04d%02d%02d%02d%02d%02d";
	public static final String		TIME_FORMAT_2	= "%04d%02d%02d %02d%02d%02d";
	public static final String		TIME_FORMAT_3	= "%04d/%02d/%02d %02d:%02d:%02d";

	public static BufferedWriter	bw				= null;

	public static void init(String logFilePath) throws Exception {
		bw = new BufferedWriter(new FileWriter(logFilePath));
	}

	public static void close() throws Exception {
		if (bw != null) {
			bw.flush();
			bw.close();
		}
	}

	/**
	 * Log to the console
	 * 
	 * @param log
	 */
	public static void log(String log) {
		try {
			if (bw != null) {
				bw.write(String.format("%s : %s : %s\n", CommonUtils.getTimeStamp(CommonUtils.TIME_FORMAT_3),
						CommonUtils.getStackElementInfo(3), log));
			}
		} catch (Exception e) {
		}
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
	
	/**
	 * Show a dialog containing the stack trace.
	 * @param e
	 */
	public static void exception(Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Exception in thread \"%s\" %s: %s%n", Thread.currentThread().getName(), e.getClass().getName(), e.getMessage()));
		for (StackTraceElement ste : e.getStackTrace()) {
			sb.append(String.format("\t at %s.%s(%s:%d)%n", ste.getClassName(), ste.getMethodName(), ste.getFileName(), ste.getLineNumber()));
		}
		log(sb.toString());
		JOptionPane.showMessageDialog(null, sb.toString(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
}
