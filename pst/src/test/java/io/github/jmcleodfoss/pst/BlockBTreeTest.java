package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;

/** Test the java.io.github.jmcledofss.BlockBTree class. */
public class BlockBTreeTest extends TestFrame
{
	/** Test the BlockBTree class by creating the BlockBTree object for the file.
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
			PSTFile pstFile = new PSTFile(new FileInputStream(file));
			new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Cannot allocate memory")) {
				throw new InsufficientMemoryException(e);
			}
			throw e;
		}
	}
}
