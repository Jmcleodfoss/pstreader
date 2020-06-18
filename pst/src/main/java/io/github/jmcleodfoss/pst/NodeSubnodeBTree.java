package io.github.jmcleodfoss.pst;

/**	The NodeSubnodeBtree provides a B-Tree containing all nodes and subnodes. Note that this requires more information to create
*	than the node B-Tree without subnodes.
*/
public class NodeSubnodeBTree extends NodeBTree
{
	/**	The Context class includes information about the node/sub-node B-Tree. This holds information required to build the
	*	B-tree but which doesn't need to be saved in it.
	*/
	private static class NSNContext extends NBTContextBase<BTree, BTreeLeaf>
	{
		/**	The block B-Tree for this PST file. */
		final BlockMap bbt;

		/**	Create the context object for building the node/sub-node B-tree.
		*	@param	bref	The block reference for the node/sub-node node currently under construction.
		*	@param	bbt	The PS file's block B-tree.
		*	@param	pstFile	The PST file's data stream, header, etc.
		*	@throws	java.io.IOException	The data for the node sub-node B-tree context could not be read.
		*/
		protected NSNContext(final BREF bref, final BlockMap bbt, PSTFile pstFile)
		throws
			java.io.IOException
		{
			super(bref, pstFile);
			this.bbt = bbt;
		}

		/**	{@inheritDoc} */
		protected NodeSubnodeBTree intermediateNodeFactory(java.nio.ByteBuffer byteBuffer)
		throws
			java.io.IOException
		{
			final BTEntry entry = new BTEntry(this, byteBuffer);
			return new NodeSubnodeBTree(entry.key, entry.bref, bbt, pstFile);
		}

		/**	{@inheritDoc} */
		protected NodeLeafEntry leafNodeFactory(java.nio.ByteBuffer entryStream)
		throws
			java.io.IOException
		{
			return new NodeLeafEntry(this, entryStream);
		}
	}

	/**	The "NodeLeafEntry" class is a leaf entry of the node B-Tree with its subnode B-Tree. */
	private static class NodeLeafEntry extends NBTEntry
	{
		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	The subnode B-Tree. (Note that these are typically small, consisting of:
		*	<ul>
		*	<li>1 recipient table
		*	<li>0 or 1 attachment tables
		*	<li>1 attachment PC for each attachment
		*	<li>a small number of LPT PCs holding additional message data
		*	</ul>
		*	Note also that the data is not stored in the subnode B-Tree; only the location of the data is stored.
		*/
		final SubnodeBTree snb;

		/**	List of the subnode leaf nodes. */
		final java.util.Vector<SLEntry> leafNodes;

		/**	Create a node B-Tree leaf entry with the subnode B-Tree.
		*	@param	context		The context from which to build this node.
		*	@param	byteBuffer	The data stream from which to read this node.
		*	@throws	java.io.IOException	The data for the leaf node could not be read.
		*/
		protected NodeLeafEntry(final NSNContext context, java.nio.ByteBuffer byteBuffer)
		throws
			java.io.IOException
		{
			super(byteBuffer, context);
			if (bidSubnode.isNull()) {
				snb = null;
				leafNodes = new java.util.Vector<SLEntry>(0);
			} else {
				snb = new SubnodeBTree(bidSubnode, context.bbt, context.pstFile);
				leafNodes = new java.util.Vector<SLEntry>();
				for (java.util.Iterator<BTreeNode> iter = snb.iterator(); iter.hasNext(); ) {
					final SLEntry slEntry = (SLEntry)iter.next();
					leafNodes.add(slEntry);
				}
			}
		}
	}

	/**	Create a node &amp; subnode B-Tree from the given key, block reference, block BTree, and PSTfile.
	*	@param	key	The key for this node. All sub-nodes are guaranteed to have keys greater than or equal to this key.
	*	@param	bref	The block reference for this node.
	*	@param	bbt	The PST file's block B-tree.
	*	@param	pstFile	The PST file's data stream, etc.
	*	@throws java.io.IOException	There was a problem reading the sub-node B-tree.
	*/
	NodeSubnodeBTree(final long key, final BREF bref, final BlockMap bbt, PSTFile pstFile)
	throws
		java.io.IOException
	{
		super(key, bref, new NSNContext(bref, bbt, pstFile));
	}

	/**	{@inheritDoc} */
	@Override
	public Object getChild(final Object parent, int index)
	{
		final int numNBTChildren = super.getChildCount(parent);
		if (index < numNBTChildren)
			return super.getChild(parent, index);
		index -= numNBTChildren;

		return ((NodeLeafEntry)(parent)).leafNodes.get(index);
	}

	/**	{@inheritDoc} */
	@Override
	public int getChildCount(final Object parent)
	{
		if (parent instanceof BTree) {
			final BTree bTree = (BTree)parent;
			if (bTree.children != null)
				return bTree.children.length;
		}

		return ((NodeLeafEntry)(parent)).leafNodes.size();
	}

	/**	{@inheritDoc} */
	@Override
	public String getNodeText(final Object value)
	{
		if (value instanceof SLEntry)
			return ((SLEntry)value).toString();
		return super.getNodeText(value);
	}

	/**	{@inheritDoc} */
	@Override
	public boolean isLeaf(final Object node)
	{
		if (!super.isLeaf(node))
			return false;

		if (node instanceof NodeLeafEntry)
			return ((NodeLeafEntry)node).leafNodes.size() == 0;

		if (node instanceof SLEntry)
			return true;

		return false;
	}
}
