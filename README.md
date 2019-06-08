
NSL 3.0s for Java
Installation procedure for Windows 7 or higher

-=-

To install NSL after cloning this repository:

1.  Install Java SE Development Kit 8 from Oracle's website.
    https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

    Alternatively, you may install AdoptOpenJDK 8 or Amazon Corretto 8 instead
    which are based on the OpenJDK open source libraries.
    https://adoptopenjdk.net/
    https://docs.aws.amazon.com/corretto/latest/corretto-8-ug/downloads-list.html

2.  Copy the `NSL3_0_s` folder to `Program Files`.

3.  In the `NSL3_0_s` folder, edit the `resume.bat` file by right clicking it and
    clicking on "Edit" to open it in Notepad or other default editor.

4.  Change `<jdk-version>` in the line that sets `JAVA_HOME` to your JDK folder name.
    This is the folder in `C:\Program Files\Java\`.
    If using OpenJDK, the JDK folder may be stored under the name of your OpenJDK
    vendor like `C:\Program Files\Amazon Corretto\`.

5.  Once the change has been done, right click on `resume.bat` 
    and run as administrator to set environment variables
    and compile the NSL sources.

6.  Copy the files `SCS_PREFERENCES` and `SCS_LIBRARY_PATHS` to your home folder.
    The folder to store it in should be `C:\Users\<username>`.

Basic NSL installation should now be complete.

To uninstall NSL at any time:

1.  Remove the `NSL3_0_s` folder.

2.  Remove the following environment variables:
```
NSLJ_ROOT
NSL_OS
NSL_SIM
CLASSPATH
```

3. Remove the following entries from the PATH environment variable:
```
%NSLJ_ROOT%\nslj\bin
```
