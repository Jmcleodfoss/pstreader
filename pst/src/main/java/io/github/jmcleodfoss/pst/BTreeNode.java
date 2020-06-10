package io.github.jmcleodfoss.pst;

/**	The BTreeNode interface defines the functions required for intermediate and leaf B-tree nodes.
*	@see	BTree
*	@see	BTreeLeaf
*/
public interface BTreeNode
{
	/**	The actualSize function must returns the actual number of bytes read from the input stream to populate this node.
	*	This is used with the defined size given in the B-tree metadata (if present) to determine how many bytes must be
	*	skipped between entries.
	*	@param	context	The context (PST object and other information) from which to retrieve the size.
	*	@return	The size of the entry, including the number of bytes skipped between entries.
	*/
	int actualSize(BTree.Context<BTree, BTreeLeaf> context);

	/**	Get a table model which can be used to describe this node.
	*	@return	A TableModel describing this node.
	*/
	javax.swing.table.TableModel getNodeTableModel();

	/**	Retrieve text for this node.
	*	@return	A String which may be used to describe the node, typically within a JTree display for the B-tree.
	*/
	String getNodeText();

	/**	Obtain the lookup key for this node.
	*	@return	The key for this node. This is guaranteed to be the smallest key of all leaf nodes descended from this node.
	*/
	long key();

	/**	Provide a mechanism to read the data from the node via a ByteBuffer.
	*	@param	bbt	The PST file's block B-tree
	*	@param	pstFile	The PST file's input data stream, header, etc.
	*	@return	A ByteBuffer containing the data for this leaf node of a B-tree.
	*	@throws java.io.IOException	The PST file could not be read.
	*/
	public java.nio.ByteBuffer rawData(final BlockMap bbt, final PSTFile pstFile)
	throws
		java.io.IOException;
}
