package io.github.jmcleodfoss.pst;

/**	The PropertyTagName class contains the names for the known property tags.
*
*	@see	io.github.jmcleodfoss.pst.PropertyID
*	@see	io.github.jmcleodfoss.pst.PropertyTag
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">[MS-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*/
public class PropertyTagName {

	/**	The named properties, stored by ID. */
	static final java.util.HashMap<Integer, String> names = new java.util.HashMap<Integer, String>();
	static {
		names.put(PropertyTag.NameidBucketCount, "NameidBucketCount");
		names.put(PropertyTag.NameidStreamGuid, "NameidStreamGuid");
		names.put(PropertyTag.AlternateRecipientAllowed, "AlternateRecipientAllowed");
		names.put(PropertyTag.NameidStreamEntry, "NameidStreamEntry");
		names.put(PropertyTag.NameidStreamString, "NameidStreamString");
		names.put(PropertyTag.LidIsRecurring, "LidIsRecurring");
		names.put(PropertyTag.LidRequiredAttendees, "LidRequiredAttendees");
		names.put(PropertyTag.LidOptionalAttendees, "LidOptionalAttendees");
		names.put(PropertyTag.LidResourceAttendees, "LidResourceAttendees");
		names.put(PropertyTag.LidDelegateMail, "LidDelegateMail");
		names.put(PropertyTag.LidIsException, "LidIsException");
		names.put(PropertyTag.LidTimeZone, "LidTimeZone");
		names.put(PropertyTag.LidStartRecurrenceDate, "StartRecurrenceDate");
		names.put(PropertyTag.LidStartRecurrenceTime, "StartRecurrenceTime");
		names.put(PropertyTag.DeferredDeliveryTime, "DeferredDeliveryTime");
		names.put(PropertyTag.LidEndRecurrenceTime, "LidEndRecurrenceTime");
		names.put(PropertyTag.LidDayInterval, "DayInterval");
		names.put(PropertyTag.LidMonthInterval, "MonthInterval");
		names.put(PropertyTag.LidWeekInterval, "WeekInterval");
		names.put(PropertyTag.LidYearInterval, "FInvited");
		names.put(PropertyTag.ExpiryTime, "ExpiryTime");
		names.put(PropertyTag.Importance, "Importance");
		names.put(PropertyTag.LidOldRecurrenceType, "LidOldRecurrenceType");
		names.put(PropertyTag.MessageClass, "MessageClass");
		names.put(PropertyTag.MessageClassW, "MessageClassW");
		names.put(PropertyTag.LidCalendarType, "LidCalendarType");
		names.put(PropertyTag.OriginatorDeliveryReportRequested, "OriginatorDeliveryReportRequested");
		names.put(PropertyTag.LidApposhortmentMessageClass, "LidApposhortmentMessageClass");
		names.put(PropertyTag.Priority, "Priority");
		names.put(PropertyTag.LidOldLocation, "LidOldLocation");
		names.put(PropertyTag.ReadReceiptRequested, "ReadReceiptRequested");
		names.put(PropertyTag.LidOldWhenEndWhole, "OldWhenEndWhole");
		names.put(PropertyTag.RecipientReassignmentProhibited, "RecipientReassignmentProhibited");
		names.put(PropertyTag.OriginalSensitivity, "OriginalSensitivity");
		names.put(PropertyTag.ReplyTime, "ReplyTime");
		names.put(PropertyTag.Sensitivity, "Sensitivity");
		names.put(PropertyTag.Subject, "Subject");
		names.put(PropertyTag.SubjectW, "SubjectW");
		names.put(PropertyTag.ClientSubmitTime, "ClientSubmitTime");
		names.put(PropertyTag.SentRepresentingSearchKey, "SentRepresentingSearchKey");
		names.put(PropertyTag.ReceivedByEntryId, "ReceivedByEntryId");
		names.put(PropertyTag.ReceivedByName, "ReceivedByName");
		names.put(PropertyTag.ReceivedByNameW, "ReceivedByNameW");
		names.put(PropertyTag.SentRepresentingEntryId, "SentRepresentingEntryId");
		names.put(PropertyTag.SentRepresentingName, "SentRepresentingName");
		names.put(PropertyTag.SentRepresentingNameW, "SentRepresentingNameW");
		names.put(PropertyTag.ReceivedRepresentingEntryId, "ReceivedRepresentingEntryId");
		names.put(PropertyTag.ReceivedRepresentingName, "ReceivedRepresentingName");
		names.put(PropertyTag.ReadReceiptEntryId, "ReadReceiptEntryId");
		names.put(PropertyTag.MessageSubmissionId, "MessageSubmissionId");
		names.put(PropertyTag.OriginalSubject, "OriginalSubject");
		names.put(PropertyTag.OriginalSubmitTime, "OriginalSubmitTime");
		names.put(PropertyTag.ReplyRecipientEntries, "ReplyRecipientEntries");
		names.put(PropertyTag.ReplyRecipientNames, "ReplyRecipientNames");
		names.put(PropertyTag.ReplyRecipientNamesW, "ReplyRecipientNamesW");
		names.put(PropertyTag.ReceivedBySearchKey, "ReceivedBySearchKey");
		names.put(PropertyTag.ReceivedRepresentingSearchKey, "ReceivedRepresentingSearchKey");
		names.put(PropertyTag.MessageToMe, "MessageToMe");
		names.put(PropertyTag.MessageCcMe, "MessageCcMe");
		names.put(PropertyTag.OriginalSenderName, "OriginalSenderName");
		names.put(PropertyTag.OriginalSenderEntryId, "OriginalSenderEntryId");
		names.put(PropertyTag.OriginalSenderSearchKey, "OriginalSenderSearchKey");
		names.put(PropertyTag.OriginalSentRepresentingName, "OriginalSentRepresentingName");
		names.put(PropertyTag.OriginalSentRepresentingEntryId, "OriginalSentRepresentingEntryId");
		names.put(PropertyTag.OriginalSentRepresentingSearchKey, "OriginalSentRepresentingSearchKey");
		names.put(PropertyTag.StartDate, "StartDate");
		names.put(PropertyTag.EndDate, "EndDate");
		names.put(PropertyTag.OwnerApposhortmentId, "OwnerApposhortmentId");
		names.put(PropertyTag.ResponseRequested, "ResponseRequested");
		names.put(PropertyTag.SentRepresentingAddressType, "SentRepresentingAddressType");
		names.put(PropertyTag.SentRepresentingAddressTypeW, "SentRepresentingAddressTypeW");
		names.put(PropertyTag.SentRepresentingEmailAddress, "SentRepresentingEmailAddress");
		names.put(PropertyTag.SentRepresentingEmailAddressW, "SentRepresentingEmailAddressW");
		names.put(PropertyTag.OriginalSenderAddressType, "OriginalSenderAddressType");
		names.put(PropertyTag.OriginalSenderEmailAddress, "OriginalSenderEmailAddress");
		names.put(PropertyTag.OriginalSentRepresentingAddressType, "OriginalSentRepresentingAddressType");
		names.put(PropertyTag.OriginalSentRepresentingEmailAddress, "OriginalSentRepresentingEmailAddress");
		names.put(PropertyTag.ConversationTopic, "ConversationTopic");
		names.put(PropertyTag.ConversationTopicW, "ConversationTopicW");
		names.put(PropertyTag.ConversationIndex, "ConversationIndex");
		names.put(PropertyTag.OriginalDisplayTo, "OriginalDisplayTo");
		names.put(PropertyTag.ReceivedByAddressType, "ReceivedByAddressType");
		names.put(PropertyTag.ReceivedByAddressTypeW, "ReceivedByAddressTypeW");
		names.put(PropertyTag.ReceivedByEmailAddress, "ReceivedByEmailAddress");
		names.put(PropertyTag.ReceivedByEmailAddressW, "ReceivedByEmailAddressW");
		names.put(PropertyTag.ReceivedRepresentingAddressType, "ReceivedRepresentingAddressType");
		names.put(PropertyTag.RemoteHeaderLoc, "RemoteHeaderLoc");
		names.put(PropertyTag.TransportMessageHeaders, "TransportMessageHeaders");
		names.put(PropertyTag.TransportMessageHeadersW, "TransportMessageHeadersW");
		names.put(PropertyTag.TnefCorrelationKey, "TnefCorrelationKey");
		names.put(PropertyTag.ReportDisposition, "ReportDisposition");
		names.put(PropertyTag.ReportDispositionMode, "ReportDispositionMode");
		names.put(PropertyTag.ReportOriginalSender, "ReportOriginalSender");
		names.put(PropertyTag.ReportDispositionToNames, "ReportDispositionToNames");
		names.put(PropertyTag.ReportDispositionToEmailAddresses, "ReportDispositionToEmailAddresses");
		names.put(PropertyTag.RecipientType, "RecipientType");
		names.put(PropertyTag.ReplyRequested, "ReplyRequested");
		names.put(PropertyTag.SenderEntryId, "SenderEntryId");
		names.put(PropertyTag.SenderName, "SenderName");
		names.put(PropertyTag.SenderNameW, "SenderNameW");
		names.put(PropertyTag.SenderSearchKey, "SenderSearchKey");
		names.put(PropertyTag.SenderAddressType, "SenderAddressType");
		names.put(PropertyTag.SenderAddressTypeW, "SenderAddressTypeW");
		names.put(PropertyTag.SenderEmailAddress, "SenderEmailAddress");
		names.put(PropertyTag.SenderEmailAddressW, "SenderEmailAddressW");
		names.put(PropertyTag.DeleteAfterSubmit, "DeleteAfterSubmit");
		names.put(PropertyTag.DisplayBcc, "DisplayBcc");
		names.put(PropertyTag.DisplayBccW, "DisplayBccW");
		names.put(PropertyTag.DisplayCc, "DisplayCc");
		names.put(PropertyTag.DisplayCcW, "DisplayCcW");
		names.put(PropertyTag.DisplayTo, "DisplayTo");
		names.put(PropertyTag.DisplayToW, "DisplayToW");
		names.put(PropertyTag.ParentDisplayW, "ParentDisplayW");
		names.put(PropertyTag.MessageDeliveryTime, "MessageDeliveryTime");
		names.put(PropertyTag.MessageFlags, "MessageFlags");
		names.put(PropertyTag.MessageSize, "MessageSize");
		names.put(PropertyTag.SentMailEntryId, "SentMailEntryId");
		names.put(PropertyTag.Responsibility, "Responsibility");
		names.put(PropertyTag.SubmitFlags, "SubmitFlags");
		names.put(PropertyTag.MessageStatus, "MessageStatus");
		names.put(PropertyTag.AttachmentSize, "AttachmentSize");
		names.put(PropertyTag.InternetArticleNumber, "InternetArticleNumber");
		names.put(PropertyTag.PrimarySendAccount, "PrimarySendAccount");
		names.put(PropertyTag.NextSendAcct, "NextSendAcct");
		names.put(PropertyTag.RecordKey, "RecordKey");
		names.put(PropertyTag.ObjectType, "ObjectType");
		names.put(PropertyTag.EntryId, "EntryId");

		names.put(PropertyTag.Body, "Body");
		names.put(PropertyTag.BodyW, "BodyW");
		names.put(PropertyTag.RtfSyncBodyCrc, "RtfSyncBodyCrc");
		names.put(PropertyTag.RtfSyncBodyCount , "RtfSyncBodyCount ");
		names.put(PropertyTag.RtfSyncBodyTag, "RtfSyncBodyTag");
		names.put(PropertyTag.RtfSyncBodyTagW, "RtfSyncBodyTagW");
		names.put(PropertyTag.RtfCompressed, "RtfCompressed ");
		names.put(PropertyTag.RtfSyncPrefixCount, "RtfSyncPrefixCount");
		names.put(PropertyTag.RtfSyncTrailingCount, "RtfSyncTrailingCount");
		names.put(PropertyTag.BodyHtml, "BodyHtml");
		names.put(PropertyTag.BodyHtmlW, "BodyHtmlW");
		names.put(PropertyTag.InternetMessageId, "InternetMessageId");
		names.put(PropertyTag.InternetMessageIdW, "InternetMessageIdW");
		names.put(PropertyTag.InternetOrganization, "InternetOrganization");
		names.put(PropertyTag.InReplyToId, "InReplyToId");
		names.put(PropertyTag.InReplyToIdW, "InReplyToIdW");
		names.put(PropertyTag.OriginalMessageId, "OriginalMessageId");
		names.put(PropertyTag.OriginalMessageIdW, "OriginalMessageIdW");
		names.put(PropertyTag.IconIndex, "IconIndex");
		names.put(PropertyTag.LastVerbExecuted , "LastVerbExecuted ");
		names.put(PropertyTag.LastVerbExecutionTime, "LastVerbExecutionTime");
		names.put(PropertyTag.FlagStatus, "FlagStatus");
		names.put(PropertyTag.FollowupIcon, "FollowupIcon");
		names.put(PropertyTag.BlockStatus, "BlockStatus");
		names.put(PropertyTag.ItemTemporaryFlags, "ItemTemporaryFlags");
		names.put(PropertyTag.ConflictItems, "ConflictItems");
		names.put(PropertyTag.AttributeHidden, "AttributeHidden");

		names.put(PropertyTag.RowId, "RowId");
		names.put(PropertyTag.AddressType, "AddressType");
		names.put(PropertyTag.EmailAddress, "EmailAddress");
		names.put(PropertyTag.EmailAddressW, "EmailAddressW");
		names.put(PropertyTag.Comment, "Comment");
		names.put(PropertyTag.CommentW, "CommentW");
		names.put(PropertyTag.CreationTime, "CreationTime");
		names.put(PropertyTag.LastModificationTime, "LastModificationTime");
		names.put(PropertyTag.SearchKey, "SearchKey");
		names.put(PropertyTag.DisplayName, "DisplayName");
		names.put(PropertyTag.DisplayNameW, "DisplayNameW");
		names.put(PropertyTag.ValidFolderMask, "ValidFolderMask");
		names.put(PropertyTag.IpmSubTreeEntryId, "IpmSubTreeEntryId");
		names.put(PropertyTag.IpmOutboxEntryId, "IpmOutboxEntryId");
		names.put(PropertyTag.IpmWasteBasketEntryId, "IpmWasteBasketEntryId");
		names.put(PropertyTag.IpmSentMailEntryId, "IpmSentMailEntryId");
		names.put(PropertyTag.ViewsEntryId, "ViewsEntryId");
		names.put(PropertyTag.CommonViewsEntryId, "CommonViewsEntryId");
		names.put(PropertyTag.FinderEntryId, "FinderEntryId");
		names.put(PropertyTag.ContentCount, "ContentCount");
		names.put(PropertyTag.ContentUnreadCount, "ContentUnreadCount");
		names.put(PropertyTag.Subfolders, "Subfolders");
		names.put(PropertyTag.ContainerClass, "ContainerClass");
		names.put(PropertyTag.ContainerClassW, "ContainerClassW");
		names.put(PropertyTag.IpmApposhortmentEntryId, "IpmApposhortmentEntryId");
		names.put(PropertyTag.IpmContactEntryId, "IpmContactEntryId");
		names.put(PropertyTag.IpmJournalEntryId, "IpmJournalEntryId");
		names.put(PropertyTag.IpmNoteEntryId, "IpmNoteEntryId");
		names.put(PropertyTag.IpmTaskEntryId, "IpmTaskEntryId");
		names.put(PropertyTag.RemindersOnlineEntryId, "RemindersOnlineEntryId");
		names.put(PropertyTag.IpmDraftsEntryId, "IpmDraftsEntryId");
		names.put(PropertyTag.AdditionalRenEntryIds, "AdditionalRenEntryIds");
		names.put(PropertyTag.ExtendedFolderFlags, "ExtendedFolderFlags");
		names.put(PropertyTag.OrdinalMost, "OrdinalMost");
		names.put(PropertyTag.FreeBusyEntryIds, "FreeBusyEntryIds");
		names.put(PropertyTag.AttachDataObject, "AttachDataObject");
		names.put(PropertyTag.AttachDataBinary, "AttachDataBinary");
		names.put(PropertyTag.AttachEncoding, "AttachEncoding");
		names.put(PropertyTag.AttachExtension, "AttachExtension");
		names.put(PropertyTag.AttachExtensionW, "AttachExtensionW");
		names.put(PropertyTag.AttachFilename, "AttachFilename");
		names.put(PropertyTag.AttachFilenameW, "AttachFilenameW");
		names.put(PropertyTag.AttachMethod, "AttachMethod");
		names.put(PropertyTag.AttachLongFilename, "AttachLongFilename");
		names.put(PropertyTag.AttachLongFilenameW, "AttachLongFilenameW");
		names.put(PropertyTag.AttachPathname, "AttachPathname");
		names.put(PropertyTag.AttachPathnameW, "AttachPathnameW");
		names.put(PropertyTag.AttachRendering, "AttachRendering");
		names.put(PropertyTag.AttachMimeSequence, "AttachMimeSequence");
		names.put(PropertyTag.AttachTag, "AttachTag");
		names.put(PropertyTag.RenderingPosition, "RenderingPosition");
		names.put(PropertyTag.AttachTransportName, "AttachTransportName");
		names.put(PropertyTag.AttachLongPathname, "AttachLongFilename");
		names.put(PropertyTag.AttachLongPathnameW, "AttachLongFilenameW");
		names.put(PropertyTag.AttachMimeTag, "AttachMimeTag");
		names.put(PropertyTag.AttachMimeTagW, "AttachMimeTagW");
		names.put(PropertyTag.AttachAdditionalInfo, "AttachAdditionalInfo");
		names.put(PropertyTag.AttachContentBase, "AttachContentBase");
		names.put(PropertyTag.AttachContentId, "AttachContentId");
		names.put(PropertyTag.AttachContentLocation, "AttachContentLocation");
		names.put(PropertyTag.AttachFlags, "AttachFlags");
		names.put(PropertyTag.AttachPayloadProviderGuidString, "AttachPayloadProviderGuidString");
		names.put(PropertyTag.AttachPayloadClass, "AttachPayloadClass");
		names.put(PropertyTag.DisplayType, "DisplayType");
		names.put(PropertyTag.SevenBitDisplayName, "7BitDisplayName");
		names.put(PropertyTag.Account, "Account");
		names.put(PropertyTag.Generation, "Generation");
		names.put(PropertyTag.GivenName, "GivenName");
		names.put(PropertyTag.GivenNameW, "GivenNameW");
		names.put(PropertyTag.BusinessTelephoneNumber, "BusinessTelephoneNumber");
		names.put(PropertyTag.BusinessTelephoneNumberW, "BusinessTelephoneNumberW");
		names.put(PropertyTag.HomeTelephoneNumber, "HomeTelephoneNumber");
		names.put(PropertyTag.HomeTelephoneNumberW, "HomeTelephoneNumberW");
		names.put(PropertyTag.Initials, "Initials");
		names.put(PropertyTag.Keyword, "Keyword");
		names.put(PropertyTag.Language, "Language");
		names.put(PropertyTag.Location, "Location");
		names.put(PropertyTag.Surname, "Surname");
		names.put(PropertyTag.SurnameW, "SurnameW");
		names.put(PropertyTag.PostalAddress, "PostalAddress");
		names.put(PropertyTag.PostalAddressW, "PostalAddressW");
		names.put(PropertyTag.CompanyName, "CompanyName");
		names.put(PropertyTag.CompanyNameW, "CompanyNameW");
		names.put(PropertyTag.Title, "Title");
		names.put(PropertyTag.DepartmentName, "DepartmentName");
		names.put(PropertyTag.Business2TelephoneNumbers, "Business2TelephoneNumbers");
		names.put(PropertyTag.MobileTelephoneNumber, "MobileTelephoneNumber");
		names.put(PropertyTag.MobileTelephoneNumberW, "MobileTelephoneNumberW");
		names.put(PropertyTag.OtherTelephoneNumber, "OtherTelephoneNumber");
		names.put(PropertyTag.OtherTelephoneNumberW, "OtherTelephoneNumberW");
		names.put(PropertyTag.PrimaryFaxNumber, "PrimaryFaxNumber");
		names.put(PropertyTag.BusinessFaxNumber, "BusinessFaxNumber");
		names.put(PropertyTag.BusinessFaxNumberW, "BusinessFaxNumberW");
		names.put(PropertyTag.HomeFaxNumber, "HomeFaxNumber");
		names.put(PropertyTag.HomeFaxNumberW, "HomeFaxNumberW");
		names.put(PropertyTag.Country, "Country");
		names.put(PropertyTag.Locality, "Locality");
		names.put(PropertyTag.StateOrProvince, "StateOrProvince");
		names.put(PropertyTag.StreetAddress, "StreetAddress");
		names.put(PropertyTag.PostalCode, "PostalCode");
		names.put(PropertyTag.Home2TelephoneNumbers, "Home2TelephoneNumbers");
		names.put(PropertyTag.SendRichInfo, "SendRichInfo");
		names.put(PropertyTag.Birthday, "Birthday");
		names.put(PropertyTag.MiddleName, "MiddleName");
		names.put(PropertyTag.MiddleNameW, "MiddleNameW");
		names.put(PropertyTag.DisplayNamePrefix, "DisplayNamePrefix");
		names.put(PropertyTag.SpouseName, "SpouseName");
		names.put(PropertyTag.BusinessHomePage, "BusinessHomePage");
		names.put(PropertyTag.HomeAddressCity, "HomeAddressCity");
		names.put(PropertyTag.HomeAddressCountry, "HomeAddressCountry");
		names.put(PropertyTag.HomeAddressPostalCode, "HomeAddressPostalCode");
		names.put(PropertyTag.HomeAddressStateOrProvince, "HomeAddressStateOrProvince");
		names.put(PropertyTag.HomeAddressStreet, "HomeAddressStreet");
		names.put(PropertyTag.HomeAddressPostOfficeBox, "HomeAddressPostOfficeBox");
		names.put(PropertyTag.OtherAddressCity, "OtherAddressCity");
		names.put(PropertyTag.OtherAddressCountry, "OtherAddressCountry");
		names.put(PropertyTag.OtherAddressPostalCode, "OtherAddressPostalCode");
		names.put(PropertyTag.OtherAddressStateOrProvince, "OtherAddressStateOrProvince");
		names.put(PropertyTag.OtherAddressStreet, "OtherAddressStreet");
		names.put(PropertyTag.OtherAddressPostOfficeBox, "OtherAddressPostOfficeBox");
		names.put(PropertyTag.InternetCodepage, "InternetCodepage");
		names.put(PropertyTag.MessageLocaleId, "MessageLocaleId");
		names.put(PropertyTag.CreatorName, "CreatorName");
		names.put(PropertyTag.CreatorEntryId, "CreatorEntryId");
		names.put(PropertyTag.LastModifierName, "LastModifierName");
		names.put(PropertyTag.LastModifierEntryId, "LastModifierEntryId");
		names.put(PropertyTag.MessageCodepage, "MessageCodepage");

		names.put(PropertyTag.SenderFlags, "SenderFlags");
		names.put(PropertyTag.SentRepresentingFlags, "SentRepresentingFlags");
		names.put(PropertyTag.SenderSimpleDisplayName, "SenderSimpleDisplayName");
		names.put(PropertyTag.SentRepresentingSimpleDisplayName, "SentRepresentingSimpleDisplayName");
		names.put(PropertyTag.CreatorSimpleDisplayName, "CreatorSimpleDisplayName");
		names.put(PropertyTag.LastModifierSimpleDisplayName, "LastModifierSimpleDisplayName");
		names.put(PropertyTag.ContentFilterSpamConfidenceLevel, "ContentFilterSpamConfidenceLevel");

		names.put(PropertyTag.InternetMailOverrideFormat, "InternetMailOverrideFormat");
		names.put(PropertyTag.MessageEditorFormat, "MessageEditorFormat");
		names.put(PropertyTag.RecipientDisplayName, "RecipientDisplayName");

		names.put(PropertyTag.ChangeKey, "ChangeKey");
		names.put(PropertyTag.PredecessorChangeList, "PredecessorChangeList");
		names.put(PropertyTag.PstBodyPrefix, "PstBodyPrefix");
		names.put(PropertyTag.PstLrNoRestrictions, "PstLrNoRestrictions");
		names.put(PropertyTag.PstHiddenCount, "PstHiddenCount");
		names.put(PropertyTag.PstHiddenUnread, "PstHiddenUnread");
		names.put(PropertyTag.FolderId, "FolderId");
		names.put(PropertyTag.LtpParentNid, "LtpParentNid");
		names.put(PropertyTag.LtpRowId, "LtpRowId");
		names.put(PropertyTag.LtpRowVer, "LtpRowVer");
		names.put(PropertyTag.LtpPstPassword, "LtpPstPassword");
		names.put(PropertyTag.OfflineAddressBookName, "OfflineAddressBookName");
		names.put(PropertyTag.VoiceMessageDuration, "VoiceMessageDuration");
		names.put(PropertyTag.SenderTelephoneNumber, "SenderTelephoneNumber");
		names.put(PropertyTag.SendOutlookRecallReport, "SendOutlookRecallReport");
		names.put(PropertyTag.FaxNumberOfPages, "FaxNumberOfPages");
		names.put(PropertyTag.OfflineAddressBookTuncatedProperties, "OfflineAddressBookTuncatedProperties");
		names.put(PropertyTag.CallId, "CallId");
		names.put(PropertyTag.OfflineAddressBookLanguageId, "OfflineAddressBookLanguageId");
		names.put(PropertyTag.OfflineAddressBookFileType, "OfflineAddressBookFileType");
		names.put(PropertyTag.OfflineAddressBookCompressedSize, "OfflineAddressBookCompressedSize");
		names.put(PropertyTag.MapiFormComposeCommand, "MapiFormComposeCommand");
		names.put(PropertyTag.SearchFolderLastUsed, "SearchFolderLastUsed");
		names.put(PropertyTag.SearchFolderExpiration, "SearchFolderExpiration");
		names.put(PropertyTag.SearchFolderTemplateId, "SearchFolderTemplateId");
		names.put(PropertyTag.SearchFolderId, "SearchFolderId");
		names.put(PropertyTag.ScheduleInfoDontMailDelegates, "ScheduleInfoDontMailDelegates");
		names.put(PropertyTag.SearchFolderDefinition, "SearchFolderDefinition");
		names.put(PropertyTag.SearchFolderStorageType, "SearchFolderStorageType");
		names.put(PropertyTag.SearchFolder, "SearchFolder");
		names.put(PropertyTag.SearchFolderEfpFlags, "SearchFolderEfpFlags");
		names.put(PropertyTag.ScheduleInfoMonthsAway, "ScheduleInfoMonthsAway");
		names.put(PropertyTag.ScheduleInfoFreeBusyAway, "ScheduleInfoFreeBusyAway");

		names.put(PropertyTag.ViewDescriptorFlags, "ViewDescriptorDescriptorFlags");
		names.put(PropertyTag.ViewDescriptorLinkTo, "ViewDescriptorLinkTo");
		names.put(PropertyTag.ViewDescriptorViewFolder, "ViewDescriptorViewFolder");
		names.put(PropertyTag.ViewDescriptorName, "ViewDescriptorName");
		names.put(PropertyTag.ViewDescriptorVersion, "ViewDescriptorVersion");
		names.put(PropertyTag.Processed, "Processed");
		names.put(PropertyTag.AttachmentLinkId, "AttachmentLinkId");
		names.put(PropertyTag.ExceptionStartTime, "ExceptionStartTime");
		names.put(PropertyTag.ExceptionEndTime, "ExceptionEndTime");
		names.put(PropertyTag.AttachmentFlags, "AttachmentFlags");
		names.put(PropertyTag.AttachmentHidden, "AttachmentHidden");
		names.put(PropertyTag.AttachContactPhoto, "AttachContactPhoto");

		// There is an ambiguity with these names.
		// How can one distinguish e.g. AddressBookManagerDistinguishedName 0x8005001f and PidLidFileUnder 0x8005001f? Both
		// are address book related.
		names.put(PropertyTag.AddressBookFolderPathname, "AddressBookFolderPathname");
		names.put(PropertyTag.AddressBookManagerDistinguishedName, "AddressBookManagerDistinguishedName");
		names.put(PropertyTag.AddressBookIsMemberOfDistributionList, "AddressBookIsMemberOfDistributionList");
		names.put(PropertyTag.AddressBookMember, "AddressBookMember");
	}

	/**	Does the list of property tags contain a given one?
	*
	*	@param	tag	The property tag to look for.
	*
	*	@return	true if the given property is found in the list, false if it is not present.
	*/
	static boolean containsName(final int tag)
	{
		return names.containsKey(tag);
	}

	/**	Get the name, if any, for the given tag. Return a generic, but hopefully useful, string if the key was not found.
	*
	*	@param	tag	The tag to retrieve the descriptive name of.
	*
	*	@return	The name of the property ID corresponding to the tag, or a generic string containing the property tag if the
	*		corresponding property ID is not known.
	*/
	static String name(final int tag)
	{
		final String name = names.get(tag);
		if (name != null)
			return name;

		return String.format("propertyTag-%08x", tag);
	}
}
