package com.isnotok.sleep.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

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
	List<SpriteLayer> sprites = new ArrayList<SpriteLayer>(MAX_LAYERS);
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
		boolean codeFound = true;
		//Make sure objects have the magic code
		for(int i = 0; i < MAGIC_CODE.length(); i++){
			if(data[offset++] != MAGIC_CODE.charAt(i)){
				codeFound = false;
				break;
			}
		}
		
		if(codeFound){
			objectVersion = data[offset++];
		}
		else{
			offset = 0;
		}
		
		numLayers = data[offset++];
		
		for(int i = 0; i < (numLayers & 0xFF); i++){
			SpriteLayer spriteLayer = new SpriteLayer(file);
			sprites.add(spriteLayer);
			
			byte [] b = BytesUtil.readBytes(data, offset, UniqueId.MAX_DIGITS);
			offset += UniqueId.MAX_DIGITS;
			
			spriteLayer.setId(b);
			
			byte [] offs = new byte[2];
			offs[0] = data[offset++];
			offs[1] = data[offset++];
			spriteLayer.setOffset(offs);
			
			if(objectVersion >= 2){
				byte trans = data[offset++];
				spriteLayer.setTrans(trans);
				
				if(objectVersion >= 3){
					byte glow = data[offset++];
					spriteLayer.setGlow(glow);
				}
				else{
					spriteLayer.setGlow((byte)0);
				}
			}
			else{
				spriteLayer.setTrans((byte)255);
			}
		}
		
		nameOffset = offset;
	}
	
	@Override
	public String getType() {
		return "object";
	};
	
	public ImageData getFullImageData(){
		if(data == null)	// || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);

		/*
		if(imgBytes != null && imgAlpha != null){
		//	ImageData imgData = new ImageData(13*16, 13*16, 32, palette, 1, imgBytes);
			//imgData.alphaData = imgAlpha;
		//	return imgData;
		}
		*/
		
		byte [] bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		byte [] alpha = new byte[SIZE * GRID * SIZE * GRID];
		
		int imageCenter = (GRID * SIZE)/2;
		int spriteCenter = SIZE/2;
		
		//System.out.println(resourceName);
		
		int hix = 0;
		int hiy = 0;
		int lowx = SIZE * GRID;
		int lowy = SIZE * GRID;
		
		for(SpriteLayer sr : sprites){
			float layerTrans = (sr.getTrans() & 0xFF) / 255.0f;
			byte layerGlow = sr.getGlow();
			
			
			
			byte [] srdata = sr.getData();
			
			
			if(srdata == null){
				continue;
			}
			
			//System.out.println(sr.getResourceName());
			
			for(int y = 0; y < SIZE; y++){
				for(int x = 0; x < SIZE; x++){
					
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
						
						//If pixel is transparent, skip
						if(colorTrans > 0){
							//If not transparent, draw the pixel
							
							int fullPixelIndex = fullIndex * 4;
							float [] color = new float[4];
							
							//What is the alpha of my full object pixel
							if((alpha[fullIndex] & 0xFF) > 0){
								if((layerGlow & 0xFF) != 1){
									color[0] = (bytes[fullPixelIndex] & 0xFF)/255.0f * (1-colorTrans) + (srdata[pixelindex] & 0xFF)/255.0f* (colorTrans);
									color[1] = (bytes[fullPixelIndex+1] & 0xFF)/255.0f  * (1-colorTrans) + (srdata[pixelindex+1] & 0xFF)/255.0f * (colorTrans);
									color[2] = (bytes[fullPixelIndex+2] & 0xFF)/255.0f  * (1-colorTrans) + (srdata[pixelindex+2] & 0xFF)/255.0f * (colorTrans);
									color[3] = (bytes[fullPixelIndex+3] & 0xFF)/255.0f  * (1-colorTrans) + (srdata[pixelindex+3] & 0xFF)/255.0f* (colorTrans);
								}
								else{
									color[0] = color[0] > 1 ? 1 : color[0];
									color[1] = color[1] > 1 ? 1 : color[1];
									color[2] = color[2] > 1 ? 1 : color[2];
									color[3] = color[3] > 1 ? 1 : color[3];
								}
							}
							else{
								color[0] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[1] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[2] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[3] = colorTrans;//(float) (srdata[pixelindex++] & 0xFF) /255.0f;
							}
							
							bytes[fullPixelIndex++] = (byte) (color[0] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[1] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[2] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[3] * 255.0f);
							alpha[fullIndex] = (byte) (color[3] * 255.0f);
						}
					}
				}
			}
		}
		
		ImageData imgData = new ImageData(13*16, 13*16, 32, palette, 1, bytes);
		imgData.alphaData = alpha;
		
		return imgData;
	}
	
	public void saveImage(){
		//Saving imgData to PNG file
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] {getImageData()};
		imageLoader.save(file.getAbsolutePath() + ".PNG", SWT.IMAGE_PNG);
	}

	@Override
	protected ImageData calculateImageData(){
		if(data == null)	// || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);

		/*
		if(imgBytes != null && imgAlpha != null){
		//	ImageData imgData = new ImageData(13*16, 13*16, 32, palette, 1, imgBytes);
			//imgData.alphaData = imgAlpha;
		//	return imgData;
		}
		*/
		
		byte [] bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		byte [] alpha = new byte[SIZE * GRID * SIZE * GRID];
		
		int imageCenter = (GRID * SIZE)/2;
		int spriteCenter = SIZE/2;
		
		//System.out.println(resourceName);
		
		int hix = 0;
		int hiy = 0;
		int lowx = SIZE * GRID;
		int lowy = SIZE * GRID;
		
		for(SpriteLayer sr : sprites){
			float layerTrans = (sr.getTrans() & 0xFF) / 255.0f;
			byte layerGlow = sr.getGlow();
			
			
			
			byte [] srdata = sr.getData();
			
			
			if(srdata == null){
				continue;
			}
			
			//System.out.println(sr.getResourceName());
			
			for(int y = 0; y < SIZE; y++){
				for(int x = 0; x < SIZE; x++){
					
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
						
						//If pixel is transparent, skip
						if(colorTrans > 0){
							//If not transparent, draw the pixel
							
							int fullPixelIndex = fullIndex * 4;
							float [] color = new float[4];
							
							//What is the alpha of my full object pixel
							if((alpha[fullIndex] & 0xFF) > 0){
								if((layerGlow & 0xFF) != 1){
									color[0] = (bytes[fullPixelIndex] & 0xFF)/255.0f * (1-colorTrans) + (srdata[pixelindex] & 0xFF)/255.0f* (colorTrans);
									color[1] = (bytes[fullPixelIndex+1] & 0xFF)/255.0f  * (1-colorTrans) + (srdata[pixelindex+1] & 0xFF)/255.0f * (colorTrans);
									color[2] = (bytes[fullPixelIndex+2] & 0xFF)/255.0f  * (1-colorTrans) + (srdata[pixelindex+2] & 0xFF)/255.0f * (colorTrans);
									color[3] = (bytes[fullPixelIndex+3] & 0xFF)/255.0f  * (1-colorTrans) + (srdata[pixelindex+3] & 0xFF)/255.0f* (colorTrans);
								}
								else{
									color[0] = color[0] > 1 ? 1 : color[0];
									color[1] = color[1] > 1 ? 1 : color[1];
									color[2] = color[2] > 1 ? 1 : color[2];
									color[3] = color[3] > 1 ? 1 : color[3];
								}
							}
							else{
								color[0] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[1] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[2] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
								color[3] = colorTrans;//(float) (srdata[pixelindex++] & 0xFF) /255.0f;
							}
							
							bytes[fullPixelIndex++] = (byte) (color[0] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[1] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[2] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[3] * 255.0f);
							alpha[fullIndex] = (byte) (color[3] * 255.0f);
							/*
							System.out.println("sprite alpha: " + alpha[fullIndex]);
							System.out.println("color: 1:" + color[0] * 255.0f);
							System.out.println("color: 2:" + color[1] * 255.0f);
							System.out.println("color: 3:" + color[2] * 255.0f);
							System.out.println("color: 4:" + color[3] * 255.0f);
							*/
							
							if(lowx > fullX){
								lowx = fullX;
							}
							if(lowy > fullY){
								lowy = fullY;
							}
							if(hix < fullX){
								hix = fullX;
							}
							if(hiy < fullY){
								hiy = fullY;
							}
						}
					}
				}
			}
		}
		
		//System.out.println("low: "  + lowx + ", " + lowy);
		//System.out.println("hi: "  + hix + ", " + hiy);
		
		if(lowx < hix && lowy < hiy){
			//Can we get a smaller image
			int width = hix - lowx;
			int height = hiy - lowy;
			int size = width * height;
			
			byte [] subimage = new byte[size * BYTES_PER_PIXEL];
			byte [] alphadata = new byte[size];
			
			for(int y = 0; y < height; y++){
				//for(int x = 0; x < width; x++){
				int srcPos = (y + lowy) * (SIZE * GRID) + lowx;
				int desPos = y * (width);
				
				System.out.println("srcPos: " + srcPos);
				System.out.println("desPos: " + desPos);
				
				System.arraycopy(bytes, srcPos * BYTES_PER_PIXEL, subimage, desPos * BYTES_PER_PIXEL, width * BYTES_PER_PIXEL);
				
				srcPos = (y + lowy) * (SIZE * GRID) + lowx;
				desPos = y * width;
				
				System.arraycopy(alpha, srcPos, alphadata, desPos, width);
			}
			
			ImageData imgData = new ImageData(width, height, 32, palette, 1, subimage);
			imgData.alphaData = alphadata;
			
			return imgData;
		}
		
		
		ImageData imgData = new ImageData(13*16, 13*16, 32, palette, 1, bytes);
		imgData.alphaData = alpha;
		
		return imgData;
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		ObjectResource resourceFile = new ObjectResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
