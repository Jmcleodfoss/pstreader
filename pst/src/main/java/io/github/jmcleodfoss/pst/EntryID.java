package io.github.jmcleodfoss.pst;

/**	The EntryID class contains a PST file Entry ID (ENTRYID structure).
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/office_file_formats/ms-pst/9378e8b9-7b6a-45bf-a51a-f21daf24d9ce">MS-PST Section 2.4.3.2: Mapping between EntryID and NID</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/MS-OXCDATA/57e8bcbf-11d0-40fe-8833-5558bb9c0c89">MS-OXCDATA Section 2.2: EntryID and Related Types</a>
*/
public class EntryID
	{
	/**	The node ID of the entry */
	public final NID nid;

	/**	Create an EntryID object from raw bytes.
	*	@param	rawData	The bytes from which to create the EntryID (a little-endian sequence of bytes).
	*/
	EntryID(byte[] rawData)
	{
		final int rawNID = PSTFile.makeByteBuffer(rawData, 20, 4).getInt();
		nid = new NID(rawNID);
	}

	/**	Obtain a string representation of this EntryID object (for debugging).
	*	@return	A description of this EntryID object.
	*/
	@Override
	public String toString()
	{
		return nid.toString();
	}
}
