package io.github.jmcleodfoss.pst;

/**	The Options class allows control over some of the behavior of PST file processing. */
public class Options
{
	/**	This veriable dictates whether the CRC should be checked. */
	public static boolean checkCRC = true;

	/**	This dictates whether to throw an exception if we find a non-heap-node block signature when reading a Heap on Node Header (HNHDR); {@link HeapOnNode.Header#Header} */
	public static boolean strictHeapNodes = false;
}
