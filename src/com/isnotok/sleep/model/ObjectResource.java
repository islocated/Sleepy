package com.isnotok.sleep.model;

import java.io.File;
import java.rmi.server.LoaderHandler;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import com.isnotok.sleep.util.BytesUtil;

public class ObjectResource extends Resource{
	public static final int SIZE = 16;//P
	public static final int GRID = 13;
	public static final int BYTES_PER_PIXEL = 4;
	//public static final int BYTES_FOR_ROOM = GRID * GRID * UniqueId.MAX_DIGITS;
	//public static final int BYTES_FOR_WALL = GRID * GRID;
	//public static final int BYTES_TOTAL = BYTES_FOR_ROOM + BYTES_FOR_WALL;

	public static final int MAX_LAYERS = 255;
	
	public static final String MAGIC_CODE = "SiD1977";
	
	//BUGGY, this is called after load... so it gets over written
	private byte objectVersion = 0;
	private byte numLayers = 0;
	List<SpriteResource> sprites = new ArrayList<SpriteResource>(MAX_LAYERS);
	private String resourceName = "";
	
	public ObjectResource(File file){
		super(file);
		load();
	}
	
	@Override
	protected void load() {
		// TODO Auto-generated method stub
		super.load();
		
		int offset = 0;
		//Make sure objects have the magic code
		for(int i = 0; i < MAGIC_CODE.length(); i++){
			if(data[offset++] != MAGIC_CODE.charAt(i)){
				return;
			}
		}
		
		objectVersion = data[offset++];
		numLayers = data[offset++];
		
		for(int i = 0; i < numLayers; i++){
			byte [] b = BytesUtil.readBytes(data, offset, UniqueId.MAX_DIGITS);
			offset += UniqueId.MAX_DIGITS;
			
			File parent = new File(file.getParentFile().getParentFile(), "sprite");
			File sprite = new File(parent, new UniqueId(b).toHexString());
			SpriteResource spriteResource = new SpriteResource(sprite);
			
			sprites.add(spriteResource);
			
			byte [] offs = new byte[2];
			offs[0] = data[offset++];
			offs[1] = data[offset++];
			spriteResource.setOffset(offs);
			
			if(objectVersion >= 2){
				byte trans = data[offset++];
				spriteResource.setTrans(trans);
				
				if(objectVersion >= 3){
					byte glow = data[offset++];
					spriteResource.setGlow(glow);
				}
				else{
					spriteResource.setGlow((byte)0);
				}
			}
			else{
				spriteResource.setTrans(255);
			}
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i = offset; i < data.length - 1; i++){
			sb.append((char)data[i]);
		}
		System.out.println(sb.toString());
		
		resourceName = sb.toString();
		
	}
	
	@Override
	public String getResourceName(){
		if(data == null){
			return null;
		}
		return resourceName;
	}
	
	@Override
	public String getType() {
		return "object";
	};


	@Override
	public ImageData getImageData(){
		if(data == null)	// || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
		
		byte [] bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		
		/*
		int imageCenter = (GRID * SIZE)/2;
		int spriteCenter = SIZE/2;
		//int numLayers = spriteLayer.size();
		List<SpriteResource> spriteLayers = new ArrayList<SpriteResource>(255);
		
		for(SpriteResource sr : spriteLayers){
			//int x = sr.getX();
			//int y = sr.getY();
			
			float layerTrans = 0;//layertrans
			byte layerGlow = 0; //layerglow
			
			
			
			for(int y = 0; y < SIZE; y++){
				for(int x = 0; x < SIZE; x++){
					int fullY = y - spriteCenter - sr.getOffset()[1];
					fullY += imageCenter;
					
					int fullX = x - spriteCenter - sr.getOffset()[0];
					fullX += imageCenter;
					
					if(sr.getTrans() > 0)
					{
						int fullIndex = fullY * SIZE * GRID + fullX;
						//If the pixel is within the object frame
						if(fullY < SIZE * GRID && fullY >= 0 && fullX < SIZE * GRID && fullX >= 0){
							int index = y * SIZE + x;
							//blend colors
							
							int pixelindex = index * 4;
							
							int fullPixelIndex = fullIndex * 4;
							bytes[fullPixelIndex++] = sr.getData()[pixelindex++];
							bytes[fullPixelIndex++] = sr.getData()[pixelindex++];
							bytes[fullPixelIndex++] = sr.getData()[pixelindex++];
							bytes[fullPixelIndex++] = sr.getData()[pixelindex++];
						}
					}
				}
			}
		}
		
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
		
		*/
		
		return new ImageData(13*16, 13*16, 32, palette, 1, bytes);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		ObjectResource resourceFile = new ObjectResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
