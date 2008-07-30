/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
import com.jniwrapper.*;
import com.jniwrapper.util.FunctionCache;
import com.jniwrapper.win32.FunctionName;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastError;

/**
 * This sample includes implementations of several commonly used functions
 * of Windows Active Directory API, particularly DsBindWithCred, DsMakePasswordCredentials,
 * DsBind, DsAddSidHistory, DsFreePasswordCredentials and DsUnBind.
 *
 * @author Vladimir Kondrashchenko
 */
public class ActiveDirectoryAPISample
{
    private static final FunctionName FUNCTION_DSBINDWCRED = new FunctionName("DsBindWithCred");
    private static final FunctionName FUNCTION_DSMAKEPWCRED = new FunctionName("DsMakePasswordCredentials");
    private static final FunctionName FUNCTION_DSBIND = new FunctionName("DsBind");
    private static final FunctionName FUNCTION_DSADDSIDHISTORY = new FunctionName("DsAddSidHistory");
    private static final String FUNCTION_DSFREEPASSWORDCRED = "DsFreePasswordCredentials";
    private static final FunctionName FUNCTION_DSUNBIND = new FunctionName("DsUnBind");


    public static void main(String[] args)
    {
        Handle ds = DsBind(null, null);
        DsFreePasswordCredentials(null);
        DsUnBind(ds);
    }

    public static Handle DsBindWithCred(String domainControllerName, String domainName, Handle authIdentity)
    {
        Function dsBindWCred = NtdsApi.getInstance().getFunction(FUNCTION_DSBINDWCRED.toString());

        Handle ds = new Handle();
        UInt32 result = new UInt32();

        dsBindWCred.invoke(result, new Parameter[]
        {
            domainControllerName == null ? new Pointer(null, true) :
                new Pointer(new Str(domainControllerName)),
            domainName == null ? new Pointer(null, true) : new Pointer(new Str(domainName)),
            authIdentity,
            new Pointer(ds)
        });

        if (result.getValue() != 0)
        {
            throw new RuntimeException(LastError.getMessage(result.getValue()));
        }

        return ds;
    }


    public static Handle DsMakePasswordCredentials(String userName, String domainName, String password)
    {
        Function DsMakePasswordCredentials = NtdsApi.getInstance().getFunction(FUNCTION_DSMAKEPWCRED.toString());

        Handle ds = new Handle();
        UInt32 result = new UInt32();

        DsMakePasswordCredentials.invoke(result, new Parameter[]
        {
            userName == null ? new Pointer(null, true) : new Pointer(new Str(userName)),
            domainName == null ? new Pointer(null, true) : new Pointer(new Str(domainName)),
            password == null ? new Pointer(null, true) : new Pointer(new Str(password)),
            new Pointer(ds)
        });

        if (result.getValue() != 0)
        {
            throw new
                    RuntimeException(LastError.getMessage(result.getValue()));
        }

        return ds;
    }

    public static Handle DsBind(String domainControllerName, String domainName)
    {
        Function dsBind = NtdsApi.getInstance().getFunction(FUNCTION_DSBIND.toString());

        Handle ds = new Handle();
        UInt32 result = new UInt32();

        dsBind.invoke(result, new Parameter[]
        {
            domainControllerName == null ? new Pointer(null, true) : new Pointer(new Str(domainControllerName)),
            domainName == null ? new Pointer(null, true) : new Pointer(new Str(domainName)),
            new Pointer(ds)
        });

        if (result.getValue() != 0)
        {
            throw new RuntimeException(LastError.getMessage(result.getValue()));
        }

        return ds;
    }

    public static void DsAddSidHistory(Handle DS, String srcDomain, String srcPrincipal, String srcDomainController,
                                       String dstDomain, String dstPrincipal)
    {
        Function dsAddSidHiFunction = NtdsApi.getInstance().getFunction(FUNCTION_DSADDSIDHISTORY.toString());

        UInt32 result = new UInt32();

        dsAddSidHiFunction.invoke(result, new Parameter[]
        {
            DS,
            new UInt32(0),
            srcDomain == null ? new Pointer(null, true) : new Pointer(new Str(srcDomain)),
            srcPrincipal == null ? new Pointer(null, true) : new Pointer(new Str(srcPrincipal)),
            srcDomainController == null ? new Pointer(null, true) : new Pointer(new Str(srcDomainController)),
            new Pointer.Void(),
            dstDomain == null ? new Pointer(null, true) : new Pointer(new Str(dstDomain)),
            dstPrincipal == null ? new Pointer(null, true) : new Pointer(new Str(dstPrincipal))
        });

        if (result.getValue() != 0)
        {
            throw new RuntimeException(LastError.getMessage(result.getValue()));
        }
    }

    public static void DsFreePasswordCredentials(Handle authIdentity)
    {
        Function DsFreePasswordCredentials = NtdsApi.getInstance().getFunction(FUNCTION_DSFREEPASSWORDCRED);

        DsFreePasswordCredentials.invoke(null,
                authIdentity == null ? new Handle() : authIdentity);
    }

    public static void DsUnBind(Handle ds)
    {
        Function DsUnBind = NtdsApi.getInstance().getFunction(FUNCTION_DSUNBIND.toString());

        UInt32 result = new UInt32();

        DsUnBind.invoke(result, ds == null ? new Pointer(null, true) : new Pointer(ds));

        if (result.getValue() != 0)
        {
            throw new RuntimeException(LastError.getMessage(result.getValue()));
        }
    }

    /**
     * This class represents Ntdsapi.dll native library.
     */
    public static class NtdsApi extends FunctionCache
    {
        private static NtdsApi _instance;

        private NtdsApi()
        {
            super("NtdsApi");
        }

        public static NtdsApi getInstance()
        {
            if (_instance == null)
            {
                _instance = new NtdsApi();
            }
            return _instance;
        }
    }
}
