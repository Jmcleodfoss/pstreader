#!/bin/bash
declare -r sourcefile=~/work/ms-oxprops-db/releases/1.0.0/ms-oxprops-2020-05-25.csv
declare -r outfile=pst_meetings_to_html.xml
declare -r folder_name=Calendar
declare -r page_title=Calendar
declare -r message_class=IPM.Appointment
declare -r area=Meetings
declare -r version=190618
declare -r entry_title=ConversationTopicW

cat << END_HEADER > "$outfile"
<?xml version="1.0" encoding="UTF-8"?>
<!-- Extract the contents of the $folder_name folder from an XML file representing a PST file as an HTML file. -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes" encoding="UTF-8"/>

	<xsl:template match="/pst">
		<html>
		<body>
		<h1>$page_title</h1>
			<xsl:apply-templates select="//folder/object[MessageClassW = '$message_class']"/>
		</body>
		</html>
	</xsl:template>

	<xsl:template match="//folder/object[MessageClassW = '$message_class']">
		<h2>
			<xsl:if test="$entry_title"><xsl:value-of select="$entry_title"/></xsl:if>
		</h2>
		<ul>
		<xsl:for-each select="./*">
			<xsl:choose>
END_HEADER

cat "$sourcefile" | grep "$version$" | grep -e "PidTagSubject," -e "$area" | cut "-d," -f 1 | cut '-d"' -f 2 |sed "/^Pid...\(.*\)$/s//\t\t\t\t<xsl:when test=\"name() = \'\1\'\"><li><xsl:value-of select=\"name()\"\/>: <xsl:value-of select=\".\"\/><\/li><\/xsl:when>/" >> "$outfile"

cat << END_FOOTER >> "$outfile"
			</xsl:choose>
		</xsl:for-each>
		</ul>
	</xsl:template>

</xsl:stylesheet>
END_FOOTER
