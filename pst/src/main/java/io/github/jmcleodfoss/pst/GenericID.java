package io.github.jmcleodfoss.pst;

/**	The GenericID class represents a Folder ID or MessageID
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/MS-OXCDATA/1c934e18-441b-4c47-9de0-eb34ffea47e3">2.2.1.1 Folder ID Structure</a>
*	@see	<a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/MS-OXCDATA/f1004d6b-b314-41a8-83cb-c64c3dbeebc4">2.2.1.2 Message ID Structure</a>
*/
public class GenericID
{
	private int replicaId; // 2 bytes 
	private long globalCounter; // 6 bytes

	GenericID(int replicaId, long globalCounter)
	{
		this.replicaId = replicaId;
		this.globalCounter = globalCounter;
	}

	@Override
	public String toString()
	{
		return String.format("Replica ID: %d Global Counter %d", replicaId, globalCounter);
	}
}
