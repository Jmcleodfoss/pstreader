package io.github.jmcleodfoss.pst;

/**	The PropertyIDByGUID class contains the names of known property IDs which are stored by GUID in the
*	{@link NameToIDMap Named Properties Store.}
*
*	@see	io.github.jmcleodfoss.pst.PropertyID
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">[MS-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*/
class PropertyIDByGUID {

	/**	The StringByGUID class holds the mapping of GUIDs to property names for a given property ID. Note that the
	*	mapping is by String instead of by GUID, to allow look-up by GUIDs read in from the Name/ID map.
	*/
	static class StringByGUID extends java.util.HashMap<String, String> {

		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;
	}

	/**	The list of property names stored by index and GUID.	*/
	static final java.util.HashMap<Short, StringByGUID> names = new java.util.HashMap<Short, StringByGUID>();
	static {
		put(PropertyID.FileUnderId, GUID.PS_ADDRESS, "FileUnderID");
		put(PropertyID.ContactItemData, GUID.PS_ADDRESS, "ContactItemData");
		put(PropertyID.ReferredBy, GUID.PS_ADDRESS, "ReferredBy");
		put(PropertyID.Department, GUID.PS_ADDRESS, "Department");
		put(PropertyID.HasPicture, GUID.PS_ADDRESS, "HasPicture");
		put(PropertyID.HomeAddress, GUID.PS_ADDRESS, "HomeAddress");
		put(PropertyID.WorkAddress, GUID.PS_ADDRESS, "WorkAddress");
		put(PropertyID.OtherAddress, GUID.PS_ADDRESS, "OtherAddress");
		put(PropertyID.PostalAddressId, GUID.PS_ADDRESS, "PostalAddressId");
		put(PropertyID.ContactCharacterSet, GUID.PS_ADDRESS, "ContactCharacterSet");
		put(PropertyID.AutoLog, GUID.PS_ADDRESS, "AutoLog");
		put(PropertyID.FileUnderList, GUID.PS_ADDRESS, "FileUnderList");
		put(PropertyID.EmailList, GUID.PS_ADDRESS, "EmailList");
		put(PropertyID.AddressBookProviderEmailList, GUID.PS_ADDRESS, "AddressBookProviderEmailList");
		put(PropertyID.AddressBookProviderArrayType, GUID.PS_ADDRESS, "AddressBookProviderArrayType");
		put(PropertyID.Html, GUID.PS_ADDRESS, "Html");
		put(PropertyID.YomiFirstName, GUID.PS_ADDRESS, "YomiFirstName");
		put(PropertyID.YomiLastName, GUID.PS_ADDRESS, "YomiLastName");
		put(PropertyID.YomiCompanyName, GUID.PS_ADDRESS, "YomiCompanyName");
		put(PropertyID.WorkAddressStreet, GUID.PS_ADDRESS, "WorkAddressStreet");
		put(PropertyID.WorkAddressCity, GUID.PS_ADDRESS, "WorkAddressCity");
		put(PropertyID.WorkAddressState, GUID.PS_ADDRESS, "WorkAddressState");
		put(PropertyID.WorkAddressPostalCode, GUID.PS_ADDRESS, "WorkAddressPostalCode");
		put(PropertyID.WorkAddressCountry, GUID.PS_ADDRESS, "WorkAddressCountry");
		put(PropertyID.WorkAddressPostOfficeBox, GUID.PS_ADDRESS, "WorkAddressPostOfficeBox");
		put(PropertyID.DistributionListChecksum, GUID.PS_ADDRESS, "DistributionListChecksum");
		put(PropertyID.ContactUserField1, GUID.PS_ADDRESS, "ContactUserField1");
		put(PropertyID.ContactUserField2, GUID.PS_ADDRESS, "ContactUserField2");
		put(PropertyID.ContactUserField3, GUID.PS_ADDRESS, "ContactUserField3");
		put(PropertyID.ContactUserField4, GUID.PS_ADDRESS, "ContactUserField4");
		put(PropertyID.DistributionListName, GUID.PS_ADDRESS, "DistributionListName");
		put(PropertyID.DistributionListOneOffMembers, GUID.PS_ADDRESS, "DistributionListOneOffMembers");
		put(PropertyID.DistributionListMembers, GUID.PS_ADDRESS, "DistributionListMembers");
		put(PropertyID.InstantMessagingAddress, GUID.PS_ADDRESS, "InstantMessagingAddress");
		put(PropertyID.Email1DisplayName, GUID.PS_ADDRESS, "Email1DisplayName");
		put(PropertyID.Email1AddressType, GUID.PS_ADDRESS, "Email1AddressType");
		put(PropertyID.Email1EmailAddress, GUID.PS_ADDRESS, "Email1EmailAddress");
		put(PropertyID.Email1OriginalDisplayName, GUID.PS_ADDRESS, "Email1OriginalDisplayName");
		put(PropertyID.Email1OriginalEntryId, GUID.PS_ADDRESS, "Email1OriginalEntryId");
		put(PropertyID.Email1RichTextFormat, GUID.PS_ADDRESS, "Email1RichTextFormat");
		put(PropertyID.Email2DisplayName, GUID.PS_ADDRESS, "Email2DisplayName");
		put(PropertyID.Email2AddressType, GUID.PS_ADDRESS, "Email2AddressType");
		put(PropertyID.Email2EmailAddress, GUID.PS_ADDRESS, "Email12EmailAddress");
		put(PropertyID.Email2OriginalDisplayName, GUID.PS_ADDRESS, "Email2OriginalDisplayName");
		put(PropertyID.Email2OriginalEntryId, GUID.PS_ADDRESS, "Email12OriginalEntryId");
		put(PropertyID.Email2RichTextFormat, GUID.PS_ADDRESS, "Email2RichTextFormat");
		put(PropertyID.Email3DisplayName, GUID.PS_ADDRESS, "Email3DisplayName");
		put(PropertyID.Email3AddressType, GUID.PS_ADDRESS, "Email3AddressType");
		put(PropertyID.Email3EmailAddress, GUID.PS_ADDRESS, "Email13EmailAddress");
		put(PropertyID.Email3OriginalEntryId, GUID.PS_ADDRESS, "Email3OriginalEntryId");
		put(PropertyID.Email3OriginalDisplayName, GUID.PS_ADDRESS, "Email3OriginalDisplayName");
		put(PropertyID.Email3RichTextFormat, GUID.PS_ADDRESS, "Email3RichTextFormat");
		put(PropertyID.Fax1AddressType, GUID.PS_ADDRESS, "Fax1AddressType");
		put(PropertyID.Fax1EmailAddress, GUID.PS_ADDRESS, "Fax1EmailAddress");
		put(PropertyID.Fax1OriginalDisplayName, GUID.PS_ADDRESS, "Fax1OriginalDisplayName");
		put(PropertyID.Fax1OriginalEntryId, GUID.PS_ADDRESS, "Fax1OriginalEntryId");
		put(PropertyID.Fax2AddressType, GUID.PS_ADDRESS, "Fax2AddressType");
		put(PropertyID.Fax2EmailAddress, GUID.PS_ADDRESS, "Fax2EmailAddress");
		put(PropertyID.Fax2OriginalDisplayName, GUID.PS_ADDRESS, "Fax2OriginalDisplayName");
		put(PropertyID.Fax2OriginalEntryId, GUID.PS_ADDRESS, "Fax2OriginalEntryId");
		put(PropertyID.Fax3AddressType, GUID.PS_ADDRESS, "Fax3AddressType");
		put(PropertyID.Fax3EmailAddress, GUID.PS_ADDRESS, "Fax3EmailAddress");
		put(PropertyID.Fax3OriginalDisplayName, GUID.PS_ADDRESS, "Fax3OriginalDisplayName");
		put(PropertyID.Fax3OriginalEntryId, GUID.PS_ADDRESS, "Fax3OriginalEntryId");
		put(PropertyID.FreeBusyLocation, GUID.PS_ADDRESS, "FreeBusyLocation");
		put(PropertyID.HomeAddressCountryCode, GUID.PS_ADDRESS, "HomeAddressCountryCode");
		put(PropertyID.WorkAddressCountryCode, GUID.PS_ADDRESS, "WorkAddressCountryCode");
		put(PropertyID.OtherAddressCountryCode, GUID.PS_ADDRESS, "OtherAddressCountryCode");
		put(PropertyID.AddressCountryCode, GUID.PS_ADDRESS, "AddressCountryCode");

		put(PropertyID.AddressBookHomeMessageDatabase, GUID.PS_INTERNAL, "AddressBookHomeMessageDatabase");
		put(PropertyID.AddressBookHomeMessageTransferAgent, GUID.PS_INTERNAL, "AddressBookHomeMessageTransferAgent");
		put(PropertyID.TcvConstLongOne, GUID.PS_INTERNAL, "TcvConstLongOne");
		put(PropertyID.AddressBookMember, GUID.PS_INTERNAL, "AddressBookMember");

		put(PropertyID.TaskStatus, GUID.PS_TASK, "TaskStatus");
		put(PropertyID.PercentComplete, GUID.PS_TASK, "PercentComplete");
		put(PropertyID.TeamTask , GUID.PS_TASK, "TeamTask ");
		put(PropertyID.TaskStartDate, GUID.PS_TASK, "TaskStartDate");
		put(PropertyID.TaskDueDate, GUID.PS_TASK, "TaskDueDate");
		put(PropertyID.TaskResetReminder, GUID.PS_TASK, "TaskResetReminder");
		put(PropertyID.TaskAccepted, GUID.PS_TASK, "TaskAccepted");
		put(PropertyID.TaskDeadOccurrence, GUID.PS_TASK, "TaskDeadOccurrence");
		put(PropertyID.TaskDateCompleted, GUID.PS_TASK, "TaskDateCompleted");
		put(PropertyID.TaskActualEffort, GUID.PS_TASK, "TaskActualEffort");
		put(PropertyID.TaskEstimatedEffort, GUID.PS_TASK, "TaskEstimatedEffort");
		put(PropertyID.TaskVersion, GUID.PS_TASK, "TaskVersion");
		put(PropertyID.TaskState, GUID.PS_TASK, "TaskState");
		put(PropertyID.TaskLastUpdate, GUID.PS_TASK, "TaskLastUpdate");
		put(PropertyID.TaskRecurrence, GUID.PS_TASK, "TaskRecurrence");
		put(PropertyID.TaskAssigners, GUID.PS_TASK, "TaskAssigners");
		put(PropertyID.TaskStatusOnComplete, GUID.PS_TASK, "TaskStatusOnComplete");
		put(PropertyID.TaskHistory, GUID.PS_TASK, "TaskHistory");
		put(PropertyID.TaskUpdates, GUID.PS_TASK, "TaskUpdates");
		put(PropertyID.TaskComplete, GUID.PS_TASK, "TaskComplete");
		put(PropertyID.TaskFCreator, GUID.PS_TASK, "TaskFCreator");
		put(PropertyID.TaskOwner, GUID.PS_TASK, "TaskOwner");
		put(PropertyID.TaskMultipleRecipients, GUID.PS_TASK, "TaskMultipleRecipients");
		put(PropertyID.TaskAssigner, GUID.PS_TASK, "TaskAssigner");
		put(PropertyID.TaskLastUser, GUID.PS_TASK, "TaskLastUser");
		put(PropertyID.TaskOrdinal, GUID.PS_TASK, "TaskOrdinal");
		put(PropertyID.TaskNoCompute, GUID.PS_TASK, "TaskNoCompute");
		put(PropertyID.TaskLastDelegate, GUID.PS_TASK, "TaskLastDelegate");
		put(PropertyID.TaskFRecurring, GUID.PS_TASK, "TaskFRecurring");
		put(PropertyID.TaskRole, GUID.PS_TASK, "TaskRole");
		put(PropertyID.TaskOwnership, GUID.PS_TASK, "TaskOwnership");
		put(PropertyID.TaskAcceptanceState, GUID.PS_TASK, "TaskAcceptanceState");
		put(PropertyID.TaskFFixOffline, GUID.PS_TASK, "TaskFFixOffline");

		put(PropertyID.AppointmentSequence, GUID.PS_APPOINTMENT, "AppointmentSequence");
		put(PropertyID.AppointmentSequenceTime, GUID.PS_APPOINTMENT, "AppointmentSequenceTime");
		put(PropertyID.AppointmentLastSequence, GUID.PS_APPOINTMENT, "AppointmentLastSequence");
		put(PropertyID.ChangeHighlight, GUID.PS_APPOINTMENT, "ChangeHighlight");
		put(PropertyID.BusyStatus, GUID.PS_APPOINTMENT, "BusyStatus");
		put(PropertyID.FExceptionalBody, GUID.PS_APPOINTMENT, "FExceptionalBody");
		put(PropertyID.AppointmentAuxiliaryFlags, GUID.PS_APPOINTMENT, "AppointmentAuxiliaryFlags");
		put(PropertyID.AppointmentLocation, GUID.PS_APPOINTMENT, "Location");
		put(PropertyID.ForwardInstance, GUID.PS_APPOINTMENT, "ForwardInstance");
		put(PropertyID.MeetingWorkspaceURL, GUID.PS_APPOINTMENT, "MeetingWorkspaceURL");
		put(PropertyID.AppointmentStartWhole, GUID.PS_APPOINTMENT, "AppointmentStartWhole");
		put(PropertyID.AppointmentEndWhole, GUID.PS_APPOINTMENT, "AppointmentEndWhole");
		put(PropertyID.AppointmentStartTime, GUID.PS_APPOINTMENT, "AppointmentStartTime");
		put(PropertyID.AppointmentEndType, GUID.PS_APPOINTMENT, "AppointmentEndType");
		put(PropertyID.AppointmentEndDate, GUID.PS_APPOINTMENT, "AppointmentEndDate");
		put(PropertyID.AppointmentStartDate, GUID.PS_APPOINTMENT, "AppointmentStartDate");
		put(PropertyID.AppointmentDuration, GUID.PS_APPOINTMENT, "AppointmentDuration");
		put(PropertyID.AppointmentColor, GUID.PS_APPOINTMENT, "AppointmentColor");
		put(PropertyID.AppointmentSubType, GUID.PS_APPOINTMENT, "AppointmentSubType");
		put(PropertyID.AppointmentRecur, GUID.PS_APPOINTMENT, "AppointmentRecur");
		put(PropertyID.AppointmentStateFlags, GUID.PS_APPOINTMENT, "AppointmentStateFlags");
		put(PropertyID.ResponseStatus, GUID.PS_APPOINTMENT, "ResponseStatus");
		put(PropertyID.AppointmentReplyTime, GUID.PS_APPOINTMENT, "AppointmentReplyTime");
		put(PropertyID.Recurring, GUID.PS_APPOINTMENT, "Recurring");
		put(PropertyID.IntendedBusyStatus, GUID.PS_APPOINTMENT, "IntendedBusyStatus");
		put(PropertyID.AppointmentUpdateTime, GUID.PS_APPOINTMENT, "AppointmentUpdateTime");
		put(PropertyID.ExceptionReplaceTime, GUID.PS_APPOINTMENT, "ExceptionReplaceTime");
		put(PropertyID.FInvited, GUID.PS_APPOINTMENT, "FInvited");
		put(PropertyID.ExceptionalAttendees, GUID.PS_APPOINTMENT, "ExceptionalAttendees");
		put(PropertyID.OwnerName, GUID.PS_APPOINTMENT, "OwnerName");
		put(PropertyID.AppointmentReplyName, GUID.PS_APPOINTMENT, "AppointmentReplyName");
		put(PropertyID.RecurrenceType, GUID.PS_APPOINTMENT, "RecurrenceType");
		put(PropertyID.RecurrencePattern, GUID.PS_APPOINTMENT, "RecurrencePattern");
		put(PropertyID.TimeZoneStruct, GUID.PS_APPOINTMENT, "TimeZoneStruct");
		put(PropertyID.TimeZoneDescription, GUID.PS_APPOINTMENT, "TimeZoneDescription");
		put(PropertyID.ClipStart, GUID.PS_APPOINTMENT, "ClipStart");
		put(PropertyID.ClipEnd, GUID.PS_APPOINTMENT, "ClipEnd");
		put(PropertyID.OriginalStoreEntryId, GUID.PS_APPOINTMENT, "OriginalStoreEntryId");
		put(PropertyID.AllAttendeesString, GUID.PS_APPOINTMENT, "AllAttendeesString");
		put(PropertyID.AutoFillAppointment, GUID.PS_APPOINTMENT, "AutoFillAppointment");
		put(PropertyID.ToAttendeesString, GUID.PS_APPOINTMENT, "ToAttendeesString");
		put(PropertyID.CcAttendeesString, GUID.PS_APPOINTMENT, "CcAttendeesString");
		put(PropertyID.TrustRecipientHighlights, GUID.PS_APPOINTMENT, "TrustRecipientHighlights");
		put(PropertyID.ConferencingCheck, GUID.PS_APPOINTMENT, "ConferencingCheck");
		put(PropertyID.ConferencingType, GUID.PS_APPOINTMENT, "ConferencingType");
		put(PropertyID.Directory, GUID.PS_APPOINTMENT, "Directory");
		put(PropertyID.OrganizerAlias, GUID.PS_APPOINTMENT, "OrganizerAlias");
		put(PropertyID.AutoStartCheck, GUID.PS_APPOINTMENT, "AutoStartCheck");
		put(PropertyID.AllowExternalCheck, GUID.PS_APPOINTMENT, "AllowExternalCheck");
		put(PropertyID.CollaborateDoc, GUID.PS_APPOINTMENT, "CollaborateDoc");
		put(PropertyID.NetShowURL, GUID.PS_APPOINTMENT, "NetShowURL");
		put(PropertyID.OnlinePassword, GUID.PS_APPOINTMENT, "OnlinePassword");
		put(PropertyID.AppointmentProposedStartWhole, GUID.PS_APPOINTMENT, "AppointmentProposedStartWhole");
		put(PropertyID.AppointmentProposedEndWhole, GUID.PS_APPOINTMENT, "AppointmentProposedEndWhole");
		put(PropertyID.AppointmentProposedDuration, GUID.PS_APPOINTMENT, "AppointmentProposedDuration");
		put(PropertyID.AppointmentCounterProposal, GUID.PS_APPOINTMENT, "AppointmentCounterProposal");
		put(PropertyID.AppointmentProposalNumber, GUID.PS_APPOINTMENT, "AppointmentProposalNumber");
		put(PropertyID.AppointmentNotAllowPropose, GUID.PS_APPOINTMENT, "AppointmentNotAllowPropose");
		put(PropertyID.AppointmentTimeZoneDefinitionStartDisplay, GUID.PS_APPOINTMENT, "AppointmentTimeZoneDefinitionStartDisplay");
		put(PropertyID.AppointmentTimeZoneDefinitionEndDisplay, GUID.PS_APPOINTMENT, "AppointmentTimeZoneDefinitionEndDisplay");
		put(PropertyID.AppointmentTimeZoneDefinitionRecur, GUID.PS_APPOINTMENT, "AppointmentTimeZoneDefinitionRecur");

		put(PropertyID.ReminderDelta, GUID.PS_COMMON, "ReminderDelta");
		put(PropertyID.ReminderTime, GUID.PS_COMMON, "ReminderTime");
		put(PropertyID.ReminderSet , GUID.PS_COMMON, "ReminderSet ");
		put(PropertyID.Private, GUID.PS_COMMON, "Private");
		put(PropertyID.AgingDontAgeMe, GUID.PS_COMMON, "AgingDontAgeMe");
		put(PropertyID.SideEffects, GUID.PS_COMMON, "SideEffects");
		put(PropertyID.RemoteStatus, GUID.PS_COMMON, "RemoteStatus");
		put(PropertyID.SmartNoAttach, GUID.PS_COMMON, "SmartNoAttach");
		put(PropertyID.CommonStart, GUID.PS_COMMON, "CommonStart");
		put(PropertyID.CommonEnd, GUID.PS_COMMON, "CommonEnd");
		put(PropertyID.TaskMode, GUID.PS_COMMON, "TaskMode");
		put(PropertyID.TaskGlobalId, GUID.PS_COMMON, "TaskGlobalId");
		put(PropertyID.AutoProcessState, GUID.PS_COMMON, "AutoProcessState");
		put(PropertyID.ReminderOverride, GUID.PS_COMMON, "ReminderOverride");
		put(PropertyID.NonSendableCc, GUID.PS_COMMON, "NonSendableCc");
		put(PropertyID.NonSendableBcc, GUID.PS_COMMON, "NonSendableBcc");
		put(PropertyID.Companies, GUID.PS_COMMON, "Companies");
		put(PropertyID.ReminderType, GUID.PS_COMMON, "ReminderType");
		put(PropertyID.ReminderPlaySound, GUID.PS_COMMON, "ReminderPlaySound");
		put(PropertyID.ReminderFileParameter, GUID.PS_COMMON, "ReminderFileParameter");
		put(PropertyID.VerbStream, GUID.PS_COMMON, "VerbStream");
		put(PropertyID.VerbResponse, GUID.PS_COMMON, "VerbResponse");
		put(PropertyID.FlagRequest, GUID.PS_COMMON, "FlagRequest");
		put(PropertyID.Billing, GUID.PS_COMMON, "Billing");
		put(PropertyID.NonSendableTo, GUID.PS_COMMON, "NonSendableTo");
		put(PropertyID.NonSendToTrackStatus, GUID.PS_COMMON, "NonSendToTrackStatus");
		put(PropertyID.NonSendCcTrackStatus , GUID.PS_COMMON, "NonSendCcTrackStatus ");
		put(PropertyID.NonSendBccTrackStatus, GUID.PS_COMMON, "NonSendBccTrackStatus");
		put(PropertyID.Contacts, GUID.PS_COMMON, "Contacts");
		put(PropertyID.CurrentVersion, GUID.PS_COMMON, "CurrentVersion");
		put(PropertyID.CurrentVersionName, GUID.PS_COMMON, "CurrentVersionName");
		put(PropertyID.ReminderSignalTime, GUID.PS_COMMON, "ReminderSignalTime");
		put(PropertyID.InternetAccountName, GUID.PS_COMMON, "InternetAccountName");
		put(PropertyID.InternetAccountStamp, GUID.PS_COMMON, "InternetAccountStamp");
		put(PropertyID.UseTnef, GUID.PS_COMMON, "UseTnef");
		put(PropertyID.ContactLinkSearchKey, GUID.PS_COMMON, "ContactLinkSearchKey");
		put(PropertyID.ContactLinkEntry, GUID.PS_COMMON, "ContactLinkEntry");
		put(PropertyID.ContactLinkName, GUID.PS_COMMON, "ContactLinkName");
		put(PropertyID.SpamOriginalFolder, GUID.PS_COMMON, "SpamOriginalFolder");
		put(PropertyID.ValidFlagStringProof, GUID.PS_COMMON, "ValidFlagStringProof");

		put(PropertyID.NoteColor, GUID.PS_NOTE, "NoteColor");
		put(PropertyID.NoteWidth, GUID.PS_NOTE, "NoteWidth");
		put(PropertyID.NoteHeight, GUID.PS_NOTE, "noteHeight");
		put(PropertyID.NoteX, GUID.PS_NOTE, "NoteX");
		put(PropertyID.NoteY, GUID.PS_NOTE, "NoteY");
	}

	/**	Does the list of property ID's contain the given ID?
	*
	*	@param	id	The property ID to look up.
	*	@param	guid	The GUID of the property ID.
	*
	*	@return	true if the list contains the given property ID/GUID combination, false otherwise.
	*/
	static boolean containsKey(final short id, final GUID guid)
	{
		StringByGUID sgMap = names.get(id);
		if (sgMap == null)
			return false;
		return sgMap.containsKey(guid.toString());
	}

	/**	Associate the given property ID with the given GUID and store its name.
	*
	*	@param	id	The property ID to store.
	*	@param	guid	The GUID of the property ID.
	*	@param	name	The name of this property ID for this GUID.
	*/
	private static void put(final short id, final GUID guid, final String name)
	{
		StringByGUID sgMap = names.get(id);
		if (sgMap == null)
			sgMap = new StringByGUID();
		sgMap.put(guid.toString(), name);
		names.put(id, sgMap);
	}

	/**	Retrieve the name for this property ID under the given GUID.
	*
	*	@param	id	The property ID to look up.
	*	@param	guid	The GUID of the property ID.
	*
	*	@return	The name of the property ID for this GUID, if known, or null if it is not present.
	*/
	static String name(final short id, final GUID guid)
	{
		final StringByGUID sgMap = names.get(id);
		if (sgMap == null)
			return null;
		final String name = sgMap.get(guid.toString());
		return name;
	}
}
