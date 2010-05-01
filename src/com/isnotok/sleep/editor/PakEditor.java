package com.isnotok.sleep.editor;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
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

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;

public class PakEditor extends EditorPart{
	public static final String ID = "com.isnotok.sleep.editor.PakEditor";
	
	private static final String[] TYPES = {"tile", "sprite", "scale", "music", "room"};
	
	private GalleryViewer gallery;

	private PakFile pakfile;
	
	public PakEditor() {
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
         
         pakfile = new PakFile((File) input.getAdapter(File.class));
         pakfile.load();
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
		gallery = new GalleryViewer(grid, SWT.V_SCROLL | SWT.VIRTUAL | SWT.MULTI);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gallery.setLayoutData(gridData);
		
		//Set up gallery as the provider
		gallery.setAsSelectionProvider(getSite());
		
		//Set up default renders
		gallery.setDefaultRenderers();

		//Set up context menu
		MenuManager menuManager = new MenuManager ();
		Menu menu = menuManager.createContextMenu (gallery);
		gallery.setMenu(menu);
		getSite ().registerContextMenu (menuManager, gallery.getProvider());
		
		
		/*
		gallery.addDragDetectListener(new DragDetectListener(){

			public void dragDetected(DragDetectEvent e) {
				// TODO Auto-generated method stub
				System.out.println("being dragged");
			}
			
		});
		*/
		
		
		//Drag drop
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
        Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
        DragSource source = new DragSource(gallery, ops);
        source.setTransfer(transfers);
        source.addDragListener(new DragSourceListener(){

			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
				
			}

			public void dragSetData(DragSourceEvent event) {
				// TODO Auto-generated method stub
				event.data = gallery.getSelection();
			}

			public void dragStart(DragSourceEvent event) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        //gallery.add;//.get.addDragSupport(ops, transfers, this);
		//addDragSupport();
		
		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
			public void handleEvent(Event event) {
				if(pakfile == null){
					return;
				}
				
				GalleryItem item = (GalleryItem) event.item;
				if (item.getParentItem() == null) {
					// It's a group
					int index = gallery.indexOf(item);
					if (index >= 0 && index < TYPES.length) {
						item.setText(TYPES[index]);
						item.setItemCount(pakfile.getResourceType(TYPES[index]).length);
						item.setExpanded(true);
					} else {
						// Should never be used
						item.setItemCount(0);
					}
				} else {
					// It's an item
					GalleryItem parentItem = item.getParentItem();

					// Get item index
					int index = parentItem.indexOf(item);

					PakRecord pakrecord = (PakRecord) pakfile
							.getResourceType(parentItem.getText())[index];
					
					item.setText(pakrecord.getWordString());

					Image img = new Image(parent.getDisplay(), pakrecord
							.getImageData());
					item.setImage(img);
					
					item.setData(pakrecord);
				}
			}
		});
		
		gallery.setItemCount(TYPES.length);
		
		setFilterField(grid);
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
				pakfile.setFilter(text.getText());
				gallery.clearAll();
				gallery.setItemCount(TYPES.length);
			}
			
		});
	}

	@Override
	public void setFocus() {
		gallery.setFocus();
	}
}
