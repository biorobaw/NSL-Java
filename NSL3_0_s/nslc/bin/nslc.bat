@echo off
java -cp ".;%NSLJ_ROOT%\nslc\src;%NSLJ_ROOT%;%NSL_SIM%\main;%NSL_SIM%\nsls\jacl;%NSL_SIM%\nsls\tcljava" NslCompiler %1 %2 %3 %4 %5 %6 %7 %8 %9
@echo on
