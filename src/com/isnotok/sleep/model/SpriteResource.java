package com.isnotok.sleep.model;

import java.io.File;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class SpriteResource extends TileResource{
	public static final int SIZE = 16;
	public static final int BYTES_PER_PIXEL = 4;
	public static final int BYTES_USED = SIZE * SIZE * BYTES_PER_PIXEL;
	public static final int ALPHA_USED = SIZE * SIZE;
	public static final int BYTES_TOTAL = BYTES_USED + ALPHA_USED;
	
	
	private byte[] offset = new byte[2];
	private float trans = 0f;
	private byte glow = 0;
	
	public SpriteResource(File file){
		super(file);
		load();
	}
	
	@Override
	public String getResourceName() {
		if(data == null){
			return null;
		}
		else{
			StringBuffer sb = new StringBuffer();
			for(int i = BYTES_TOTAL; i < data.length-1; i++){
				sb.append((char)data[i]);
			}
			return sb.toString();
		}
	}
	
	@Override
	public ImageData getImageData(){
		if(data == null || data.length < BYTES_TOTAL)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
	    
		ImageData imgData = new ImageData(SIZE, SIZE, 8 * BYTES_PER_PIXEL, palette, 1, data);
		
		byte [] alpha = new byte[SIZE * SIZE];
		
		for(int i = 0; i < ALPHA_USED; i++){
			//Is pixel transparent?
			alpha[i] = (byte) (data[BYTES_USED + i] == 1 ? 0x0 : 0xff);
		}
		
		imgData.alphaData = alpha;
		
		return imgData;
	}
	
	@Override
	public String getType() {
		return "sprite";
	};
	
	public byte[] getOffset() {
		return offset;
	}

	public void setOffset(byte[] offs) {
		this.offset = offs;
	}

	public float getTrans() {
		return trans;
	}

	public void setTrans(float trans) {
		this.trans = trans;
	}

	public byte getGlow() {
		return glow;
	}

	public void setGlow(byte glow) {
		this.glow = glow;
	}

	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		SpriteResource resourceFile = new SpriteResource(file);
		//resourceFile.load();
	}
}
