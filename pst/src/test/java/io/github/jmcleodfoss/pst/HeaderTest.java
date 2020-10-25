package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import org.junit.Test;

/** Test the Header class. */
public class HeaderTest extends TestFrame
{
	/** Confirm the Header class correctly throws the expected exception for a pst file which is shorter than the header length.
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
	@Test(expected = BufferUnderflowException.class) public void shorterThanHeader()
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
		test(new File("truncated-in-header.bin"));
	}

	/** Confirm the Header class correctly throws the expected exception for a file which is not a pst file.
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
	@Test(expected = NotPSTFileException.class) public void notPST()
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
		test(new File("not-a-pst-file.txt"));
	}

	/** Test the Header class by reading the header for the file.
	*   The {@link Header#Header} constructor will thrown an exception if it is not a pst file.
	*	@param	file	{@inheritDoc}
	*	@throws	BufferUnderflowException	{@inheritDoc}
	*	@throws	FileNotFoundException		{@inheritDoc}
	*	@throws	IllegalAccessException		{@inheritDoc}
	*	@throws	InsufficientMemoryException	{@inheritDoc}
	*	@throws	InstantiationException		{@inheritDoc}
	*	@throws	IOException			{@inheritDoc}
	*	@throws	NotPSTFileException		{@inheritDoc}
	*	@throws	NoSuchMethodException		{@inheritDoc}
	*	@throws	Throwable			{@inheritDoc}
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
			FileInputStream stream = new FileInputStream(file);
			FileChannel fc = stream.getChannel();
			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			mbb.order(java.nio.ByteOrder.LITTLE_ENDIAN);
			new Header(mbb);
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Cannot allocate memory")) {
				throw new InsufficientMemoryException(e);
			}
			throw e;
		}
	}
}
