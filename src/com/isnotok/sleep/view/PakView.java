package com.isnotok.sleep.view;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;

public class PakView extends ViewPart {
	public final static String ID = "com.isnotok.sleep.view.PakView";
	
	public PakView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(final Composite parent) {
		// TODO Auto-generated method stub
		
		 final Gallery gallery = new Gallery(parent, SWT.V_SCROLL | SWT.VIRTUAL);

		// Open Directory
		final File f = new File("C:\\Images");
		final File[] contents = f.listFiles();
		
		File file = new File("/Users/Mint/Documents/RCP/com.isnotok.sleep", "input/4.pak");
		final PakFile pakFile = new PakFile(file);
		
		try {
			pakFile.load();
			//PakRecord record = pakFile.getResource("tile", "table nm").get(0);
			//imageData = record.getImageData();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// SetData is called when Gallery creates an item.
		gallery.addListener(SWT.SetData, new Listener() {
		   public void handleEvent(Event event) {
		      GalleryItem item = (GalleryItem) event.item;
		      if (item.getParentItem() == null) {
		         // It's a group
		         int index = gallery.indexOf(item);
		         if( index == 0 ) {
		            // This is group 1
		            item.setText("tiles");
		            item.setItemCount(pakFile.getResourceType("tile").length);
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

		       // Load corresponding items
		       Object [] objs = pakFile.getResourceType("tile");
		       
		       PakRecord pakrecord = (PakRecord) pakFile.getResourceType("tile")[index];
		       Image img = new Image(parent.getDisplay(), pakrecord.getImageData());
		       item.setImage(img);
		     }
		   }
		});

		// Create one group (will call SetData)
		gallery.setItemCount( 1 ); 

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
