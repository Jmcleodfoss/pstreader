package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Test;

/** Driver for pst library tests, which runs the {@see #test} function on each file in tne working directory.
*   Each test case derives from this, and overrides the {@see #test} function to actually do the testing, as well as any other testing needed.
*/
abstract class TestFrame
{
	/** The primary test function, called for all test cases to run tests on each file in the working directory.
	*	@throws	BufferUnderflowException	Some tests may result in buffer underflows
	*	@throws	FileNotFoundException		The file to be tested was not found
	*	@throws	IllegalAccessExceotion		A reflection problem was encountered in the test framework
	*	@throws	InsufficientMemoryException	There was not enough memory to process a file
	*	@throws	InstantiationException		A reflection problem was encountered in the test framework
	*	@throws	IOException			There was a problem reading a file
	*	@throws	NotPSTException			The given file was not a PST file
	*	@throws	NoSuchMethodException		A reflection problem was encountered in the test framework
	*	@throws	Throwable			A run-time exception was encountered
	*/
	@Test
	public void pst()
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
		File pstPattern = new File(".");
		File[] files = pstPattern.listFiles(ExtensionFileFilter.pstFileFilter);
		for (File file : files)
			test(file);
	}

	/** Test function which is over-ridden by derived test classes to perform tests on the given file.
	*	@param	file	The file to be tested.
	*	@throws	BufferUnderflowException	Some tests may result in buffer underflows
	*	@throws	FileNotFoundException		The file to be tested was not found
	*	@throws	IllegalAccessExceotion		A reflection problem was encountered in the test framework
	*	@throws	InsufficientMemoryException	There was not enough memory to process a file
	*	@throws	InstantiationException		A reflection problem was encountered in the test framework
	*	@throws	IOException			There was a problem reading a file
	*	@throws	NotPSTException			The given file was not a PST file
	*	@throws	NoSuchMethodException		A reflection problem was encountered in the test framework
	*	@throws	Throwable			A run-time exception was encountered
	*/
	abstract protected void test(File file)
	throws
		BufferUnderflowException,
		FileNotFoundException,
		IllegalAccessException,
		InsufficientMemoryException,
		InstantiationException,
		IOException,
		NotPSTFileException,
		NoSuchMethodException,
		Throwable;
}
