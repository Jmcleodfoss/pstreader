package io.github.jmcleodfoss.pst;

/**	The ServerID class represents a Server ID.
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/MS-OXCDATA/83c2c837-f718-4c24-83cd-c935070cc6c7">2.11.1.4: PtypServerId Type</a>
*/
public class ServerID
{
	private boolean ours;
	private byte[] rawData;
	private GenericID folderId;
	private GenericID messageId;
	private int instance;

	ServerID(byte[] rawData)
	{
		this.ours = false;
		this.rawData = rawData;
		this.folderId = new GenericID(-1,01);
		this.messageId = new GenericID(-1,01);
	}
	ServerID(GenericID folderId, GenericID messageId, int instance)
	{
		this.ours = true;
		this.rawData = new byte[0];
		this.folderId = folderId;
		this.messageId = messageId;
		this.instance = instance;
	}

	@Override
	public String toString()
	{
		if (ours)
			return String.format("folderId {%s}. message Id {%s} instance %d", folderId.toString(), messageId.toString(), instance);
		else
			return ByteUtil.createHexByteString(rawData);
	}
}
