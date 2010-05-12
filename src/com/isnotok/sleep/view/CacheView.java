package com.isnotok.sleep.view;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.gallery.AbstractGridGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.CacheManager;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.Resource;
import com.isnotok.sleep.model.ResourceTypes;
import com.isnotok.sleep.model.TileResource;

public class CacheView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.CacheView";
	//private static final String[] TYPES = {"object", "tile", "sprite", "room", "music", "timbre", "scale"};
	
	private HashMap<String, Image> map = new HashMap<String, Image>();
	
	
	//private File resourceCache;
	private GalleryViewer gallery;
	//private CacheManager cache = new CacheManager();
	private PakManager pakManager = new PakManager();

	public CacheView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(final Composite parent) {
		Composite grid = new Composite(parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		grid.setLayout(gridLayout);
		
		GridData gridData;

		//Add Gallery to grid
		gallery = new GalleryViewer(grid, SWT.V_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 3;
		gallery.setLayoutData(gridData);
		
		gallery.setAsSelectionProvider(getSite());
		
		gallery.setDefaultRenderers();

		//Sets up the ability for this view to get selection events from all views
		getSite().getPage().addSelectionListener(this);

		//Drag drop
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { FileTransfer.getInstance() };
        DragSource source = new DragSource(gallery, ops);
        source.setTransfer(transfers);
        source.addDragListener(new DragSourceListener(){

			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				
			}

			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				 if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
					 GalleryItem [] galleryItems = gallery.getSelection();
					 List<String> files = new ArrayList<String>(galleryItems.length);
					 //String [] files = new String[galleryItems.length];
					 for(int i = 0; i < galleryItems.length; i++){
						 File file = (File) galleryItems[i].getData();
						 if(file == null)
							 continue;
						 files.add(file.getAbsolutePath());
					 }
					 event.data = files.toArray(new String[0]);
				}
			}

			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(gallery.getSelection() == null){
					event.doit = false;
				}
			}
        });
		
		

		setupScale(grid);
        
		//Set the filter for the keywords
		setFilterField(grid);
		
		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if(index >= 0 && index < ResourceTypes.TYPES.length){
						item.setText(ResourceTypes.TYPES[index]);
						item.setItemCount(pakManager.getFileCount(ResourceTypes.TYPES[index]));
						item.setExpanded(true);
					}
					else {
						// Should never be used
						item.setItemCount(0);
					}
				} else {
					// It's an item
					GalleryItem parentItem = item.getParentItem();

					// Get item index
					int index = parentItem.indexOf(item);
					
					File resourceFile = pakManager.getFilesByType(parentItem.getText(), index);
					
					Resource resource = CacheManager.getInstance().getResource(resourceFile);
					
					item.setText(resource.getResourceName());

					Image img = map.get(resource.getFile().getAbsolutePath());
					if(img == null){
						img = new Image(parent.getDisplay(), resource.getImageData());
						map.put(resource.getFile().getAbsolutePath(), img);
					}
						
					item.setImage(img);
					item.setData(resourceFile);
				}
			}
		});
	}

	private void setupScale(Composite grid) {
		GridData gridData;
		final Scale scale = new Scale(grid, SWT.NONE);
		scale.setMinimum(2);
		scale.setMaximum(15);
		scale.setIncrement(1);
		gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gridData.horizontalSpan = 1;
		scale.setLayoutData(gridData);
		scale.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				AbstractGridGroupRenderer gr = (AbstractGridGroupRenderer) gallery.getGroupRenderer();
				gr.setItemSize(32 * scale.getSelection()+ 10, 32 * scale.getSelection());
			}
			
		});
		
		scale.setSelection(3);
	}
	

	private void setFilterField(Composite grid) {
		final Text text = new Text(grid, SWT.SEARCH);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		//gridData.h
		gridData.horizontalSpan = 2;
		text.setLayoutData(gridData);
		
		text.setMessage("Filter by keyword");
		text.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				pakManager.setFilter(text.getText());
				//System.out.println("filtering: " + text.getText());
				gallery.clearAll();
				disposeGalleryItems();
				gallery.setItemCount(ResourceTypes.TYPES.length);
			}
			
		});
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		gallery.setFocus();
	}
	
	//ISelectionListner interface
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if(selection instanceof IStructuredSelection){
			IStructuredSelection sel = (IStructuredSelection) selection;
			Object element = sel.getFirstElement();
			if(element instanceof File){
				final File file = (File) element;
				File [] files = file.listFiles();
				
				if(files == null){
					return;
				}
				
				//System.out.println("detect view:" + file.getName());
				
				for(File f : file.listFiles()){
					//System.out.println("   listing: " + f.getName());
					for(String s : ResourceTypes.TYPES){
						if(f.getName().equals(s)){
							
							CacheManager.getInstance().clearCache();
							
							Job job = new Job("Loading resources..."){

								@Override
								protected IStatus run(IProgressMonitor monitor) {
									File [] types = file.listFiles();
									if(types == null)
										return Status.OK_STATUS;
									
									int count = 0;
									int worked = 0;
									for(File type : types){
										File [] children = type.listFiles();
										
										if(children == null){
											continue;
										}
										
										count += children.length; //CacheManager.getInstance().getResource(res);
									}
									
									
									monitor.beginTask("Resource", count + 5);
									
									
									for(File type : types){
										File [] children = type.listFiles();
										
										if(children == null){
											continue;
										}
										
										for(File res : children){
											Resource resource = CacheManager.getInstance().getResource(res);
											if(resource != null){
												resource.getImageData();
											}
											else{
												System.out.println("what happened here?: " + res.getAbsolutePath());
											}
											monitor.worked(1);
											worked += 1;
										}
									}
									
									
									System.out.println(count + " : worked: " + worked);
									
									pakManager.initDirectory(file);
									monitor.worked(5);
									
									//CacheManager.getInstance().setCacheDirectory(file);
									//if(file.getName().equals("resourceCache")){
									//resourceCache = file;
									//gallery.re
									
									Display display = Display.getDefault();
									display.syncExec(new Runnable(){

										public void run() {
											// TODO Auto-generated method stub
											gallery.clearAll();
											disposeGalleryItems();	
											gallery.setItemCount(ResourceTypes.TYPES.length);
										}
										
									});
									
									monitor.done();
									
									// TODO Auto-generated method stub
									return Status.OK_STATUS;
								}
								
							};
							
							//job.setUser(true);
							job.schedule();
							
							/*
							ProgressMonitorDialog dialog = new ProgressMonitorDialog(this.getSite().getShell());
						    try {
								dialog.run(true, true, new IRunnableWithProgress(){
								    public void run(IProgressMonitor monitor)
											throws InvocationTargetException,
											InterruptedException {
										// TODO Auto-generated method stub
										 monitor.beginTask("Loading cache resources...", 100);
								            
								            pakManager.initDirectory(file);
											monitor.worked(100);
								            // execute the task ...
								            monitor.done();
									}
								});
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/

							
							//System.out.println("   view should be refreshed: " + file.getName());
							return;
						}
					}
				}
			}
		}
	}
	
	@Override
	public void dispose() {
		disposeGalleryItems();
		// TODO Auto-generated method stub
		super.dispose();
	}

	private void disposeGalleryItems() {
		// TODO Auto-generated method stub
		for(Image img : map.values()){
			if(img != null && !img.isDisposed()){
				img.dispose();
				//System.out.println("disposing images");
			}
		}
		map.clear();
	}
}
