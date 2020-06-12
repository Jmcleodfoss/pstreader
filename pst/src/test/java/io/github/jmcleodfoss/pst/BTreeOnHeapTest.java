package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.lang.reflect.InvocationTargetException;

/** TBD */
public class BTreeOnHeapTest extends TestFrame
{
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
