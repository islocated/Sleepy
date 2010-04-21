package com.isnotok.sleep.model;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import com.isnotok.sleep.IImageKeys;
import com.isnotok.sleep.util.BytesUtil;

public class PakRecord {
	private String type;
    private byte[] id;
    private String wordString;
    private int length;
    private byte[] data;
	private ImageData imageData;
    
    public PakRecord(byte[] bytes, int offset) {
		// TODO Auto-generated constructor stub
		type = BytesUtil.readString(bytes, offset);
		offset += type.length() + 1;
		
		id = BytesUtil.readBytes(bytes, offset, 6);
		offset += 6;
		
		wordString = BytesUtil.readString(bytes, offset);
		offset += wordString.length() + 1;
		
		length = BytesUtil.readInt(bytes, offset);
		offset += 4;
		
		data = BytesUtil.readBytes(bytes, offset, length);
		offset += length;
	}

	public int getUsedBytes(){
		return type.length() + 1 + 6 + wordString.length() + 1 + 4 + length;
	}
	
	public String getType() {
		return type;
	}

	public byte[] getId() {
		return id;
	}

	public String getWordString() {
		return wordString;
	}

	public byte[] getData() {
		return data;
	}

	public int getLength() {
		return length;
	}
	
	public ImageData getImageData(){
		if(imageData == null){
			if(type.equals("tile"))
				imageData = getTileImageData();
			else if(type.equals("room"))
				imageData = getTileImageData();
			else if(type.equals("scene"))
				imageData = getTileImageData();
			else if(type.equals("object"))
				imageData = getTileImageData();
			else if(type.equals("sprite"))
				imageData = getTileImageData();
			else if(type.equals("music"))
				imageData = getTileImageData();
		}
		if(imageData == null){
			return new ImageData(IImageKeys.FOLDER);
		}
	    return imageData;
	}
	
	//Maybe we need different objects
	private ImageData getTileImageData(){
		//Format is RGBA, if we need alpha, we have to do this separately
		//byte [] pixelBytes = new byte[16*16*4];
		//System.arraycopy(data, 0, pixelBytes, 0, 16*16*4);
		
		//Format is RGBA, data contains more bytes than necessary, but it is ok
		//because ImageData won't complain with more bytes in data
		//The last few bytes are for the tile name
		
		//ImageData is 16x16x4bytes, last byte is alpha so the masks have extra byte
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
	    return new ImageData(16,16,32,palette, 1, data);
	}
}
