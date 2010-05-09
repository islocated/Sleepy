package com.isnotok.sleep.model;

public class Pixel {
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	public static final int ALPHA = 3;
	public static final int COMPONENTS = 4;
	
	byte [] color = new byte[4];
	
	public Pixel(){
	}
	
	public Pixel(byte [] bytes, int offset){
		System.arraycopy(bytes, offset, color, 0, COMPONENTS);
	}
	
	public byte getRed(){
		return color[RED];
	}
	
	public byte getBlue(){
		return color[BLUE];
	}
	
	public byte getGreen(){
		return color[GREEN];
	}
	
	public byte getAlpha(){
		return color[ALPHA];
	}
}
