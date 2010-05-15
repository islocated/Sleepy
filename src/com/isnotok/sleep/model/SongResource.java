package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import com.isnotok.sleep.util.BytesUtil;

public class SongResource extends Resource{
	public static final int SIZE = 16;
	public static final int PHRASES = 9;
	public static final int ROWS = 8;
	public static final int BYTES_PER_PIXEL = 1;
	
	//public static final int BYTES_PER_PIXEL = 4;
	//public static final int BYTES_FOR_ROOM = GRID * GRID * UniqueId.MAX_DIGITS;
	//public static final int BYTES_FOR_WALL = GRID * GRID;
	//public static final int BYTES_TOTAL = BYTES_FOR_ROOM + BYTES_FOR_WALL;

	MusicResource [][] notes = new MusicResource[PHRASES][ROWS];
	
	public SongResource(File file){
		super(file);
		load();
	}
	
	@Override
	protected void load() {
		// TODO Auto-generated method stub
		super.load();
		
		
		int offset = 0;
		for(int y = 0; y < ROWS; y++){
			for(int x = 0; x < PHRASES; x++){
				byte [] b = BytesUtil.readBytes(data, offset, UniqueId.MAX_DIGITS);
				offset += UniqueId.MAX_DIGITS;
				
				File parent = new File(file.getParentFile().getParentFile(), "music");
				File music = new File(parent, new UniqueId(b).toHexString());
				
				notes[x][y] = (MusicResource) CacheManager.getInstance().getResource(music);
			}
		}
		
		//Read in row length
		for(int y = 0; y < ROWS; y++){
			offset += 1;
		}
		
		//Read in timbre
		for(int y = 0; y < ROWS; y++){
			byte [] b = BytesUtil.readBytes(data, offset, UniqueId.MAX_DIGITS);
			offset += UniqueId.MAX_DIGITS;
		}
		
		//Read in loudness
		for(int y = 0; y < ROWS; y++){
			offset += 1;
		}
		
		//Read in stereo
		for(int y = 0; y < ROWS; y++){
			offset += 1;
		}
		
		//Read in scale
		byte [] b = BytesUtil.readBytes(data, offset, UniqueId.MAX_DIGITS);
		offset += UniqueId.MAX_DIGITS;
		
		//Read in speed
		offset += 1;
		
		nameOffset = offset;
	}
	
	@Override
	public String getType() {
		return "song";
	};

	@Override
	protected ImageData calculateImageData(){
		if(data == null)
			return null;
		
		PaletteData palette = new PaletteData(0x1, 0x1, 0x1);
		
		byte [] bytes = new byte[SIZE * SIZE * PHRASES * ROWS * BYTES_PER_PIXEL];
		
		for(int y = 0; y < ROWS; y++){
			for(int x = 0; x < PHRASES; x++){
				Resource resource = notes[x][y];
				
				int offset = y * PHRASES * SIZE * SIZE + x * SIZE;
				System.out.println("song: " + offset);
				
				//Use image data because that one has already been reversed etc...
				BytesUtil.copyBlock(resource.getImageData().data, MusicResource.SIZE, MusicResource.SIZE, bytes, SIZE * PHRASES * BYTES_PER_PIXEL, SIZE * ROWS, offset);
			}
		}
		
		return new ImageData(SIZE * PHRASES, SIZE * ROWS, 8 * BYTES_PER_PIXEL, palette, 1, bytes);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		SongResource resourceFile = new SongResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
