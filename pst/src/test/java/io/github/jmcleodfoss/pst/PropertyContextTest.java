package io.github.jmcleodfoss.pst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.lang.reflect.InvocationTargetException;
import junit.extensions.PrivilegedAccessor;
import org.junit.Test;

/** TBD */
public class PropertyContextTest extends TestFrame
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
		try {
			PSTFile pstFile = new PSTFile(new java.io.FileInputStream(file));
			final BlockBTree bbt = new BlockBTree(0, pstFile.header.bbtRoot, pstFile);
			final NodeBTree nbt = new NodeBTree(0, pstFile.header.nbtRoot, pstFile);
			final NameToIDMap namedProperties = new NameToIDMap(bbt, nbt, pstFile);

			java.util.Iterator<BTreeNode> iterator = nbt.iterator();
			while (iterator.hasNext()) {
				final NBTEntry node = (NBTEntry)iterator.next();
				if (node.nid.type == NID.INTERNAL)
					continue;

				final BBTEntry dataBlock = bbt.find(node.bidData);
				if (dataBlock == null)
					continue;

				// We can continue processing if any of the exceptions below are caught.
				try {
					// Check for valid property context. We expect to encounter quite a few non-PC blocks, so this is completely benign.
					HeapOnNode hon = new HeapOnNode(dataBlock, bbt, pstFile);
					if (!hon.clientSignature().equals(ClientSignature.PropertyContext))
						continue;

					new PropertyContext(node, bbt, pstFile);
				} catch (final NotHeapNodeException e) {
				} catch (final NullDataBlockException e) {
				} catch (final UnknownClientSignatureException e) {
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
