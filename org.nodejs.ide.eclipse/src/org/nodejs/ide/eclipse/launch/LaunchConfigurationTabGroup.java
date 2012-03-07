/*******************************************************************************
 * Copyright (c) 2012 Lamb. All rights reserved. 
 *
 *******************************************************************************/
package org.nodejs.ide.eclipse.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * Using "Run"-->"Run Configurations"--> "New Configuration"-- > "Run" willlead
 * here.
 **/
public class LaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

    @Override
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
        ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { new LaunchConfigurationMainTab(), new CommonTab() };
        setTabs(tabs);

    }
}
