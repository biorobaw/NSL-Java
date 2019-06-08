@echo off

echo Initializing environment variables
echo For safety reasons, we cannot modify PATH for you.
echo Ensure to add the following to PATH manually after this is done:
echo "%JAVA_HOME%\bin;%NSLJ_ROOT%\nslj\bin;%NSLJ_ROOT%\nslc\bin"
echo.

setx NSLJ_ROOT "C:\Program Files\NSL3_0_s" /m
setx JAVA_HOME "C:\Program Files\Java\<jdk-version>" /m
setx NSL_OS "nt" /m
setx NSL_SIM "%NSLJ_ROOT%\nslj\src" /m
setx CLASSPATH ".;%NSLJ_ROOT%\nslc\src;%NSLJ_ROOT%;%NSL_SIM%\main;%NSL_SIM%\nsls\jacl;%NSL_SIM%\nsls\tcljava" /m
doskey /insert

echo.
echo Compiling NSL (May produce a ton of errors)

cd %NSLJ_ROOT%
dir *.java /s /b > sources.txt
javac -cp "%CLASSPATH%" @sources.txt

pause

@echo on
