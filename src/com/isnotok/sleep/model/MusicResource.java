package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class MusicResource extends Resource{
	public static final int SIZE = 16;
	public static final int BYTES_PER_PIXEL = 1;
	public static final int BYTES_TOTAL = SIZE * SIZE * BYTES_PER_PIXEL;
	
	public MusicResource(File file){
		super(file);
		load();
		
		nameOffset = BYTES_TOTAL;
	}
	
	@Override
	public String getType() {
		return "music";
	};

	@Override
	public ImageData getImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0x1, 0x1, 0x1);
		
		byte [] imgData = new byte[BYTES_TOTAL];
		int rev = BYTES_TOTAL - 1;
		for(int y = 0; y < SIZE; y++){
			for(int x = 0; x < SIZE; x++){
				int index = y * SIZE + (SIZE-x-1);
				imgData[index] = data[rev--];
			}
		}
		
		return new ImageData(SIZE, SIZE, 8 * BYTES_PER_PIXEL, palette, 1, imgData);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		MusicResource resourceFile = new MusicResource(file);
		//resourceFile.load();
	}
}
