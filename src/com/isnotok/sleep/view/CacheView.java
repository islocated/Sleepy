package com.isnotok.sleep.view;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.CacheManager;
import com.isnotok.sleep.model.PakManager;
import com.isnotok.sleep.model.Resource;
import com.isnotok.sleep.model.TileResource;

public class CacheView extends ViewPart implements ISelectionListener{
	public final static String ID = "com.isnotok.sleep.view.CacheView";
	private static final String[] TYPES = {"tile", "sprite", "room", "music", "timbre", "scale"};
	
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
		gridLayout.numColumns = 1;
		grid.setLayout(gridLayout);
		
		GridData gridData;

		//Add Gallery to grid
		gallery = new GalleryViewer(grid, SWT.V_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
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
					 String [] files = new String[galleryItems.length];
					 for(int i = 0; i < files.length; i++){
						 files[i] = ((File) galleryItems[i].getData()).getAbsolutePath();
					 }
					 event.data = files;
				}
			}

			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				if(gallery.getSelection() == null){
					event.doit = false;
				}
			}
        });
		
		
		//Set the filter for the keywords
		setFilterField(grid);
		
		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if(index >= 0 && index < TYPES.length){
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
				File file = (File) element;
				File [] files = file.listFiles();
				
				if(files == null){
					return;
				}
				
				System.out.println("detect view:" + file.getName());
				
				for(File f : file.listFiles()){
					System.out.println("   listing: " + f.getName());
					for(String s : TYPES){
						if(f.getName().equals(s)){
							//CacheManager.getInstance().setCacheDirectory(file);
							pakManager.initDirectory(file);
							//if(file.getName().equals("resourceCache")){
							//resourceCache = file;
							gallery.clearAll();
							gallery.setItemCount(TYPES.length);
							
							System.out.println("   view should be refreshed: " + file.getName());
							return;
						}
					}
				}
			}
		}
	}

}
