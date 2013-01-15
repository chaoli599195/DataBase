package edu.ncsu.dbms.logger;

import edu.ncsu.dbms.constants.DBMSLoggingLevelsEnum;

public class DBMSLogger {

	private boolean INFO_LEVEL = false;

	private boolean DEBUG_LEVEL = false;

	private boolean WARN_LEVEL = false;

	private boolean ERROR_LEVEL = false;

	private static DBMSLogger logger = null;

	/**
	 * Constructor
	 * 
	 * @param log_level
	 */
	private DBMSLogger(DBMSLoggingLevelsEnum log_level) {

		switch (log_level) {
		case DEBUG:
			DEBUG_LEVEL = true;
		case INFO:
			INFO_LEVEL = true;
		case WARN:
			WARN_LEVEL = true;
		case ERROR:
			ERROR_LEVEL = true;
			break;
		}

	}

	/**
	 * Get single instance of the logger
	 * 
	 * @param log_level
	 * @return {@link DBMSLogger}
	 */
	public static DBMSLogger getInstance(DBMSLoggingLevelsEnum log_level) {

		if (logger == null) {
			logger = new DBMSLogger(log_level);
		}

		return logger;
	}

	/**
	 * Get  default single instance of the logger
	 *
	 * @return {@link DBMSLogger}
	 */
	public static DBMSLogger getInstance() {

		if (logger == null) {
			logger = new DBMSLogger(DBMSLoggingLevelsEnum.WARN);
		}

		return logger;
	}

	
	/**
	 * Prints info log if level is enabled
	 * 
	 * @param log
	 */
	public void info(String log) {
		if (INFO_LEVEL) {
			System.out.println("[INFO] : " + log);
		}
	}

	/**
	 * Prints debug log if level is enabled
	 * 
	 * @param log
	 */
	public void debug(String log) {
		if (DEBUG_LEVEL) {
			System.out.println("[DEBUG] : " + log);
		}
	}

	/**
	 * Prints warn log if level is enabled
	 * 
	 * @param log
	 */
	public void warn(String log) {
		if (WARN_LEVEL) {
			System.out.println("[WARN] : " + log);
		}
	}

	/**
	 * Prints error log if level is enabled
	 * 
	 * @param log
	 */
	public void error(String log) {
		if (ERROR_LEVEL) {
			System.out.println("[ERROR] : " + log);
		}
	}
}
