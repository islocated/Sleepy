package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class SpriteResource extends TileResource{
	public static final int SIZE = 16;
	public static final int BYTES_PER_PIXEL = 4;
	public static final int BYTES_USED = SIZE * SIZE * BYTES_PER_PIXEL;
	public static final int ALPHA_USED = SIZE * SIZE;
	public static final int BYTES_TOTAL = BYTES_USED + ALPHA_USED;
	
	public SpriteResource(File file){
		super(file);
		load();
		
		nameOffset = BYTES_TOTAL;
	}
	
	@Override
	public ImageData getImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
	    
		ImageData imgData = new ImageData(SIZE, SIZE, 8 * BYTES_PER_PIXEL, palette, 1, data);
		
		byte [] alpha = new byte[SIZE * SIZE];
		
		for(int i = 0; i < ALPHA_USED; i++){
			//Is pixel transparent?
			alpha[i] = (byte) (data[BYTES_USED + i] == 1 ? 0x0 : 0xff);
		}
		
		imgData.alphaData = alpha;
		
		return imgData;
	}
	
	@Override
	public String getType() {
		return "sprite";
	};
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		SpriteResource resourceFile = new SpriteResource(file);
		//resourceFile.load();
	}
}
