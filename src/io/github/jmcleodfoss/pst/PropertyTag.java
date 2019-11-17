package io.github.jmcleodfoss.pst;

/**	The PropertyTag class holds the known property tags.
*
*	@see	io.github.jmcleodfoss.pst.PropertyID
*	@see	io.github.jmcleodfoss.pst.PropertyTagName
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">[MS-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*/
public class PropertyTag {

	public static final int NameidBucketCount = makeTag(PropertyID.NameidBucketCount, DataType.INTEGER_32);
	public static final int NameidStreamGuid = makeTag(PropertyID.NameidStreamGuid, DataType.BINARY);
	public static final int AlternateRecipientAllowed = makeTag(PropertyID.AlternateRecipientAllowed, DataType.BOOLEAN);
	public static final int NameidStreamEntry = makeTag(PropertyID.NameidStreamEntry, DataType.BINARY);
	public static final int NameidStreamString = makeTag(PropertyID.NameidStreamString, DataType.BINARY);
	public static final int LidIsRecurring = makeTag(PropertyID.LidIsRecurring, DataType.BOOLEAN);
	public static final int LidRequiredAttendees = makeTag(PropertyID.LidRequiredAttendees, DataType.STRING);
	public static final int LidOptionalAttendees = makeTag(PropertyID.LidOptionalAttendees, DataType.STRING);
	public static final int LidResourceAttendees = makeTag(PropertyID.LidResourceAttendees, DataType.STRING);
	public static final int LidDelegateMail = makeTag(PropertyID.LidDelegateMail, DataType.BOOLEAN);
	public static final int LidIsException = makeTag(PropertyID.LidIsException, DataType.BOOLEAN);
	public static final int LidTimeZone = makeTag(PropertyID.LidTimeZone, DataType.INTEGER_32);
	public static final int LidStartRecurrenceDate = makeTag(PropertyID.LidStartRecurrenceDate, DataType.INTEGER_32);
	public static final int LidStartRecurrenceTime = makeTag(PropertyID.LidStartRecurrenceTime, DataType.INTEGER_32);
	public static final int DeferredDeliveryTime = makeTag(PropertyID.DeferredDeliveryTime, DataType.TIME);
	public static final int LidEndRecurrenceTime = makeTag(PropertyID.LidEndRecurrenceTime, DataType.INTEGER_32);
	public static final int LidDayInterval = makeTag(PropertyID.LidDayInterval, DataType.INTEGER_16);
	public static final int LidMonthInterval = makeTag(PropertyID.LidMonthInterval, DataType.INTEGER_16);
	public static final int LidWeekInterval = makeTag(PropertyID.LidWeekInterval, DataType.INTEGER_16);
	public static final int LidYearInterval = makeTag(PropertyID.LidYearInterval, DataType.INTEGER_16);
	public static final int ExpiryTime = makeTag(PropertyID.ExpiryTime, DataType.TIME);
	public static final int Importance = makeTag(PropertyID.Importance, DataType.INTEGER_32);
	public static final int LidOldRecurrenceType = makeTag(PropertyID.LidOldRecurrenceType, DataType.INTEGER_16);
	public static final int MessageClass = makeTag(PropertyID.MessageClass, DataType.STRING_8);
	public static final int MessageClassW = makeTag(PropertyID.MessageClass, DataType.STRING);
	public static final int LidCalendarType = makeTag(PropertyID.LidCalendarType, DataType.INTEGER_32);
	public static final int OriginatorDeliveryReportRequested = makeTag(PropertyID.OriginatorDeliveryReportRequested, DataType.BOOLEAN);
	public static final int LidApposhortmentMessageClass = makeTag(PropertyID.LidApposhortmentMessageClass, DataType.STRING);
	public static final int Priority = makeTag(PropertyID.Priority, DataType.INTEGER_32);
	public static final int LidOldLocation = makeTag(PropertyID.LidOldLocation, DataType.STRING);
	public static final int ReadReceiptRequested = makeTag(PropertyID.ReadReceiptRequested, DataType.BOOLEAN);
	public static final int LidOldWhenEndWhole = makeTag(PropertyID.LidOldWhenEndWhole, DataType.TIME);
	public static final int RecipientReassignmentProhibited = makeTag(PropertyID.RecipientReassignmentProhibited, DataType.BOOLEAN);
	public static final int OriginalSensitivity = makeTag(PropertyID.OriginalSensitivity, DataType.INTEGER_32);
	public static final int ReplyTime = makeTag(PropertyID.ReplyTime, DataType.TIME);
	public static final int Sensitivity = makeTag(PropertyID.Sensitivity, DataType.INTEGER_32);
	public static final int Subject = makeTag(PropertyID.SubjectW, DataType.STRING_8);
	public static final int SubjectW = makeTag(PropertyID.SubjectW, DataType.STRING);
	public static final int ClientSubmitTime = makeTag(PropertyID.ClientSubmitTime, DataType.TIME);
	public static final int SentRepresentingSearchKey = makeTag(PropertyID.SentRepresentingSearchKey, DataType.BINARY);
	public static final int ReceivedByEntryId = makeTag(PropertyID.ReceivedByEntryId, DataType.BINARY);
	public static final int ReceivedByName = makeTag(PropertyID.ReceivedByName, DataType.STRING_8);
	public static final int ReceivedByNameW = makeTag(PropertyID.ReceivedByName, DataType.STRING);
	public static final int SentRepresentingEntryId = makeTag(PropertyID.SentRepresentingEntryId, DataType.BINARY);
	public static final int SentRepresentingName = makeTag(PropertyID.SentRepresentingName, DataType.STRING_8);
	public static final int SentRepresentingNameW = makeTag(PropertyID.SentRepresentingName, DataType.STRING);
	public static final int ReceivedRepresentingEntryId = makeTag(PropertyID.ReceivedRepresentingEntryId, DataType.BINARY);
	public static final int ReceivedRepresentingName = makeTag(PropertyID.ReceivedRepresentingName, DataType.STRING);
	public static final int ReadReceiptEntryId = makeTag(PropertyID.ReadReceiptEntryId, DataType.BINARY);
	public static final int MessageSubmissionId = makeTag(PropertyID.MessageSubmissionId, DataType.BINARY);
	public static final int OriginalSubject = makeTag(PropertyID.OriginalSubject, DataType.STRING);
	public static final int OriginalSubmitTime = makeTag(PropertyID.OriginalSubmitTime, DataType.TIME);
	public static final int ReplyRecipientEntries = makeTag(PropertyID.ReplyRecipientEntries, DataType.BINARY);
	public static final int ReplyRecipientNames = makeTag(PropertyID.ReplyRecipientNames, DataType.STRING_8);
	public static final int ReplyRecipientNamesW = makeTag(PropertyID.ReplyRecipientNames, DataType.STRING);
	public static final int ReceivedBySearchKey = makeTag(PropertyID.ReceivedBySearchKey, DataType.BINARY);
	public static final int ReceivedRepresentingSearchKey = makeTag(PropertyID.ReceivedRepresentingSearchKey, DataType.BINARY);
	public static final int MessageToMe = makeTag(PropertyID.MessageToMe, DataType.BOOLEAN);
	public static final int MessageCcMe = makeTag(PropertyID.MessageCcMe, DataType.BOOLEAN);
	public static final int OriginalSenderName = makeTag(PropertyID.OriginalSenderName, DataType.STRING);
	public static final int OriginalSenderEntryId = makeTag(PropertyID.OriginalSenderEntryId, DataType.STRING);
	public static final int OriginalSenderSearchKey = makeTag(PropertyID.OriginalSenderSearchKey, DataType.BINARY);
	public static final int OriginalSentRepresentingName = makeTag(PropertyID.OriginalSentRepresentingName, DataType.STRING);
	public static final int OriginalSentRepresentingEntryId = makeTag(PropertyID.OriginalSentRepresentingEntryId, DataType.BINARY);
	public static final int OriginalSentRepresentingSearchKey = makeTag(PropertyID.OriginalSentRepresentingSearchKey, DataType.BINARY);
	public static final int StartDate = makeTag(PropertyID.StartDate, DataType.TIME);
	public static final int EndDate = makeTag(PropertyID.EndDate, DataType.TIME);
	public static final int OwnerApposhortmentId = makeTag(PropertyID.OwnerApposhortmentId, DataType.INTEGER_32);
	public static final int ResponseRequested = makeTag(PropertyID.ResponseRequested, DataType.BOOLEAN);
	public static final int SentRepresentingAddressType = makeTag(PropertyID.SentRepresentingAddressType, DataType.STRING_8);
	public static final int SentRepresentingAddressTypeW = makeTag(PropertyID.SentRepresentingAddressType, DataType.STRING);
	public static final int SentRepresentingEmailAddress = makeTag(PropertyID.SentRepresentingEmailAddress, DataType.STRING_8);
	public static final int SentRepresentingEmailAddressW = makeTag(PropertyID.SentRepresentingEmailAddress, DataType.STRING);
	public static final int OriginalSenderAddressType = makeTag(PropertyID.OriginalSenderAddressType, DataType.STRING);
	public static final int OriginalSenderEmailAddress = makeTag(PropertyID.OriginalSenderEmailAddress, DataType.STRING);
	public static final int OriginalSentRepresentingAddressType = makeTag(PropertyID.OriginalSentRepresentingAddressType, DataType.STRING);
	public static final int OriginalSentRepresentingEmailAddress = makeTag(PropertyID.OriginalSentRepresentingEmailAddress, DataType.STRING);
	public static final int ConversationTopic = makeTag(PropertyID.ConversationTopic, DataType.STRING_8);
	public static final int ConversationTopicW = makeTag(PropertyID.ConversationTopic, DataType.STRING);
	public static final int ConversationIndex = makeTag(PropertyID.ConversationIndex, DataType.BINARY);
	public static final int OriginalDisplayTo = makeTag(PropertyID.OriginalDisplayTo, DataType.STRING);
	public static final int ReceivedByAddressType = makeTag(PropertyID.ReceivedByAddressType, DataType.STRING_8);
	public static final int ReceivedByAddressTypeW = makeTag(PropertyID.ReceivedByAddressType, DataType.STRING);
	public static final int ReceivedByEmailAddress = makeTag(PropertyID.ReceivedByEmailAddress, DataType.STRING_8);
	public static final int ReceivedByEmailAddressW = makeTag(PropertyID.ReceivedByEmailAddress, DataType.STRING);
	public static final int ReceivedRepresentingAddressType = makeTag(PropertyID.ReceivedRepresentingAddressType, DataType.STRING);
	public static final int RemoteHeaderLoc = makeTag(PropertyID.RemoteHeaderLoc, DataType.STRING);
	public static final int TransportMessageHeaders = makeTag(PropertyID.TransportMessageHeaders, DataType.STRING_8);
	public static final int TransportMessageHeadersW = makeTag(PropertyID.TransportMessageHeaders, DataType.STRING);
	public static final int TnefCorrelationKey = makeTag(PropertyID.TnefCorrelationKey, DataType.BINARY);
	public static final int ReportDisposition = makeTag(PropertyID.ReportDisposition, DataType.STRING_8);
	public static final int ReportDispositionMode = makeTag(PropertyID.ReportDispositionMode, DataType.STRING_8);
	public static final int ReportOriginalSender = makeTag(PropertyID.ReportOriginalSender, DataType.STRING_8);
	public static final int ReportDispositionToNames = makeTag(PropertyID.ReportDispositionToNames, DataType.STRING_8);
	public static final int ReportDispositionToEmailAddresses = makeTag(PropertyID.ReportDispositionToEmailAddresses, DataType.STRING_8);
	public static final int RecipientType = makeTag(PropertyID.RecipientType, DataType.INTEGER_32);
	public static final int ReplyRequested = makeTag(PropertyID.ReplyRequested, DataType.BOOLEAN);
	public static final int SenderEntryId = makeTag(PropertyID.SenderEntryId, DataType.BINARY);
	public static final int SenderName = makeTag(PropertyID.SenderName, DataType.STRING_8);
	public static final int SenderNameW = makeTag(PropertyID.SenderName, DataType.STRING);
	public static final int SenderSearchKey = makeTag(PropertyID.SenderSearchKey, DataType.BINARY);
	public static final int SenderAddressType = makeTag(PropertyID.SenderAddressType, DataType.STRING_8);
	public static final int SenderAddressTypeW = makeTag(PropertyID.SenderAddressType, DataType.STRING);
	public static final int SenderEmailAddress = makeTag(PropertyID.SenderEmailAddress, DataType.STRING_8);
	public static final int SenderEmailAddressW = makeTag(PropertyID.SenderEmailAddress, DataType.STRING);
	public static final int DeleteAfterSubmit = makeTag(PropertyID.DeleteAfterSubmit, DataType.BOOLEAN);
	public static final int DisplayBcc = makeTag(PropertyID.DisplayBcc, DataType.STRING_8);
	public static final int DisplayBccW = makeTag(PropertyID.DisplayBcc, DataType.STRING);
	public static final int DisplayCc = makeTag(PropertyID.DisplayCc, DataType.STRING_8);
	public static final int DisplayCcW = makeTag(PropertyID.DisplayCc, DataType.STRING);
	public static final int DisplayTo = makeTag(PropertyID.DisplayTo, DataType.STRING_8);
	public static final int DisplayToW = makeTag(PropertyID.DisplayTo, DataType.STRING);
	public static final int ParentDisplayW = makeTag(PropertyID.ParentDisplayW, DataType.STRING);
	public static final int MessageDeliveryTime = makeTag(PropertyID.MessageDeliveryTime, DataType.TIME);
	public static final int MessageFlags = makeTag(PropertyID.MessageFlags, DataType.INTEGER_32);
	public static final int MessageSize = makeTag(PropertyID.MessageSize, DataType.INTEGER_32);
	public static final int SentMailEntryId = makeTag(PropertyID.SentMailEntryId, DataType.BINARY);
	public static final int Responsibility = makeTag(PropertyID.Responsibility, DataType.BOOLEAN);
	public static final int SubmitFlags = makeTag(PropertyID.SubmitFlags, DataType.INTEGER_32);
	public static final int MessageStatus = makeTag(PropertyID.MessageStatus, DataType.INTEGER_32);
	public static final int AttachmentSize = makeTag(PropertyID.AttachmentSize, DataType.INTEGER_32);
	public static final int InternetArticleNumber = makeTag(PropertyID.InternetArticleNumber, DataType.INTEGER_32);
	public static final int PrimarySendAccount = makeTag(PropertyID.PrimarySendAccount, DataType.STRING_8);
	public static final int PrimarySendAccountW = makeTag(PropertyID.PrimarySendAccount, DataType.STRING);
	public static final int NextSendAcct = makeTag(PropertyID.NextSendAcct, DataType.STRING_8);
	public static final int NextSendAcctW = makeTag(PropertyID.NextSendAcct, DataType.STRING);
	public static final int RecordKey = makeTag(PropertyID.RecordKey, DataType.BINARY);
	public static final int ObjectType = makeTag(PropertyID.ObjectType, DataType.INTEGER_32);
	public static final int EntryId = makeTag(PropertyID.EntryId, DataType.BINARY);

	public static final int Body = makeTag(PropertyID.Body, DataType.STRING_8);
	public static final int BodyW = makeTag(PropertyID.Body, DataType.STRING);
	public static final int RtfSyncBodyCrc = makeTag(PropertyID.RtfSyncBodyCrc, DataType.INTEGER_32);
	public static final int RtfSyncBodyCount = makeTag(PropertyID.RtfSyncBodyCount , DataType.INTEGER_32);
	public static final int RtfSyncBodyTag = makeTag(PropertyID.RtfSyncBodyTag, DataType.STRING);
	public static final int RtfSyncBodyTagW = makeTag(PropertyID.RtfSyncBodyTag, DataType.STRING_8);
	public static final int RtfCompressed  = makeTag(PropertyID.RtfCompressed , DataType.BINARY);
	public static final int RtfSyncPrefixCount = makeTag(PropertyID.RtfSyncPrefixCount, DataType.INTEGER_32);
	public static final int RtfSyncTrailingCount = makeTag(PropertyID.RtfSyncTrailingCount, DataType.INTEGER_32);
	public static final int BodyHtml = makeTag(PropertyID.BodyHtml, DataType.STRING_8);
	public static final int BodyHtmlW = makeTag(PropertyID.BodyHtml, DataType.BINARY);
	public static final int InternetMessageId = makeTag(PropertyID.InternetMessageId, DataType.STRING_8);
	public static final int InternetMessageIdW = makeTag(PropertyID.InternetMessageId, DataType.STRING);
	public static final int InternetOrganization = makeTag(PropertyID.InternetOrganization, DataType.STRING);
	public static final int InReplyToId = makeTag(PropertyID.InReplyToId, DataType.STRING_8);
	public static final int InReplyToIdW = makeTag(PropertyID.InReplyToId, DataType.STRING);
	public static final int OriginalMessageId = makeTag(PropertyID.OriginalMessageId, DataType.STRING_8);
	public static final int OriginalMessageIdW = makeTag(PropertyID.OriginalMessageId, DataType.STRING);
	public static final int IconIndex = makeTag(PropertyID.IconIndex, DataType.INTEGER_32);
	public static final int LastVerbExecuted  = makeTag(PropertyID.LastVerbExecuted , DataType.INTEGER_32);
	public static final int LastVerbExecutionTime = makeTag(PropertyID.LastVerbExecutionTime, DataType.TIME);
	public static final int FlagStatus = makeTag(PropertyID.FlagStatus, DataType.INTEGER_32);
	public static final int FollowupIcon = makeTag(PropertyID.FollowupIcon, DataType.INTEGER_32);
	public static final int BlockStatus = makeTag(PropertyID.BlockStatus, DataType.INTEGER_32);
	public static final int ItemTemporaryFlags = makeTag(PropertyID.ItemTemporaryFlags, DataType.INTEGER_32);
	public static final int ConflictItems = makeTag(PropertyID.ConflictItems, DataType.MULTIPLE_BINARY);
	public static final int AttributeHidden = makeTag(PropertyID.AttributeHidden, DataType.BOOLEAN);

	public static final int RowId = makeTag(PropertyID.RowId, DataType.INTEGER_32);
	public static final int AddressType = makeTag(PropertyID.AddressType, DataType.STRING);
	public static final int EmailAddress = makeTag(PropertyID.EmailAddress, DataType.STRING_8);
	public static final int EmailAddressW = makeTag(PropertyID.EmailAddress, DataType.STRING);
	public static final int Comment = makeTag(PropertyID.Comment, DataType.STRING_8);
	public static final int CommentW = makeTag(PropertyID.Comment, DataType.STRING);
	public static final int CreationTime = makeTag(PropertyID.CreationTime, DataType.TIME);
	public static final int LastModificationTime = makeTag(PropertyID.LastModificationTime, DataType.TIME);
	public static final int SearchKey = makeTag(PropertyID.SearchKey, DataType.BINARY);
	public static final int DisplayName = makeTag(PropertyID.DisplayNameW, DataType.STRING_8);
	public static final int DisplayNameW = makeTag(PropertyID.DisplayNameW, DataType.STRING);
	public static final int ValidFolderMask = makeTag(PropertyID.ValidFolderMask, DataType.INTEGER_32);
	public static final int IpmSubTreeEntryId = makeTag(PropertyID.IpmSubTreeEntryId, DataType.BINARY);
	public static final int IpmOutboxEntryId = makeTag(PropertyID.IpmOutboxEntryId, DataType.BINARY);
	public static final int IpmWasteBasketEntryId = makeTag(PropertyID.IpmWasteBasketEntryId, DataType.BINARY);
	public static final int IpmSentMailEntryId = makeTag(PropertyID.IpmSentMailEntryId, DataType.BINARY);
	public static final int ViewsEntryId = makeTag(PropertyID.ViewsEntryId, DataType.BINARY);
	public static final int CommonViewsEntryId = makeTag(PropertyID.CommonViewsEntryId, DataType.BINARY);
	public static final int FinderEntryId = makeTag(PropertyID.FinderEntryId, DataType.BINARY);
	public static final int ContentCount = makeTag(PropertyID.ContentCount, DataType.INTEGER_32);
	public static final int ContentUnreadCount = makeTag(PropertyID.ContentUnreadCount, DataType.INTEGER_32);
	public static final int Subfolders = makeTag(PropertyID.Subfolders, DataType.BOOLEAN);
	public static final int ContainerClass = makeTag(PropertyID.ContainerClass, DataType.STRING_8);
	public static final int ContainerClassW = makeTag(PropertyID.ContainerClass, DataType.STRING);
	public static final int IpmApposhortmentEntryId = makeTag(PropertyID.IpmApposhortmentEntryId, DataType.BINARY);
	public static final int IpmContactEntryId = makeTag(PropertyID.IpmContactEntryId, DataType.BINARY);
	public static final int IpmJournalEntryId = makeTag(PropertyID.IpmJournalEntryId, DataType.BINARY);
	public static final int IpmNoteEntryId = makeTag(PropertyID.IpmNoteEntryId, DataType.BINARY);
	public static final int IpmTaskEntryId = makeTag(PropertyID.IpmTaskEntryId, DataType.BINARY);
	public static final int RemindersOnlineEntryId = makeTag(PropertyID.RemindersOnlineEntryId, DataType.BINARY);
	public static final int IpmDraftsEntryId = makeTag(PropertyID.IpmDraftsEntryId, DataType.BINARY);
	public static final int AdditionalRenEntryIds = makeTag(PropertyID.AdditionalRenEntryIds, DataType.MULTIPLE_BINARY);
	public static final int ExtendedFolderFlags = makeTag(PropertyID.ExtendedFolderFlags, DataType.BINARY);
	public static final int OrdinalMost = makeTag(PropertyID.OrdinalMost, DataType.INTEGER_32);
	public static final int FreeBusyEntryIds = makeTag(PropertyID.FreeBusyEntryIds, DataType.MULTIPLE_BINARY);

	/**	The tag to look up for the attachment when stored as an object.
	*
	*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 2.648"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee219415(v=EXCHG.80).aspx">PidTagAttachDataObject</a>
	*/
	public static final int AttachDataObject = makeTag(PropertyID.AttachDataObject, DataType.OBJECT);

	public static final int AttachDataBinary = makeTag(PropertyID.AttachDataBinary, DataType.BINARY);
	public static final int AttachEncoding = makeTag(PropertyID.AttachEncoding, DataType.BINARY);

	/**	The tag to look up for the attachment filename extension.
	*
	*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 2.650"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee218685(v=EXCHG.80).aspx">PidTagAttachExtension</a>
	*/
	public static final int AttachExtension = makeTag(PropertyID.AttachExtension, DataType.STRING_8);
	public static final int AttachExtensionW = makeTag(PropertyID.AttachExtension, DataType.STRING);

	/**	The tag to look up for the attachment filename.
	*
	*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 2.651"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee237483(v=EXCHG.80).aspx">PidTagAttachFilename</a>
	*/
	public static final int AttachFilename = makeTag(PropertyID.AttachFilename, DataType.STRING_8);
	public static final int AttachFilenameW = makeTag(PropertyID.AttachFilename, DataType.STRING);

	/**	The tag to look up for the attachment method.
	*
	*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 2.659"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee217425(v=exchg.80).aspx">PidTagAttachMethod</a>
	*/
	public static final int AttachMethod = makeTag(PropertyID.AttachMethod, DataType.INTEGER_32);

	public static final int AttachLongFilename = makeTag(PropertyID.AttachLongFilename, DataType.STRING_8);
	public static final int AttachLongFilenameW = makeTag(PropertyID.AttachLongFilename, DataType.STRING);
	public static final int AttachPathname = makeTag(PropertyID.AttachPathname, DataType.STRING_8);
	public static final int AttachPathnameW = makeTag(PropertyID.AttachPathname, DataType.STRING);
	public static final int AttachRendering = makeTag(PropertyID.AttachRendering, DataType.BINARY);
	public static final int AttachMimeSequence = makeTag(PropertyID.AttachMimeSequence, DataType.INTEGER_32);
	public static final int AttachTag = makeTag(PropertyID.AttachTag, DataType.BINARY);
	public static final int RenderingPosition = makeTag(PropertyID.RenderingPosition, DataType.INTEGER_32);
	public static final int AttachTransportName = makeTag(PropertyID.AttachTransportName, DataType.STRING);
	public static final int AttachLongPathname = makeTag(PropertyID.AttachLongPathname, DataType.STRING_8);
	public static final int AttachLongPathnameW = makeTag(PropertyID.AttachLongPathname, DataType.STRING);

	/**	The tag to look up for the attachment mime type.
	*
	*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026 section 2.660"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ee200718(v=EXCHG.80).aspx">PidTagAttachMimeType</a>
	*/
	public static final int AttachMimeTag = makeTag(PropertyID.AttachMimeTag, DataType.STRING_8);
	public static final int AttachMimeTagW = makeTag(PropertyID.AttachMimeTag, DataType.STRING);

	public static final int AttachAdditionalInfo = makeTag(PropertyID.AttachAdditionalInfo, DataType.BINARY);
	public static final int AttachContentBase = makeTag(PropertyID.AttachContentBase, DataType.STRING);
	public static final int AttachContentId = makeTag(PropertyID.AttachContentId, DataType.STRING);
	public static final int AttachContentLocation = makeTag(PropertyID.AttachContentLocation, DataType.STRING);
	public static final int AttachFlags = makeTag(PropertyID.AttachFlags, DataType.INTEGER_32);
	public static final int AttachPayloadProviderGuidString = makeTag(PropertyID.AttachPayloadProviderGuidString, DataType.STRING);
	public static final int AttachPayloadClass = makeTag(PropertyID.AttachPayloadClass, DataType.STRING);
	public static final int DisplayType = makeTag(PropertyID.DisplayType, DataType.INTEGER_32);
	public static final int SevenBitDisplayName = makeTag(PropertyID.SevenBitDisplayName, DataType.STRING);
	public static final int Account = makeTag(PropertyID.Account, DataType.STRING);
	public static final int Generation = makeTag(PropertyID.Generation, DataType.STRING);
	public static final int GivenName = makeTag(PropertyID.GivenName, DataType.STRING_8);
	public static final int GivenNameW = makeTag(PropertyID.GivenName, DataType.STRING);
	public static final int BusinessTelephoneNumber = makeTag(PropertyID.BusinessTelephoneNumber, DataType.STRING_8);
	public static final int BusinessTelephoneNumberW = makeTag(PropertyID.BusinessTelephoneNumber, DataType.STRING);
	public static final int HomeTelephoneNumber = makeTag(PropertyID.HomeTelephoneNumber, DataType.STRING_8);
	public static final int HomeTelephoneNumberW = makeTag(PropertyID.HomeTelephoneNumber, DataType.STRING);
	public static final int Initials = makeTag(PropertyID.Initials, DataType.STRING);
	public static final int Keyword = makeTag(PropertyID.Keyword, DataType.STRING);
	public static final int Language = makeTag(PropertyID.Language, DataType.STRING);
	public static final int Location = makeTag(PropertyID.Location, DataType.STRING);
	public static final int Surname = makeTag(PropertyID.Surname, DataType.STRING_8);
	public static final int SurnameW = makeTag(PropertyID.Surname, DataType.STRING);
	public static final int PostalAddress = makeTag(PropertyID.PostalAddress, DataType.STRING_8);
	public static final int PostalAddressW = makeTag(PropertyID.PostalAddress, DataType.STRING);
	public static final int CompanyName = makeTag(PropertyID.CompanyName, DataType.STRING_8);
	public static final int CompanyNameW = makeTag(PropertyID.CompanyName, DataType.STRING);
	public static final int Title = makeTag(PropertyID.Title, DataType.STRING);
	public static final int DepartmentName = makeTag(PropertyID.DepartmentName, DataType.STRING);
	public static final int Business2TelephoneNumber = makeTag(PropertyID.Business2TelephoneNumber, DataType.STRING);
	public static final int Business2TelephoneNumberW = makeTag(PropertyID.Business2TelephoneNumber, DataType.STRING_8);
	public static final int Business2TelephoneNumbers = makeTag(PropertyID.Business2TelephoneNumber, DataType.MULTIPLE_STRING);
	public static final int MobileTelephoneNumber = makeTag(PropertyID.MobileTelephoneNumber, DataType.STRING_8);
	public static final int MobileTelephoneNumberW = makeTag(PropertyID.MobileTelephoneNumber, DataType.STRING);
	public static final int OtherTelephoneNumber = makeTag(PropertyID.OtherTelephoneNumber, DataType.STRING_8);
	public static final int OtherTelephoneNumberW = makeTag(PropertyID.OtherTelephoneNumber, DataType.STRING);
	public static final int PrimaryFaxNumber = makeTag(PropertyID.PrimaryFaxNumber, DataType.STRING);
	public static final int BusinessFaxNumber = makeTag(PropertyID.BusinessFaxNumber, DataType.STRING_8);
	public static final int BusinessFaxNumberW = makeTag(PropertyID.BusinessFaxNumber, DataType.STRING);
	public static final int HomeFaxNumber = makeTag(PropertyID.HomeFaxNumber, DataType.STRING_8);
	public static final int HomeFaxNumberW = makeTag(PropertyID.HomeFaxNumber, DataType.STRING);
	public static final int Country = makeTag(PropertyID.Country, DataType.STRING);
	public static final int Locality = makeTag(PropertyID.Locality, DataType.STRING);
	public static final int StateOrProvince = makeTag(PropertyID.StateOrProvince, DataType.STRING);
	public static final int StreetAddress = makeTag(PropertyID.StreetAddress, DataType.STRING);
	public static final int PostalCode = makeTag(PropertyID.PostalCode, DataType.STRING);
	public static final int Home2TelephoneNumber = makeTag(PropertyID.Home2TelephoneNumber, DataType.STRING);
	public static final int Home2TelephoneNumberW = makeTag(PropertyID.Home2TelephoneNumber, DataType.STRING_8);
	public static final int Home2TelephoneNumbers = makeTag(PropertyID.Home2TelephoneNumber, DataType.MULTIPLE_STRING);
	public static final int SendRichInfo = makeTag(PropertyID.SendRichInfo, DataType.BOOLEAN);
	public static final int Birthday = makeTag(PropertyID.Birthday, DataType.TIME);
	public static final int MiddleName = makeTag(PropertyID.MiddleName, DataType.STRING_8);
	public static final int MiddleNameW = makeTag(PropertyID.MiddleName, DataType.STRING);
	public static final int DisplayNamePrefix = makeTag(PropertyID.DisplayNamePrefix, DataType.STRING);
	public static final int SpouseName = makeTag(PropertyID.SpouseName, DataType.STRING);
	public static final int BusinessHomePage = makeTag(PropertyID.BusinessHomePage, DataType.STRING);
	public static final int HomeAddressCity = makeTag(PropertyID.HomeAddressCity, DataType.STRING);
	public static final int HomeAddressCountry = makeTag(PropertyID.HomeAddressCountry, DataType.STRING);
	public static final int HomeAddressPostalCode = makeTag(PropertyID.HomeAddressPostalCode, DataType.STRING);
	public static final int HomeAddressStateOrProvince = makeTag(PropertyID.HomeAddressStateOrProvince, DataType.STRING);
	public static final int HomeAddressStreet = makeTag(PropertyID.HomeAddressStreet, DataType.STRING);
	public static final int HomeAddressPostOfficeBox = makeTag(PropertyID.HomeAddressPostOfficeBox, DataType.STRING);
	public static final int OtherAddressCity = makeTag(PropertyID.OtherAddressCity, DataType.STRING);
	public static final int OtherAddressCountry = makeTag(PropertyID.OtherAddressCountry, DataType.STRING);
	public static final int OtherAddressPostalCode = makeTag(PropertyID.OtherAddressPostalCode, DataType.STRING);
	public static final int OtherAddressStateOrProvince = makeTag(PropertyID.OtherAddressStateOrProvince, DataType.STRING);
	public static final int OtherAddressStreet = makeTag(PropertyID.OtherAddressStreet, DataType.STRING);
	public static final int OtherAddressPostOfficeBox = makeTag(PropertyID.OtherAddressPostOfficeBox, DataType.STRING);
	public static final int InternetCodepage = makeTag(PropertyID.InternetCodepage, DataType.INTEGER_32);
	public static final int MessageLocaleId = makeTag(PropertyID.MessageLocaleId, DataType.INTEGER_32);
	public static final int CreatorName = makeTag(PropertyID.CreatorName, DataType.STRING);
	public static final int CreatorEntryId = makeTag(PropertyID.CreatorEntryId, DataType.BINARY);
	public static final int LastModifierName = makeTag(PropertyID.LastModifierName, DataType.STRING);
	public static final int LastModifierEntryId = makeTag(PropertyID.LastModifierEntryId, DataType.BINARY);
	public static final int MessageCodepage = makeTag(PropertyID.MessageCodepage, DataType.INTEGER_32);

	public static final int SenderFlags = makeTag(PropertyID.SenderFlags, DataType.INTEGER_32);
	public static final int SentRepresentingFlags = makeTag(PropertyID.SentRepresentingFlags, DataType.INTEGER_32);
	public static final int SenderSimpleDisplayName = makeTag(PropertyID.SenderSimpleDisplayName, DataType.STRING);
	public static final int SentRepresentingSimpleDisplayName = makeTag(PropertyID.SentRepresentingSimpleDisplayName, DataType.STRING);
	public static final int CreatorSimpleDisplayName = makeTag(PropertyID.CreatorSimpleDisplayName, DataType.STRING);
	public static final int LastModifierSimpleDisplayName = makeTag(PropertyID.LastModifierSimpleDisplayName, DataType.STRING);
	public static final int ContentFilterSpamConfidenceLevel = makeTag(PropertyID.ContentFilterSpamConfidenceLevel, DataType.INTEGER_32);

	public static final int InternetMailOverrideFormat = makeTag(PropertyID.InternetMailOverrideFormat, DataType.INTEGER_32);
	public static final int MessageEditorFormat = makeTag(PropertyID.MessageEditorFormat, DataType.INTEGER_32);
	public static final int RecipientDisplayName = makeTag(PropertyID.RecipientDisplayName, DataType.STRING);

	public static final int ChangeKey = makeTag(PropertyID.ChangeKey, DataType.BINARY);
	public static final int PredecessorChangeList = makeTag(PropertyID.PredecessorChangeList, DataType.BINARY);
	public static final int PstBodyPrefix = makeTag(PropertyID.PstBodyPrefix, DataType.STRING);
	public static final int PstLrNoRestrictions = makeTag(PropertyID.PstLrNoRestrictions, DataType.BOOLEAN);
	public static final int PstHiddenCount = makeTag(PropertyID.PstHiddenCount, DataType.INTEGER_32);
	public static final int PstHiddenUnread = makeTag(PropertyID.PstHiddenUnread, DataType.INTEGER_32);
	public static final int FolderId = makeTag(PropertyID.FolderId, DataType.INTEGER_32);
	public static final int LtpParentNid = makeTag(PropertyID.LtpParentNid, DataType.INTEGER_32);
	public static final int LtpRowId = makeTag(PropertyID.LtpRowId, DataType.INTEGER_32);
	public static final int LtpRowVer = makeTag(PropertyID.LtpRowVer, DataType.INTEGER_32);
	public static final int LtpPstPassword = makeTag(PropertyID.LtpPstPassword, DataType.INTEGER_32);
	public static final int OfflineAddressBookName = makeTag(PropertyID.OfflineAddressBookName, DataType.STRING);
	public static final int VoiceMessageDuration = makeTag(PropertyID.VoiceMessageDuration, DataType.INTEGER_32);
	public static final int SenderTelephoneNumber = makeTag(PropertyID.SenderTelephoneNumber, DataType.STRING);
	public static final int SendOutlookRecallReport = makeTag(PropertyID.SendOutlookRecallReport, DataType.BOOLEAN);
	public static final int FaxNumberOfPages = makeTag(PropertyID.FaxNumberOfPages, DataType.INTEGER_32);
	public static final int OfflineAddressBookTuncatedProperties = makeTag(PropertyID.OfflineAddressBookTuncatedProperties, DataType.MULTIPLE_INTEGER_32);
	public static final int CallId = makeTag(PropertyID.CallId, DataType.STRING);
	public static final int OfflineAddressBookLanguageId = makeTag(PropertyID.OfflineAddressBookLanguageId, DataType.INTEGER_32);
	public static final int OfflineAddressBookFileType = makeTag(PropertyID.OfflineAddressBookFileType, DataType.INTEGER_32);
	public static final int OfflineAddressBookCompressedSize = makeTag(PropertyID.OfflineAddressBookCompressedSize, DataType.INTEGER_32);
	public static final int MapiFormComposeCommand = makeTag(PropertyID.MapiFormComposeCommand, DataType.STRING);
	public static final int SearchFolderLastUsed = makeTag(PropertyID.SearchFolderLastUsed, DataType.INTEGER_32);
	public static final int SearchFolderExpiration = makeTag(PropertyID.SearchFolderExpiration, DataType.INTEGER_32);
	public static final int SearchFolderTemplateId = makeTag(PropertyID.SearchFolderTemplateId, DataType.INTEGER_32);
	public static final int SearchFolderId = makeTag(PropertyID.SearchFolderId, DataType.BINARY);
	public static final int ScheduleInfoDontMailDelegates = makeTag(PropertyID.ScheduleInfoDontMailDelegates, DataType.BOOLEAN);
	public static final int SearchFolderDefinition = makeTag(PropertyID.SearchFolderDefinition, DataType.BINARY);
	public static final int SearchFolderStorageType = makeTag(PropertyID.SearchFolderStorageType, DataType.INTEGER_32);
	public static final int SearchFolder = makeTag(PropertyID.SearchFolder, DataType.INTEGER_32);
	public static final int SearchFolderEfpFlags = makeTag(PropertyID.SearchFolderEfpFlags, DataType.INTEGER_32);
	public static final int ScheduleInfoMonthsAway = makeTag(PropertyID.ScheduleInfoMonthsAway, DataType.MULTIPLE_INTEGER_32);
	public static final int ScheduleInfoFreeBusyAway = makeTag(PropertyID.ScheduleInfoFreeBusyAway, DataType.MULTIPLE_BINARY);

	public static final int ViewDescriptorFlags = makeTag(PropertyID.ViewDescriptorFlags, DataType.INTEGER_32);
	public static final int ViewDescriptorLinkTo = makeTag(PropertyID.ViewDescriptorLinkTo, DataType.BINARY);
	public static final int ViewDescriptorViewFolder = makeTag(PropertyID.ViewDescriptorViewFolder, DataType.BINARY);
	public static final int ViewDescriptorName = makeTag(PropertyID.ViewDescriptorName, DataType.STRING);
	public static final int ViewDescriptorVersion = makeTag(PropertyID.ViewDescriptorVersion, DataType.INTEGER_32);
	public static final int Processed = makeTag(PropertyID.Processed, DataType.BOOLEAN);
	public static final int AttachmentLinkId = makeTag(PropertyID.AttachmentLinkId, DataType.INTEGER_32);
	public static final int ExceptionStartTime = makeTag(PropertyID.ExceptionStartTime, DataType.TIME);
	public static final int ExceptionEndTime = makeTag(PropertyID.ExceptionEndTime, DataType.TIME);
	public static final int AttachmentFlags = makeTag(PropertyID.AttachmentFlags, DataType.INTEGER_32);
	public static final int AttachmentHidden = makeTag(PropertyID.AttachmentHidden, DataType.BOOLEAN);
	public static final int AttachContactPhoto = makeTag(PropertyID.AttachContactPhoto,  DataType.BOOLEAN);

	// Named Properties (0x8000 to 0x8fff)
	public static final int AddressBookFolderPathname = makeTag(PropertyID.AddressBookFolderPathname, DataType.STRING);
	public static final int AddressBookManagerDistinguishedName = makeTag(PropertyID.AddressBookManagerDistinguishedName, DataType.STRING);
	public static final int FileUnderId = makeTag(PropertyID.FileUnderId, DataType.STRING);
	public static final int AddressBookHomeMessageDatabase = makeTag(PropertyID.AddressBookHomeMessageDatabase, DataType.STRING);
	public static final int AddressBookHomeMessageTransferAgent = makeTag(PropertyID.AddressBookHomeMessageTransferAgent, DataType.STRING);
	public static final int ContactItemData = makeTag(PropertyID.ContactItemData, DataType.MULTIPLE_INTEGER_32);
	public static final int AddressBookIsMemberOfDistributionList = makeTag(PropertyID.AddressBookIsMemberOfDistributionList, DataType.MULTIPLE_STRING);
	public static final int TcvConstLongOne = makeTag(PropertyID.TcvConstLongOne, DataType.INTEGER_32);
	public static final int AddressBookMember = makeTag(PropertyID.AddressBookMember, DataType.MULTIPLE_STRING);
	public static final int ReferredBy = makeTag(PropertyID.ReferredBy, DataType.STRING);
	public static final int Department = makeTag(PropertyID.Department, DataType.STRING);
	public static final int HasPicture = makeTag(PropertyID.HasPicture, DataType.BOOLEAN);
	public static final int HomeAddress = makeTag(PropertyID.HomeAddress, DataType.STRING);
	public static final int WorkAddress = makeTag(PropertyID.WorkAddress, DataType.STRING);
	public static final int OtherAddress = makeTag(PropertyID.OtherAddress, DataType.STRING);
	public static final int PostalAddressId = makeTag(PropertyID.PostalAddressId, DataType.INTEGER_32);
	public static final int ContactCharacterSet = makeTag(PropertyID.ContactCharacterSet, DataType.INTEGER_32);
	public static final int AutoLog = makeTag(PropertyID.AutoLog, DataType.BOOLEAN);
	public static final int FileUnderList = makeTag(PropertyID.FileUnderList, DataType.MULTIPLE_INTEGER_32);
	public static final int EmailList = makeTag(PropertyID.EmailList, DataType.MULTIPLE_INTEGER_32);
	public static final int AddressBookProviderEmailList = makeTag(PropertyID.AddressBookProviderEmailList, DataType.MULTIPLE_INTEGER_32);
	public static final int AddressBookProviderArrayType = makeTag(PropertyID.AddressBookProviderArrayType, DataType.INTEGER_32);
	public static final int Html = makeTag(PropertyID.Html, DataType.STRING);
	public static final int YomiFirstName = makeTag(PropertyID.YomiFirstName, DataType.STRING);
	public static final int YomiLastName = makeTag(PropertyID.YomiLastName, DataType.STRING);
	public static final int YomiCompanyName = makeTag(PropertyID.YomiCompanyName, DataType.STRING);
	public static final int WorkAddressStreet = makeTag(PropertyID.WorkAddressStreet, DataType.STRING);
	public static final int WorkAddressCity = makeTag(PropertyID.WorkAddressCity, DataType.STRING);
	public static final int WorkAddressState = makeTag(PropertyID.WorkAddressState, DataType.STRING);
	public static final int WorkAddressPostalCode = makeTag(PropertyID.WorkAddressPostalCode, DataType.STRING);
	public static final int WorkAddressCountry = makeTag(PropertyID.WorkAddressCountry, DataType.STRING);
	public static final int WorkAddressPostOfficeBox = makeTag(PropertyID.WorkAddressPostOfficeBox, DataType.STRING);
	public static final int DistributionListChecksum = makeTag(PropertyID.DistributionListChecksum, DataType.INTEGER_32);
	public static final int ContactUserField1 = makeTag(PropertyID.ContactUserField1, DataType.STRING);
	public static final int ContactUserField2 = makeTag(PropertyID.ContactUserField2, DataType.STRING);
	public static final int ContactUserField3 = makeTag(PropertyID.ContactUserField3, DataType.STRING);
	public static final int ContactUserField4 = makeTag(PropertyID.ContactUserField4, DataType.STRING);
	public static final int DistributionListName = makeTag(PropertyID.DistributionListName, DataType.STRING);
	public static final int DistributionListOneOffMembers = makeTag(PropertyID.DistributionListOneOffMembers, DataType.MULTIPLE_BINARY);
	public static final int DistributionListMembers = makeTag(PropertyID.DistributionListMembers, DataType.MULTIPLE_BINARY);
	public static final int InstantMessagingAddress = makeTag(PropertyID.InstantMessagingAddress, DataType.STRING);
	public static final int Email1DisplayName = makeTag(PropertyID.Email1DisplayName, DataType.STRING);
	public static final int Email1AddressType = makeTag(PropertyID.Email1AddressType, DataType.STRING);
	public static final int Email1EmailAddress = makeTag(PropertyID.Email1EmailAddress, DataType.STRING);
	public static final int Email1OriginalDisplayName = makeTag(PropertyID.Email1OriginalDisplayName, DataType.STRING);
	public static final int Email1OriginalEntryId = makeTag(PropertyID.Email1OriginalEntryId, DataType.BINARY);
	public static final int Email1RichTextFormat = makeTag(PropertyID.Email1RichTextFormat, DataType.BOOLEAN);
	public static final int Email2DisplayName = makeTag(PropertyID.Email2DisplayName, DataType.STRING);
	public static final int Email2AddressType = makeTag(PropertyID.Email2AddressType, DataType.STRING);
	public static final int Email2EmailAddress = makeTag(PropertyID.Email2EmailAddress, DataType.STRING);
	public static final int Email2OriginalDisplayName = makeTag(PropertyID.Email2OriginalDisplayName, DataType.STRING);
	public static final int Email2OriginalEntryId = makeTag(PropertyID.Email2OriginalEntryId, DataType.BINARY);
	public static final int Email2RichTextFormat = makeTag(PropertyID.Email2RichTextFormat, DataType.BOOLEAN);
	public static final int Email3DisplayName = makeTag(PropertyID.Email3DisplayName, DataType.STRING);
	public static final int Email3AddressType = makeTag(PropertyID.Email3AddressType, DataType.STRING);
	public static final int Email3EmailAddress = makeTag(PropertyID.Email3EmailAddress, DataType.STRING);
	public static final int Email3OriginalEntryId = makeTag(PropertyID.Email3OriginalEntryId, DataType.BINARY);
	public static final int Email3OriginalDisplayName = makeTag(PropertyID.Email3OriginalDisplayName, DataType.STRING);
	public static final int Email3RichTextFormat = makeTag(PropertyID.Email3RichTextFormat, DataType.BOOLEAN);
	public static final int Fax1AddressType = makeTag(PropertyID.Fax1AddressType, DataType.STRING);
	public static final int Fax1EmailAddress = makeTag(PropertyID.Fax1EmailAddress, DataType.STRING);
	public static final int Fax1OriginalDisplayName = makeTag(PropertyID.Fax1OriginalDisplayName, DataType.STRING);
	public static final int Fax1OriginalEntryId = makeTag(PropertyID.Fax1OriginalEntryId, DataType.BINARY);
	public static final int Fax2AddressType = makeTag(PropertyID.Fax2AddressType, DataType.STRING);
	public static final int Fax2EmailAddress = makeTag(PropertyID.Fax2EmailAddress, DataType.STRING);
	public static final int Fax2OriginalDisplayName = makeTag(PropertyID.Fax2OriginalDisplayName, DataType.STRING);
	public static final int Fax2OriginalEntryId = makeTag(PropertyID.Fax2OriginalEntryId, DataType.BINARY);
	public static final int Fax3AddressType = makeTag(PropertyID.Fax3AddressType, DataType.STRING);
	public static final int Fax3EmailAddress = makeTag(PropertyID.Fax3EmailAddress, DataType.STRING);
	public static final int Fax3OriginalDisplayName = makeTag(PropertyID.Fax3OriginalDisplayName, DataType.STRING);
	public static final int Fax3OriginalEntryId = makeTag(PropertyID.Fax3OriginalEntryId, DataType.BINARY);
	public static final int FreeBusyLocation = makeTag(PropertyID.FreeBusyLocation, DataType.STRING);
	public static final int HomeAddressCountryCode = makeTag(PropertyID.HomeAddressCountryCode, DataType.STRING);
	public static final int WorkAddressCountryCode = makeTag(PropertyID.WorkAddressCountryCode, DataType.STRING);
	public static final int OtherAddressCountryCode = makeTag(PropertyID.OtherAddressCountryCode, DataType.STRING);
	public static final int AddressCountryCode = makeTag(PropertyID.AddressCountryCode, DataType.STRING);

	public static final int TaskStatus = makeTag(PropertyID.TaskStatus, DataType.INTEGER_32);
	public static final int PercentComplete = makeTag(PropertyID.PercentComplete, DataType.FLOATING_64);
	public static final int TeamTask  = makeTag(PropertyID.TeamTask , DataType.BOOLEAN);
	public static final int TaskStartDate = makeTag(PropertyID.TaskStartDate, DataType.TIME);
	public static final int TaskDueDate = makeTag(PropertyID.TaskDueDate, DataType.TIME);
	public static final int TaskResetReminder = makeTag(PropertyID.TaskResetReminder, DataType.BOOLEAN);
	public static final int TaskAccepted = makeTag(PropertyID.TaskAccepted, DataType.BOOLEAN);
	public static final int TaskDeadOccurrence = makeTag(PropertyID.TaskDeadOccurrence, DataType.BOOLEAN);
	public static final int TaskDateCompleted = makeTag(PropertyID.TaskDateCompleted, DataType.TIME);
	public static final int TaskActualEffort = makeTag(PropertyID.TaskActualEffort, DataType.INTEGER_32);
	public static final int TaskEstimatedEffort = makeTag(PropertyID.TaskEstimatedEffort, DataType.INTEGER_32);
	public static final int TaskVersion = makeTag(PropertyID.TaskVersion, DataType.INTEGER_32);
	public static final int TaskState = makeTag(PropertyID.TaskState, DataType.INTEGER_32);
	public static final int TaskLastUpdate = makeTag(PropertyID.TaskLastUpdate, DataType.TIME);
	public static final int TaskRecurrence = makeTag(PropertyID.TaskRecurrence, DataType.BINARY);
	public static final int TaskAssigners = makeTag(PropertyID.TaskAssigners, DataType.BINARY);
	public static final int TaskStatusOnComplete = makeTag(PropertyID.TaskStatusOnComplete, DataType.BOOLEAN);
	public static final int TaskHistory = makeTag(PropertyID.TaskHistory, DataType.INTEGER_32);
	public static final int TaskUpdates = makeTag(PropertyID.TaskUpdates, DataType.BOOLEAN);
	public static final int TaskComplete = makeTag(PropertyID.TaskComplete, DataType.BOOLEAN);
	public static final int TaskFCreator = makeTag(PropertyID.TaskFCreator, DataType.BOOLEAN);
	public static final int TaskOwner = makeTag(PropertyID.TaskOwner, DataType.STRING);
	public static final int TaskMultipleRecipients = makeTag(PropertyID.TaskMultipleRecipients, DataType.INTEGER_32);
	public static final int TaskAssigner = makeTag(PropertyID.TaskAssigner, DataType.STRING);
	public static final int TaskLastUser = makeTag(PropertyID.TaskLastUser, DataType.STRING);
	public static final int TaskOrdinal = makeTag(PropertyID.TaskOrdinal, DataType.INTEGER_32);
	public static final int TaskNoCompute = makeTag(PropertyID.TaskNoCompute, DataType.BOOLEAN);
	public static final int TaskLastDelegate = makeTag(PropertyID.TaskLastDelegate, DataType.STRING);
	public static final int TaskFRecurring = makeTag(PropertyID.TaskFRecurring, DataType.BOOLEAN);
	public static final int TaskRole = makeTag(PropertyID.TaskRole, DataType.STRING);
	public static final int TaskOwnership = makeTag(PropertyID.TaskOwnership, DataType.INTEGER_32);
	public static final int TaskAcceptanceState = makeTag(PropertyID.TaskAcceptanceState, DataType.INTEGER_32);
	public static final int TaskFFixOffline = makeTag(PropertyID.TaskFFixOffline, DataType.BOOLEAN);

	public static final int AppointmentSequence = makeTag(PropertyID.AppointmentSequence, DataType.INTEGER_32);
	public static final int AppointmentSequenceTime = makeTag(PropertyID.AppointmentSequenceTime, DataType.TIME);
	public static final int AppointmentLastSequence = makeTag(PropertyID.AppointmentLastSequence, DataType.INTEGER_32);
	public static final int ChangeHighlight = makeTag(PropertyID.ChangeHighlight, DataType.INTEGER_32);
	public static final int BusyStatus = makeTag(PropertyID.BusyStatus, DataType.INTEGER_32);
	public static final int FExceptionalBody = makeTag(PropertyID.FExceptionalBody, DataType.BOOLEAN);
	public static final int AppointmentAuxiliaryFlags = makeTag(PropertyID.AppointmentAuxiliaryFlags, DataType.INTEGER_32);
	public static final int AppointmentLocation = makeTag(PropertyID.Location, DataType.STRING);
	public static final int ForwardInstance = makeTag(PropertyID.ForwardInstance, DataType.BOOLEAN);
	public static final int MeetingWorkspaceURL = makeTag(PropertyID.MeetingWorkspaceURL, DataType.STRING);
	public static final int AppointmentStartWhole = makeTag(PropertyID.AppointmentStartWhole, DataType.TIME);
	public static final int AppointmentEndWhole = makeTag(PropertyID.AppointmentEndWhole, DataType.TIME);
	public static final int AppointmentStartTime = makeTag(PropertyID.AppointmentStartTime, DataType.TIME);
	public static final int AppointmentEndType = makeTag(PropertyID.AppointmentEndType, DataType.TIME);
	public static final int AppointmentEndDate = makeTag(PropertyID.AppointmentEndDate, DataType.TIME);
	public static final int AppointmentStartDate = makeTag(PropertyID.AppointmentStartDate, DataType.TIME);
	public static final int AppointmentDuration = makeTag(PropertyID.AppointmentDuration, DataType.INTEGER_32);
	public static final int AppointmentColor = makeTag(PropertyID.AppointmentColor, DataType.INTEGER_32);
	public static final int AppointmentSubType = makeTag(PropertyID.AppointmentSubType, DataType.BOOLEAN);
	public static final int AppointmentRecur = makeTag(PropertyID.AppointmentRecur, DataType.BINARY);
	public static final int AppointmentStateFlags = makeTag(PropertyID.AppointmentStateFlags, DataType.INTEGER_32);
	public static final int ResponseStatus = makeTag(PropertyID.ResponseStatus, DataType.INTEGER_32);
	public static final int AppointmentReplyTime = makeTag(PropertyID.AppointmentReplyTime, DataType.TIME);
	public static final int Recurring = makeTag(PropertyID.Recurring, DataType.BOOLEAN);
	public static final int IntendedBusyStatus = makeTag(PropertyID.IntendedBusyStatus, DataType.INTEGER_32);
	public static final int AppointmentUpdateTime = makeTag(PropertyID.AppointmentUpdateTime, DataType.TIME);
	public static final int ExceptionReplaceTime = makeTag(PropertyID.ExceptionReplaceTime, DataType.TIME);
	public static final int FInvited = makeTag(PropertyID.FInvited, DataType.BOOLEAN);
	public static final int ExceptionalAttendees = makeTag(PropertyID.ExceptionalAttendees, DataType.BOOLEAN);
	public static final int OwnerName = makeTag(PropertyID.OwnerName, DataType.STRING);
	public static final int AppointmentReplyName = makeTag(PropertyID.AppointmentReplyName, DataType.STRING);
	public static final int RecurrenceType = makeTag(PropertyID.RecurrenceType, DataType.BOOLEAN);
	public static final int RecurrencePattern = makeTag(PropertyID.RecurrencePattern, DataType.STRING_8);
	public static final int RecurrencePatternW = makeTag(PropertyID.RecurrencePattern, DataType.STRING);
	public static final int TimeZoneStruct = makeTag(PropertyID.TimeZoneStruct, DataType.BINARY);
	public static final int TimeZoneDescription = makeTag(PropertyID.TimeZoneDescription, DataType.STRING);
	public static final int ClipStart = makeTag(PropertyID.ClipStart, DataType.TIME);
	public static final int ClipEnd = makeTag(PropertyID.ClipEnd, DataType.TIME);
	public static final int OriginalStoreEntryId = makeTag(PropertyID.OriginalStoreEntryId, DataType.BINARY);
	public static final int AllAttendeesString = makeTag(PropertyID.AllAttendeesString, DataType.STRING);
	public static final int AutoFillAppointment = makeTag(PropertyID.AutoFillAppointment, DataType.BOOLEAN);
	public static final int ToAttendeesString = makeTag(PropertyID.ToAttendeesString, DataType.STRING);
	public static final int CcAttendeesString = makeTag(PropertyID.CcAttendeesString, DataType.STRING);
	public static final int TrustRecipientHighlights = makeTag(PropertyID.TrustRecipientHighlights, DataType.BOOLEAN);
	public static final int ConferencingCheck = makeTag(PropertyID.ConferencingCheck, DataType.BOOLEAN);
	public static final int ConferencingType = makeTag(PropertyID.ConferencingType, DataType.INTEGER_32);
	public static final int Directory = makeTag(PropertyID.Directory, DataType.STRING);
	public static final int OrganizerAlias = makeTag(PropertyID.OrganizerAlias, DataType.STRING);
	public static final int AutoStartCheck = makeTag(PropertyID.AutoStartCheck, DataType.BOOLEAN);
	public static final int AllowExternalCheck = makeTag(PropertyID.AllowExternalCheck, DataType.BOOLEAN);
	public static final int CollaborateDoc = makeTag(PropertyID.CollaborateDoc, DataType.STRING);
	public static final int NetShowURL = makeTag(PropertyID.NetShowURL, DataType.STRING);
	public static final int OnlinePassword = makeTag(PropertyID.OnlinePassword, DataType.STRING);
	public static final int AppointmentProposedStartWhole = makeTag(PropertyID.AppointmentProposedStartWhole, DataType.TIME);
	public static final int AppointmentProposedEndWhole = makeTag(PropertyID.AppointmentProposedEndWhole, DataType.TIME);
	public static final int AppointmentProposedDuration = makeTag(PropertyID.AppointmentProposedDuration, DataType.INTEGER_32);
	public static final int AppointmentCounterProposal = makeTag(PropertyID.AppointmentCounterProposal, DataType.BOOLEAN);
	public static final int AppointmentProposalNumber = makeTag(PropertyID.AppointmentProposalNumber, DataType.INTEGER_32);
	public static final int AppointmentNotAllowPropose = makeTag(PropertyID.AppointmentNotAllowPropose, DataType.BOOLEAN);
	public static final int AppointmentTimeZoneDefinitionStartDisplay = makeTag(PropertyID.AppointmentTimeZoneDefinitionStartDisplay, DataType.BINARY);
	public static final int AppointmentTimeZoneDefinitionEndDisplay = makeTag(PropertyID.AppointmentTimeZoneDefinitionEndDisplay, DataType.BINARY);
	public static final int AppointmentTimeZoneDefinitionRecur = makeTag(PropertyID.AppointmentTimeZoneDefinitionRecur, DataType.BINARY);

	public static final int ReminderDelta = makeTag(PropertyID.ReminderDelta, DataType.INTEGER_32);
	public static final int ReminderTime = makeTag(PropertyID.ReminderTime, DataType.TIME);
	public static final int ReminderSet  = makeTag(PropertyID.ReminderSet , DataType.BOOLEAN);
	public static final int Private = makeTag(PropertyID.Private, DataType.BOOLEAN);
	public static final int AgingDontAgeMe = makeTag(PropertyID.AgingDontAgeMe, DataType.BOOLEAN);
	public static final int SideEffects = makeTag(PropertyID.SideEffects, DataType.INTEGER_32);
	public static final int RemoteStatus = makeTag(PropertyID.RemoteStatus, DataType.INTEGER_32);
	public static final int SmartNoAttach = makeTag(PropertyID.SmartNoAttach, DataType.BOOLEAN);
	public static final int CommonStart = makeTag(PropertyID.CommonStart, DataType.TIME);
	public static final int CommonEnd = makeTag(PropertyID.CommonEnd, DataType.TIME);
	public static final int TaskMode = makeTag(PropertyID.TaskMode, DataType.INTEGER_32);
	public static final int TaskGlobalId = makeTag(PropertyID.TaskGlobalId, DataType.BINARY);
	public static final int AutoProcessState = makeTag(PropertyID.AutoProcessState, DataType.INTEGER_32);
	public static final int ReminderOverride = makeTag(PropertyID.ReminderOverride, DataType.BOOLEAN);
	public static final int NonSendableCc = makeTag(PropertyID.NonSendableCc, DataType.STRING);
	public static final int NonSendableBcc = makeTag(PropertyID.NonSendableBcc, DataType.STRING);
	public static final int Companies = makeTag(PropertyID.Companies, DataType.MULTIPLE_STRING);
	public static final int ReminderType = makeTag(PropertyID.ReminderType, DataType.INTEGER_32);
	public static final int ReminderPlaySound = makeTag(PropertyID.ReminderPlaySound, DataType.BOOLEAN);
	public static final int ReminderFileParameter = makeTag(PropertyID.ReminderFileParameter, DataType.STRING);
	public static final int VerbStream = makeTag(PropertyID.VerbStream, DataType.BINARY);
	public static final int VerbResponse = makeTag(PropertyID.VerbResponse, DataType.STRING);
	public static final int FlagRequest = makeTag(PropertyID.FlagRequest, DataType.STRING);
	public static final int Billing = makeTag(PropertyID.Billing, DataType.STRING);
	public static final int NonSendableTo = makeTag(PropertyID.NonSendableTo, DataType.STRING);
	public static final int NonSendToTrackStatus = makeTag(PropertyID.NonSendToTrackStatus, DataType.MULTIPLE_INTEGER_32);
	public static final int NonSendCcTrackStatus  = makeTag(PropertyID.NonSendCcTrackStatus , DataType.MULTIPLE_INTEGER_32);
	public static final int NonSendBccTrackStatus = makeTag(PropertyID.NonSendBccTrackStatus, DataType.MULTIPLE_INTEGER_32);
	public static final int Contacts = makeTag(PropertyID.Contacts, DataType.MULTIPLE_STRING);
	public static final int CurrentVersion = makeTag(PropertyID.CurrentVersion, DataType.INTEGER_32);
	public static final int CurrentVersionName = makeTag(PropertyID.CurrentVersionName, DataType.STRING);
	public static final int ReminderSignalTime = makeTag(PropertyID.ReminderSignalTime, DataType.TIME);
	public static final int InternetAccountName = makeTag(PropertyID.InternetAccountName, DataType.STRING);
	public static final int InternetAccountStamp = makeTag(PropertyID.InternetAccountStamp, DataType.STRING);
	public static final int UseTnef = makeTag(PropertyID.UseTnef, DataType.BOOLEAN);
	public static final int ContactLinkSearchKey = makeTag(PropertyID.ContactLinkSearchKey, DataType.BINARY);
	public static final int ContactLinkEntry = makeTag(PropertyID.ContactLinkEntry, DataType.BINARY);
	public static final int ContactLinkName = makeTag(PropertyID.ContactLinkName, DataType.STRING);
	public static final int SpamOriginalFolder = makeTag(PropertyID.SpamOriginalFolder, DataType.BINARY);
	public static final int ValidFlagStringProof = makeTag(PropertyID.ValidFlagStringProof, DataType.TIME);

	public static final int NoteColor = makeTag(PropertyID.NoteColor, DataType.INTEGER_32);
	public static final int NoteWidth = makeTag(PropertyID.NoteWidth, DataType.INTEGER_32);
	public static final int NoteHeight = makeTag(PropertyID.NoteHeight, DataType.INTEGER_32);
	public static final int NoteX = makeTag(PropertyID.NoteX, DataType.INTEGER_32);
	public static final int NoteY = makeTag(PropertyID.NoteY, DataType.INTEGER_32);

	/**	Make a tag from a property ID and data type.
	*
	*	@param	propertyId	The property ID from which to create the tag.
	*	@param	dataType	The data type of the property the tag represents.
	*
	*	@return	The tag for the given property ID and data type.
	*/
	private static int makeTag(final short propertyId, final short dataType)
	{
		return (propertyId << 16) | dataType;
	}
}
