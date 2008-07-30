/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.win32.winhttp.DefaultProxySettings;
import com.jniwrapper.win32.winhttp.IEProxySettings;
import com.jniwrapper.win32.winhttp.RemoteProxySettings;

import java.util.Arrays;

/**
 * This sample demonstrates how to get Proxy settings using
 * the WinHTTP functionality. 
 *
 * @author Vladimir Kondrashchenko
 */
public class WinHttpSample
{
    public static void main(String[] args) throws Exception
    {
        //Receiving the system default proxy settings
        DefaultProxySettings proxySettings = new DefaultProxySettings();
        System.out.println("Default Proxy Settings: ");
        System.out.println("\tIs Proxy Set: " + proxySettings.isSet());
        System.out.println("\tProxy URL: " + proxySettings.getURL());
        System.out.println("\tProxy Port: " + proxySettings.getPort());
        System.out.println("\tProxy Bypass: " + Arrays.asList(proxySettings.getProxyBypass()));

        //Receiving the IE proxy settings
        System.out.println("\nInternet Explorer Proxy Settings:");
        IEProxySettings ieProxySettings = new IEProxySettings();
        System.out.println("\tIs Proxy Set: " + ieProxySettings.isSet());
        System.out.println("\tProxy URL: " + ieProxySettings.getURL());
        System.out.println("\tProxy Port: " + ieProxySettings.getPort());
        System.out.println("\tProxy Bypass: " + Arrays.asList(ieProxySettings.getProxyBypass()));
        System.out.println("\tIs Auto Detect: " + ieProxySettings.isAutoDetect());
        System.out.println("\tAuto Config URL: " + ieProxySettings.getAutoConfigURL());

        //Receiving proxy settings for URL
        System.out.println("\nProxy Settings for the Specified URL: ");
        RemoteProxySettings remoteProxySettings = new RemoteProxySettings("www.google.com", true);
        System.out.println("\tIs Proxy Set: " + remoteProxySettings.isSet());
        System.out.println("\tProxy URL: " + remoteProxySettings.getURL());
        System.out.println("\tProxy Port: " + remoteProxySettings.getPort());
        System.out.println("\tProxy Bypass:" + Arrays.asList(remoteProxySettings.getProxyBypass()));
    }
}
