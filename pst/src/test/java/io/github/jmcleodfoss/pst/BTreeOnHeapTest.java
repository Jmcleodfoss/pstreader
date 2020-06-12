package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.lang.reflect.InvocationTargetException;

/** Test the io.github.jmcledofoss.pst.BTreeOnHeap class. */
public class BTreeOnHeapTest extends TestFrame
{
	/** Test the BTreeOnHeap class by creating a BTreeOnHeap object for each datablock which contains a heap-on-node structure.
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
		PSTFile pstFile = new PSTFile(new java.io.FileInputStream(file));
		BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
		NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);

		java.util.Iterator iterator = nbt.iterator();
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
				} catch (NotHeapNodeException e) {
				} catch (UnknownClientSignatureException e) {
				}
			}
		}
	}
}
