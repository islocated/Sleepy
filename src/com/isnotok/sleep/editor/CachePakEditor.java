package com.isnotok.sleep.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.nebula.widgets.gallery.AbstractGridGroupRenderer;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.CacheManager;
import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.model.Resource;
import com.isnotok.sleep.model.ResourceTypes;

public class CachePakEditor extends EditorPart{
	public static final String ID = "com.isnotok.sleep.editor.CachePakEditor";
	
	private HashMap<String, Image> map = new HashMap<String, Image>();
	
	
	private GalleryViewer gallery;
	//private CacheManager cache = new CacheManager();
	private PakManager pakManager = new PakManager();

	public CachePakEditor() {
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
		
		File file = (File) input.getAdapter(File.class);
		setPartName(file.getName());
		
		pakManager.initDirectory(file);
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
		//getSite().getPage().addSelectionListener(this);

		//Drag drop
		addDragNDrop();
		
		
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
						item.setItemCount(pakManager.getFileCount(ResourceTypes.TYPES[index]));//cache.getFileCount(TYPES[index]));
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
		
		gallery.setItemCount(ResourceTypes.TYPES.length);
	}

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
				System.out.println("filtering: " + text.getText());
				gallery.clearAll();
				disposeGalleryItems();
				gallery.setItemCount(ResourceTypes.TYPES.length);
			}
			
		});
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
}
