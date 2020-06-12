package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.lang.reflect.InvocationTargetException;

/** Test the java.io.github.jmcledofss.BlockBTree class. */
public class BlockBTreeTest extends TestFrame
{
	/** Test the BlockBTree class by creating the BlockBTree object for the file.
	*	@param	file	The file to be tested.
	*	@throws	BufferUnderflowException	Some tests may result in buffer underflows
	*	@throws	FileNotFoundException		The file to be tested was not found
	*	@throws	IllegalAccessException		A reflection problem was encountered in the test framework
	*	@throws	InsufficientMemoryException	There was not enough memory to process a file
	*	@throws	InstantiationException		A reflection problem was encountered in the test framework
	*	@throws	IOException			There was a problem reading a file
	*	@throws	NotPSTFileException		The given file was not a PST file
	*	@throws	NoSuchMethodException		A reflection problem was encountered in the test framework
	*	@throws	Throwable			A run-time exception was encountered
	*/
	protected void test(File file)
	throws
		BufferUnderflowException,
		FileNotFoundException,
		IllegalAccessException,
		InsufficientMemoryException,
		InstantiationException,
		IOException,
		NotPSTFileException,
		NoSuchMethodException,
		Throwable
	{
		try {
			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(file));
			new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Cannot allocate memory")) {
				throw new InsufficientMemoryException(e);
			}
			throw e;
		}
	}
}
