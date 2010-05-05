package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class ScaleResource extends Resource{
	public static final int HALFTONE = 12;
	public static final int GRID = 16;
	public static final int BYTES_PER_PIXEL = 1;
	public static final int BYTES_TOTAL = HALFTONE;
	
	public ScaleResource(File file){
		super(file);
		load();
	}
	
	@Override
	public String getResourceName(){
		if(data == null){
			return null;
		}
		else{
			StringBuffer sb = new StringBuffer();
			for(int i = BYTES_TOTAL; i < data.length-1; i++){
				sb.append((char)data[i]);
			}
			return sb.toString();
		}
	}
	
	@Override
	public String getType() {
		return "scale";
	};


	@Override
	public ImageData getImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF, 0xFF, 0xFF);
		
		byte [] imgData = new byte[16*16];
		
		for(int i = 0; i < HALFTONE; i++){
			//Find the diagonal, go from bottom left to top right
			int index = ((HALFTONE - i - 1) + 2) * GRID + i + 2; //add 2 to shift picture over and down by 2
			imgData[index] = data[i] == 1 ? (byte) 0xFF : 0;
		}
		
		return new ImageData(GRID, GRID, 8 * BYTES_PER_PIXEL, palette, 1, imgData);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		ScaleResource resourceFile = new ScaleResource(file);
		//resourceFile.load();
	}
}
