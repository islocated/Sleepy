package com.isnotok.sleep.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.isnotok.sleep.gallery.GalleryViewer;
import com.isnotok.sleep.input.PakFileInput;
import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;
import com.isnotok.sleep.model.ResourceManager;

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
		// TODO Auto-generated method stub
		//getSite().getPage().addSelectionListener(this);
		
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
		
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(64);
		gr.setItemWidth(72);
		gr.setAutoMargin(true);
		//gr.setTitleBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		///gr.setTitleBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
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
				}
			}
		});
		
		gallery.setItemCount(TYPES.length);
		
		
		
		final Text text = new Text(grid, SWT.SEARCH);
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		text.setLayoutData(gridData);
		
		text.setMessage("Filter Keyword");
		//text.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK));
		
		//slider = new Slider(canvas, SWT.HORIZONTAL);
		//slider.setValues(2, 2, 5, 1, 1, 1);
		
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
