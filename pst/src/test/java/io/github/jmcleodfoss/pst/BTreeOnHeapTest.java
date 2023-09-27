package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;

/** Test the io.github.jmcledofoss.pst.BTreeOnHeap class. */
public class BTreeOnHeapTest extends TestFrame
{
	/** Test the BTreeOnHeap class by creating a BTreeOnHeap object for each datablock which contains a heap-on-node structure.
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
	@Override
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
		BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
		NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

		java.util.Iterator<BTreeNode> iterator = nbt.iterator();
		while (iterator.hasNext()) {
			NBTEntry node = (NBTEntry)iterator.next();
			if (!node.nid.isHeapOnNodeNID())
				continue;

			BBTEntry dataBlock = bbt.find(node.bidData);
			if (dataBlock != null) {
				try {
					HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
					if (!hon.containsData() || (!hon.clientSignature().equals(ClientSignature.PropertyContext) && !hon.clientSignature().equals(ClientSignature.TableContext)))
						continue;
	
					new BTreeOnHeap(hon, pstFile);
				} catch (final	NotHeapNodeException
					|	UnknownClientSignatureException e) {
				}
			}
		}
		pstFile.close();
	}
}
