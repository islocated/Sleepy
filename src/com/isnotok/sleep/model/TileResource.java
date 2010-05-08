package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class TileResource extends Resource{
	public static final int SIZE = 16;
	public static final int BYTES_PER_PIXEL = 4;
	public static final int BYTES_TOTAL = SIZE * SIZE * BYTES_PER_PIXEL;
	
	public TileResource(File file){
		super(file);
		load();
		
		nameOffset = BYTES_TOTAL;
	}
	
	@Override
	public String getType() {
		return "tile";
	};

	@Override
	public ImageData getImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
	    return new ImageData(SIZE, SIZE, 8 * BYTES_PER_PIXEL, palette, 1, data);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		TileResource resourceFile = new TileResource(file);
		//resourceFile.load();
	}
}
