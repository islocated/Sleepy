package com.isnotok.sleep.model;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import com.isnotok.sleep.IImageKeys;
import com.isnotok.sleep.util.BytesUtil;

public class PakRecord {
	private String type;
    private UniqueId id;
	//private byte[] id;
    private String wordString;
    private int length;
    private byte[] data;
	private ImageData imageData;
	private PakFile parent;
    
    public PakRecord(PakFile parent, byte[] bytes, int offset) {
    	this.parent = parent;
    	
		// TODO Auto-generated constructor stub
		type = BytesUtil.readString(bytes, offset);
		offset += type.length() + 1;
		
		id = new UniqueId(BytesUtil.readBytes(bytes, offset, 6));
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

	public UniqueId getId() {
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
				imageData = getRoomImageData();
			else if(type.equals("scene"))
				imageData = getTileImageData();
			else if(type.equals("object"))
				imageData = getTileImageData();
			else if(type.equals("sprite"))
				imageData = getTileImageData();
			else if(type.equals("music"))
				imageData = getTileImageData();
			else if(type.equals("scale"))
				imageData = getScaleImageData();
		}
		if(imageData == null){
			return new ImageData(IImageKeys.FOLDER);
		}
	    return imageData;
	}
	
	private ImageData getScaleImageData() {
		//PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		
		PaletteData palette = new PaletteData(0xFF, 0xFF, 0xFF);
		
		byte [] imgData = new byte[16*16];
		
		//12 halftones
		for(int i = 0; i < 12; i++){
			//Find the diagonal, go from bottom left to top right
			int index = ((12 - i - 1) + 2) * 16 + i + 2; //add 2 to shift picture over and down by 2
			imgData[index] = data[i] == 1 ? (byte) 0xFF : 0;
			//imgData[index++] = data[i] == 1 ? (byte) 0xFF : 0;
			//imgData[index++] = data[i] == 1 ? (byte) 0xFF : 0;
		}
		
		return new ImageData(16, 16, 8, palette, 1, imgData);
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
	
	private ImageData getRoomImageData(){
		PakRecord [][] tiles = new PakRecord[13][13];
		
		for(int y = 0; y < 13; y++){
			for(int x = 0; x < 13; x++){
				int index = (y * 13 + x) * UniqueId.MAX_DIGITS;
				byte [] b = new byte[UniqueId.MAX_DIGITS];
				System.arraycopy(data, index, b, 0, UniqueId.MAX_DIGITS);
				
				tiles[x][y] = parent.getResourceById(new UniqueId(b));
			}
		}
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
		
		byte [] bytes = new byte[13*16*13*16*4];
		
		for(int y = 0; y < 13; y++){
			for(int x = 0; x < 13; x++){
				//Copy tiles
				//Copy tile[x][y] to bytes location
				PakRecord tile = tiles[x][y];
				
				//Index is for all the pixels in here
				int index = 0;
				for(int j = 0; j < 16; j++){
					for(int i = 0; i < 16; i++){
						for(int k = 0; k < 4; k++){
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
}
