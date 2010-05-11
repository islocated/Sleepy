package com.isnotok.sleep.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.galleryviewer.GalleryTreeViewer;
import org.eclipse.nebula.widgets.gallery.AbstractGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.AbstractGridGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
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

import com.isnotok.sleep.gallery.CustomItemRenderer;
import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.CacheManager;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.Resource;
import com.isnotok.sleep.model.ResourceTypes;

//Implement ISelectionProvider if we want this view to return the zoom
public class MagnifyView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.MagnifyView";
	
	private HashMap<String, Image> map = new HashMap<String, Image>();
	
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
		
		AbstractGridGroupRenderer gr = new NoGroupRenderer();//DefaultGalleryGroupRenderer();
		//gr.setMinMargin(2);
		gr.setItemHeight(64);
		gr.setItemWidth(84);
		//gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);
		gallery.setAntialias(SWT.OFF);
		//gallery.setLowQualityOnUserAction(true);
		//gallery.setHigherQualityDelay(500);
		gallery.setInterpolation(SWT.NONE);
		
		DefaultGalleryItemRenderer ir = new CustomItemRenderer();//DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		ir.setDropShadows(true);
		ir.setDropShadowsSize(2);
		gallery.setItemRenderer(ir);
		
		//gallery.setDefaultRenderers();

		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if (index >= 0 && index < ResourceTypes.TYPES.length) {
						// This is group 1
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
				AbstractGridGroupRenderer gr = (AbstractGridGroupRenderer) gallery.getGroupRenderer();
				gr.setItemSize(32 * scale.getSelection()+ 10, 32 * scale.getSelection());
			}
			
		});
		
		scale.setSelection(9);
		gr.setItemSize(32 * scale.getSelection()+ 10, 32 * scale.getSelection());
	}

	@Override
	public void setFocus() {
		gallery.setFocus();
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
				System.out.println("disposing images");
			}
		}
		map.clear();
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
				disposeGalleryItems();
				gallery.setItemCount(ResourceTypes.TYPES.length);
			}
		}
	}

	public void zoom(boolean in) {
		// TODO Auto-generated method stub
		AbstractGridGroupRenderer gr = (AbstractGridGroupRenderer) gallery.getGroupRenderer();
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
		gallery.setItemCount(ResourceTypes.TYPES.length);
	}
}
