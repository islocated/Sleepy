package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import com.isnotok.sleep.util.BytesUtil;

public class RoomResource extends Resource{
	public static final int SIZE = 16;
	public static final int GRID = 13;
	public static final int BYTES_PER_PIXEL = 4;
	public static final int BYTES_FOR_ROOM = GRID * GRID * UniqueId.MAX_DIGITS;
	public static final int BYTES_FOR_WALL = GRID * GRID;
	public static final int BYTES_TOTAL = BYTES_FOR_ROOM + BYTES_FOR_WALL;

	Resource [][] tiles;
	
	public RoomResource(File file){
		super(file);
		load();
		
		nameOffset = BYTES_TOTAL;
	}
	
	@Override
	public String getType() {
		return "room";
	};

	@Override
	protected void load() {
		super.load();
		
		tiles = new Resource[GRID][GRID];
		
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
	}

	@Override
	protected ImageData calculateImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
		
		byte [] bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		
		for(int y = 0; y < GRID; y++){
			for(int x = 0; x < GRID; x++){
				Resource tile = tiles[x][y];
				int yoffset = y * GRID * SIZE * BYTES_PER_PIXEL * SIZE;
				int xoffset = x * SIZE * BYTES_PER_PIXEL;
				int offset = yoffset + xoffset;
				
				//System.out.println("yoffset: " + yoffset);
				//System.out.println("yoffset: " + yoffset);
				
				
				BytesUtil.copyBlock(
					tile.getData(), 
					TileResource.SIZE * TileResource.BYTES_PER_PIXEL, 
					TileResource.SIZE, 
					bytes, 
					SIZE * GRID * BYTES_PER_PIXEL, 
					SIZE * GRID, 
					offset);
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
