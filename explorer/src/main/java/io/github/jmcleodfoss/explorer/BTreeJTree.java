package io.github.jmcleodfoss.explorer;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import java.awt.event.MouseListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import io.github.jmcleodfoss.pst.TreeCustomNodeText;
import io.github.jmcleodfoss.swingutil.EmptyTreeModel;

/**	The BTreeJTree provides special handling of JTrees which live in a JScrollPane and use BTree objectss for the tree model.
*	This class provides some wrappers to allow the underlying tree to be manipulated by client code.
*/
@SuppressWarnings("serial")
class BTreeJTree extends JScrollPane
{
	/**	The Tree class is a JTree which gets node names from a io.github.jmcleodfoss.pst.TreeCustomNode object. */
	static private class Tree extends JTree
	{
		/**	Construct a JTree with an EmptyTreeModel. */
		private Tree()
		{
			super(EmptyTreeModel.model);
		}

		/**	Get node text - the PST BTrees use toString for purposes inconsistent with BTree nodes, so this function uses
		*	the interface provided by io.github.jmcleodfoss.pst.TreeCustomNodeText to get the node label text.
		*	@param	value		The node of the tree.
		*	@param	selected	A flag indicating whether the node is selected.
		*	@param	expanded	A flag indicating whether the node is expanded.
		*	@param	leaf		A flag indicating whether the node is a leaf node.
		*	@param	row		The row of the tree in the display.
		*	@param	hasFocus	A flag indicating whether the node has keyboard focus.
		*	@return	A string which may be used as the node label on the JTree.
		*/
		public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			javax.swing.tree.TreeModel tm = getModel();
			if (tm instanceof TreeCustomNodeText)
				return ((TreeCustomNodeText)tm).getNodeText(value);
			return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}
	}

	/**	The actual tree display. */
	private Tree tree;

	/**	Construct a BTreeJTree object, which contains horizontal and vertical scrollbars if necessary, and a Tree object. */
	BTreeJTree()
	{
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tree = new Tree();
		getViewport().add(tree);

		TreeSelectionModel treeSelectionModel = tree.getSelectionModel();
		treeSelectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**	Add a MouseListener to the underlying tree.
	*	@param	listener	The MouseListener to add to the tree.
	*/
	public void addMouseListener(MouseListener listener)
	{
		tree.addMouseListener(listener);
	}

	/**	Add a TreeExpansionListener to the underlying tree.
	*	@param	listener	The TreeExpansionListener to add to the tree.
	*/
	void addTreeExpansionListener(TreeExpansionListener listener)
	{
		tree.addTreeExpansionListener(listener);
	}

	/**	Add a TreeSelectionListener to the underlying tree.
	*	@param	listener	The TreeSelectionListener to add to the tree.
	*/
	void addTreeSelectionListener(TreeSelectionListener listener)
	{
		tree.addTreeSelectionListener(listener);
	}

	/**	Add a TreeWillExpandListener to the underlying tree.
	*	@param	listener	The TreeWillExpandListener to add to the tree.
	*/
	void addTreeWillExpandListener(TreeWillExpandListener listener)
	{
		tree.addTreeWillExpandListener(listener);
	}

	/**	Set the model for the underlying tree.
	*	@param	tm	The new TreeModel for the underlying tree.
	*/
	void setModel(TreeModel tm)
	{
		tree.setModel(tm == null ? EmptyTreeModel.model : tm);
	}
}
