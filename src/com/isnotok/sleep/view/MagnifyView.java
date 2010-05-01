package com.isnotok.sleep.view;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
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
import org.eclipse.swt.widgets.Slider;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.editor.PakEditor;
import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.model.ResourceManager;

//Implement ISelectionProvider if we want this view to return the zoom
public class MagnifyView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.MagnifyView";
	private static final String[] TYPES = {"tile", "sprite", "room", "music", "object", "scale"};;
	
	private PakFile pakfile;
	private Gallery gallery;
	private Slider slider;
	
	private GalleryItem[] gi;

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

		gallery = new Gallery(canvas, SWT.V_SCROLL | SWT.VIRTUAL);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridData.horizontalSpan = 2;
		gallery.setLayoutData(gridData);
		
		final DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(128);
		gr.setItemWidth(128);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);
		gallery.setAntialias(SWT.OFF);

		
		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		ir.setDropShadows(true);
		ir.setDropShadowsSize(2);
		gallery.setItemRenderer(ir);

		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				if(gi == null){
					return;
				}
				
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
						item.setItemCount(gi.length);
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
					
					item.setText(gi[index].getText());
					item.setImage(gi[index].getImage());
					
					//PakRecord pakrecord = (PakRecord) pakfile.getResourceType(parentItem.getText())[index];
					
					//item.setText(pakrecord.getWordString());

					//Image img = new Image(parent.getDisplay(), pakrecord
					//		.getImageData());
					//item.setImage(img);
				}
			}
		});
		
		slider = new Slider(canvas, SWT.HORIZONTAL);
		slider.setValues(2, 2, 5, 1, 1, 1);
		
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		slider.setLayoutData(gridData);
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
			if(element == null){
				return;
			}
			
			System.out.println(element.getClass());
			/*
			if(element instanceof File){
				File file = (File) element;
				if(file.getName().endsWith(".pak")){
					pakfile = ResourceManager.getInstance().getPakFile(file);
					gallery.clearAll();
					gallery.setItemCount(3);
				}
			}*/
			
			
			if(element instanceof GalleryItem){
				gi = (GalleryItem[]) sel.toArray();
				//gi.getText()
				//if(file.getName().endsWith(".pak")){
				//	//pakfile = ResourceManager.getInstance().getPakFile(file);
					
				gallery.clearAll();
				//gallery.setI
				gallery.setItemCount(1);
			}
		}
	}
}
