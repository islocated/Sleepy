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

public class SceneResource extends Resource{
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
	List<ObjectLayer> objects = new ArrayList<ObjectLayer>(MAX_LAYERS);
	private String resourceName = "";
	private RoomResource room;
	private byte roomTrans;
	private byte numObjectsFrozen;
	private byte numObjects;
	
	public SceneResource(File file){
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
		
		//numLayers = data[offset++];
		
		byte [] b = BytesUtil.readBytes(data, offset, UniqueId.MAX_DIGITS);
		offset += UniqueId.MAX_DIGITS;
		
		System.out.println("roomid needed:" + new UniqueId(b).toHexString());
		
		File parent = new File(file.getParentFile().getParentFile(), "room");
		File roomFile = new File(parent, new UniqueId(b).toHexString());
		
		room = (RoomResource) CacheManager.getInstance().getResource(roomFile);
		
		roomTrans = data[offset++];
		
		numObjectsFrozen = data[offset++];
		
		numObjects = data[offset++];
		
		for(int i = 0; i < numObjects; i++){
			ObjectLayer object = new ObjectLayer(file);
			objects.add(object);
			
			b = BytesUtil.readBytes(data, offset, UniqueId.MAX_DIGITS);
			offset += UniqueId.MAX_DIGITS;
			
			object.setId(b);
			
			//get objectoffset
			int [] offs = new int[2];
			offs[0] = BytesUtil.readInt(data, offset);
			offset += 4;
			offs[1] = BytesUtil.readInt(data, offset);
			offset += 4;
			object.setOffset(offs);
			
			//get speechoffsets
			int [] speechOffs = new int[2];
			speechOffs[0] = BytesUtil.readInt(data, offset);
			offset += 4;
			speechOffs[1] = BytesUtil.readInt(data, offset);
			offset += 4;
			object.setSpeechOffset(speechOffs);
			
			//get flipflag
			byte isFlipped = data[offset++];
			object.setFlip(isFlipped);
			
			//get boxflag
			byte isBox = data[offset++];
			object.setBox(isBox);
			
			//get lock flag
			byte isLocked = data[offset++];
			object.setLock(isLocked);
			
			//get trans flag
			byte trans = data[offset++];
			object.setTrans(trans);
		}
		
		nameOffset = offset;
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

		byte [] bytes;// = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		
		if(room != null){
			bytes = room.getImageData().data;
			//System.arraycopy(room.getImageData().data, 0, bytes, 0, SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL);
			//System.out.println("have data");
		}
		else{
			bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		}
		
		/*
		if(imgBytes != null && imgAlpha != null){
		//	ImageData imgData = new ImageData(13*16, 13*16, 32, palette, 1, imgBytes);
			//imgData.alphaData = imgAlpha;
		//	return imgData;
		}
		*/
		
		
		int imageCenter = (GRID * SIZE)/2;
		int objectCenter = (GRID * SIZE)/2;
		
		//int spriteCenter = SIZE/2;
		
		System.out.println(resourceName);
		
		float mroomTrans = roomTrans / 255.0f;
		
		
		//Don't have to worry about darkening until later?
		
		for(int i = 0; i < numObjects; i++){
			ObjectLayer object = objects.get(i);
			
			ImageData imgData = object.getImageData();
			byte [] img = imgData.data;
			byte [] alpha = imgData.alphaData;
			
			System.out.println("offset: x:" + object.getOffset()[0] + " y:" + object.getOffset()[1]);
			
			//int yoffset = object.getOffset()[1];// * SIZE * GRID;// * BYTES_PER_PIXEL;
			//int xoffset = object.getOffset()[0];// * SIZE;// * BYTES_PER_PIXEL;
			//int offset = -yoffset + xoffset;
			
			//Convert bl to tl
			
			//xoffset = -objectCenter + object.getOffset()[0];//SIZE * GRID - (yoffset - spriteCenter) + xoffset + spriteCenter;
			
			//With 0 offset, object is drawn right in the middle (this is because the top left is blank)
			//This sets up all objects to be drawn at top left
			int yoffset = -(GRID * SIZE * GRID * SIZE)/2;	//move object up half a screen
			int xoffset = -(GRID * SIZE)/2;					//move object to the left half a screen
			int offset = yoffset + xoffset;					//this is offset at top left
			
			//Add offset of image
			yoffset = -(SIZE)*2 * (GRID * SIZE);
			xoffset = (SIZE)/2;
			offset += yoffset + xoffset;
			
			//Convert bottom left to top left (take size - yoffset of object)
			int objOffsety = (GRID * SIZE) - object.getOffset()[1];
			objOffsety *= (GRID * SIZE);
			
			int objOffsetx = object.getOffset()[0];
			//objOffsetx *= (GRID * SIZE);
			
			offset += objOffsety + objOffsetx;
			
			//offset += -(SIZE) * (GRID * SIZE) ;
			
			//int objOffsetx = 
			
			System.out.println(offset);
			
			BytesUtil.copyBytesTrans(
					img, 
					ObjectResource.SIZE * ObjectResource.GRID,// * ObjectResource.BYTES_PER_PIXEL,
					ObjectResource.SIZE * ObjectResource.GRID,
					bytes,
					SIZE * GRID,// * BYTES_PER_PIXEL,
					SIZE * GRID, offset, alpha);
		}
		
		/*
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
								color[3] = (float) (srdata[pixelindex++] & 0xFF) /255.0f;
							}
							
							bytes[fullPixelIndex++] = (byte) (color[0] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[1] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[2] * 255.0f);
							bytes[fullPixelIndex++] = (byte) (color[3] * 255.0f);
							alpha[fullIndex] = (byte) (color[3] * 255.0f);
							
							//System.out.println(alpha[index]);
						}
					}
				}
			}
		}
		
		*/
		
		ImageData imgData = new ImageData(13*16, 13*16, 32, palette, 1, bytes);
		//imgData.alphaData = alpha;
		
		//imgBytes = bytes;
		//imgAlpha = alpha;
		
		//Image image = new Image(Display.getCurrent(), imgData);
		
		//Saving imgData to PNG file
		//ImageLoader imageLoader = new ImageLoader();
		//imageLoader.data = new ImageData[] {imgData};
		//imageLoader.save(file.getAbsolutePath() + ".PNG", SWT.IMAGE_PNG);
		
		return imgData;
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		SceneResource resourceFile = new SceneResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
