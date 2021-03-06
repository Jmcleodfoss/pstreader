#!/bin/bash
# shellcheck disable=SC1004
# Process properties.csv to get property LIDs
cat << END_HEADER > PropertyLIDs.java
// DO NOT EDIT THIS FILE
// Automatically generated on $(date) by pstreader/extras/getpropertylids.sh
// Any changes must be made to that file.
package io.github.jmcleodfoss.pst;

/** Known Property Long IDs
*	@see <a href="https://github.com/Jmcleodfoss/pstreader/blob/master/extras/properties.csv">pstreader properties.pst</a>
*	@see <a href="https://github.com/Jmcleodfoss/msgreader/blob/master/extras/getpropertylids.sh">getpropertylids.sh</a>
*	@see <a href="https://docs.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxprops/f6ab1613-aefe-447d-a49c-18217230b148">MS-OXPROPS</a>
*/

@SuppressWarnings("PMD.ExcessiveClassLength")
public class PropertyLIDs
{
	// Sentinel value which can be used for property LIDs which are uninitialized and have to be looked up as Named Properties.
	static final int UNKNOWN = 0xffffffff;

END_HEADER
sort -t , -k 2 properties.csv | sed '
	${
		i\

		i\
	static final java.util.HashMap<Integer, String> lids = new java.util.HashMap<Integer, String>();
		i\
	static final java.util.HashMap<Integer, GUID> guids = new java.util.HashMap<Integer, GUID>();
		i\
	static {
		g
		a\
	}
	}

	/\([^.]\)PS_PUBLIC_STRINGS/s//\1GUID.PS_PUBLIC_STRINGS/
	/PSETID_Address/s//GUID.PSETID_ADDRESS/
	/PSETID_Appointment/s//GUID.PSETID_APPOINTMENT/
	/PSETID_CalendarAssistant/s//GUID.PSETID_CALENDAR_ASSISTANT/
	/PSETID_Common/s//GUID.PSETID_COMMON/
	/PS_Internal/s//GUID.PS_INTERNAL/
	/PSETID_Log/s//GUID.PSETID_LOG/
	/PSETID_Meeting/s//GUID.PSETID_MEETING/
	/PSETID_Note/s//GUID.PSETID_NOTE/
	/PSETID_PostRss/s//GUID.PSETID_POST_RSS/
	/PSETID_Sharing/s//GUID.PSETID_SHARING/
	/PSETID_Task/s//GUID.PSETID_TASK/

	# This LID is listed in versions 1.04, 1.05, and 1.06 of the OX-PROPS document and nothing later. Given its unusual definition
	# (value "17" when all other values are 4-byte hex values, and PSETID "GUID_PKMDocDummaryInformation" which is not used anywhere else,
	# we ll ignore it for now.
	/PidLidPKMDocSummaryInformation17/d

	/^PidLid\([^,]*\),0x\([^,]*\),\([^,]*\),0x\([^,]*\),\([^,]*\),\(.*\)$/{
		s//\	public static final int \1 = 0x\2;\
\	\	lids.put(\1, "\1");\
\	\	guids.put(\1, \5);/
		P
		/=/s/^[^\n]*\n\(.*\)$/\1/
		H
		d
	}

	/n\/a/d

	/^PidTag/d
	' >> PropertyLIDs.java

cat << END_FOOTER >> PropertyLIDs.java

	static String name(int lid, GUID guid)
	{
		if (lids.containsKey(lid) && guids.containsKey(lid) && guid.equals(guids.get(lid)))
			return PropertyLIDs.lids.get(lid);
		return String.format("LID-%08x (%s)", lid, GUID.name(guid));
	}

	public static void main(String[] args)
	{
		java.util.Iterator<java.util.Map.Entry<Integer, String>> iter = PropertyLIDs.lids.entrySet().iterator();
		while (iter.hasNext()) {
			java.util.Map.Entry<Integer, String> e = iter.next();
			System.out.printf("0x%08x: %s%n", e.getKey(), e.getValue());
		}
	}
}
END_FOOTER
