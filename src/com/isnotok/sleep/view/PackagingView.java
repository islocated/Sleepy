package com.isnotok.sleep.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.CacheManager;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.Resource;

//Implement ISelectionProvider if we want this view to return the zoom
public class PackagingView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.PackagingView";
	private static final String[] TYPES = {"tile", "sprite", "room", "music", "timbre", "scale"};
	
	private GalleryViewer gallery;
	
	private PakManager pakManager = new PakManager();
	
	public PackagingView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(final Composite parent) {
		// TODO Auto-generated method stub
		
		Composite canvas = new Composite(parent, SWT.NONE);
		GridData gridData;
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		canvas.setLayout(gridLayout);
		
		//Sets up the ability for this view to get selection events from all views
		//getSite().getPage().addSelectionListener(this);

		gallery = new GalleryViewer(canvas, SWT.V_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		gallery.setLayoutData(gridData);
		
		gallery.setDefaultRenderers();
		
		addDragNDrop();
		
		gallery.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.keyCode == SWT.BS){
					GalleryItem [] galleryItems = gallery.getSelection();
					if(galleryItems == null)
						return;
					
					for(GalleryItem galleryItem : galleryItems){
						File file = (File) galleryItem.getData();
						File parent = file.getParentFile();
						pakManager.removeFile(parent.getName(), file);
					}
					
					pakManager.setFilter("");
					gallery.clearAll();
					gallery.setItemCount(TYPES.length);
				}
			}
			
		});

		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if (index >= 0 && index < TYPES.length) {
						// This is group 1
						item.setText(TYPES[index]);
						item.setItemCount(pakManager.getFileCount(TYPES[index]));
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

					Image img = new Image(parent.getDisplay(), resource.getImageData());
					item.setImage(img);
					item.setData(resourceFile);
				}
			}
		});
	}

	private void addDragNDrop() {
		//Drag drop
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { FileTransfer.getInstance() };
        DropTarget target = new DropTarget(gallery, ops);
        target.setTransfer(transfers);
        target.addDropListener(new DropTargetListener(){

			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}

			public void drop(DropTargetEvent event) {
				// TODO Auto-generated method stub
				if (FileTransfer.getInstance().isSupportedType(event.currentDataType)){
					String[] files = (String[]) event.data;
					for (int i = 0; i < files.length; i++) {
						File file = new File(files[i]);
						
						File parent = file.getParentFile();
						pakManager.addFile(parent.getName(), file);
						//Calls update filter to get filtered cache to update
						//TODO:make this better
						//Resource resource = CacheManager.getInstance().getResource(file);
						//resources.add(resource);
					}
				}
				
				pakManager.setFilter("");
				
				System.out.println("dropped");
				gallery.clearAll();
				gallery.setItemCount(TYPES.length);
			}

			public void dropAccept(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}
        });
	}

	@Override
	public void setFocus() {
		gallery.setFocus();
	}
	
	//ISelectionListner interface
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		//addSelection(selection);
	}
}
