package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import org.junit.Assert;

/** Test the io.github.jmcledofoss.pst.BlockFinder class */
public class BlockFinderTest extends TestFrame
{
	/** Test the BlockFinder class by creating a BlockFinder object for the file's BlockBTree and then trying to find each entry in the Block BTree.
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
			final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
			BlockFinder bf = new BlockFinder(pstFile);

			java.util.Iterator iterator = bbt.iterator();
			while (iterator.hasNext()) {
				final BBTEntry treeEntry = (BBTEntry)iterator.next();
				final BBTEntry findEntry = bf.find(treeEntry.bref.bid);
				Assert.assertEquals(treeEntry.key(), findEntry.key());
			}
			pstFile.close();
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Cannot allocate memory")) {
				throw new InsufficientMemoryException(e);
			}
			throw e;
		}
	}
}
