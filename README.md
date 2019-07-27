NSL 3.0s for Java
Installation procedure for Windows 7 or higher

-=-

To install NSL after cloning this repository:

1.  Install Java SE Development Kit 8 from Oracle's website.
    https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

    Alternatively, you may install AdoptOpenJDK 8 or Amazon Corretto 8 instead which are based on the OpenJDK open source libraries.
    https://adoptopenjdk.net/
    https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html

2.  Install Java3D 1.5.1 from Oracle's website.
    https://www.oracle.com/technetwork/java/javase/tech/index-jsp-138252.html

3.  Copy the `NSL3_0_s` folder to `Program Files`.

4.  Copy the files `SCS_PREFERENCES` and `SCS_LIBRARY_PATHS` to your home folder.
    The folder to store it in should be `C:\Users\<username>`.

5.  Set the following system environment variables:
    <jdk-version> for JAVA_HOME is the folder in `C:\Program Files\Java\`.
    If using OpenJDK, the JDK folder may be stored under the name of your OpenJDK vendor like `C:\Program Files\Amazon Corretto\`. Change `<jdk-version>` in the line that sets `JAVA_HOME` to your JDK folder name.
```
JAVA_HOME = "C:\Program Files\Java\<jdk-version>"

NSL_OS = "nt"

CLASSPATH = ".;C:\Program Files\NSL3_0_s\nslc\src;C:\Program Files\NSL3_0_s;C:\Program Files\NSL3_0_s\nslj\src\main;C:\Program Files\NSL3_0_s\nslj\src\nsls\jacl;C:\Program Files\NSL3_0_s\nslj\src\nsls\tcljava"

PATH = "%JAVA_HOME%\bin;C:\Program Files\NSL3_0_s\nslj\bin;C:\Program Files\NSL3_0_s\nslc\bin"
```

6.  Compile the source files by running `javac @sources.txt` with `C:\Program Files\NSL3_0_s` as your working directory.

Basic NSL installation should now be complete.

To uninstall NSL at any time:

1.  Remove the `NSL3_0_s` folder.

2.  Remove the following environment variables:
```
NSL_OS
```

3. Remove the following entries from the PATH environment variable:
```
C:\Program Files\NSL3_0_s\nslj\bin
C:\Program Files\NSL3_0_s\nslc\bin
```

4. Remove the following entries from the CLASSPATH environment variable:
```
C:\Program Files\NSL3_0_s\nslc\src
C:\Program Files\NSL3_0_s
C:\Program Files\NSL3_0_s\nslj\src\main
C:\Program Files\NSL3_0_s\nslj\src\nsls\jacl
C:\Program Files\NSL3_0_s\nslj\src\nsls\tcljava
```