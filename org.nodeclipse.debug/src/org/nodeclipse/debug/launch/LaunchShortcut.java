package org.nodeclipse.debug.launch;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.nodeclipse.debug.util.Constants;
import org.nodeclipse.ui.util.NodeclipseConsole;

/**
 * Using "Run As" --> "Node Application" or "Run As" --> "coffee" will lead here
 **/
public class LaunchShortcut implements ILaunchShortcut {
	
    private void showDialogNotImplemented(String what) {
    	//TODO CommonDialog "Not Implemented"
        MessageDialog.openWarning(null, "Warning", 
        	"Launching of type "+what+" is not implemeneted yet!\n"+
        	"Search/raise an issue if you care at https://github.com/nodeclipse/nodeclipse-1/issues/");
	}
	

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers
     *      .ISelection, java.lang.String)
     **/
    @Override
    public void launch(ISelection selection, String mode) {
        try {
            Object selectObj = ((IStructuredSelection) selection).getFirstElement();
            if (selectObj instanceof IFile) {
                launchFile((IFile) selectObj, mode);
            } else {
                //MessageDialog.openWarning(null, "Warning", "Not implemeneted yet!");
                showDialogNotImplemented(selection.getClass().getName());
            }
        } catch (CoreException e) {
        	NodeclipseConsole.write(e.getLocalizedMessage()+"\n");
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
     *      java.lang.String)
     **/
    @Override
    public void launch(IEditorPart editor, String mode) {
        try {
            IEditorInput editorInput = editor.getEditorInput();
            if (editorInput instanceof IFileEditorInput) {
                IFile selectObj = ((IFileEditorInput) editorInput).getFile();
                launchFile((IFile) selectObj, mode);
            } else {
                //MessageDialog.openWarning(null, "Warning", "Not implemeneted yet!");
                showDialogNotImplemented(editor.getClass().getName());
            }
        } catch (CoreException e) {
        	NodeclipseConsole.write(e.getLocalizedMessage()+"\n");
        }
    }

    /**
     * Launch an file,using the file information, which means using default
     * launch configurations.
     * 
     * @param file
     * @param mode
     */
    private void launchFile(IFile file, String mode) throws CoreException {
        // check for an existing launch config for the file
        String path = file.getFullPath().toString();
        ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(Constants.LAUNCH_CONFIGURATION_TYPE_ID);
        ILaunchConfiguration configuration = createLaunchConfiguration(type, path, file);
        DebugUITools.launch(configuration, mode);
        // then execution goes in LaunchConfigurationDelegate.java launch() method
    }

    /**
     * Create a new configuration and set useful data.
     * 
     * @param type
     * @param path
     * @param file
     * @return
     * @throws CoreException
     */
    private ILaunchConfiguration createLaunchConfiguration(ILaunchConfigurationType type, String path, IFile file) throws CoreException {
    	String configname = file.getFullPath().toString().replace('/', '-');
    	if(configname.startsWith("-")) {
    		configname = configname.substring(1);
    	}

    	ILaunchConfiguration[] configs = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(type);
    	for(ILaunchConfiguration config : configs) {
    		if(configname.equals(config.getName())) {
    			return config;
    		}
    	}
    	
    	IContainer container = null;
    	//TODO save LaunchConfiguration in project's .settings folder by default
//    	if(true){
//    		container =  
//    	}
    	
    	// create a new configuration for the file
        ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(container, configname);
        workingCopy.setAttribute(Constants.KEY_FILE_PATH, path);
        setMoreAttributes(workingCopy);
        return workingCopy.doSave();
    }

	protected void setMoreAttributes(ILaunchConfigurationWorkingCopy workingCopy) {
		NodeclipseConsole.write(this.getClass().getName()+"\n");
	}
}
