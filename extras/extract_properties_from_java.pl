#!/usr/bin/perl
# Script to retrieve property information from io.github.jmcleodfoss.pst files PropertyID.java, PropertyTag.java, and PropertyIDByGUID.java and
# format the output so that it can be combined with properties.csv
# The ultimate goal is to generate the property constants automatically from properties.csv, as is done by the msgreader project.
#
# Use
#	./extract_properties_from_java.pl | cat properties.csv - | sort -u > new-properties-file
#
# Do some basic validation on the output.
# Check lines with the same property tags:
#	sort -t, -k2 new-properties-file |grep -v n/a |sed "s/,/       /g" | uniq -D -f1 
#
# This was used to create the starting point for the ms-oxprops database, and uses files which are deprecated and will disappear.
# It is offered as a matter of historical interest only.

use strict;

# Store an array of values by the base property ID name. The arrays are the output (full name, value, type, type-valye, PSETID name (if any), PSET GUID (if any) 
my %rows;

my %propertyNames = (
	"BINARY" => "PtypBinary",
	"BOOLEAN" => "PtypBoolean",
	"CURRENCY" => "PtypCurrency",
	"ERROR_CODE" => "PtypErrorCode***",
	"FLOATING_TIME" => "PtypFloatingTime",
	"FLOATING_32" => "PtypFloating32",
	"FLOATING_64" => "PtypFloating64",
	"GUID" => "PtypGUID",
	"INTEGER_16" => "PtypInteger16",
	"INTEGER_32" => "PtypInteger32",
	"INTEGER_64" => "PtypInteger64",
	"MULTIPLE_BINARY" => "PtypMultipleBinary",
	"MULTIPLE_CURRENCY" => "PtypMultipleCurrency",
	"MULTIPLE_FLOATING_TIME" => "PtypMultipleFloatingTime",
	"MULTIPLE_INTEGER_16" => "PtypMultipleInteger16",
	"MULTIPLE_INTEGER_32" => "PtypMultipleInteger32",
	"MULTIPLE_INTEGER_64" => "PtypMultipleInteger64",
	"MULTIPLE_FLOATING_32" => "PtypMultipleFloating32",
	"MULTIPLE_FLOATING_64" => "PtypMultipleFloating64",
	"MULTIPLE_GUID" => "PtypMultipleGUID",
	"MULTIPLE_STRING" => "PtypMultipleString",
	"MULTIPLE_STRING_8" => "PtypMultipleString8",
	"MULTIPLE_TIME" => "PtypMultipleTime",
	"NULL" => "PtypNull***",
	"OBJECT" => "PtypObject",
	"RESTRICTION" => "PtypRestriction***",
	"RULE_ACTION" => "PtypeRuleAction***",
	"SERVER_ID" => "PtypServerID***",
	"STRING" => "PtypString",
	"STRING_8" => "PtypString8",
	"TIME" => "PtypTime",
	"UNSPECIFIED" => "PtypUnspecified"
);

my %psInfo = (
	"PS_INTERNAL" => "PS_Internal,C1843281-8505-D011-B290-00AA003CF676",
	"PSETID_ADDRESS" => "PSETID_Address,00062004-0000-0000-C000-000000000046",
	"PSETID_APPOINTMENT" => "PSETID_Appointment,00062002-0000-0000-C000-000000000046",
	"PSETID_COMMON" => "PSETID_Common,00062008-0000-0000-C000-000000000046",
	"PSETID_MEETING" => "PSETID_Meeting,6ED8DA90-450B-101B-98DA-00AA003F1305",
	"PSETID_NOTE" => "PSETID_Note,0006200E-0000-0000-C000-000000000046",
	"PSETID_TASK" => "PSETID_Task,00062003-0000-0000-C000-000000000046"
);

# Read in properties names and values
open(FH, "<", "../pst/src/main/java/io/github/jmcleodfoss/PropertyID.java") or die $!;
while(<FH>) {
	chomp;
	next if /Nameid/;
	next if /NameTo/;
	next if /NamedProperty/;
	/static final short ([^ ]*) = (\(short\))?0x([^;]*);/ and $rows{$1}[0] = $3;
}
close(FH);

# Read in the property types to map property type names to values
my %propertyTypes;
open(FH, "<", "../pst/src/main/java/io/github/jmcleodfoss/DataType.java") or die $!;
while(<FH>) {
	chomp;
	/static final short ([^ ]*) = (0x[^;]*);.*$/ and $propertyTypes{$1} = "$propertyNames{$1},$2";
}
close(FH);

# Read in the property tags to get the property types
open(FH, "<", "../pst/src/main/java/io/github/jmcleodfoss/PropertyTag.java") or die $!;
while(<FH>) {
	chomp;
	next if /Nameid/;
	if (/public static final int ([^ ]*) = makeTag\([^,]*, *DataType\.([^)]*)\);/) {
		exists $rows{$1} or $rows{$1}[0] = $1;
		push @{$rows{$1}}, $propertyTypes{$2};
	}
}
close(FH);

# Get the Property Sets
open(FH, "<", "../pst/src/main/java/io/github/jmcleodfoss/PropertyIDByGUID.java") or die $!;
while(<FH>) {
	chomp;
	/put\(PropertyID\.([^,]*). GUID\.([^,]*),/ and push @{$rows{$1}}, $psInfo{$2};
}
close(FH);

foreach my $key (keys %rows) {
	my $lookupKey = $key;
	$key =~ /^(.*)W$/ && $key !~ /ParentDisplayW/ && $key !~ /DisplayNameW/ and $lookupKey = $1;
	$key =~ /^(Home2TelephoneNumber)s$/ and $lookupKey = $1;
	$key =~ /^(Business2TelephoneNumber)s$/ and $lookupKey = $1;
	$key =~ /^(TelexNumber)Nspi$/ and $lookupKey = $1;
	$key =~ /^(ParentDisplay)$/ and $lookupKey = "$1W";
	$key =~ /^(DisplayName)$/ and $lookupKey = "$1W";

	# Each element of %rows is an array as follows:
	#	Tags: Property Value (hex), Property Type Name + Property Type Code
	#	Long IDs: Property Value (hex), Property Type Name + Property Type Code, Property Set Name + Property Set GUID
	# Use the differing array lengths to display the correct prefix and data
	if (scalar @{$rows{$lookupKey}} == 3) {
		my $tagName = $key;
		$key =~ /AppointmentLocation/ and $tagName = "Location";
		printf "PidLid%s,0x0000%s,%s,%s\n", $tagName, $rows{$lookupKey}[0], $rows{$key}[1], $rows{$lookupKey}[2];
	} else {
		printf "PidTag%s,0x%s,%s,,\n", $key, $rows{$lookupKey}[0], $rows{$key}[1];
	}
}
