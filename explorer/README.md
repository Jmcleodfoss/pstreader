# Explorer
A Swing application for looking at PST files on any platform that supports Java.

In all of the usage examples given below, the current working directory is assumed to be the home explorer directory.

## Windows cmd
    java -cp target\explorer-1.0-SNAPSHOT.jar;..\pst\target\pst-1.0-SNAPSHOT.jar;..\swingutil\target\swingutil-1.0-SNAPSHOT.jar io.github.jmcleodfoss.explorer.pstExplorer

## Linux
    java -cp target/explorer-1.0-SNAPSHOT.jar:../pst/target/pst-1.0-SNAPSHOT.jar:../swingutil/target/swingutil-1.0-SNAPSHOT.jar io.github.jmcleodfoss.explorer.pstExplorer

## Cygwin
    java -cp "target/explorer-1.0-SNAPSHOT.jar;../pst/target/pst-1.0-SNAPSHOT.jar;../swingutil/target/swingutil-1.0-SNAPSHOT.jar" io.github.jmcleodfoss.explorer.pstExplorer
