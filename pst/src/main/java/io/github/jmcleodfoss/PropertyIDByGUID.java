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
		put(PropertyID.FileUnderId, GUID.PSETID_ADDRESS, "FileUnderID");
		put(PropertyID.ContactItemData, GUID.PSETID_ADDRESS, "ContactItemData");
		put(PropertyID.ReferredBy, GUID.PSETID_ADDRESS, "ReferredBy");
		put(PropertyID.Department, GUID.PSETID_ADDRESS, "Department");
		put(PropertyID.HasPicture, GUID.PSETID_ADDRESS, "HasPicture");
		put(PropertyID.HomeAddress, GUID.PSETID_ADDRESS, "HomeAddress");
		put(PropertyID.WorkAddress, GUID.PSETID_ADDRESS, "WorkAddress");
		put(PropertyID.OtherAddress, GUID.PSETID_ADDRESS, "OtherAddress");
		put(PropertyID.PostalAddressId, GUID.PSETID_ADDRESS, "PostalAddressId");
		put(PropertyID.ContactCharacterSet, GUID.PSETID_ADDRESS, "ContactCharacterSet");
		put(PropertyID.AutoLog, GUID.PSETID_ADDRESS, "AutoLog");
		put(PropertyID.FileUnderList, GUID.PSETID_ADDRESS, "FileUnderList");
		put(PropertyID.EmailList, GUID.PSETID_ADDRESS, "EmailList");
		put(PropertyID.AddressBookProviderEmailList, GUID.PSETID_ADDRESS, "AddressBookProviderEmailList");
		put(PropertyID.AddressBookProviderArrayType, GUID.PSETID_ADDRESS, "AddressBookProviderArrayType");
		put(PropertyID.Html, GUID.PSETID_ADDRESS, "Html");
		put(PropertyID.YomiFirstName, GUID.PSETID_ADDRESS, "YomiFirstName");
		put(PropertyID.YomiLastName, GUID.PSETID_ADDRESS, "YomiLastName");
		put(PropertyID.YomiCompanyName, GUID.PSETID_ADDRESS, "YomiCompanyName");
		put(PropertyID.WorkAddressStreet, GUID.PSETID_ADDRESS, "WorkAddressStreet");
		put(PropertyID.WorkAddressCity, GUID.PSETID_ADDRESS, "WorkAddressCity");
		put(PropertyID.WorkAddressState, GUID.PSETID_ADDRESS, "WorkAddressState");
		put(PropertyID.WorkAddressPostalCode, GUID.PSETID_ADDRESS, "WorkAddressPostalCode");
		put(PropertyID.WorkAddressCountry, GUID.PSETID_ADDRESS, "WorkAddressCountry");
		put(PropertyID.WorkAddressPostOfficeBox, GUID.PSETID_ADDRESS, "WorkAddressPostOfficeBox");
		put(PropertyID.DistributionListChecksum, GUID.PSETID_ADDRESS, "DistributionListChecksum");
		put(PropertyID.ContactUserField1, GUID.PSETID_ADDRESS, "ContactUserField1");
		put(PropertyID.ContactUserField2, GUID.PSETID_ADDRESS, "ContactUserField2");
		put(PropertyID.ContactUserField3, GUID.PSETID_ADDRESS, "ContactUserField3");
		put(PropertyID.ContactUserField4, GUID.PSETID_ADDRESS, "ContactUserField4");
		put(PropertyID.DistributionListName, GUID.PSETID_ADDRESS, "DistributionListName");
		put(PropertyID.DistributionListOneOffMembers, GUID.PSETID_ADDRESS, "DistributionListOneOffMembers");
		put(PropertyID.DistributionListMembers, GUID.PSETID_ADDRESS, "DistributionListMembers");
		put(PropertyID.InstantMessagingAddress, GUID.PSETID_ADDRESS, "InstantMessagingAddress");
		put(PropertyID.Email1DisplayName, GUID.PSETID_ADDRESS, "Email1DisplayName");
		put(PropertyID.Email1AddressType, GUID.PSETID_ADDRESS, "Email1AddressType");
		put(PropertyID.Email1EmailAddress, GUID.PSETID_ADDRESS, "Email1EmailAddress");
		put(PropertyID.Email1OriginalDisplayName, GUID.PSETID_ADDRESS, "Email1OriginalDisplayName");
		put(PropertyID.Email1OriginalEntryId, GUID.PSETID_ADDRESS, "Email1OriginalEntryId");
		put(PropertyID.Email1RichTextFormat, GUID.PSETID_ADDRESS, "Email1RichTextFormat");
		put(PropertyID.Email2DisplayName, GUID.PSETID_ADDRESS, "Email2DisplayName");
		put(PropertyID.Email2AddressType, GUID.PSETID_ADDRESS, "Email2AddressType");
		put(PropertyID.Email2EmailAddress, GUID.PSETID_ADDRESS, "Email12EmailAddress");
		put(PropertyID.Email2OriginalDisplayName, GUID.PSETID_ADDRESS, "Email2OriginalDisplayName");
		put(PropertyID.Email2OriginalEntryId, GUID.PSETID_ADDRESS, "Email12OriginalEntryId");
		put(PropertyID.Email2RichTextFormat, GUID.PSETID_ADDRESS, "Email2RichTextFormat");
		put(PropertyID.Email3DisplayName, GUID.PSETID_ADDRESS, "Email3DisplayName");
		put(PropertyID.Email3AddressType, GUID.PSETID_ADDRESS, "Email3AddressType");
		put(PropertyID.Email3EmailAddress, GUID.PSETID_ADDRESS, "Email13EmailAddress");
		put(PropertyID.Email3OriginalEntryId, GUID.PSETID_ADDRESS, "Email3OriginalEntryId");
		put(PropertyID.Email3OriginalDisplayName, GUID.PSETID_ADDRESS, "Email3OriginalDisplayName");
		put(PropertyID.Email3RichTextFormat, GUID.PSETID_ADDRESS, "Email3RichTextFormat");
		put(PropertyID.Fax1AddressType, GUID.PSETID_ADDRESS, "Fax1AddressType");
		put(PropertyID.Fax1EmailAddress, GUID.PSETID_ADDRESS, "Fax1EmailAddress");
		put(PropertyID.Fax1OriginalDisplayName, GUID.PSETID_ADDRESS, "Fax1OriginalDisplayName");
		put(PropertyID.Fax1OriginalEntryId, GUID.PSETID_ADDRESS, "Fax1OriginalEntryId");
		put(PropertyID.Fax2AddressType, GUID.PSETID_ADDRESS, "Fax2AddressType");
		put(PropertyID.Fax2EmailAddress, GUID.PSETID_ADDRESS, "Fax2EmailAddress");
		put(PropertyID.Fax2OriginalDisplayName, GUID.PSETID_ADDRESS, "Fax2OriginalDisplayName");
		put(PropertyID.Fax2OriginalEntryId, GUID.PSETID_ADDRESS, "Fax2OriginalEntryId");
		put(PropertyID.Fax3AddressType, GUID.PSETID_ADDRESS, "Fax3AddressType");
		put(PropertyID.Fax3EmailAddress, GUID.PSETID_ADDRESS, "Fax3EmailAddress");
		put(PropertyID.Fax3OriginalDisplayName, GUID.PSETID_ADDRESS, "Fax3OriginalDisplayName");
		put(PropertyID.Fax3OriginalEntryId, GUID.PSETID_ADDRESS, "Fax3OriginalEntryId");
		put(PropertyID.FreeBusyLocation, GUID.PSETID_ADDRESS, "FreeBusyLocation");
		put(PropertyID.HomeAddressCountryCode, GUID.PSETID_ADDRESS, "HomeAddressCountryCode");
		put(PropertyID.WorkAddressCountryCode, GUID.PSETID_ADDRESS, "WorkAddressCountryCode");
		put(PropertyID.OtherAddressCountryCode, GUID.PSETID_ADDRESS, "OtherAddressCountryCode");
		put(PropertyID.AddressCountryCode, GUID.PSETID_ADDRESS, "AddressCountryCode");

		put(PropertyID.AddressBookHomeMessageDatabase, GUID.PS_INTERNAL, "AddressBookHomeMessageDatabase");
		put(PropertyID.AddressBookHomeMessageTransferAgent, GUID.PS_INTERNAL, "AddressBookHomeMessageTransferAgent");
		put(PropertyID.TcvConstLongOne, GUID.PS_INTERNAL, "TcvConstLongOne");
		put(PropertyID.AddressBookMember, GUID.PS_INTERNAL, "AddressBookMember");

		put(PropertyID.TaskStatus, GUID.PSETID_TASK, "TaskStatus");
		put(PropertyID.PercentComplete, GUID.PSETID_TASK, "PercentComplete");
		put(PropertyID.TeamTask, GUID.PSETID_TASK, "TeamTask");
		put(PropertyID.TaskStartDate, GUID.PSETID_TASK, "TaskStartDate");
		put(PropertyID.TaskDueDate, GUID.PSETID_TASK, "TaskDueDate");
		put(PropertyID.TaskResetReminder, GUID.PSETID_TASK, "TaskResetReminder");
		put(PropertyID.TaskAccepted, GUID.PSETID_TASK, "TaskAccepted");
		put(PropertyID.TaskDeadOccurrence, GUID.PSETID_TASK, "TaskDeadOccurrence");
		put(PropertyID.TaskDateCompleted, GUID.PSETID_TASK, "TaskDateCompleted");
		put(PropertyID.TaskActualEffort, GUID.PSETID_TASK, "TaskActualEffort");
		put(PropertyID.TaskEstimatedEffort, GUID.PSETID_TASK, "TaskEstimatedEffort");
		put(PropertyID.TaskVersion, GUID.PSETID_TASK, "TaskVersion");
		put(PropertyID.TaskState, GUID.PSETID_TASK, "TaskState");
		put(PropertyID.TaskLastUpdate, GUID.PSETID_TASK, "TaskLastUpdate");
		put(PropertyID.TaskRecurrence, GUID.PSETID_TASK, "TaskRecurrence");
		put(PropertyID.TaskAssigners, GUID.PSETID_TASK, "TaskAssigners");
		put(PropertyID.TaskStatusOnComplete, GUID.PSETID_TASK, "TaskStatusOnComplete");
		put(PropertyID.TaskHistory, GUID.PSETID_TASK, "TaskHistory");
		put(PropertyID.TaskUpdates, GUID.PSETID_TASK, "TaskUpdates");
		put(PropertyID.TaskComplete, GUID.PSETID_TASK, "TaskComplete");
		put(PropertyID.TaskFCreator, GUID.PSETID_TASK, "TaskFCreator");
		put(PropertyID.TaskOwner, GUID.PSETID_TASK, "TaskOwner");
		put(PropertyID.TaskMultipleRecipients, GUID.PSETID_TASK, "TaskMultipleRecipients");
		put(PropertyID.TaskAssigner, GUID.PSETID_TASK, "TaskAssigner");
		put(PropertyID.TaskLastUser, GUID.PSETID_TASK, "TaskLastUser");
		put(PropertyID.TaskOrdinal, GUID.PSETID_TASK, "TaskOrdinal");
		put(PropertyID.TaskNoCompute, GUID.PSETID_TASK, "TaskNoCompute");
		put(PropertyID.TaskLastDelegate, GUID.PSETID_TASK, "TaskLastDelegate");
		put(PropertyID.TaskFRecurring, GUID.PSETID_TASK, "TaskFRecurring");
		put(PropertyID.TaskRole, GUID.PSETID_TASK, "TaskRole");
		put(PropertyID.TaskOwnership, GUID.PSETID_TASK, "TaskOwnership");
		put(PropertyID.TaskAcceptanceState, GUID.PSETID_TASK, "TaskAcceptanceState");
		put(PropertyID.TaskFFixOffline, GUID.PSETID_TASK, "TaskFFixOffline");

		put(PropertyID.AppointmentSequence, GUID.PSETID_APPOINTMENT, "AppointmentSequence");
		put(PropertyID.AppointmentSequenceTime, GUID.PSETID_APPOINTMENT, "AppointmentSequenceTime");
		put(PropertyID.AppointmentLastSequence, GUID.PSETID_APPOINTMENT, "AppointmentLastSequence");
		put(PropertyID.ChangeHighlight, GUID.PSETID_APPOINTMENT, "ChangeHighlight");
		put(PropertyID.BusyStatus, GUID.PSETID_APPOINTMENT, "BusyStatus");
		put(PropertyID.FExceptionalBody, GUID.PSETID_APPOINTMENT, "FExceptionalBody");
		put(PropertyID.AppointmentAuxiliaryFlags, GUID.PSETID_APPOINTMENT, "AppointmentAuxiliaryFlags");
		put(PropertyID.AppointmentLocation, GUID.PSETID_APPOINTMENT, "Location");
		put(PropertyID.ForwardInstance, GUID.PSETID_APPOINTMENT, "ForwardInstance");
		put(PropertyID.MeetingWorkspaceURL, GUID.PSETID_APPOINTMENT, "MeetingWorkspaceURL");
		put(PropertyID.AppointmentStartWhole, GUID.PSETID_APPOINTMENT, "AppointmentStartWhole");
		put(PropertyID.AppointmentEndWhole, GUID.PSETID_APPOINTMENT, "AppointmentEndWhole");
		put(PropertyID.AppointmentStartTime, GUID.PSETID_APPOINTMENT, "AppointmentStartTime");
		put(PropertyID.AppointmentEndType, GUID.PSETID_APPOINTMENT, "AppointmentEndType");
		put(PropertyID.AppointmentEndDate, GUID.PSETID_APPOINTMENT, "AppointmentEndDate");
		put(PropertyID.AppointmentStartDate, GUID.PSETID_APPOINTMENT, "AppointmentStartDate");
		put(PropertyID.AppointmentDuration, GUID.PSETID_APPOINTMENT, "AppointmentDuration");
		put(PropertyID.AppointmentColor, GUID.PSETID_APPOINTMENT, "AppointmentColor");
		put(PropertyID.AppointmentSubType, GUID.PSETID_APPOINTMENT, "AppointmentSubType");
		put(PropertyID.AppointmentRecur, GUID.PSETID_APPOINTMENT, "AppointmentRecur");
		put(PropertyID.AppointmentStateFlags, GUID.PSETID_APPOINTMENT, "AppointmentStateFlags");
		put(PropertyID.ResponseStatus, GUID.PSETID_APPOINTMENT, "ResponseStatus");
		put(PropertyID.AppointmentReplyTime, GUID.PSETID_APPOINTMENT, "AppointmentReplyTime");
		put(PropertyID.Recurring, GUID.PSETID_APPOINTMENT, "Recurring");
		put(PropertyID.IntendedBusyStatus, GUID.PSETID_APPOINTMENT, "IntendedBusyStatus");
		put(PropertyID.AppointmentUpdateTime, GUID.PSETID_APPOINTMENT, "AppointmentUpdateTime");
		put(PropertyID.ExceptionReplaceTime, GUID.PSETID_APPOINTMENT, "ExceptionReplaceTime");
		put(PropertyID.FInvited, GUID.PSETID_APPOINTMENT, "FInvited");
		put(PropertyID.ExceptionalAttendees, GUID.PSETID_APPOINTMENT, "ExceptionalAttendees");
		put(PropertyID.OwnerName, GUID.PSETID_APPOINTMENT, "OwnerName");
		put(PropertyID.AppointmentReplyName, GUID.PSETID_APPOINTMENT, "AppointmentReplyName");
		put(PropertyID.RecurrenceType, GUID.PSETID_APPOINTMENT, "RecurrenceType");
		put(PropertyID.RecurrencePattern, GUID.PSETID_APPOINTMENT, "RecurrencePattern");
		put(PropertyID.TimeZoneStruct, GUID.PSETID_APPOINTMENT, "TimeZoneStruct");
		put(PropertyID.TimeZoneDescription, GUID.PSETID_APPOINTMENT, "TimeZoneDescription");
		put(PropertyID.ClipStart, GUID.PSETID_APPOINTMENT, "ClipStart");
		put(PropertyID.ClipEnd, GUID.PSETID_APPOINTMENT, "ClipEnd");
		put(PropertyID.OriginalStoreEntryId, GUID.PSETID_APPOINTMENT, "OriginalStoreEntryId");
		put(PropertyID.AllAttendeesString, GUID.PSETID_APPOINTMENT, "AllAttendeesString");
		put(PropertyID.AutoFillAppointment, GUID.PSETID_APPOINTMENT, "AutoFillAppointment");
		put(PropertyID.ToAttendeesString, GUID.PSETID_APPOINTMENT, "ToAttendeesString");
		put(PropertyID.CcAttendeesString, GUID.PSETID_APPOINTMENT, "CcAttendeesString");
		put(PropertyID.TrustRecipientHighlights, GUID.PSETID_APPOINTMENT, "TrustRecipientHighlights");
		put(PropertyID.ConferencingCheck, GUID.PSETID_APPOINTMENT, "ConferencingCheck");
		put(PropertyID.ConferencingType, GUID.PSETID_APPOINTMENT, "ConferencingType");
		put(PropertyID.Directory, GUID.PSETID_APPOINTMENT, "Directory");
		put(PropertyID.OrganizerAlias, GUID.PSETID_APPOINTMENT, "OrganizerAlias");
		put(PropertyID.AutoStartCheck, GUID.PSETID_APPOINTMENT, "AutoStartCheck");
		put(PropertyID.AllowExternalCheck, GUID.PSETID_APPOINTMENT, "AllowExternalCheck");
		put(PropertyID.CollaborateDoc, GUID.PSETID_APPOINTMENT, "CollaborateDoc");
		put(PropertyID.NetShowURL, GUID.PSETID_APPOINTMENT, "NetShowURL");
		put(PropertyID.OnlinePassword, GUID.PSETID_APPOINTMENT, "OnlinePassword");
		put(PropertyID.AppointmentProposedStartWhole, GUID.PSETID_APPOINTMENT, "AppointmentProposedStartWhole");
		put(PropertyID.AppointmentProposedEndWhole, GUID.PSETID_APPOINTMENT, "AppointmentProposedEndWhole");
		put(PropertyID.AppointmentProposedDuration, GUID.PSETID_APPOINTMENT, "AppointmentProposedDuration");
		put(PropertyID.AppointmentCounterProposal, GUID.PSETID_APPOINTMENT, "AppointmentCounterProposal");
		put(PropertyID.AppointmentProposalNumber, GUID.PSETID_APPOINTMENT, "AppointmentProposalNumber");
		put(PropertyID.AppointmentNotAllowPropose, GUID.PSETID_APPOINTMENT, "AppointmentNotAllowPropose");
		put(PropertyID.AppointmentTimeZoneDefinitionStartDisplay, GUID.PSETID_APPOINTMENT, "AppointmentTimeZoneDefinitionStartDisplay");
		put(PropertyID.AppointmentTimeZoneDefinitionEndDisplay, GUID.PSETID_APPOINTMENT, "AppointmentTimeZoneDefinitionEndDisplay");
		put(PropertyID.AppointmentTimeZoneDefinitionRecur, GUID.PSETID_APPOINTMENT, "AppointmentTimeZoneDefinitionRecur");

		put(PropertyID.ReminderDelta, GUID.PSETID_COMMON, "ReminderDelta");
		put(PropertyID.ReminderTime, GUID.PSETID_COMMON, "ReminderTime");
		put(PropertyID.ReminderSet, GUID.PSETID_COMMON, "ReminderSet");
		put(PropertyID.Private, GUID.PSETID_COMMON, "Private");
		put(PropertyID.AgingDontAgeMe, GUID.PSETID_COMMON, "AgingDontAgeMe");
		put(PropertyID.SideEffects, GUID.PSETID_COMMON, "SideEffects");
		put(PropertyID.RemoteStatus, GUID.PSETID_COMMON, "RemoteStatus");
		put(PropertyID.SmartNoAttach, GUID.PSETID_COMMON, "SmartNoAttach");
		put(PropertyID.CommonStart, GUID.PSETID_COMMON, "CommonStart");
		put(PropertyID.CommonEnd, GUID.PSETID_COMMON, "CommonEnd");
		put(PropertyID.TaskMode, GUID.PSETID_COMMON, "TaskMode");
		put(PropertyID.TaskGlobalId, GUID.PSETID_COMMON, "TaskGlobalId");
		put(PropertyID.AutoProcessState, GUID.PSETID_COMMON, "AutoProcessState");
		put(PropertyID.ReminderOverride, GUID.PSETID_COMMON, "ReminderOverride");
		put(PropertyID.NonSendableCc, GUID.PSETID_COMMON, "NonSendableCc");
		put(PropertyID.NonSendableBcc, GUID.PSETID_COMMON, "NonSendableBcc");
		put(PropertyID.Companies, GUID.PSETID_COMMON, "Companies");
		put(PropertyID.ReminderType, GUID.PSETID_COMMON, "ReminderType");
		put(PropertyID.ReminderPlaySound, GUID.PSETID_COMMON, "ReminderPlaySound");
		put(PropertyID.ReminderFileParameter, GUID.PSETID_COMMON, "ReminderFileParameter");
		put(PropertyID.VerbStream, GUID.PSETID_COMMON, "VerbStream");
		put(PropertyID.VerbResponse, GUID.PSETID_COMMON, "VerbResponse");
		put(PropertyID.FlagRequest, GUID.PSETID_COMMON, "FlagRequest");
		put(PropertyID.Billing, GUID.PSETID_COMMON, "Billing");
		put(PropertyID.NonSendableTo, GUID.PSETID_COMMON, "NonSendableTo");
		put(PropertyID.NonSendToTrackStatus, GUID.PSETID_COMMON, "NonSendToTrackStatus");
		put(PropertyID.NonSendCcTrackStatus, GUID.PSETID_COMMON, "NonSendCcTrackStatus");
		put(PropertyID.NonSendBccTrackStatus, GUID.PSETID_COMMON, "NonSendBccTrackStatus");
		put(PropertyID.Contacts, GUID.PSETID_COMMON, "Contacts");
		put(PropertyID.CurrentVersion, GUID.PSETID_COMMON, "CurrentVersion");
		put(PropertyID.CurrentVersionName, GUID.PSETID_COMMON, "CurrentVersionName");
		put(PropertyID.ReminderSignalTime, GUID.PSETID_COMMON, "ReminderSignalTime");
		put(PropertyID.InternetAccountName, GUID.PSETID_COMMON, "InternetAccountName");
		put(PropertyID.InternetAccountStamp, GUID.PSETID_COMMON, "InternetAccountStamp");
		put(PropertyID.UseTnef, GUID.PSETID_COMMON, "UseTnef");
		put(PropertyID.ContactLinkSearchKey, GUID.PSETID_COMMON, "ContactLinkSearchKey");
		put(PropertyID.ContactLinkEntry, GUID.PSETID_COMMON, "ContactLinkEntry");
		put(PropertyID.ContactLinkName, GUID.PSETID_COMMON, "ContactLinkName");
		put(PropertyID.SpamOriginalFolder, GUID.PSETID_COMMON, "SpamOriginalFolder");
		put(PropertyID.ValidFlagStringProof, GUID.PSETID_COMMON, "ValidFlagStringProof");

		put(PropertyID.NoteColor, GUID.PSETID_NOTE, "NoteColor");
		put(PropertyID.NoteWidth, GUID.PSETID_NOTE, "NoteWidth");
		put(PropertyID.NoteHeight, GUID.PSETID_NOTE, "noteHeight");
		put(PropertyID.NoteX, GUID.PSETID_NOTE, "NoteX");
		put(PropertyID.NoteY, GUID.PSETID_NOTE, "NoteY");
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
