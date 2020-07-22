package io.github.jmcleodfoss.pst;

/**	The Options class allows control over some of the behavior of PST file processing. */
public class Options
{
	/**	This veriable dictates whether the CRC should be checked. */
	static boolean checkCRC = true;

	/**	This dictates whether or not to log failures to expand zipped OST 2013 blocks. */
	static boolean logUnzipFailures = false;

	/**	This dictates whether to throw an exception if we find a non-heap-node block signature when reading a Heap on Node Header (HNHDR); {@link HeapOnNode.Header#Header} */
	static boolean strictHeapNodes = false;

	/**	Control whether to check the CRC.
	*	@param	newValue	The new value to set {@link #checkCRC} to
	*	@see	CRC
	*/
	public static void setCheckCRC(boolean newValue)
	{
		checkCRC = newValue;
	}

	/**	Control whether to log unzip failures.
	*	@param	newValue	The new value to set {@link #logUnzipFailures} to
	*	@see	SimpleBlock#SimpleBlock
	*/
	public static void setLogUnzipFailures(boolean newValue)
	{
		logUnzipFailures = newValue;
	}

	/**	Control whether to throw an exception on finding non-heap-node block signatures in a Node Header.
	/**	Control whether to throw an exception on finding non-heap-node block signatures in a Node Header.
	*	@param	newValue	The new value to set {@link strictHeapNodes} to
	*	@see	HeapOnNode.Header#Header
	*/
	public static void setStrictHeapNodes(boolean newValue)
	{
		strictHeapNodes = newValue;
	}
}
