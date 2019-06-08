@echo off

echo Please ensure the batch file sets JAVA_HOME to the path of your Java installation before continuing!
pause

echo Initializing NSL environment variables

setx NSLJ_ROOT "C:\Program Files\NSL3_0_s" /m
setx JAVA_HOME "C:\Program Files\Java\<jdk-version>" /m
setx NSL_OS "nt" /m

echo Updating path and classpath

setx NSL_SIM "%NSLJ_ROOT%\nslj\src" /m
setx path "%PATH%;%JAVA_HOME%\bin;%NSLJ_ROOT%\nslj\bin;%NSLJ_ROOT%\nslc\bin" /m
setx CLASSPATH ".;%NSLJ_ROOT%\nslc\src;%NSLJ_ROOT%;%NSL_SIM%\main;%NSL_SIM%\nsls\jacl;%NSL_SIM%\nsls\tcljava" /m
doskey /insert

echo Compiling NSL (May produce a ton of errors; they could be disregarded)

cd %NSLJ_ROOT%
find -name "*.java" > sources.txt
javac @sources.txt

pause

@echo on
