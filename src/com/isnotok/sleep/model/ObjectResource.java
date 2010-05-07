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
				spriteResource.setTrans((byte)255);
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
		byte [] alpha = new byte[SIZE * GRID * SIZE * GRID];
		
		int imageCenter = (GRID * SIZE)/2;
		int spriteCenter = SIZE/2;
		//int numLayers = spriteLayer.size();
		//List<SpriteResource> spriteLayers = new ArrayList<SpriteResource>(255);
		
		for(SpriteResource sr : sprites){
			//int x = sr.getX();
			//int y = sr.getY();
			
			float layerTrans = (sr.getTrans() & 0xFF) / 255.0f;
			byte layerGlow = sr.getGlow();
			
			byte [] srdata = sr.getData();
			
			
			if(srdata == null){
				continue;
			}
			
			System.out.println(sr.getResourceName());
			
			for(int y = 0; y < SIZE; y++){
				for(int x = 0; x < SIZE; x++){
					
					System.out.println("offsety:" + (int)(sr.getOffset()[1]));
					System.out.println("offsetx:" + (int)(sr.getOffset()[0]));
					int fullY = y - spriteCenter - (int)(sr.getOffset()[1]);
					fullY += imageCenter;
					
					int fullX = x - spriteCenter + (int)(sr.getOffset()[0]);
					fullX += imageCenter;
					
					
					int fullIndex = fullY * SIZE * GRID + fullX;
					//If the pixel is within the object frame
					if(fullY < SIZE * GRID && fullY >= 0 && fullX < SIZE * GRID && fullX >= 0){
						int index = y * SIZE + x;
						//blend colors
						int pixelindex = index * 4;
							
						float colorTrans = (float) (srdata[index + SpriteResource.BYTES_USED] & 0xFF) == 1 ? 0 : layerTrans;
						
							
						if(colorTrans > 0){
							
							int fullPixelIndex = fullIndex * 4;
							float [] color = new float[4];
							
							if((int) (alpha[index] & 0xFF) > 0){
								if((int) (layerGlow & 0xFF) > 0){
									color[0] = (float) bytes[fullPixelIndex] * colorTrans + srdata[pixelindex] * (1-colorTrans);
									color[1] = (float) bytes[fullPixelIndex+1] * colorTrans + srdata[pixelindex+1] * (1-colorTrans);
									color[2] = (float) bytes[fullPixelIndex+2] * colorTrans + srdata[pixelindex+2] * (1-colorTrans);
									color[3] = (float) bytes[fullPixelIndex+3] * colorTrans + srdata[pixelindex+3] * (1-colorTrans);
								}
								else{
									color[0] = bytes[fullPixelIndex] * colorTrans;
									color[1] = bytes[fullPixelIndex+1] * colorTrans;
									color[2] = bytes[fullPixelIndex+2] * colorTrans;
									color[3] = bytes[fullPixelIndex+3] * colorTrans;
									
									color[0] = color[0] > 1 ? 1 : color[0];
									color[1] = color[1] > 1 ? 1 : color[1];
									color[2] = color[2] > 1 ? 1 : color[2];
									color[3] = color[3] > 1 ? 1 : color[3];
								}
							}
							else{
								System.out.println(pixelindex + ": " +srdata[pixelindex]);
								System.out.println(pixelindex+1 + ": " +srdata[pixelindex+1]);
								System.out.println(pixelindex+2 + ": " +srdata[pixelindex+2]);
								System.out.println(pixelindex+3 + ": " +srdata[pixelindex+3]);
								
								color[0] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[1] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[2] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[3] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
							}
							
							bytes[fullPixelIndex++] = (byte) (color[0] * 255);
							bytes[fullPixelIndex++] = (byte) (color[1] * 255);
							bytes[fullPixelIndex++] = (byte) (color[2] * 255);
							bytes[fullPixelIndex++] = (byte) (color[3] * 255);
							alpha[index] = (byte) (color[3] * 255);
						}
					}
				}
			}
		}
		
		return new ImageData(13*16, 13*16, 32, palette, 1, bytes);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		ObjectResource resourceFile = new ObjectResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
