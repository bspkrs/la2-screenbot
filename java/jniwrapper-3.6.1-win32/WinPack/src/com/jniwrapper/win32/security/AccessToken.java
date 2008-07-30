/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.security;

import com.jniwrapper.*;
import com.jniwrapper.util.EnumItem;
import com.jniwrapper.win32.Handle;
import com.jniwrapper.win32.LastError;
import com.jniwrapper.win32.process.CurrentProcess;
import com.jniwrapper.win32.system.AdvApi32;

/**
 * This class helps to retrieve information about access tokens, containing
 * the security information for a logon session. The token identifies the user,
 * the user's groups, and the user's privileges.
 */

public class AccessToken extends Handle
{
    /**
     * Attaches a primary token to a process.
     */
    public static final int TOKEN_ASSIGN_PRIMARY = 0x0001;
    /**
     * Duplicates access token.
     */
    public static final int TOKEN_DUPLICATE = 0x0002;
    /**
     * Attaches impersonation access token to a process
     */
    public static final int TOKEN_IMPERSONATE = 0x0004;
    /**
     * Queries access token
     */
    public static final int TOKEN_QUERY = 0x0008;
    /**
     * Queries the source of access token
     */
    public static final int TOKEN_QUERY_SOURCE = 0x0010;
    /**
     * Enables or disables the privileges in access token
     */
    public static final int TOKEN_ADJUST_PRIVILEGES = 0x0020;
    /**
     * Adjusts the attributes of the groups in access token
     */
    public static final int TOKEN_ADJUST_GROUPS = 0x0040;
    /**
     * Changes the default owner, primary group, or DACL of access token
     */
    public static final int TOKEN_ADJUST_DEFAULT = 0x0080;
    /**
     * Adjusts the session ID of access token
     */
    public static final int TOKEN_ADJUST_SESSIONID = 0x0100;

    /**
     * Enables the privilege
     */
    private static final int SE_PRIVILEGE_ENABLED = 0x00000002;

    private static final String FUNCTION_OPEN_PROCESS_TOKEN = "OpenProcessToken";
    private static final String FUNCTION_GET_TOKEN_INFORMATION = "GetTokenInformation";
    private static final String FUNCTION_ADJUST_TOKEN_PRIVILEGES = "AdjustTokenPrivileges";

    /**
     * Creates a process token for the current process with ADJUST_PRIVILEGES, QUERY access.
     */
    public AccessToken()
    {
        openProcessToken(new CurrentProcess(), TOKEN_ADJUST_PRIVILEGES | TOKEN_QUERY, this);
    }

    /**
     * Opens the access token associated with a process.
     *
     * @param processHandle  - Handle to the process whose access token is opened.
     * @param desiredAcces   - Specifies an access mask that specifies the requested types of access to the access token.
     * @param resultToken   - Pointer to a handle that identifies the newly opened access token.
     * @return true if succeeded, else returns false
     */
    public static boolean openProcessToken(Handle processHandle, int desiredAcces, Handle resultToken)
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_OPEN_PROCESS_TOKEN);
        Bool returnValue = new Bool();
        function.invoke(returnValue, new Parameter[]{processHandle, new UInt32(desiredAcces), new Pointer(resultToken)});
        return returnValue.getValue();
    }

    /**
     * Retrieves a specified type of information about an access token
     *
     * @param tokenHandle  -  Handle to access token.
     * @param tokenInformationClass  - Specifies a value from the TOKEN_INFORMATION_CLASS enumerated type to identify the type of information the function retrieves.
     * @param tokenUserPtrVoid    -  Pointer to a buffer the function fills with the requested information.
     * @param tokenInformationLength  - Specifies the size, in bytes, of the buffer pointed to by the TokenInformation parameter.
     * @param returnLength    - Pointer to a variable that receives the number of bytes needed for the buffer.
     * @return true if succeeded, else returns false
     */
    public static boolean getTokenInformation(Handle tokenHandle, TokenInformationClass tokenInformationClass, Parameter tokenUserPtrVoid, UInt32 tokenInformationLength, Pointer returnLength)
    {
        IntBool result = new IntBool();

        Function getTokenInformation = AdvApi32.get(FUNCTION_GET_TOKEN_INFORMATION);
        getTokenInformation.invoke(result,
                new Parameter[]{tokenHandle, new Int(tokenInformationClass.getValue()), tokenUserPtrVoid, tokenInformationLength, returnLength});
        return result.getValue() != 0;
    }

    /**
     * Enables the specified privilege.
     *
     * @param name name of the privilege
     * @return true if succeeded, else returns false
     */
    public boolean enablePrivelege(String name)
    {
        TokenPrivileges tokenPrivileges = TokenPrivileges.lookup(name);

        if (tokenPrivileges != null)
        {
            tokenPrivileges.setPrivilegeCount(1);
            tokenPrivileges.getPrivileges(0).setAttributes(SE_PRIVILEGE_ENABLED);

            return adjustTokenPrivileges(tokenPrivileges);
        }

        return false;
    }

    /**
     * Changes privileges in the specified access token.
     *
     * @param newState  - TOKEN_PRIVILEGES structure representation that specifies an array of privileges and their attributes.
     * @return true if succeeded, else returns false
     */
    private boolean adjustTokenPrivileges(TokenPrivileges newState)
    {
        Function function = AdvApi32.getInstance().getFunction(FUNCTION_ADJUST_TOKEN_PRIVILEGES);

        Bool returnValue = new Bool();
        long lastError = function.invoke(returnValue, new Parameter[]{
                this,
                new Int(0),
                new Pointer(newState),
                new UInt32(0),
                new Handle(),
                new Pointer(new UInt32())
        });

        return returnValue.getValue() && (lastError == LastError.NO_ERROR);
    }

    /**
     * This class is the wrapper for the TOKEN_INFORMATION_CLASS enumeration, which contains values
     * that specify the type of information being assigned to or retrieved from the {@link AccessToken} class.
     */
    public static class TokenInformationClass extends EnumItem
    {
        /**
         * Specifies a TOKEN_USER structure that contains the user account of the token.
         */
        public static final TokenInformationClass TOKEN_USER = new TokenInformationClass(1);
        /**
         * Specifies a TOKEN_GROUPS structure that contains the group accounts associated with the token.
         */
        public static final TokenInformationClass TOKEN_GROUPS = new TokenInformationClass(2);
        /**
         * Specifies a TOKEN_PRIVILEGES structure that contains the privileges of the token.
         */
        public static final TokenInformationClass TOKEN_PRIVILEGES = new TokenInformationClass(2);
        /**
         * Specifies a TOKEN_OWNER structure that contains the default owner security
         * identifier (SID) for newly created objects.
         */
        public static final TokenInformationClass TOKEN_OWNER = new TokenInformationClass(3);
        /**
         * Specifies a TOKEN_PRIMARY_GROUP structure that contains the default
         * primary group SID for newly created objects.
         */
        public static final TokenInformationClass TOKEN_PRIMARY_GROUP = new TokenInformationClass(5);
        /**
         * Specifies a TOKEN_DEFAULT_DACL structure that contains the default DACL
         * for newly created objects.
         */
        public static final TokenInformationClass TOKEN_DEFAULT_DACL = new TokenInformationClass(6);
        /**
         * Specifies a TOKEN_SOURCE structure that contains the source of the token.
         * TOKEN_QUERY_SOURCE access is needed to retrieve this information.
         */
        public static final TokenInformationClass TOKEN_SOURCE = new TokenInformationClass(7);
        /**
         * Specifies a value that indicates whether the token is a primary or impersonation token.
         */
        public static final TokenInformationClass TOKEN_TYPE = new TokenInformationClass(8);
        /**
         * Specifies a value that indicates the impersonation level of the token.
         */
        public static final TokenInformationClass TOKEN_IMPERSONATION_LEVEL = new TokenInformationClass(9);
        /**
         * Specifies a TOKEN_STATISTICS structure that contains various token statistics.
         */
        public static final TokenInformationClass TOKEN_STATISTICS = new TokenInformationClass(10);
        /**
         * Specifies a TOKEN_RESTRICTED_SIDS structure that contains the list of restricting SIDs in a restricted token.
         */
        public static final TokenInformationClass TOKEN_RESTRICTED_SIDS = new TokenInformationClass(11);
        /**
         * Specifies a value that indicates the Terminal Services session identifier that is associated with the token.
         */
        public static final TokenInformationClass TOKEN_SESSION_ID = new TokenInformationClass(12);
        /**
         * Specifies a TOKEN_GROUPS_AND_PRIVILEGES structure that contains the user SID, the group accounts, the restricted SIDs, and the authentication ID associated with the token.
         */
        public static final TokenInformationClass TOKEN_GROUPS_AND_PRIVILEGES = new TokenInformationClass(13);
        /**
         * Reserved
         */
        public static final TokenInformationClass TOKEN_SESSION_REFERENCE = new TokenInformationClass(14);
        /**
         * Specifies a value that is nonzero if the token includes the SANDBOX_INERT flag.
         */
        public static final TokenInformationClass TOKEN_SAND_BOX_INERT = new TokenInformationClass(15);
        /**
         * Reserved
         */
        public static final TokenInformationClass TOKEN_AUDIT_POLICY = new TokenInformationClass(16);
        /**
         * Specifies a TOKEN_ORIGIN structure which can contains the ID of the logon session that created it.
         */
        public static final TokenInformationClass TOKEN_ORIGIN = new TokenInformationClass(17);

        private TokenInformationClass(int value)
        {
            super(value);
        }
    }
}
