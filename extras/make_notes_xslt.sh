#!/bin/bash
declare -r sourcefile=~/work/outlook-files/ms-oxprops-db/releases/1.0.0/ms-oxprops-2020-05-25.csv
declare -r outfile=pst_notes_to_html.xml
declare -r page_title=Calendar
declare -r message_class=IPM.Note
declare -r area1="Message Properties"
declare -r area2="Address Properties"
declare -r version=190618
declare -r entry_title=SubjectW

cat << END_HEADER > "$outfile"
<?xml version="1.0" encoding="UTF-8"?>
<!-- Extract the contents of the messages with message class $message_class from an XML file representing a PST file as an HTML file. -->

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
			<xsl:if test="$entry_title">
				<xsl:if test="starts-with(./$entry_title,'&amp;#01;&amp;#01;')"><xsl:value-of select="substring-after(./$entry_title,'&amp;#01;&amp;#01;')"/></xsl:if>
				<xsl:if test="not(starts-with(./$entry_title,'&amp;'))"><xsl:value-of select="$entry_title"/></xsl:if>
			</xsl:if>
		</h2>
		<ul>
		<xsl:for-each select="./*">
			<xsl:choose>
END_HEADER

grep "$version\$" "$sourcefile" | grep -e "$area1" -e "$area2" | cut "-d," -f 1 | cut '-d"' -f 2 |sed "/^Pid...\(.*\)$/s//\t\t\t\t<xsl:when test=\"name() = \'\1\'\"><li><xsl:value-of select=\"name()\"\/>: <xsl:value-of select=\".\"\/><\/li><\/xsl:when>/" >> "$outfile"

cat << END_FOOTER >> "$outfile"
			</xsl:choose>
		</xsl:for-each>
		</ul>
	</xsl:template>

</xsl:stylesheet>
END_FOOTER
