package com.isnotok.sleep.view;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

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
	private PakFile pakFile;
	private ImageData imageData;

	public ResourceView() {
		// TODO Auto-generated constructor stub
		//TODO: figure this out
		File file = new File("/Users/Mint/Documents/RCP/com.isnotok.sleep", "input/4.pak");
		pakFile = new PakFile(file);
		
		try {
			pakFile.load();
			PakRecord record = pakFile.getTile("table nm");
			
			byte [] bytes = record.getData();
			
			//Format is RGBA
			byte [] pixelBytes = new byte[16*16*3];
			byte [] alphaBytes = new byte[16*16];
			
			int i = 0;
			int j = 0;
			int k = 0;
			while(i < 16*16*4){
				pixelBytes[j++] = bytes[i++];
				pixelBytes[j++] = bytes[i++];
				pixelBytes[j++] = bytes[i++];
				alphaBytes[k++] = bytes[i++];
			}
			
			//System.arraycopy(bytes, 0, pixelBytes, 0, 16*16*4);
			
			/*
			pixelBytes[0] = (byte)0;
			pixelBytes[1] = (byte)0xff;
			pixelBytes[2] = (byte)0;
			pixelBytes[3] = (byte)0;
			
			pixelBytes[4] = (byte)0;
			pixelBytes[5] = (byte)0xff;
			pixelBytes[6] = (byte)0;
			pixelBytes[7] = (byte)0;
			
			pixelBytes[8] = (byte)0;
			pixelBytes[9] = (byte)0;
			pixelBytes[10] = (byte)0xff;
			pixelBytes[11] = (byte)0;
			*/
			
			//Format is RGBA
			PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		    imageData = new ImageData(16,16,24,palette, 1, pixelBytes);
		    //imageData.alpha = 0xFF;
		    imageData.alphaData = alphaBytes;
			//ImageData data = new ImageData(16, 16, 3, palette, 0, bytes);

			
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
		canvas = new Canvas(parent, SWT.NO_REDRAW_RESIZE);
		
		canvas.addPaintListener(new	PaintListener(){
			public void paintControl(PaintEvent e) {
				// TODO Auto-generated method stub
				paint(e.gc);
			}});
		
		image = new Image(parent.getDisplay(), imageData);
		
	}

	protected void paint(GC gc) {
		// TODO Auto-generated method stub
		if(image != null)
			gc.drawImage(image, 0, 0, 16, 16, 0, 0, 256, 256);
			//gc.drawImage(image, 0, 0);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		canvas.setFocus();
	}
}
