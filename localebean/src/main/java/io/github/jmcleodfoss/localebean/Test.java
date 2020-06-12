package io.github.jmcleodfoss.localebean;

/**	Test logic of LocaleBean separation of filename and extension.
*	Note: This is only to test the concept; it's a standalone file that really belongs in a sandbox somewhere rather than as part of this project. It
*	does not belong in any kind of Maven test
*/
class Test
{
	/**	Split the filename into file + extension and print the results
	*	@param	fn	The file name to split
	*/
	static void process(String fn)
	{
		System.out.printf("\"%s\"", fn);
		int iExtension = fn.lastIndexOf('.');

		String fnFilename = iExtension >= 0 ? fn.substring(0, iExtension) : fn;
		String fnExtension = iExtension >= 0 ? fn.substring(iExtension) : "";
		System.out.printf(" => \"%s\" + \"%s\"\n", fnFilename, fnExtension);
	}

	/** Run test function for some fixed test cases and any input passed from the command line.
	*	@param	args	The additional filenames to process.
	*/
	public static void main(String[] args)
	{
		process("filename");
		process("filename.");
		process(".extension");
		process("filename.extension");
		process("filename.second-component.extension");

		for (String a: args)
			process(a);
	}
}
