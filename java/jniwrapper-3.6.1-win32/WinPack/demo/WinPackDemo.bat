@echo off

REM Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.

set LIB_DIRECTORY=../lib
set JNIW_LIB_DIRECTORY=../../lib
set BIN_DIRECTORY=../../bin

set PATH=%PATH%;%BIN_DIRECTORY%

if not exist %BIN_DIRECTORY%/jniwrap.lic goto error

set CORE_CLASSES=%JNIW_LIB_DIRECTORY%/jniwrap-3.6.1.jar;%JNIW_LIB_DIRECTORY%/comfyj-2.4.jar;%LIB_DIRECTORY%/winpack-3.6.jar
set CUSTOM_CLASSES=winpackdemo.jar

set SAMPLE_CLASSPATH=%CORE_CLASSES%;%CUSTOM_CLASSES%
set SAMPLE_MAINCLASS=com.jniwrapper.win32.samples.demo.WinPackDemo

start "" javaw.exe -Dsun.java2d.noddraw=true -Djavax.swing.adjustPopupLocationToFit=false -classpath %SAMPLE_CLASSPATH% %SAMPLE_MAINCLASS%
goto end

:error
echo Cannot start WinPackDemo: no JNIWrapper license file found. Ensure the license is located in the bin subfolder of the JNIWrapper distribution.
echo.
pause

:end
