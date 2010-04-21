package com.isnotok.sleep.view;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.isnotok.sleep.model.PakFile;
import com.isnotok.sleep.model.PakRecord;

public class ResourceView extends ViewPart {

	public final static String ID = "com.isnotok.sleep.view.ResourceView";
	
	private Canvas canvas;
	private Image image;
	private File file;
	private PakFile pakfile;

	public ResourceView() {
		
		/*
		// TODO Auto-generated constructor stub
		//TODO: figure this out
		File file = new File("/Users/Mint/Documents/RCP/com.isnotok.sleep", "input/4.pak");
		pakFile = new PakFile(file);
		
		try {
			pakFile.load();
			PakRecord record = pakFile.getResource("tile", "table nm").get(0);
			imageData = record.getImageData();
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public void setFile(File file){
		this.file = file;
		pakfile = new PakFile(file);
		try {
			pakfile.load();
			canvas.layout(true);
			canvas.redraw();
			canvas.update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		canvas = new Canvas(parent, SWT.NONE);//SWT.NO_REDRAW_RESIZE);
		
		canvas.addPaintListener(new	PaintListener(){
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				paint(e.gc);
			}});
	}

	protected void paint(GC gc) {
		
		
		// TODO Auto-generated method stub
		if(image != null)
			gc.drawImage(image, 0, 0, 16, 16, 0, 0, 256, 256);
			//gc.drawImage(image, 0, 0);
		else{
			if(pakfile != null)
				image = new Image(getSite().getShell().getDisplay(), pakfile.getResource("tile", "table nm").get(0).getImageData());
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		canvas.setFocus();
	}
}
