package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class TimbreResource extends Resource{
	public static final int SIZE = 16;
	public static final int BYTES_PER_PIXEL = 1;
	public static final int BYTES_TOTAL = SIZE + 4;
	
	public TimbreResource(File file){
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
		return "timbre";
	};

	@Override
	public ImageData getImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0x1, 0x1, 0x1);
		
		byte [] imgData = new byte[SIZE*SIZE];
		for(int i = 0; i < SIZE; i++){
			int y = SIZE - i - 1;	//Order this backwards
			
			for(int j = 0; j < data[i]; j++){
				int index = y*SIZE + j;
				imgData[index] = 1;
			}
		}
		
		return new ImageData(SIZE, SIZE, 8 * BYTES_PER_PIXEL, palette, 1, imgData);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		TimbreResource resourceFile = new TimbreResource(file);
		//file.delete();
		//resourceFile.load();
	}
}
