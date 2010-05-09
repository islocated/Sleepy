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
		byte [] alpha = new byte[SIZE * GRID * SIZE * GRID];
		
		if(room != null){
			bytes = room.getImageData().data;
			//System.arraycopy(room.getImageData().data, 0, bytes, 0, SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL);
			//System.out.println("have data");
		}
		else{
			bytes = new byte[SIZE * GRID * SIZE * GRID * BYTES_PER_PIXEL];
		}
		
		System.out.println(resourceName);
		
		float mroomTrans = roomTrans / 255.0f;
		
		
		//Don't have to worry about darkening until later?
		
		//Draw locked objects first
		for(int i = 0; i < numObjects; i++){
			ObjectLayer object = objects.get(i);
			
			if(object.getLock() == 1){
				drawObject(bytes, alpha, object);
			}
		}
		
		//Draw unlocked objects afterwards
		for(int i = 0; i < numObjects; i++){
			ObjectLayer object = objects.get(i);
			
			if(object.getLock() != 1){
				drawObject(bytes, alpha, object);
			}
		}
		
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

	private void drawObject(byte[] bytes, byte [] destAlpha, ObjectLayer object) {
		ImageData imgData = object.getImageData();
		byte [] img = imgData.data;
		byte [] imgAlpha = imgData.alphaData;
		
		//Make the offsets put the images at the bottom left
		int yoffset = (GRID * SIZE)/2;
		int xoffset = -(GRID * SIZE)/2;
		
		//Move image up and right to compensate for the size of the sprite...
		yoffset += -SIZE/2;
		xoffset += SIZE/2;
		
		yoffset += -object.getOffset()[1];
		xoffset += object.getOffset()[0];
	
		BytesUtil.copyBytesTrans(
				img, 
				ObjectResource.SIZE * ObjectResource.GRID,// * ObjectResource.BYTES_PER_PIXEL,
				ObjectResource.SIZE * ObjectResource.GRID,
				bytes,
				SIZE * GRID,// * BYTES_PER_PIXEL,
				SIZE * GRID, xoffset, yoffset, imgAlpha, destAlpha);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		SceneResource resourceFile = new SceneResource(file);
		//resourceFile.getImageData();
		//resourceFile.load();
	}
}
