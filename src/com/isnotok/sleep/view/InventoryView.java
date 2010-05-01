package com.isnotok.sleep.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.PakRecord;

//Implement ISelectionProvider if we want this view to return the zoom
public class InventoryView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.InventoryView";
	private GalleryViewer gallery;
	
	private List<GalleryItem> galleryItems = new ArrayList<GalleryItem>();

	public InventoryView() {
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

		gallery = new GalleryViewer(canvas, SWT.V_SCROLL | SWT.VIRTUAL);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		gallery.setLayoutData(gridData);
		
		gallery.setDefaultRenderers();

		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					/*
					if (index >= 0 && index < TYPES.length) {
						// This is group 1
						item.setText(TYPES[index]);
						item.setItemCount(pakfile.getResourceType(TYPES[index]).length);
						item.setExpanded(true);
					} 
					*/
					if(index == 0){
						item.setText("IMAGE");
						item.setItemCount(galleryItems.size());
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
					
					item.setText(galleryItems.get(index).getText());
					item.setImage(galleryItems.get(index).getImage());
					item.setData(galleryItems.get(index).getData());
				}
			}
		});
		
		//Drag drop
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
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
				System.out.println("dropped");
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
		addSelection(selection);
	}

	public void addSelection(ISelection selection) {
		// TODO Auto-generated method stub
		if(selection instanceof IStructuredSelection){
			IStructuredSelection sel = (IStructuredSelection) selection;
			Object element = sel.getFirstElement();
			if(element == null){
				return;
			}
			
			System.out.println(element.getClass());
			
			if(element instanceof GalleryItem){
				GalleryItem [] gi = (GalleryItem[]) sel.toArray();
				
				for(GalleryItem item: gi){
					if(!galleryItems.contains(gi))
						galleryItems.add(item);
				}
				
				gallery.clearAll();
				gallery.setItemCount(1);
			}
		}
	}

	public List<PakRecord> getInventory() {
		List<PakRecord> list = new ArrayList<PakRecord>();
		
		for(GalleryItem g : galleryItems){
			list.add((PakRecord) g.getData());
		}
		// TODO Auto-generated method stub
		return list;
	}
}
