package com.isnotok.sleep.handler;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.isnotok.sleep.editor.CacheEditor;
import com.isnotok.sleep.editor.CachePakEditor;
import com.isnotok.sleep.input.PakFileInput;
import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.view.NavigatorView;

public class OpenPakHandler implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage page = window.getActivePage();
		final NavigatorView view = (NavigatorView) page
				.findView(NavigatorView.ID);

		ISelection selection = view.getSite().getSelectionProvider()
				.getSelection();

		if (selection != null) {
			if (selection != null && selection instanceof IStructuredSelection) {
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				// If we had a selection lets open the editor
				if (obj != null) {
					final File file = (File) obj;
					if (!file.getName().endsWith(".pak"))
						return null;

					Job job = new Job("Unpacking pak file...") {

						@Override
						protected IStatus run(IProgressMonitor monitor) {
							PakFile pfile = new PakFile(file);
							pfile.load();
							pfile.unpack();

							Display display = Display.getDefault();
							display.syncExec(new Runnable() {

								public void run() {
									// TODO Auto-generated method stub
									view.getCommonViewer().refresh();
									
									File newfile = new File(file.getParentFile(), file
											.getName().replace('.', '-'));

									PakFileInput pakFile = new PakFileInput(newfile);
									try {
										page.openEditor(pakFile, CachePakEditor.ID);
									} catch (PartInitException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

							});

							

							// TODO Auto-generated method stub
							return Status.OK_STATUS;
						}

					};
					job.schedule();
				}
			}
			return null;

		}

		return null;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub

		return true;
	}

	public boolean isHandled() {
		// TODO Auto-generated method stub
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
