/*
 * Copyright (c) 2002-2007 TeamDev Ltd. All rights reserved.
 *
 * Use is subject to license terms.
 *
 * The complete licence text can be found at
 * http://www.teamdev.com/winpack/license.jsf
 */
package com.jniwrapper.win32.registry.ui;

import com.jniwrapper.win32.registry.RegistryKey;
import com.jniwrapper.win32.registry.RegistryException;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.List;

/**
 * Windows Registry Tree Model.
 *
 * @author Serge Piletsky
 */
public class RegistryTreeModel extends DefaultTreeModel
{
    /**
     * Creates a default windows registry tree model.
     */
    public RegistryTreeModel()
    {
        super(new DefaultMutableTreeNode("Windows Registry"));
        MutableTreeNode root = (MutableTreeNode)getRoot();
        MutableTreeNode classesRoot = new RegistryKeyNode(RegistryKey.CLASSES_ROOT);
        insertNodeInto(classesRoot, root, 0);
        MutableTreeNode currentUser = new RegistryKeyNode(RegistryKey.CURRENT_USER);
        insertNodeInto(currentUser, root, 1);
        MutableTreeNode localMachine = new RegistryKeyNode(RegistryKey.LOCAL_MACHINE);
        insertNodeInto(localMachine, root, 2);
        MutableTreeNode users = new RegistryKeyNode(RegistryKey.USERS);
        insertNodeInto(users, root, 3);
        MutableTreeNode currentConfig = new RegistryKeyNode(RegistryKey.CURRENT_CONFIG);
        insertNodeInto(currentConfig, root, 4);
    }

    /**
     * Creates a model with a specified registry key in the root.
     *
     * @param rootRegistryKey is a root registry key.
     */
    public RegistryTreeModel(RegistryKey rootRegistryKey)
    {
        super(new RegistryKeyNode(rootRegistryKey));
    }

    /**
     * Sets a root registry key.
     *
     * @param rootRegistryKey
     */
    public void setRootRegistryKey(RegistryKey rootRegistryKey)
    {
        setRoot(new RegistryKeyNode(rootRegistryKey));
    }

    public static class RegistryKeyNode extends DefaultMutableTreeNode
    {
        private List _subkeys;

        public RegistryKeyNode(RegistryKey registryKey)
        {
            super(registryKey, true);
        }

        public TreeNode getChildAt(int index)
        {
            return new RegistryKeyNode((RegistryKey)getSubkeys().get(index));
        }

        public int getChildCount()
        {
            int subKeyCount = 0;
            try
            {
                subKeyCount = getRegistryKey().getSubKeyCount();
            }
            catch (RegistryException e)
            {
            }
            return subKeyCount;
        }

        public String toString()
        {
            return getRegistryKey().getName();
        }

        private List getSubkeys()
        {
            if (_subkeys == null)
            {
                _subkeys = getRegistryKey().getSubkeys();
            }
            return _subkeys;
        }

        public RegistryKey getRegistryKey()
        {
            return (RegistryKey)getUserObject();
        }

        public boolean isLeaf()
        {
            return false;
        }
    }
}
