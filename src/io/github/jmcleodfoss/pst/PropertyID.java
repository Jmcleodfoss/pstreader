package io.github.jmcleodfoss.pst;

/**	The PropertyID class contains the names of known properties as used in the Property Context and Table Context structures.
*	Note that in the context of property ID's, a property ID is the high word of the tag, and the data type is the low word.
*
*	@see	io.github.jmcleodfoss.pst.PropertyIDByGUID
*	@see	io.github.jmcleodfoss.pst.PropertyTag
*	@see	io.github.jmcleodfoss.pst.PropertyTagName
*	@see	"[MS-OXPROPS] Exchange Server Protocols Master Property List v20101026"
*	@see	<a href="http://msdn.microsoft.com/en-us/library/cc433490(v=EXCHG.80).aspx">[MS-OXPROPS]: Exchange Server Protocols Master Property List (MSDN)</a>
*/
public class PropertyID {

	static final short NameidBucketCount = 0x0001;
	static final short NameidStreamGuid = 0x0002;
	static final short AlternateRecipientAllowed = 0x0002;
	static final short NameidStreamEntry = 0x0003;
	static final short NameidStreamString = 0x0004;
	static final short LidIsRecurring = 0x0005;
	static final short LidRequiredAttendees = 0x0006;
	static final short LidOptionalAttendees = 0x0007;
	static final short LidResourceAttendees = 0x0008;
	static final short LidDelegateMail = 0x0009;
	static final short LidIsException = 0x000a;
	static final short LidTimeZone = 0x000c;
	static final short LidStartRecurrenceDate = 0x000d;
	static final short LidStartRecurrenceTime = 0x000e;
	static final short DeferredDeliveryTime = 0x000f;
	static final short LidEndRecurrenceTime = 0x0010;
	static final short LidDayInterval = 0x0011;
	static final short LidWeekInterval = 0x0012;
	static final short LidMonthInterval = 0x0013;
	static final short LidYearInterval = 0x0014;
	static final short ExpiryTime = 0x0015;
	static final short Importance = 0x0017;
	static final short LidOldRecurrenceType = 0x0018;
	static final short MessageClass = 0x001a;
	static final short LidCalendarType = 0x001c;
	static final short OriginatorDeliveryReportRequested = 0x0023;
	static final short LidApposhortmentMessageClass = 0x0024;
	static final short Priority = 0x0026;
	static final short LidOldLocation = 0x0028;
	static final short ReadReceiptRequested = 0x0029;
	static final short LidOldWhenEndWhole = 0x002a;
	static final short RecipientReassignmentProhibited = 0x002b;
	static final short OriginalSensitivity = 0x002e;
	static final short ReplyTime = 0x0030;
	static final short Sensitivity = 0x0036;
	static final short SubjectW = 0x0037;
	static final short ClientSubmitTime = 0x0039;
	static final short SentRepresentingSearchKey = 0x003b;
	static final short ReceivedByEntryId = 0x003f;
	static final short ReceivedByName = 0x0040;
	static final short SentRepresentingEntryId = 0x0041;
	static final short SentRepresentingName = 0x0042;
	static final short ReceivedRepresentingEntryId = 0x0043;
	static final short ReceivedRepresentingName  = 0x0044;
	static final short ReadReceiptEntryId = 0x0046;
	static final short MessageSubmissionId = 0x0047;
	static final short OriginalSubject = 0x0049;
	static final short OriginalSubmitTime = 0x004e;
	static final short ReplyRecipientEntries = 0x004f;
	static final short ReplyRecipientNames = 0x0050;
	static final short ReceivedBySearchKey = 0x0051;
	static final short ReceivedRepresentingSearchKey = 0x0052;
	static final short MessageToMe = 0x0057;
	static final short MessageCcMe = 0x0058;
	static final short OriginalSenderName = 0x005a;
	static final short OriginalSenderEntryId = 0x005b;
	static final short OriginalSenderSearchKey = 0x005c;
	static final short OriginalSentRepresentingName = 0x005d;
	static final short OriginalSentRepresentingEntryId = 0x005e;
	static final short OriginalSentRepresentingSearchKey = 0x005f;
	static final short StartDate = 0x0060;
	static final short EndDate = 0x0061;
	static final short OwnerApposhortmentId = 0x0062;
	static final short ResponseRequested = 0x0063;
	static final short SentRepresentingAddressType = 0x0064;
	static final short SentRepresentingEmailAddress = 0x0065;
	static final short OriginalSenderAddressType = 0x0066;
	static final short OriginalSenderEmailAddress = 0x0067;
	static final short OriginalSentRepresentingAddressType = 0x0068;
	static final short OriginalSentRepresentingEmailAddress = 0x0069;
	static final short ConversationTopic = 0x0070;
	static final short ConversationIndex = 0x0071;
	static final short OriginalDisplayTo = 0x0074;
	static final short ReceivedByAddressType = 0x0075;
	static final short ReceivedByEmailAddress = 0x0076;
	static final short ReceivedRepresentingAddressType = 0x0077;
	static final short RemoteHeaderLoc = 0x0078;
	static final short TransportMessageHeaders = 0x007d;
	static final short TnefCorrelationKey = 0x007f;
	static final short ReportDisposition = 0x0080;
	static final short ReportDispositionMode = 0x0081;
	static final short ReportOriginalSender = 0x0082;
	static final short ReportDispositionToNames = 0x0083;
	static final short ReportDispositionToEmailAddresses = 0x0084;
	static final short RecipientType = 0x0c15;
	static final short ReplyRequested = 0x0c17;
	static final short SenderEntryId = 0x0c19;
	static final short SenderName = 0x0c1a;
	static final short SenderSearchKey = 0x0c1d;
	static final short SenderAddressType = 0x0c1e;
	static final short SenderEmailAddress = 0x0c1f;
	static final short DeleteAfterSubmit = 0x0e01;
	static final short DisplayBcc = 0x0e02;
	static final short DisplayCc = 0x0e03;
	static final short DisplayTo = 0x0e04;
	static final short ParentDisplayW = 0x0e05;
	static final short MessageDeliveryTime = 0x0e06;
	static final short MessageFlags = 0x0e07;
	static final short MessageSize = 0x0e08;
	static final short SentMailEntryId = 0x0e0a;
	static final short Responsibility = 0x0e0f;
	static final short SubmitFlags = 0x0e14;
	static final short MessageStatus = 0x0e17;
	static final short AttachmentSize = 0x0e20;
	static final short InternetArticleNumber = 0x0e23;
	static final short PrimarySendAccount = 0x0e28;
	static final short NextSendAcct = 0x0e29;
	static final short RecordKey = 0x0ff9;
	static final short ObjectType = 0x0ffe;
	static final short EntryId = 0x0fff;

	static final short NameToIdMapBucketFirst = 0x1000;
	static final short NameToIdMapBucketLast = 0x2fff;

	static final short Body = 0x1000;
	static final short RtfSyncBodyCrc = 0x1006;
	static final short RtfSyncBodyCount = 0x1007;
	static final short RtfSyncBodyTag = 0x1008;
	static final short RtfCompressed  = 0x1009;
	static final short RtfSyncPrefixCount = 0x1010;
	static final short RtfSyncTrailingCount = 0x1011;
	static final short BodyHtml = 0x1013;
	static final short InternetMessageId = 0x1035;
	static final short InternetOrganization = 0x1037;
	static final short InReplyToId = 0x1042; 
	static final short OriginalMessageId = 0x1046;
	static final short IconIndex = 0x1080;
	static final short LastVerbExecuted = 0x1081;
	static final short LastVerbExecutionTime = 0x1082;
	static final short FlagStatus = 0x1090;
	static final short FollowupIcon = 0x1095;
	static final short BlockStatus = 0x1096;
	static final short ItemTemporaryFlags = 0x1097;
	static final short ConflictItems = 0x1098;
	static final short AttributeHidden = 0x10f4;

	static final short RowId = 0x3000;
	static final short DisplayNameW = 0x3001;
	static final short AddressType = 0x3002;
	static final short EmailAddress = 0x3003;
	static final short Comment = 0x3004;
	static final short CreationTime = 0x3007;
	static final short LastModificationTime = 0x3008;
	static final short SearchKey = 0x300b;
	static final short ValidFolderMask = 0x35df;
	static final short IpmSubTreeEntryId = 0x35e0;
	static final short IpmOutboxEntryId = 0x35e2;		// from GenTagArray.h
	static final short IpmWasteBasketEntryId = 0x35e3;
	static final short IpmSentMailEntryId = 0x35e4;		// from GenTagArray.h
	static final short ViewsEntryId = 0x35e5;			// from GenTagArray.h
	static final short CommonViewsEntryId = 0x35e6;		// from GenTagArray.h
	static final short FinderEntryId = 0x35e7;
	static final short ContentCount = 0x3602;
	static final short ContentUnreadCount = 0x3603;
	static final short Subfolders = 0x360a;
	static final short ContainerClass = 0x3613;
	static final short IpmApposhortmentEntryId = 0x36d0;
	static final short IpmContactEntryId = 0x36d1;
	static final short IpmJournalEntryId = 0x36d2;
	static final short IpmNoteEntryId = 0x36d3;
	static final short IpmTaskEntryId = 0x36d4;
	static final short RemindersOnlineEntryId = 0x36d5;
	static final short IpmDraftsEntryId = 0x36d7;
	static final short AdditionalRenEntryIds = 0x36d8;
	static final short ExtendedFolderFlags = 0x36da;
	static final short OrdinalMost = 0x36e2;
	static final short FreeBusyEntryIds = 0x36e4;
	static final short AttachDataBinary = 0x3701;
	static final short AttachDataObject = 0x3701;
	static final short AttachEncoding = 0x3702;
	static final short AttachExtension = 0x3703;
	static final short AttachFilename = 0x3704;
	static final short AttachMethod = 0x3705;
	static final short AttachLongFilename = 0x3707;
	static final short AttachPathname = 0x3708;
	static final short AttachRendering = 0x3709;
	static final short AttachMimeSequence = 0x3710;
	static final short AttachTag = 0x370a;
	static final short RenderingPosition = 0x370b;
	static final short AttachTransportName = 0x370c;
	static final short AttachLongPathname = 0x370d;
	static final short AttachMimeTag = 0x370e;
	static final short AttachAdditionalInfo = 0x370f;
	static final short AttachContentBase = 0x3711;
	static final short AttachContentId = 0x3712;
	static final short AttachContentLocation = 0x3713;
	static final short AttachFlags = 0x3714;
	static final short AttachPayloadProviderGuidString = 0x3719;
	static final short AttachPayloadClass = 0x371a;
	static final short DisplayType = 0x3900;
	static final short SevenBitDisplayName = 0x39ff;
	static final short Account = 0x3a00;
	static final short Generation = 0x3a05;
	static final short GivenName = 0x3a06;
	static final short BusinessTelephoneNumber = 0x3a08;
	static final short HomeTelephoneNumber = 0x3a09;
	static final short Initials = 0x3a0a;
	static final short Keyword = 0x3a0b;
	static final short Language = 0x3a0c;
	static final short Location = 0x3a0d;
	static final short Surname = 0x3a11;
	static final short PostalAddress = 0x3a15;
	static final short CompanyName = 0x3a16;
	static final short Title = 0x3a17;
	static final short DepartmentName = 0x3a18;
	static final short Business2TelephoneNumbers = 0x3a1b;
	static final short MobileTelephoneNumber = 0x3a1c;
	static final short OtherTelephoneNumber = 0x3a1f;
	static final short PrimaryFaxNumber = 0x3a23;
	static final short BusinessFaxNumber = 0x3a24;
	static final short HomeFaxNumber = 0x3a25;
	static final short Country = 0x3a26;
	static final short Locality = 0x3a27;
	static final short StateOrProvince = 0x3a28;
	static final short StreetAddress = 0x3a29;
	static final short PostalCode = 0x3a2a;
	static final short Home2TelephoneNumbers = 0x3a2f;
	static final short SendRichInfo = 0x3a40;
	static final short Birthday = 0x3a42;
	static final short MiddleName = 0x3a44;
	static final short DisplayNamePrefix = 0x3a45;
	static final short SpouseName = 0x3a48;
	static final short BusinessHomePage = 0x3a51;
	static final short HomeAddressCity = 0x3a59;
	static final short HomeAddressCountry = 0x3a5a;
	static final short HomeAddressPostalCode = 0x3a5b;
	static final short HomeAddressStateOrProvince = 0x3a5c;
	static final short HomeAddressStreet = 0x3a5d;
	static final short HomeAddressPostOfficeBox = 0x3a5e;
	static final short OtherAddressCity = 0x3a5f;
	static final short OtherAddressCountry = 0x3a60;
	static final short OtherAddressPostalCode = 0x3a61;
	static final short OtherAddressStateOrProvince = 0x3a62;
	static final short OtherAddressStreet = 0x3a63;
	static final short OtherAddressPostOfficeBox  = 0x3a64;
	static final short InternetCodepage = 0x3fde;
	static final short MessageLocaleId = 0x3ff1;
	static final short CreatorName = 0x3ff8;
	static final short CreatorEntryId = 0x3ff9;
	static final short LastModifierName = 0x3ffa;
	static final short LastModifierEntryId = 0x3ffb;
	static final short MessageCodepage = 0x3ffd;

	static final short SenderFlags = 0x4019;
	static final short SentRepresentingFlags = 0x401a;
	static final short SenderSimpleDisplayName = 0x4030;
	static final short SentRepresentingSimpleDisplayName = 0x4031;
	static final short CreatorSimpleDisplayName = 0x4038;
	static final short LastModifierSimpleDisplayName = 0x4039;
	static final short ContentFilterSpamConfidenceLevel = 0x4076;

	static final short InternetMailOverrideFormat = 0x5902;
	static final short MessageEditorFormat = 0x5909;
	static final short RecipientDisplayName = 0x5ff6;

	static final short ChangeKey = 0x65e2;
	static final short PredecessorChangeList = 0x65e3;
	static final short PstBodyPrefix = 0x6619;
	static final short PstLrNoRestrictions = 0x6633;
	static final short PstHiddenCount = 0x6635;
	static final short PstHiddenUnread = 0x6636;
	static final short FolderId = 0x6748;
	static final short LtpParentNid = 0x67f1;
	static final short LtpRowId = 0x67f2;
	static final short LtpRowVer = 0x67f3;
	static final short LtpPstPassword = 0x67ff;
	static final short OfflineAddressBookName = 0x6800;
	static final short VoiceMessageDuration = 0x6801;
	static final short SenderTelephoneNumber = 0x6802;
	static final short SendOutlookRecallReport = 0x6803;
	static final short FaxNumberOfPages = 0x6804;
	static final short OfflineAddressBookTuncatedProperties = 0x6805;
	static final short CallId = 0x6806;
	static final short OfflineAddressBookLanguageId = 0x6807;
	static final short OfflineAddressBookFileType = 0x6808;
	static final short OfflineAddressBookCompressedSize = 0x6809;
	static final short MapiFormComposeCommand = 0x682f;
	static final short SearchFolderLastUsed = 0x6834;
	static final short SearchFolderExpiration = 0x683a;
	static final short SearchFolderTemplateId = 0x6841;
	static final short SearchFolderId = 0x6842;
	static final short ScheduleInfoDontMailDelegates = 0x6843;
	static final short SearchFolderDefinition = 0x6845;
	static final short SearchFolderStorageType = 0x6846;
	static final short SearchFolder = 0x6847;
	static final short SearchFolderEfpFlags = 0x6848;
	static final short ScheduleInfoMonthsAway = 0x6855;
	static final short ScheduleInfoFreeBusyAway = 0x6856;
	
	static final short ViewDescriptorFlags = 0x7003;
	static final short ViewDescriptorLinkTo = 0x7004;
	static final short ViewDescriptorViewFolder = 0x7005;
	static final short ViewDescriptorName = 0x7006;
	static final short ViewDescriptorVersion = 0x7007;
	static final short Processed = 0x7d01;
	static final short AttachmentLinkId = 0x7ffa;
	static final short ExceptionStartTime = 0x7ffb;
	static final short ExceptionEndTime = 0x7ffc;
	static final short AttachmentFlags = 0x7ffd;
	static final short AttachmentHidden = 0x7ffe;
	static final short AttachContactPhoto = 0x7fff;


	static final short NamedPropertyFirst = (short)0x8000;
	static final short NamedPropertyLast = (short)0x8fff;
	static final short AddressBookFolderPathname = (short)0x8004;
	static final short AddressBookManagerDistinguishedName = (short)0x8005;
	static final short FileUnderId = (short)0x8005;
	static final short AddressBookHomeMessageDatabase = (short)0x8006;
	static final short AddressBookHomeMessageTransferAgent = (short)0x8007;
	static final short ContactItemData = (short)0x8007;
	static final short AddressBookIsMemberOfDistributionList = (short)0x8008;
	static final short TcvConstLongOne = (short)0x8008;
	static final short AddressBookMember = (short)0x8009;
	static final short ReferredBy = (short)0x800e;
	static final short Department = (short)0x8010;
	static final short HasPicture = (short)0x8015;
	static final short HomeAddress = (short)0x801a;
	static final short WorkAddress = (short)0x801b;
	static final short OtherAddress = (short)0x801c;
	static final short PostalAddressId = (short)0x8022;
	static final short ContactCharacterSet = (short)0x8023;
	static final short AutoLog = (short)0x8025;
	static final short FileUnderList = (short)0x8026;
	static final short EmailList = (short)0x8027;
	static final short AddressBookProviderEmailList = (short)0x8028;
	static final short AddressBookProviderArrayType = (short)0x8029;
	static final short Html = (short)0x802b;
	static final short YomiFirstName = (short)0x802c;
	static final short YomiLastName = (short)0x802d;
	static final short YomiCompanyName = (short)0x802e;
	static final short WorkAddressStreet = (short)0x8045;
	static final short WorkAddressCity = (short)0x8046;
	static final short WorkAddressState = (short)0x8047;
	static final short WorkAddressPostalCode = (short)0x8048;
	static final short WorkAddressCountry = (short)0x8049;
	static final short WorkAddressPostOfficeBox = (short)0x804a;
	static final short DistributionListChecksum = (short)0x804c;
	static final short ContactUserField1 = (short)0x804f;
	static final short ContactUserField2 = (short)0x8050;
	static final short ContactUserField3 = (short)0x8051;
	static final short ContactUserField4 = (short)0x8052;
	static final short DistributionListName = (short)0x8053;
	static final short DistributionListOneOffMembers = (short)0x8054;
	static final short DistributionListMembers = (short)0x8055;
	static final short InstantMessagingAddress = (short)0x8062;
	static final short Email1DisplayName = (short)0x8080;
	static final short Email1AddressType = (short)0x8082;
	static final short Email1EmailAddress = (short)0x8083;
	static final short Email1OriginalDisplayName = (short)0x8084;
	static final short Email1OriginalEntryId = (short)0x8085;
	static final short Email1RichTextFormat = (short)0x8086;
	static final short Email2DisplayName = (short)0x8090;
	static final short Email2AddressType = (short)0x8092;
	static final short Email2EmailAddress = (short)0x8093;
	static final short Email2OriginalDisplayName = (short)0x8094;
	static final short Email2OriginalEntryId = (short)0x8095;
	static final short Email2RichTextFormat = (short)0x8096;
	static final short Email3DisplayName = (short)0x80a0;
	static final short Email3AddressType = (short)0x80a2;
	static final short Email3EmailAddress = (short)0x80a3;
	static final short Email3OriginalDisplayName = (short)0x80a4;
	static final short Email3OriginalEntryId = (short)0x80a5;
	static final short Email3RichTextFormat = (short)0x80a6;
	static final short Fax1AddressType = (short)0x80b2;
	static final short Fax1EmailAddress = (short)0x80b3;
	static final short Fax1OriginalDisplayName = (short)0x80b4;
	static final short Fax1OriginalEntryId = (short)0x80b5;
	static final short Fax2AddressType = (short)0x80c2;
	static final short Fax2EmailAddress = (short)0x80c3;
	static final short Fax2OriginalDisplayName = (short)0x80c4;
	static final short Fax2OriginalEntryId = (short)0x80c5;
	static final short Fax3AddressType = (short)0x80d2;
	static final short Fax3EmailAddress = (short)0x80d3;
	static final short Fax3OriginalDisplayName = (short)0x80d4;
	static final short Fax3OriginalEntryId = (short)0x80d5;
	static final short FreeBusyLocation = (short)0x80d8;
	static final short HomeAddressCountryCode = (short)0x80da;
	static final short WorkAddressCountryCode = (short)0x80db;
	static final short OtherAddressCountryCode = (short)0x80dc;
	static final short AddressCountryCode = (short)0x80dd;

	static final short TaskStatus = (short)0x8101;
	static final short PercentComplete = (short)0x8102;
	static final short TeamTask = (short)0x8103;
	static final short TaskStartDate = (short)0x8104;
	static final short TaskDueDate = (short)0x8105;
	static final short TaskResetReminder = (short)0x8107;
	static final short TaskAccepted = (short)0x8108;
	static final short TaskDeadOccurrence = (short)0x8109;
	static final short TaskDateCompleted = (short)0x810f;
	static final short TaskActualEffort = (short)0x8110;
	static final short TaskEstimatedEffort = (short)0x8111;
	static final short TaskVersion = (short)0x8112;
	static final short TaskState = (short)0x8113;
	static final short TaskLastUpdate = (short)0x8115;
	static final short TaskRecurrence = (short)0x8116;
	static final short TaskAssigners = (short)0x8117;
	static final short TaskStatusOnComplete = (short)0x8119;
	static final short TaskHistory = (short)0x811a;
	static final short TaskUpdates = (short)0x811b;
	static final short TaskComplete = (short)0x811c;
	static final short TaskFCreator = (short)0x811e;
	static final short TaskOwner = (short)0x811f;
	static final short TaskMultipleRecipients = (short)0x8120;
	static final short TaskAssigner = (short)0x8121;
	static final short TaskLastUser = (short)0x8122;
	static final short TaskOrdinal = (short)0x8123;
	static final short TaskNoCompute = (short)0x8124;
	static final short TaskLastDelegate = (short)0x8125;
	static final short TaskFRecurring = (short)0x8126;
	static final short TaskRole = (short)0x8127;
	static final short TaskOwnership = (short)0x8129;
	static final short TaskAcceptanceState = (short)0x812a;
	static final short TaskFFixOffline = (short)0x812c;

	static final short AppointmentSequence = (short)0x8201;
	static final short AppointmentSequenceTime = (short)0x8202;
	static final short AppointmentLastSequence = (short)0x8203;
	static final short ChangeHighlight = (short)0x8204;
	static final short BusyStatus = (short)0x8205;
	static final short FExceptionalBody = (short)0x8206;
	static final short AppointmentAuxiliaryFlags = (short)0x8207;
	static final short AppointmentLocation = (short)0x8208;
	static final short MeetingWorkspaceURL = (short)0x8209;
	static final short ForwardInstance = (short)0x820a;
	static final short AppointmentStartWhole = (short)0x820d;
	static final short AppointmentEndWhole = (short)0x820e;
	static final short AppointmentStartTime = (short)0x820f;
	static final short AppointmentEndType = (short)0x8210;
	static final short AppointmentEndDate = (short)0x8211;
	static final short AppointmentStartDate = (short)0x8212;
	static final short AppointmentDuration = (short)0x8213;
	static final short AppointmentColor = (short)0x8214;
	static final short AppointmentSubType = (short)0x8215;
	static final short AppointmentRecur = (short)0x8216;
	static final short AppointmentStateFlags = (short)0x8217;
	static final short ResponseStatus = (short)0x8218;
	static final short AppointmentReplyTime = (short)0x8220;
	static final short Recurring = (short)0x8223;
	static final short IntendedBusyStatus = (short)0x8224;
	static final short AppointmentUpdateTime = (short)0x8226;
	static final short ExceptionReplaceTime = (short)0x8228;
	static final short FInvited = (short)0x8229;
	static final short ExceptionalAttendees = (short)0x822b;
	static final short OwnerName = (short)0x822e;
	static final short AppointmentReplyName = (short)0x8230;
	static final short RecurrenceType = (short)0x8231;
	static final short RecurrencePattern = (short)0x8232;
	static final short TimeZoneStruct = (short)0x8233;
	static final short TimeZoneDescription = (short)0x8234;
	static final short ClipStart = (short)0x8235;
	static final short ClipEnd = (short)0x8236;
	static final short OriginalStoreEntryId = (short)0x8237;
	static final short AllAttendeesString = (short)0x8238;
	static final short AutoFillAppointment = (short)0x823a;
	static final short ToAttendeesString = (short)0x823b;
	static final short CcAttendeesString = (short)0x823c;
	static final short TrustRecipientHighlights = (short)0x823e;
	static final short ConferencingCheck = (short)0x8240;
	static final short ConferencingType = (short)0x8241;
	static final short Directory = (short)0x8242;
	static final short OrganizerAlias = (short)0x8243;
	static final short AutoStartCheck = (short)0x8244;
	static final short AllowExternalCheck = (short)0x8246;
	static final short CollaborateDoc = (short)0x8247;
	static final short NetShowURL = (short)0x8248;
	static final short OnlinePassword = (short)0x8249;
	static final short AppointmentProposedStartWhole = (short)0x8250;
	static final short AppointmentProposedEndWhole = (short)0x8251;
	static final short AppointmentProposedDuration = (short)0x8256;
	static final short AppointmentCounterProposal = (short)0x8257;
	static final short AppointmentProposalNumber = (short)0x8259;
	static final short AppointmentNotAllowPropose = (short)0x825a;
	static final short AppointmentTimeZoneDefinitionStartDisplay = (short)0x825e;
	static final short AppointmentTimeZoneDefinitionEndDisplay = (short)0x825f;
	static final short AppointmentTimeZoneDefinitionRecur = (short)0x8260;

	static final short ReminderDelta = (short)0x8501;
	static final short ReminderTime = (short)0x8502;
	static final short ReminderSet = (short)0x8503; 
	static final short Private = (short)0x8506;
	static final short AgingDontAgeMe = (short)0x850e;
	static final short SideEffects = (short)0x8510;
	static final short RemoteStatus = (short)0x8511;
	static final short SmartNoAttach = (short)0x8514;
	static final short CommonStart = (short)0x8516;
	static final short CommonEnd = (short)0x8517;
	static final short TaskMode = (short)0x8518;
	static final short TaskGlobalId = (short)0x8519;
	static final short AutoProcessState = (short)0x851a;
	static final short ReminderOverride = (short)0x851c;
	static final short NonSendableCc = (short)0x8537;
	static final short NonSendableBcc = (short)0x8538;
	static final short Companies = (short)0x8539;
	static final short ReminderType = (short)0x851d;
	static final short ReminderPlaySound = (short)0x851e;
	static final short ReminderFileParameter = (short)0x851f;
	static final short VerbStream = (short)0x8520;
	static final short VerbResponse = (short)0x8524;
	static final short FlagRequest = (short)0x8530;
	static final short Billing = (short)0x8535;
	static final short NonSendableTo = (short)0x8536;
	static final short Contacts = (short)0x853a;
	static final short NonSendToTrackStatus = (short)0x8543;
	static final short NonSendCcTrackStatus = (short)0x8544;
	static final short NonSendBccTrackStatus = (short)0x8545;
	static final short CurrentVersion = (short)0x8552;
	static final short CurrentVersionName = (short)0x8554;
	static final short ReminderSignalTime = (short)0x8560;
	static final short InternetAccountName = (short)0x8580;
	static final short InternetAccountStamp = (short)0x8581;
	static final short UseTnef = (short)0x8582;
	static final short ContactLinkSearchKey = (short)0x8584;
	static final short ContactLinkEntry = (short)0x8585;
	static final short ContactLinkName = (short)0x8586;
	static final short SpamOriginalFolder = (short)0x859c;
	static final short ValidFlagStringProof = (short)0x85bf;

	static final short NoteColor = (short)0x8b00;
	static final short NoteWidth = (short)0x8b02;
	static final short NoteHeight = (short)0x8b03;
	static final short NoteX = (short)0x8b04;
	static final short NoteY = (short)0x8b05;
}
