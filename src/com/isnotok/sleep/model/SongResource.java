package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class SongResource extends Resource{
	public static final int SIZE = 16;
	public static final int GRID = 13;
	public static final int BYTES_PER_PIXEL = 4;
	public static final int BYTES_FOR_ROOM = GRID * GRID * UniqueId.MAX_DIGITS;
	public static final int BYTES_FOR_WALL = GRID * GRID;
	public static final int BYTES_TOTAL = BYTES_FOR_ROOM + BYTES_FOR_WALL;

	public SongResource(File file){
		super(file);
		load();
		
		nameOffset = BYTES_TOTAL;
	}
	
	@Override
	public String getType() {
		return "song";
	};

	@Override
	public ImageData getImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		Resource [][] tiles = new Resource[GRID][GRID];
		
		for(int y = 0; y < GRID; y++){
			for(int x = 0; x < GRID; x++){
				int index = (y * GRID + x) * UniqueId.MAX_DIGITS;
				byte [] b = new byte[UniqueId.MAX_DIGITS];
				System.arraycopy(data, index, b, 0, UniqueId.MAX_DIGITS);
				
				File parent = new File(file.getParentFile().getParentFile(), "tile");
				File tile = new File(parent, new UniqueId(b).toHexString());
				
				tiles[x][y] = CacheManager.getInstance().getResource(tile);
			}
		}
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
		
		byte [] bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		
		for(int y = 0; y < GRID; y++){
			for(int x = 0; x < GRID; x++){
				//Copy tile[x][y] to bytes location
				Resource tile = tiles[x][y];
				
				//Index is for all the pixels in here
				int index = 0;
				for(int j = 0; j < SIZE; j++){
					for(int i = 0; i < SIZE; i++){
						for(int k = 0; k < BYTES_PER_PIXEL; k++){
							int bindex = k + i*4 + x*(16*4) + j*16*13*4 + y*16*13*13*4;
							bytes[bindex] = tile.getData()[index++];
						}
					}
				}
			}
		}
		
		return new ImageData(13*16, 13*16, 32, palette, 1, bytes);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		SongResource resourceFile = new SongResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
