package io.github.jmcleodfoss.pst;

/**	The LPTLeaf class is the base class for node and sub-node B-Tree leaf entries, both of which may be used to create table
*	contexts.
*/
public abstract class LPTLeaf implements BTreeLeaf
{
	/**	The data container holding the information read in. */
	protected DataContainer dc;

	/**	The node ID of this node in the B-tree. */
	public final NID nid;

	/**	The block ID of the data block for this node in the B-tree */
	public final BID bidData;

	/**	The block ID of the subnode B-tree for this node in the B-tree */
	public final BID bidSubnode;

	protected static final String nm_nid = "NID";
	protected static final String nm_nid_padding = "NID-padding";
	protected static final String nm_bidData = "bidData";
	protected static final String nm_bidSubnode = "bidSubnode";

	/**	Create the base object: read in the fields, and retrieve the data common to all derived classes.
	*	@param	byteBuffer	The input data stream from which to read the leaf object.
	*	@param	fields		The descriptions of the fields to be read.
	* 	@throws java.io.IOException	The PST file could not be read.
	*/
	protected LPTLeaf(java.nio.ByteBuffer byteBuffer, final DataDefinition[]... fields)
	throws
		java.io.IOException
	{
		dc = new DataContainer();
		dc.read(byteBuffer, fields);

		nid = (NID)dc.get(nm_nid);
		bidData = (BID)dc.get(nm_bidData);
		bidSubnode = (BID)dc.get(nm_bidSubnode);
	}

	/**	{@inheritDoc} */
	public javax.swing.table.TableModel getNodeTableModel()
	{
		final Object[] columnHeadings = {"", ""};
		final Object[][] cells = {
			new Object[]{"NID", nid},
			new Object[]{"Data BID", bidData},
			new Object[]{"Subnode BID", bidSubnode}
		};

		return new ReadOnlyTableModel(cells, columnHeadings);
	}

	/**	{@inheritDoc} */
	public String getNodeText()
	{
		return String.format("%s: 0x%08x", nid.description, nid.nid);
	}

	/**	Return the B-tree search key for this node.
	*	@return	The key for a B-tree search.
	*/
	public long key()
	{
		return nid.key();
	}

	/**	{@inheritDoc} */
	public java.nio.ByteBuffer rawData(final BlockMap bbt, PSTFile pstFile)
	throws
		java.io.IOException
	{
		final BBTEntry bbtEntry = bbt.find(bidData);
		if (bbtEntry == null)
			return null;

		final BlockBase block = BlockBase.read(bbtEntry, bbt, pstFile);
		if (block != null)
			return block.dataStream();

		return null;
	}

	/**	Provide a description of a node B-tree leaf node. This is typically used for debugging.
	*	@return	A String describing this object
	*/
	@Override
	public String toString()
	{
		return "NID " + nid + ", BID(data) " + bidData + ", BID(subnode) " + bidSubnode;
	}
}
