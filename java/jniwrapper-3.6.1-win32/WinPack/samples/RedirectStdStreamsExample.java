package com.jniwrapper.win32.samples;

import com.jniwrapper.*;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.process.ProcessOptions;
import com.jniwrapper.win32.process.StartupInfo;
import com.jniwrapper.win32.process.ProcessVariables;
import com.jniwrapper.win32.system.Kernel32;
import com.jniwrapper.win32.system.SecurityAttributes;

/**
 * This example demonstrates how to redirect STDOUT and STDERR process streams to files.
 */
public class RedirectStdStreamsExample {

    final static long CREATE_ALWAYS = 2;

    final static long FILE_ATTRIBUTE_NORMAL = 0x00000080;

    final static long STANDARD_RIGHTS_REQUIRED = 0x000F0000L;
    final static long SYNCHRONIZE = 0x00100000L;
    final static long FILE_ALL_ACCESS = STANDARD_RIGHTS_REQUIRED | SYNCHRONIZE | 0x3FF;

    public static void main(String[] args) {


        // These security attributes have 'true' on the ability to inherit handles
        SecurityAttributes securityAttributes = new SecurityAttributes();
        securityAttributes.setInheritHandle(true);

        String stdOutFileName = "C:/Temp/stdOut.txt";
        String stdErrFileName = "C:/Temp/stdErr.txt";

        // Creating a new StartupInfo
        StartupInfo.Options startInfoOpts = new StartupInfo.Options();
        startInfoOpts.setUseStdHandles(true);
        StartupInfo startInfo = new StartupInfo(startInfoOpts);

        Function createF = Kernel32.getInstance().getFunction("CreateFileW");

        Handle stdOut = new Handle();
        createF.invoke(stdOut, new Parameter[]{
                new WideString(stdOutFileName),
                new UInt32(FILE_ALL_ACCESS),
                new UInt32(0),
                new Pointer(securityAttributes),
                new UInt32(CREATE_ALWAYS),
                new UInt32(FILE_ATTRIBUTE_NORMAL),
                new Pointer(null, true),
        });
        System.out.println("New StdOut: " + stdOut);
        startInfo.getStdOutput().setValue(stdOut.getValue());

        Handle stdErr = new Handle();
        createF.invoke(stdErr, new Parameter[]{
                new WideString(stdErrFileName),
                new UInt32(FILE_ALL_ACCESS),
                new UInt32(0),
                new Pointer(securityAttributes),
                new UInt32(CREATE_ALWAYS),
                new UInt32(FILE_ATTRIBUTE_NORMAL),
                new Pointer(null, true)
        });
        System.out.println("New StdErr: " + stdErr);
        startInfo.getStdError().setValue(stdErr.getValue());

        ProcessOptions procOptions = new ProcessOptions();
        
        com.jniwrapper.win32.process.Process process = new com.jniwrapper.win32.process.Process(
                null,
                "java.exe -help", // "java.exe -version" will write information to new stderr handle 
                securityAttributes,
                null,
                true, // inheritHandles
                procOptions,
                new ProcessVariables(),
                null,
                startInfo
        );
        process.waitFor();
        process.close();

        Handle.closeHandle(stdErr);
        Handle.closeHandle(stdOut);
    }
}
