package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class RoomResource extends Resource{
	public static final int SIZE = 16;
	public static final int GRID = 13;
	public static final int BYTES_PER_PIXEL = 4;
	public static final int BYTES_TOTAL = GRID * GRID * UniqueId.MAX_DIGITS;
	
	public RoomResource(File file){
		super(file);
	}
	
	@Override
	public String getResourceName(){
		if(data == null){
			return null;
		}
		else{
			StringBuffer sb = new StringBuffer();
			for(int i = BYTES_TOTAL; i < data.length; i++){
				sb.append((char)data[i]);
			}
			return sb.toString();
		}
	}

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
				
				//System.out.println(file.getName());
				//System.out.println("tile: " + new UniqueId(b).toHexString());
				
				File parent = new File(file.getParentFile().getParentFile(), "tile");
				File tile = new File(parent, new UniqueId(b).toHexString());
				
				
				tiles[x][y] = CacheManager.getInstance().getResource(tile);
				//tiles[x][y].load();
			}
		}
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
		
		byte [] bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		
		for(int y = 0; y < GRID; y++){
			for(int x = 0; x < GRID; x++){
				//Copy tiles
				//Copy tile[x][y] to bytes location
				Resource tile = tiles[x][y];
				
				//Index is for all the pixels in here
				int index = 0;
				for(int j = 0; j < SIZE; j++){
					for(int i = 0; i < SIZE; i++){
						for(int k = 0; k < BYTES_PER_PIXEL; k++){
							//If X = 5, Y = 1
							
							int bindex = k + i*4 + x*(16*4) + j*16*13*4 + y*16*13*13*4;//(i+x)*16 + (y+j)*4 + k;
							//System.out.println(tile.getData()[index]);
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
		RoomResource resourceFile = new RoomResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
