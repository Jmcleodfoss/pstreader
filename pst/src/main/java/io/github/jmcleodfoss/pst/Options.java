package io.github.jmcleodfoss.pst;

/**	The Options class allows control over some of the behavior of PST file processing. */
public class Options
{
	/**	This veriable dictates whether the CRC should be checked. */
	static boolean checkCRC = true;

	/**	This dictates whether or not to log failures to expand zipped OST 2013 blocks. */
	static boolean logUnzipFailures = false;

	/**	This dictates whether properties with type MULTIPLE_GUID should be treated as multiple GUIDS (which is not supported yet) or as 32-bit integers to handle the
	*	AggregatedItemLinkIds property found in OST files which reports itself as PtypMultipleGuid but is actually a 32-bit integer.
	*	If this is false, the library will throw an exception indicating the property type is unsupported if it encounters one.
	*	@see #logMultipleGUIDSAsIntsInstances
	*/
	static boolean multipleGUIDSAsInts = true;

	/**	This dicates whether we should log a message to System.out when we encounter an object of PtypMultipleGuid and treat it as a 32-bit integer.
	*	@see #multipleGUIDSAsInts
	*/
	static boolean logMultipleGUIDSAsIntsInstances = false;

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

	/**	Control how to handle objects of PtypMultipleGuid
	*	Note that calling this function as {@code setMultipleGUIDHandling(false, true)} is a pointless, but not forbidden.
	*	@param	treatAsInt	If true, objects of PtypMultipleGuid are treated as 32-bit integers.
	*	@param	logTreatAsInt	If true, a message is logged to System.out if an object of PtypMultipleGuid is encountered {@link multipleGUIDSAsInts} is true
	*	@see	DataType#definitionFactory
	*/
	public static void setMultipleGUIDHandling(boolean treatAsInt, boolean logTreatAsInt)
	{
		multipleGUIDSAsInts = treatAsInt;
		logMultipleGUIDSAsIntsInstances = logTreatAsInt;
	}

	/**	Control whether to throw an exception on finding non-heap-node block signatures in a Node Header.
	*	@param	newValue	The new value to set {@link strictHeapNodes} to
	*	@see	HeapOnNode.Header#Header
	*/
	public static void setStrictHeapNodes(boolean newValue)
	{
		strictHeapNodes = newValue;
	}
}
