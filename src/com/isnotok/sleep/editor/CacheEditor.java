package com.isnotok.sleep.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.nebula.jface.galleryviewer.GalleryTreeViewer;
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
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.isnotok.sleep.filter.ResourceFilter;
import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.CacheDirectory;
import com.isnotok.sleep.model.CacheManager;
import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.model.Resource;
import com.isnotok.sleep.provider.CacheContentProvider;
import com.isnotok.sleep.provider.CacheLabelProvider;
import com.isnotok.sleep.sorter.ResourceSorter;

public class CacheEditor extends EditorPart{
	public static final String ID = "com.isnotok.sleep.editor.CacheEditor";
	
	private static final String[] TYPES = {"tile", "sprite", "scale", "music", "room"};
	
	private GalleryTreeViewer gallery;
	//private CacheManager cache = new CacheManager();
	private PakManager pakManager = new PakManager();
	
	private CacheDirectory cacheDirectory;

	public CacheEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		
		pakManager.initDirectory((File) input.getAdapter(File.class));
		
		cacheDirectory = new CacheDirectory((File) input.getAdapter(File.class));
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(final Composite parent) {
		Composite grid = new Composite(parent, SWT.NONE);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		grid.setLayout(gridLayout);
		
		GridData gridData;

		//Add Gallery to grid
		gallery = new GalleryTreeViewer(grid, SWT.V_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gallery.getControl().setLayoutData(gridData);
		
		gallery.setContentProvider(new CacheContentProvider());
		gallery.setLabelProvider(new CacheLabelProvider());
		gallery.setInput(cacheDirectory);
		//gallery.addFilter(new ResourceFilter());
		//gallery.setSorter(new ResourceSorter());
		
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(64);
		gr.setItemWidth(84);
		gr.setAutoMargin(true);
		((Gallery)gallery.getControl()).setGroupRenderer(gr);
		((Gallery)gallery.getControl()).setAntialias(SWT.OFF);
		
		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		ir.setShowLabels(true);
		ir.setDropShadows(true);
		ir.setDropShadowsSize(2);
		((Gallery)gallery.getControl()).setItemRenderer(ir);
		
		
		getSite().setSelectionProvider(gallery);
		
		//gallery.addDragSupport(operations, transferTypes, listener)
		
		//gallery.setAsSelectionProvider(getSite());
		
		//gallery.setDefaultRenderers();

		//Sets up the ability for this view to get selection events from all views
		//getSite().getPage().addSelectionListener(this);

		//Drag drop
		//addDragNDrop();
		
		//Set the filter for the keywords
		//setFilterField(grid);
		
		// SetData is called when Gallery creates an item.
		/*
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if(index >= 0 && index < TYPES.length){
						item.setText(TYPES[index]);
						item.setItemCount(pakManager.getFileCount(TYPES[index]));//cache.getFileCount(TYPES[index]));
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
					
					//File [] files = pakManager.getFilesByType(parentItem.getText());//cache.getFilesByType(parentItem.getText());
					
					File resourceFile = pakManager.getFilesByType(parentItem.getText(), index);//files[index];
					
					Resource resource = CacheManager.getInstance().getResource(resourceFile);
					
					item.setText(resource.getResourceName());

					Image img = new Image(parent.getDisplay(), resource.getImageData());
					item.setImage(img);
					item.setData(resourceFile);
				}
			}
		});
		
		gallery.setItemCount(TYPES.length);
		*/
	}

	/*
	private void addDragNDrop() {
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { FileTransfer.getInstance() };
        DragSource source = new DragSource(gallery, ops);
        source.setTransfer(transfers);
        source.addDragListener(new DragSourceListener(){

			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				
			}

			public void dragSetData(DragSourceEvent event) {
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
	}
	

	private void setFilterField(Composite grid) {
		final Text text = new Text(grid, SWT.SEARCH);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		text.setLayoutData(gridData);
		
		text.setMessage("Filter by keyword");
		text.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				pakManager.setFilter(text.getText());
				System.out.println("filtering: " + text.getText());
				gallery.clearAll();
				gallery.setItemCount(TYPES.length);
			}
			
		});
	}
	*/

	@Override
	public void setFocus() {
		gallery.getControl().setFocus();
	}
}
