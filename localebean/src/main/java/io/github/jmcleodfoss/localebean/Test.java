package io.github.jmcleodfoss.localebean;

/**	Test logic of LocaleBean separation of filename and extension. */
class Test
{

	static void process(String fn)
	{
		System.out.printf("\"%s\"", fn);
		int iExtension = fn.lastIndexOf('.');

		String fnFilename = iExtension >= 0 ? fn.substring(0, iExtension) : fn;
		String fnExtension = iExtension >= 0 ? fn.substring(iExtension) : "";
		System.out.printf(" => \"%s\" + \"%s\"\n", fnFilename, fnExtension);
	}

	public static void main(String[] args)
	{
		process("filename");
		process("filename.");
		process(".extension");
		process("filename.extension");
		process("filename.second-component.extension");
	}
}
