package com.isnotok.sleep.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.galleryviewer.GalleryTreeViewer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.CacheManager;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.Resource;

//Implement ISelectionProvider if we want this view to return the zoom
public class MagnifyView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.MagnifyView";
	private static final String[] TYPES = {"tile", "sprite", "room", "music", "timbre", "scale"};
	
	private GalleryViewer gallery;
	
	//private GalleryItem[] gi;
	private PakManager pakManager = new PakManager();

	public MagnifyView() {
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
		getSite().getPage().addSelectionListener(this);
		
		//GalleryTreeViewer

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
		
		final Scale scale = new Scale(canvas, SWT.NONE);
		scale.setMinimum(2);
		scale.setMaximum(9);
		scale.setIncrement(1);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		scale.setLayoutData(gridData);
		scale.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				DefaultGalleryGroupRenderer gr = (DefaultGalleryGroupRenderer) gallery.getGroupRenderer();
				gr.setItemSize(32 * scale.getSelection()+ 10, 32 * scale.getSelection());
			}
			
		});
		
		//Button button = new Button(canvas, SWT.NONE);
		//
		//MenuManager menuManager = new MenuManager ();
		//Menu menu = menuManager.createContextMenu (gallery);
		//gallery.setMenu(menu);
		//getSite ().registerContextMenu (menuManager, gallery.getProvider());
		
		//ISharedImages.IMG_ETOOL_SAVEALL_EDIT
		
		//slider.setLayoutData(gridData);
		/*
		slider.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				int zoom = (int) (16 * Math.pow(2, slider.getSelection()));
				System.out.println("zoom: " + zoom);
				gr.setItemSize(zoom, zoom);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
	}

	@Override
	public void setFocus() {
		gallery.setFocus();
	}
	
	//ISelectionListner interface
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		if(selection instanceof IStructuredSelection){
			IStructuredSelection sel = (IStructuredSelection) selection;
			Object element = sel.getFirstElement();
			if(element == null){
				return;
			}
			
			System.out.println(element.getClass());
			
			if(element instanceof GalleryItem){
				pakManager.clear();
				GalleryItem [] galleryItems = (GalleryItem []) sel.toArray();
				 //List<String> files = new ArrayList<String>(galleryItems.length);
				 for(int i = 0; i < galleryItems.length; i++){
					 File file = (File) galleryItems[i].getData();
					 if(file == null)
						 continue;
					 File parent = file.getParentFile();
					 pakManager.addFile(parent.getName(), file);
				 }
				
				pakManager.setFilter("");//gi = (GalleryItem[]) sel.toArray();
				gallery.clearAll();
				gallery.setItemCount(TYPES.length);
			}
		}
	}

	public void zoom(boolean in) {
		// TODO Auto-generated method stub
		DefaultGalleryGroupRenderer gr = (DefaultGalleryGroupRenderer) gallery.getGroupRenderer();
		int height = gr.getItemHeight();
		int width = gr.getItemWidth();
		
		if(in){
			height *= 2;
			width *= 2;
		}else{
			height /= 2;
			width /= 2;
		}
		
		if(height < 64 || width < 64){
			return;
		}
		
		if(height > 512 || width > 512){
			return;
		}
		
		gr.setItemSize(width, height);
		gallery.setItemCount(TYPES.length);
	}
}
