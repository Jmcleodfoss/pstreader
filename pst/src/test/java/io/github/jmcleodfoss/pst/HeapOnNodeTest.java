package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.lang.reflect.InvocationTargetException;

/** Test the HeapOnNode class */
public class HeapOnNodeTest extends TestFrame
{
	/** Test the HeapOnNode class by creating the HeapOnNode objects for each of the file's block B-tree entries which is a heap-on-node.
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
			final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

			java.util.Iterator iterator = nbt.iterator();
			while (iterator.hasNext()) {
				final NBTEntry node = (NBTEntry)iterator.next();
				if (!node.nid.isHeapOnNodeNID())
					continue;

				final BBTEntry dataBlock = bbt.find(node.bidData);
				if (dataBlock != null) {
					try {
						new HeapOnNode(dataBlock, bbt, pstFile);
					} catch (NotHeapNodeException e) {
					} catch (UnknownClientSignatureException e) {
					}
				}
			}
		} catch (IOException e) {
			if (e.toString().equals("java.io.IOException: Cannot allocate memory")) {
				throw new InsufficientMemoryException(e);
			}
			throw e;
		}
	}
}
