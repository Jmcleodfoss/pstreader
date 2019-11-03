package com.jsoft.pst;

/**	The Debug class provides a local repository for a com.jsoft.util.Debug object, and access to it. */
class Debug {

	/**	Should we log to console? */
	static final boolean logToConsole = false;

	/**	The filename to log to */
	static final String logFilename = "diag.txt";

	static java.io.FileOutputStream diagOutputStream;
	static {
		try {
			diagOutputStream = new java.io.FileOutputStream(logFilename);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace(System.out);
			System.exit(1);
		}
	}

	/**	Diagnostic log handler */
	static final java.util.logging.Handler logHandler = new java.util.logging.StreamHandler(diagOutputStream, new java.util.logging.SimpleFormatter());
	static {
		logHandler.setLevel(java.util.logging.Level.FINEST);
	}

	/**	Package logger - this governs child loggers. */
	static final java.util.logging.Logger packageLogger = java.util.logging.Logger.getLogger("com.jsoft.pst");
	static {
		packageLogger.setUseParentHandlers(false);
		setupLogger(packageLogger);
		packageLogger.setLevel(java.util.logging.Level.OFF);
	}

	/**	Get a logger. This is a wrapper around java.utilo.logging.Logger.getLogger(); using it to obtaina logger ensures that packageLogger is appropriately initialized. */
	static java.util.logging.Logger getLogger(final String name)
	{
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
		setupLogger(logger);
		return logger;
	}

	/**	Translate a log level string into a log level, allowing it to be one of the keywords "off", "severe", "warning", "info",
	*	"fine", "finer", "finest."
	*
	*	@param	s	The String to look up the log level for.
	*
	*	@return	The log level from java.util.logging.Level corresponding to the passed String s, if any.
	*/
	static java.util.logging.Level getLogLevel(final String s)
	throws
		NumberFormatException
	{
		if (s.equals("off"))
			return java.util.logging.Level.OFF;
		if (s.equals("severe"))
			return java.util.logging.Level.SEVERE;
		if (s.equals("warning"))
			return java.util.logging.Level.WARNING;
		if (s.equals("info"))
			return java.util.logging.Level.INFO;
		if (s.equals("fine"))
			return java.util.logging.Level.FINE;
		if (s.equals("finer"))
			return java.util.logging.Level.FINER;
		if (s.equals("finest"))
			return java.util.logging.Level.FINEST;
		throw new RuntimeException("Unknown log level name " + s);
	}

	/**	Set up a logger with the library logging defaults.
	*
	*	@param	logger	The logger to set up.
	*/
	private static void setupLogger(java.util.logging.Logger logger)
	{

		java.util.logging.Handler[] handlers = logger.getHandlers();
		boolean foundDiagLogger = false;
		for (java.util.logging.Handler handler : handlers) {
			if (handler.equals(logHandler))
				foundDiagLogger = true;
			else if (!logToConsole && handler instanceof java.util.logging.ConsoleHandler)
				packageLogger.removeHandler(handler);
		}
		if (!foundDiagLogger)
			logger.addHandler(logHandler);
	}
}
