package com.isnotok.sleep.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

public class TileResource {
	private File file;
	
	private byte [] data;
	
	public TileResource(File file){
		this.file = file;
	}
	
	public File getFile(){
		return file;
	}
	
	public String getResourceName(){
		if(data == null){
			return null;
		}
		else{
			StringBuffer sb = new StringBuffer();
			
			if(data.length > 16*16*4){
				for(int i = 16*16*4; i < data.length; i++){
					sb.append((char)data[i]);
				}
			}
			else{
				//timbre
				for(int i = 20; i < data.length; i++){
					sb.append((char)data[i]);
				}
			}
			return sb.toString();
		}
	}
	
	public void load() {
		// TODO Auto-generated method stub
		try {
			FileInputStream fis = new FileInputStream(file);
			long fileSize = file.length();
			
			if(fileSize > Integer.MAX_VALUE){
				throw new IOException();
			}
			
			data = new byte[(int)fileSize];
			
			int byteRead = fis.read(data);
			
			System.out.println("done reading resource file: " + byteRead);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageData getImageData(){
		if(data == null || data.length < 16 * 16 * 4)
			return null;
		
		PaletteData palette = new PaletteData(0xFF000000, 0xFF0000, 0xFF00);
	    return new ImageData(16,16,32,palette, 1, data);
	}
	
	public ImageData getTimbreData(){
		if(data == null || data.length < 16)
			return null;
		
		PaletteData palette = new PaletteData(0x1, 0x1, 0x1);
		
		//For harmonics use 16
		byte [] imgData = new byte[16*16];
		for(int i = 0; i < 16; i++){
			int y = 16 - i - 1;	//Order this backwards
			
			//Data holds the length of the line
			//Allow the timbre to look like it spans from left edge
			for(int j = 0; j < data[i]; j++){
				int index = y*16 + j;
				imgData[index] = 1;
			}
		}
		
		return new ImageData(16, 16, 8, palette, 1, imgData);
	}
	
	public static void main(String [] args){
		File file = new File(".", "input/0A3A96732EF2");
		TileResource resourceFile = new TileResource(file);
		resourceFile.load();
	}
}
