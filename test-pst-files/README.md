# test-pst-files Directory
Put your test pst files here to be picked up by mvn test and by extras/test.sh

## Provided test files
### not-a-pst-file.txt
A text file which is definitely not a pst file, used by io.github.jmcleodfoss.pst.HeaderTest in the test hierarchy.

### truncated-in-header.bin
The first few dozen bytes of a valid pst file, used by io.github.jmcleodfoss.pst.HeaderTest in the test hierarchy.

## Test files you must provide
TBD

## Suggestions
*   Only valid pst files should have the extension "pst" otherwise extras/test.sh will run all tests on them pointlessly
*   Use the extension "bin" for deliberately-corrupted pst files
*   Do not check in any full pst files unless:
*       They are small
*       They contain no identifying information for anybody
