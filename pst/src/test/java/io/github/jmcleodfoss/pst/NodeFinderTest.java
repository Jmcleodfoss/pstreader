package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.lang.reflect.InvocationTargetException;
import org.junit.Assert;

/** Test the NodeFinder class */
public class NodeFinderTest extends TestFrame
{
	/** Test the NodeFilder class by traversing the file's node B-tree and looking for each node.
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
		PSTFile pstFile = new PSTFile(new FileInputStream(file));
		final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);
		NodeFinder nf = new NodeFinder(pstFile);

		java.util.Iterator iterator = nbt.iterator();
		while (iterator.hasNext()) {
			final NBTEntry treeEntry = (NBTEntry)iterator.next();
			final NBTEntry findEntry = nf.find(treeEntry.nid);
			Assert.assertEquals(treeEntry.key(), findEntry.key());
		}
	}
}
