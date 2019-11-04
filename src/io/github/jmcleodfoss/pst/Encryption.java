package io.github.jmcleodfoss.pst;

/**	The Encryption class handles decryption and encryption of the PST file.
*
*	@see	Header
*/
class Encryption {

	/**	The value indicating that this PST file is unencrypted. Per the MS-PST document, its value is {@value}.
	*
	*	@see	TranslaterNone
	*	@see	#NOB_CRYPT_PERMUTE
	*	@see	#NOB_CRYPT_CYCLIC
	*/
	private static final byte NOB_CRYPT_NONE = 0x00;

	/**	The value indicating that this PST file uses permutative encryption.  Per the MS-PST document, its value is {@value}.
	*
	*	@see	TranslaterPermute
	*	@see	#NOB_CRYPT_NONE
	*	@see	#NOB_CRYPT_CYCLIC
	*/
	private static final byte NOB_CRYPT_PERMUTE = 0x01;

	/**	The value indicating that this PST file uses cyclic encryption.  Per the MS-PST document, its value is {@value}.
	*
	*	@see	TranslaterCyclic
	*	@see	#NOB_CRYPT_NONE
	*	@see	#NOB_CRYPT_PERMUTE
	*/
	private static final byte NOB_CRYPT_CYCLIC = 0x02;

	
	/**	A ready-made Encryption object of type NONE, required for some PST processing classes.
	*
	*	@see	SimpleBlock
	*	@see	SubnodeBTree
	*/
	public static final Encryption NONE = new Encryption(NOB_CRYPT_NONE);

	/**	The encryption table used by permutative and cyclic encryption. */
	private final byte mpbbCrypt[] = {
			65,		54,		19,		98,	(byte)	168,		33,		110,	(byte)	187,
		(byte)	244,		22,	(byte)	204,		4,		127,		100,	(byte)	232,		93,
			30,	(byte)	242,	(byte)	203,		42,		116,	(byte)	197,		94,		53,
		(byte)	210,	(byte)	149,		71,	(byte)	158,	(byte)	150,		45,	(byte)	154,	(byte)	136,
			76,		125,	(byte)	132,		63,	(byte)	219,	(byte)	172,		49,	(byte)	182,
			72,		95,	(byte)	246,	(byte)	196,	(byte)	216,		57,	(byte)	139,	(byte)	231,
			35,		59,		56,	(byte)	142,	(byte)	200,	(byte)	193,	(byte)	223,		37,
		(byte)	177,		32,	(byte)	165,		70,		96,		78,	(byte)	156,	(byte)	251,
		(byte)	170,	(byte)	211,		86,		81,		69,		124,		85,		0,
			7,	(byte)	201,		43,	(byte)	157,	(byte)	133,	(byte)	155,		9,	(byte)	160,
		(byte)	143,	(byte)	173,	(byte)	179,		15,		99,	(byte)	171,	(byte)	137,		75,
		(byte)	215,	(byte)	167,		21,		90,		113,		102,		66,	(byte)	191,
			38,		74,		107,	(byte)	152,	(byte)	250,	(byte)	234,		119,		83,
		(byte)	178,		112,		5,		44,	(byte)	253,		89,		58,	(byte)	134,
			126,	(byte)	206,		6,	(byte)	235,	(byte)	130,		120,		87,	(byte)	199,
		(byte)	141,		67,	(byte)	175,	(byte)	180,		28,	(byte)	212,		91,	(byte)	205,
		(byte)	226,	(byte)	233,		39,		79,	(byte)	195,		8,		114,	(byte)	128,
		(byte)	207,	(byte)	176,	(byte)	239,	(byte)	245,		40,		109,	(byte)	190,		48,
			77,		52,	(byte)	146,	(byte)	213,		14,		60,		34,		50,
		(byte)	229,	(byte)	228,	(byte)	249,	(byte)	159,	(byte)	194,	(byte)	209,		10,	(byte)	129,
			18,	(byte)	225,	(byte)	238,	(byte)	145,	(byte)	131,		118,	(byte)	227,	(byte)	151,
		(byte)	230,		97,	(byte)	138,		23,		121,	(byte)	164,	(byte)	183,	(byte)	220,
		(byte)	144,		122,		92,	(byte)	140,		2,	(byte)	166,	(byte)	202,		105,
		(byte)	222,		80,		26,		17,	(byte)	147,	(byte)	185,		82,	(byte)	135,
			88,	(byte)	252,	(byte)	237,		29,		55,		73,		27,		106,
		(byte)	224,		41,		51,	(byte)	153,	(byte)	189,		108,	(byte)	217,	(byte)	148,
		(byte)	243,		64,		84,		111,	(byte)	240,	(byte)	198,		115,	(byte)	184,
		(byte)	214,		62,		101,		24,		68,		31,	(byte)	221,		103,
			16,	(byte)	241,		12,		25,	(byte)	236,	(byte)	174,		3,	(byte)	161,
			20,		123,	(byte)	169,		11,	(byte)	255,	(byte)	248,	(byte)	163,	(byte)	192,
		(byte)	162,		1,	(byte)	247,		46,	(byte)	188,		36,		104,		117,
			13,	(byte)	254,	(byte)	186,		47,	(byte)	181,	(byte)	208,	(byte)	218,		61,
			20,		83,		15,		86,	(byte)	179,	(byte)	200,		122,	(byte)	156,
		(byte)	235,		101,		72,		23,		22,		21,	(byte)	159,		2,
		(byte)	204,		84,		124,	(byte)	131,		0,		13,		12,		11,
		(byte)	162,		98,	(byte)	168,		118,	(byte)	219,	(byte)	217,	(byte)	237,	(byte)	199,
		(byte)	197,	(byte)	164,	(byte)	220,	(byte)	172,	(byte)	133,		116,	(byte)	214,	(byte)	208,
		(byte)	167,	(byte)	155,	(byte)	174,	(byte)	154,	(byte)	150,		113,		102,	(byte)	195,
			99,	(byte)	153,	(byte)	184,	(byte)	221,		115,	(byte)	146,	(byte)	142,	(byte)	132,
			125,	(byte)	165,		94,	(byte)	209,		93,	(byte)	147,	(byte)	177,		87,
			81,		80,	(byte)	128,	(byte)	137,		82,	(byte)	148,		79,		78,
			10,		107,	(byte)	188,	(byte)	141,		127,		110,		71,		70,
			65,		64,		68,		1,		17,	(byte)	203,		3,		63,
		(byte)	247,	(byte)	244,	(byte)	225,	(byte)	169,	(byte)	143,		60,		58,	(byte)	249,
		(byte)	251,	(byte)	240,		25,		48,	(byte)	130,		9,		46,	(byte)	201,
		(byte)	157,	(byte)	160,	(byte)	134,		73,	(byte)	238,		111,		77,		109,
		(byte)	196,		45,	(byte)	129,		52,		37,	(byte)	135,		27,	(byte)	136,
		(byte)	170,	(byte)	252,		6,	(byte)	161,		18,		56,	(byte)	253,		76,
			66,		114,		100,		19,		55,		36,		106,		117,
			119,		67,	(byte)	255,	(byte)	230,	(byte)	180,		75,		54,		92,
		(byte)	228,	(byte)	216,		53,		61,		69,	(byte)	185,		44,	(byte)	236,
		(byte)	183,		49,		43,		41,		7,		104,	(byte)	163,		14,
			105,		123,		24,	(byte)	158,		33,		57,	(byte)	190,		40,
			26,		91,		120,	(byte)	245,		35,	(byte)	202,		42,	(byte)	176,
		(byte)	175,		62,	(byte)	254,		4,	(byte)	140,	(byte)	231,	(byte)	229,	(byte)	152,
			50,	(byte)	149,	(byte)	211,	(byte)	246,		74,	(byte)	232,	(byte)	166,	(byte)	234,
		(byte)	233,	(byte)	243,	(byte)	213,		47,		112,		32,	(byte)	242,		31,
			5,		103,	(byte)	173,		85,		16,	(byte)	206,	(byte)	205,	(byte)	227,
			39,		59,	(byte)	218,	(byte)	186,	(byte)	215,	(byte)	194,		38,	(byte)	212,
		(byte)	145,		29,	(byte)	210,		28,		34,		51,	(byte)	248,	(byte)	250,
		(byte)	241,		90,	(byte)	239,	(byte)	207,	(byte)	144,	(byte)	182,	(byte)	139,	(byte)	181,
		(byte)	189,	(byte)	192,	(byte)	191,		8,	(byte)	151,		30,		108,	(byte)	226,
			97,	(byte)	224,	(byte)	198,	(byte)	193,		89,	(byte)	171,	(byte)	187,		88,
		(byte)	222,		95,	(byte)	223,		96,		121,		126,	(byte)	178,	(byte)	138,
			71,	(byte)	241,	(byte)	180,	(byte)	230,		11,		106,		114,		72,
		(byte)	133,		78,	(byte)	158,	(byte)	235,	(byte)	226,	(byte)	248,	(byte)	148,		83,
		(byte)	224,	(byte)	187,	(byte)	160,		2,	(byte)	232,		90,		9,	(byte)	171,
		(byte)	219,	(byte)	227,	(byte)	186,	(byte)	198,		124,	(byte)	195,		16,	(byte)	221,
			57,		5,	(byte)	150,		48,	(byte)	245,		55,		96,	(byte)	130,
		(byte)	140,	(byte)	201,		19,		74,		107,		29,	(byte)	243,	(byte)	251,
		(byte)	143,		38,	(byte)	151,	(byte)	202,	(byte)	145,		23,		1,	(byte)	196,
			50,		45,		110,		49,	(byte)	149,	(byte)	255,	(byte)	217,		35,
		(byte)	209,		0,		94,		121,	(byte)	220,		68,		59,		26,
			40,	(byte)	197,		97,		87,		32,	(byte)	144,		61,	(byte)	131,
		(byte)	185,		67,	(byte)	190,		103,	(byte)	210,		70,		66,		118,
		(byte)	192,		109,		91,		126,	(byte)	178,		15,		22,		41,
			60,	(byte)	169,		3,		84,		13,	(byte)	218,		93,	(byte)	223,
		(byte)	246,	(byte)	183,	(byte)	199,		98,	(byte)	205,	(byte)	141,		6,	(byte)	211,
			105,		92,	(byte)	134,	(byte)	214,		20,	(byte)	247,	(byte)	165,		102,
			117,	(byte)	172,	(byte)	177,	(byte)	233,		69,		33,		112,		12,
		(byte)	135,	(byte)	159,		116,	(byte)	164,		34,		76,		111,	(byte)	191,
			31,		86,	(byte)	170,		46,	(byte)	179,		120,		51,		80,
		(byte)	176,	(byte)	163,	(byte)	146,	(byte)	188,	(byte)	207,		25,		28,	(byte)	167,
			99,	(byte)	203,		30,		77,		62,		75,		27,	(byte)	155,
			79,	(byte)	231,	(byte)	240,	(byte)	238,	(byte)	173,		58,	(byte)	181,		89,
			4,	(byte)	234,		64,		85,		37,		81,	(byte)	229,		122,
		(byte)	137,		56,		104,		82,		123,	(byte)	252,		39,	(byte)	174,
		(byte)	215,	(byte)	189,	(byte)	250,		7,	(byte)	244,	(byte)	204,	(byte)	142,		95,
		(byte)	239,		53,	(byte)	156,	(byte)	132,		43,		21,	(byte)	213,		119,
			52,		73,	(byte)	182,		18,		10,		127,		113,	(byte)	136,
		(byte)	253,	(byte)	157,		24,		65,		125,	(byte)	147,	(byte)	216,		88,
			44,	(byte)	206,	(byte)	254,		36,	(byte)	175,	(byte)	222,	(byte)	184,		54,
		(byte)	200,	(byte)	161,	(byte)	128,	(byte)	166,	(byte)	153,	(byte)	152,	(byte)	168,		47,
			14,	(byte)	129,	(byte)	101,		115,	(byte)	228,	(byte)	194,	(byte)	162,	(byte)	138,
		(byte)	212,	(byte)	225,		17,	(byte)	208,		8,	(byte)	139,		42,	(byte)	242,
		(byte)	237,	(byte)	154,		100,		63,	(byte)	193,		108,	(byte)	249,	(byte)	236
	};

	/**	An Offset into the encryption table {@link #mpbbCrypt} used when encrypting using permutative encoding, and for both
	*	encryption and decryption in cyclic encoding. */
	private final int offsR = 0;

	/**	An Offset into the encryption table {@link #mpbbCrypt} used when decrypting using permutative encoding, and for both
	*	encryption and decryption in cyclic encoding. */
	private final int offsS = 0x100;

	/**	An Offset into the encryption table {@link #mpbbCrypt} used when encrypting or decryptint in cyclic encoding. */
	private final int offsI = 0x200;

	/**	The UnknownEncryptionMethodException is thrown if one tries to create an Encryption object with an invalid value for the
	*	encryption type.
	*/
	class UnknownEncryptionMethodException extends RuntimeException {

		/**	The serialVersionUID is required because the base class is serializable. */
		private static final long serialVersionUID = 1L;

		/**	Create an UnknownEncryptionMethodException object indicating the invalid encryption method encountered.
		*
		*	@param	bCryptMethod	The encryption method encoutered in the PST's header file.
		*/
		public UnknownEncryptionMethodException(byte bCryptMethod)
		{
			super("Unknown encryption method " + Integer.toHexString(bCryptMethod & 0xff));
		}
	}

	/**	The Tanslater interface defines the functions required for all translation types. */
	private interface Translater {

		/**	The translate function performs the translation in place on the data array.
		*
		*	@param	data	The bytes to encode or decode.
		*	@param	key	Additional data used by the {@link TranslaterCyclic} translater.
		*/
		void translate(byte[] data, long key);
	}

	/**	The TranslaterNone translater is used for the {@link #NOB_CRYPT_NONE} encryption type.
	*
	*	@see	TranslaterPermute
	*	@see	TranslaterCyclic
	*/
	private class TranslaterNone implements Translater {

		/**	The translate function is trivial; it performs no conversion.
		*
		*	@param	data	This parameter is not used by the TranslaterNone translater.
		*	@param	unused	This parameter is not used by the TranslaterNone translater.
		*/
		public void translate(byte[] data, long unused)
		{
		}
	}

	/**	The TranslatePermute translater is used for the {@link #NOB_CRYPT_PERMUTE} encryption type.
	*
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, sections 5.1"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386229(v=office.12).aspx">Permutative Encoding (MSDN)</a>
	*	@see	TranslaterNone
	*	@see	TranslaterCyclic
	*/
	private class TranslaterPermute implements Translater {

		/**	The translate function is a wrapper around the convert method.
		*
		*	@param	data	The bytes to encode or decode.
		*	@param	unused	This parameter is not used by the TranslaterPermute tanslater.
		*/
		public void translate(byte[] data, long unused)
		{
			convert(data, false);
		}

		/**	The convert function performs the actual conversion in place, in the direction indicated by the fEncrypt
		*	parameter. This function was adapted from the example pfovided in Microsoft's PST definition, and exists
		*	primarily for more convenient comparison to the version in that document.
		*
		*	@param	data		The bytes to encode or decode.
		*	@param	fEncrypt	If this value is true, the data is encrypted; otherwise, it is decrypted.
		*/
		public void convert(byte[] data, boolean fEncrypt)
		{
			final int ofs = fEncrypt ? offsR : offsI;

			for (int i = 0; i < data.length; ++i) {
				int b = (data[i] & 0xff);
				data[i] = mpbbCrypt[ofs + b];
			}
		}
	}

	/**	The TranslateCyclic translater is used for the {@link #NOB_CRYPT_CYCLIC} encryption type.
	*
	*	@see	"[MS-PST] Outlook Personal Folders (.pst) File Format v20110608, sections 5.2"
	*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386960(v=office.12).aspx">Permutative Encoding (MSDN)</a>
	*	@see	TranslaterNone
	*	@see	TranslaterPermute
	*/
	private class TranslaterCyclic implements Translater {

		/**	The translate performs the conversion for cyclic encoding.
		*
		*	@param	data	The bytes to encode or decode.
		*	@param	key	This parameter is used during encoding and decoding. It should be the lower DWORD of the block ID
		*			of the block being translated.
		*
		*	@see	<a href="http://msdn.microsoft.com/en-us/library/ff386960(v=office.12).aspx">Cyclic Encoding (MSDN)</a>
		*/
		public void translate(byte[] data, long key)
		{
			short w = (short)(key ^ (key >>> 16)); 

			for (int i = 0; i < data.length; ++i) {
				byte b = data[i];
				b = (byte)(b + (w & 0xff));
				b = mpbbCrypt[offsR + (b & 0xff)];

				b = (byte)(b + (w >>> 8)); 
				b = mpbbCrypt[offsS + (b & 0xff)];

				b = (byte)(b - (w >>> 8)); 
				b = mpbbCrypt[offsI + (b & 0xff)]; 

				b = (byte)(b - (w & 0xff)); 
				data[i] = b; 
 
				w = ++w; 
			}
		}
	}

	/**	The translator object performs the required translation. */
	private final Translater translator;

	/**	The name describes the translation; it is used by the {@link #toString} function. */
	private final String name;

	/**	Create an Encryption object from the given encryption method.
	*
	*	@param	bCryptMethod	The encryption method read in from the PST file's header.
	*/
	Encryption(final byte bCryptMethod)
	{
		switch (bCryptMethod) {
		case NOB_CRYPT_NONE:
			translator = new TranslaterNone();
			name = "None";
			break;

		case NOB_CRYPT_PERMUTE:
			translator = new TranslaterPermute();
			name = "Permute";
			break;

		case NOB_CRYPT_CYCLIC:
			translator = new TranslaterCyclic();
			name = "Cyclic";
			break;

		default:
			throw new UnknownEncryptionMethodException(bCryptMethod);
		}
	}

	/**	Obtain a description of the encryption method.
	*
	*	@return	A description of the encryption method.
	*/
	@Override
	public String toString()
	{
		return name;
	}

	/**	Translate a block of data in place.
	*
	*	@param	data	The data to decrypt
	*	@param	key	The key to use when translating the data (this is only used by {link TranslaterCyclic}).
	*/
	void translate(byte[] data, int key)
	{
		translator.translate(data, key);
	}
}
