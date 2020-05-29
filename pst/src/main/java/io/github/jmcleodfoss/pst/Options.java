package io.github.jmcleodfoss.pst;

/**	The Options class allows control over some of the behavior of PST file processing. */

class Options {

	/**	This veriable dictates whether the CRC should be checked. */
	static boolean checkCRC = true;

	/**	This variable enables or disabling full debugging from the beginning of main. */
	static boolean fullDebugging = false;

	static {
		/* Anything done here will affect things called by main. */
		if (fullDebugging) {
			java.util.logging.Logger logger = java.util.logging.Logger.getLogger("io.github.jmcleodfoss.pst");
			logger.setLevel(java.util.logging.Level.FINEST);
		}
	}
}
