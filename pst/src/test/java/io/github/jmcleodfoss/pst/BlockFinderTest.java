package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Assert;

/** Test the io.github.jmcledofoss.pst.BlockFinder class */
public class BlockFinderTest extends TestFrame
{
	/** Test the BlockFinder class by creating a BlockFinder object for the file's BlockBTree and then trying to find each entry in the Block BTree.
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
			final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
			BlockFinder bf = new BlockFinder(pstFile);

			java.util.Iterator iterator = bbt.iterator();
			while (iterator.hasNext()) {
				final BBTEntry treeEntry = (BBTEntry)iterator.next();
				final BBTEntry findEntry = bf.find(treeEntry.bref.bid);
				Assert.assertEquals(treeEntry.key(), findEntry.key());
			}
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Cannot allocate memory")) {
				throw new InsufficientMemoryException(e);
			}
			throw e;
		}
	}
}
